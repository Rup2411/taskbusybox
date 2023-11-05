package com.authorization.taskbusybox.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.authorization.taskbusybox.dtos.RoleDto;
import com.authorization.taskbusybox.dtos.UserDto;
import com.authorization.taskbusybox.services.RoleService;
import com.authorization.taskbusybox.services.UserService;

@RestController
@RequestMapping("/admin")
public class AdminController {

    @Autowired
    RoleService roleService;
    
    @Autowired
    UserService userService;
    
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @PostMapping("/roles")
    public ResponseEntity<String> createRole(@RequestBody RoleDto roleDto) {
        if ("Admin".equalsIgnoreCase(roleDto.getName())) {
            return ResponseEntity.badRequest().body("Cannot create the 'Admin' role.");
        }

        RoleDto createdRole = roleService.createRole(roleDto);

        if (createdRole != null) {
            return ResponseEntity.ok("Role created successfully");
        } else {
            return ResponseEntity.badRequest().body("Role creation failed. Role Already Exists");
        }
    }


    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @PostMapping("/users/{userId}/assignRole")
    public ResponseEntity<String> assignRoleToUser(@PathVariable Integer userId, @RequestBody RoleDto roleDto) {

    	UserDto userDto = userService.getUserById(userId);
        if (userDto == null) {
            return ResponseEntity.badRequest().body("User not found");
        }
        
        if (!roleService.roleExists(roleDto.getName())) {
            return ResponseEntity.badRequest().body("Role does not exist. Admin should create the role first.");
        }

        userDto = userService.assignRoleToUser(userId, roleDto);

        if (userDto == null) {
            return ResponseEntity.badRequest().body("Role not found");
        }

        return ResponseEntity.ok("Role assigned to user successfully");
    }
}

