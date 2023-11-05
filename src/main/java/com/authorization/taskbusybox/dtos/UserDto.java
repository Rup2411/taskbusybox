package com.authorization.taskbusybox.dtos;

import java.util.Set;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class UserDto {

	private String firstName;
	
	private String lastName;
	
	private String email;
	
	private String contactNumber;
	
	private String password;
	
	private Set<RoleDto> roles;
}
