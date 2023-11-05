package com.authorization.taskbusybox.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.authorization.taskbusybox.dtos.LoginRequest;
import com.authorization.taskbusybox.dtos.LoginResponse;
import com.authorization.taskbusybox.dtos.UserDto;
import com.authorization.taskbusybox.services.UserService;

@RestController
@RequestMapping("/auth")
public class LoginController {

	@Autowired
    AuthenticationManager authenticationManager;
	
	@Autowired
    UserDetailsService userDetailsService;
	
	@Autowired
	UserService userService;


	@PostMapping("/login")
	public ResponseEntity<LoginResponse> login(@RequestBody LoginRequest loginRequest) {
	    Authentication authentication = authenticationManager.authenticate(
	        new UsernamePasswordAuthenticationToken(loginRequest.getEmail(), loginRequest.getPassword()));

	    SecurityContextHolder.getContext().setAuthentication(authentication);

	    UserDetails userDetails = userDetailsService.loadUserByUsername(loginRequest.getEmail());

	    if (userDetails != null) {
	        UserDto userDto = userService.createUserDtoFromUserDetails(userDetails);

	        LoginResponse loginResponse = new LoginResponse();
	        loginResponse.setMessage("Logged In successfully");
	        loginResponse.setUserDto(userDto);

	        return ResponseEntity.ok(loginResponse);
	    } else {
	        LoginResponse loginResponse = new LoginResponse();
	        loginResponse.setMessage("Invalid credentials");

	        return ResponseEntity.badRequest().body(loginResponse);
	    }
	}



    
    @PostMapping("/register")
	public ResponseEntity<UserDto> registerNewUser(@RequestBody UserDto userDto) {
		
		UserDto registeredUser = this.userService.registerNewUser(userDto);
		
		return new ResponseEntity<UserDto> (registeredUser, HttpStatus.CREATED);
	}
}
