package com.defaultPackage.controller.manageuser;

import java.util.Objects;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.defaultPackage.model.User;
import com.defaultPackage.request.UserCreationRequest;
import com.defaultPackage.response.LoginReponseJwtToken;
import com.defaultPackage.response.UserCreationResponse;
import com.defaultPackage.service.user.UserService;
import com.defaultPackage.userrepo.UserRepository;

@RestController
@RequestMapping("/user/manage")
public class ManageUserController {
	
	@Autowired
	UserService userService;
	
	@Autowired
	UserRepository userRepository;
	
	@Autowired
	AuthenticationManager authenticationManager;

	// Create User Request
	@PostMapping("/signup")
	public ResponseEntity<UserCreationResponse> createUser(@RequestBody UserCreationRequest userCreationRequest) {
		User user = userRepository.findByUsername(userCreationRequest.getUserEmail());
		UserCreationResponse userCreationResponse = null;

//		 Check If User is Already created or not ?
		if (Objects.nonNull(user)) {
			userCreationResponse = UserCreationResponse.builder().message("User Already Have Account").build();
			return ResponseEntity.ok().body(userCreationResponse);
		}
		userCreationResponse = userService.createUser(userCreationRequest);
		return ResponseEntity.ok().body(userCreationResponse);
	}

	@PostMapping("/login")
	public ResponseEntity<LoginReponseJwtToken> loginAndGetJwtToken(
			@RequestBody UserCreationRequest userCreationRequest) {
		System.out.println("user request comes here?");
		LoginReponseJwtToken loginReponseJwtToken = new LoginReponseJwtToken();
		Authentication authentication = null;
		// First Authenticate User
		try {
			authentication = new UsernamePasswordAuthenticationToken(userCreationRequest.getUserEmail(),
					userCreationRequest.getUserPassword());

			authenticationManager.authenticate(authentication);

		} catch (Exception exception) {
			loginReponseJwtToken.setMessage("User Not Authenticate and Token is Not generate");
			loginReponseJwtToken.setToken(null);
			return ResponseEntity.badRequest().body(loginReponseJwtToken);
		}
//		User user = (User) authentication.getPrincipal();

		loginReponseJwtToken.setToken(userService.getJwtToken(userCreationRequest));
		loginReponseJwtToken.setMessage("Token Is Send");
		return ResponseEntity.ok(loginReponseJwtToken);
	}

}