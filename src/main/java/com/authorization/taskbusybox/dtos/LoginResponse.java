package com.authorization.taskbusybox.dtos;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class LoginResponse {

	 private String message;
	 
	 private UserDto userDto;
}
