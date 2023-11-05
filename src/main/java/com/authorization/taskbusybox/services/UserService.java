package com.authorization.taskbusybox.services;

import java.util.List;

import org.springframework.security.core.userdetails.UserDetails;

import com.authorization.taskbusybox.dtos.RoleDto;
import com.authorization.taskbusybox.dtos.UserDto;

public interface UserService {

	UserDto createUser(UserDto userDto);
	
	UserDto updateUser(UserDto userDto, Integer userId);
	
	UserDto getUserById(Integer userId);
	
	List<UserDto> getAllUsers();
	
	void deleteUser(Integer userId);
	
	UserDto assignRoleToUser(Integer userId, RoleDto role);

	UserDto registerNewUser(UserDto userDto);

	UserDto createUserDtoFromUserDetails(UserDetails userDetails);
}
