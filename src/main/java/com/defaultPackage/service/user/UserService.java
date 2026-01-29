package com.defaultPackage.service.user;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.defaultPackage.model.User;
import com.defaultPackage.request.UserCreationRequest;
import com.defaultPackage.response.UserCreationResponse;
import com.defaultPackage.service.JwtService.JwtUtil;
import com.defaultPackage.userrepo.UserRepository;


@Service
public class UserService {

	@Autowired
	JwtUtil jwtUtil;
	
	@Autowired
	UserRepository userRepository;
	
	@Autowired
	PasswordEncoder passwordEncoder;
	
	
	public UserCreationResponse createUser(UserCreationRequest userCreationRequest) {
		
		User user = User.builder().
				username(userCreationRequest.getUserEmail())
				.password(passwordEncoder.encode(userCreationRequest.getUserPassword()))
				.build();
		
		userRepository.save(user);
		
		UserCreationResponse userCreationResponse = UserCreationResponse.builder()
				.username(user.getUsername())
				.message("Your Account is Created")
				.build();
		System.out.println("User is Created.");
		return userCreationResponse;
	}

	
	public String getJwtToken(UserCreationRequest user) {
		String token = jwtUtil.getToken(user);
		System.out.println("Token is Created."+token);
		return token;
	}

}
