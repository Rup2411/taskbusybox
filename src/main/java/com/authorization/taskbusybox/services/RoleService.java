package com.authorization.taskbusybox.services;

import com.authorization.taskbusybox.dtos.RoleDto;

import java.util.List;

public interface RoleService {

    RoleDto createRole(RoleDto roleDto);

    RoleDto updateRole(RoleDto roleDto, Integer roleId);

    RoleDto getRoleById(Integer roleId);

    RoleDto getRoleByName(String name);

    List<RoleDto> getAllRoles();

    void deleteRole(Integer roleId);

	boolean roleExists(String name);
}
