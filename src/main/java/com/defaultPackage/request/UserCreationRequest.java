package com.defaultPackage.request;

import com.defaultPackage.model.User;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UserCreationRequest {
	String userEmail;
	String userPassword;
}