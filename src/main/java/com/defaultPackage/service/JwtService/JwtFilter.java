package com.defaultPackage.service.JwtService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import com.defaultPackage.model.User;
import com.defaultPackage.userrepo.UserRepository;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;

@Component
@Slf4j
public class JwtFilter extends OncePerRequestFilter {

    @Autowired
    UserRepository userRepository;

    @Autowired
    JwtUtil jwtUtil;

    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) {
        String path = request.getRequestURI();
        return request.getMethod().equals("OPTIONS") ||
               path.equals("/") ||
               path.startsWith("/user/manage/") ||
               path.endsWith(".html") ||
               path.startsWith("/css/") ||
               path.startsWith("/js/");
    }

    @Override
    protected boolean shouldNotFilterAsyncDispatch() {
        return true;
    }

    @Override
    protected boolean shouldNotFilterErrorDispatch() {
        return true;
    }

    @Override
    protected void doFilterInternal(
            HttpServletRequest request,
            HttpServletResponse response,
            FilterChain filterChain)
            throws ServletException,java.io.IOException {

        log.info("Incoming Request {}", request.getRequestURI());
        System.out.println("procedding further");
        String header = request.getHeader("Authorization");

//        if (header == null || !header.startsWith("Bearer ")) {
//        	System.out.println("header property : "+header);
//            filterChain.doFilter(request, response);
//            return;
//        }
        if (header == null || !header.startsWith("Bearer ")) {
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            return;
        }

        String token = header.substring(7);
        String username = jwtUtil.getUsernameFromToken(token);

        if (username != null &&
            SecurityContextHolder.getContext().getAuthentication() == null) {

            User user = userRepository.findByUsername(username);
            if (user != null) {
                UsernamePasswordAuthenticationToken auth =
                        new UsernamePasswordAuthenticationToken(
                                user, null, user.getAuthorities());
                SecurityContextHolder.getContext().setAuthentication(auth);
            }
        }

        filterChain.doFilter(request, response);
    }
}
