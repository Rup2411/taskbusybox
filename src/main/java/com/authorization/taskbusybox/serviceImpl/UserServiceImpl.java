package com.authorization.taskbusybox.serviceImpl;

import java.util.HashSet;
import java.util.List;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.authorization.taskbusybox.config.AppConstants;
import com.authorization.taskbusybox.dtos.RoleDto;
import com.authorization.taskbusybox.dtos.UserDto;
import com.authorization.taskbusybox.entities.RoleEntity;
import com.authorization.taskbusybox.entities.UserEntity;
import com.authorization.taskbusybox.exceptions.ResourceNotFoundException;
import com.authorization.taskbusybox.repositories.RoleRepository;
import com.authorization.taskbusybox.repositories.UserRepository;
import com.authorization.taskbusybox.services.UserService;

import jakarta.transaction.Transactional;


@Service
public class UserServiceImpl implements UserService {
	
	@Autowired
	UserRepository userRepository;
	
	@Autowired
	RoleRepository roleRepository;
	
	@Autowired
	ModelMapper modelMapper;
	
	@Autowired
	PasswordEncoder passwordEncoder;

	
	@Override
	public UserDto registerNewUser(UserDto userDto) {
		
		UserEntity userEntity = this.dtoToUser(userDto);
		
		userEntity.setPassword(this.passwordEncoder.encode(userDto.getPassword()));
		
		RoleEntity roleEntity = this.roleRepository.findById(AppConstants.ROLE_USER)
			    .orElseThrow(() -> new ResourceNotFoundException("RoleEntity", "Id", AppConstants.ROLE_USER));

		if (userEntity.getRoles() == null) {
	        userEntity.setRoles(new HashSet<>());
	    }
		
		userEntity.getRoles().add(roleEntity);
		
		UserEntity newUser = this.userRepository.saveAndFlush(userEntity);
		
		return this.userToDto(newUser);
	}
	
	@Override
	public UserDto createUser(UserDto userDto) {
		
		UserEntity userEntity = this.dtoToUser(userDto);
		
		UserEntity savedUser = this.userRepository.saveAndFlush(userEntity);
		
		return this.userToDto(savedUser);
	}

	@Override
	public UserDto updateUser(UserDto userDto, Integer userId) {
		UserEntity userEntity = this.userRepository.findById(userId)
				.orElseThrow(() -> new ResourceNotFoundException("UserEntity", "Id", userId));
		
		userEntity.setFirstName(userDto.getFirstName());
		userEntity.setLastName(userDto.getLastName());
		userEntity.setPassword(userDto.getPassword());
		
		UserEntity updatedUser = this.userRepository.saveAndFlush(userEntity);
		
		UserDto userDto1 = this.userToDto(updatedUser);
		
		return userDto1;
	}

	@Override
	public UserDto getUserById(Integer userId) {
		UserEntity userEntity = this.userRepository.findById(userId)
				.orElseThrow(() -> new ResourceNotFoundException("UserEntity", "Id", userId));
		
		return this.userToDto(userEntity);
	}

	@Override
	public List<UserDto> getAllUsers() {
		
		List<UserEntity> userEntities = this.userRepository.findAll();
		
		List<UserDto> userDtos = userEntities.stream().map(userEntity -> this.userToDto(userEntity)).collect(Collectors.toList());
		
		
		return userDtos;
	}

	@Override
	public void deleteUser(Integer userId) {
		
		UserEntity userEntity = this.userRepository.findById(userId)
				.orElseThrow(() -> new ResourceNotFoundException("UserEntity", "Id", userId));
		
		this.userRepository.delete(userEntity);

	}
	
	@Override
	@Transactional
	public UserDto assignRoleToUser(Integer userId, RoleDto roleDto) {
	    UserEntity userEntity = userRepository.findById(userId)
	            .orElseThrow(() -> new ResourceNotFoundException("UserEntity", "Id", userId));

	    RoleEntity roleEntity = modelMapper.map(roleDto, RoleEntity.class);
	    
	    RoleEntity savedRole = roleRepository.save(roleEntity);

	    userEntity.getRoles().add(savedRole);

	    UserEntity updatedUser = userRepository.save(userEntity);

	    return userToDto(updatedUser);
	}
	
	@Override
	public UserDto createUserDtoFromUserDetails(UserDetails userDetails) {
	    UserEntity userEntity = userRepository.findByEmail(userDetails.getUsername())
	        .orElseThrow(() -> new ResourceNotFoundException("UserEntity", "Email", 0));

	    return userToDto(userEntity);
	}
	
	private UserEntity dtoToUser(UserDto userDto) {
		
		UserEntity userEntity = this.modelMapper.map(userDto, UserEntity.class);
		
		return userEntity;
	}
	
	private UserDto userToDto(UserEntity userEntity) {
		
		UserDto userDto = this.modelMapper.map(userEntity, UserDto.class);
		
		return userDto;
	}

}
