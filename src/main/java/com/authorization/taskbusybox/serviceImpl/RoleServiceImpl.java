package com.authorization.taskbusybox.serviceImpl;

import com.authorization.taskbusybox.dtos.RoleDto;
import com.authorization.taskbusybox.entities.RoleEntity;
import com.authorization.taskbusybox.exceptions.ResourceNotFoundException;
import com.authorization.taskbusybox.repositories.RoleRepository;
import com.authorization.taskbusybox.services.RoleService;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class RoleServiceImpl implements RoleService {

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private ModelMapper modelMapper;

    @Override
    public RoleDto createRole(RoleDto roleDto) {
        // Check if the role with the same name already exists
        RoleEntity existingRole = roleRepository.findRoleByName(roleDto.getName());

        if (existingRole != null) {
            return null;
        }

        if ("Admin".equalsIgnoreCase(roleDto.getName())) {
            return null; 
        }

        RoleEntity roleEntity = modelMapper.map(roleDto, RoleEntity.class);
        RoleEntity savedRole = roleRepository.save(roleEntity);

        return modelMapper.map(savedRole, RoleDto.class);
    }

    @Override
    public RoleDto updateRole(RoleDto roleDto, Integer roleId) {
        RoleEntity roleEntity = roleRepository.findById(roleId)
                .orElseThrow(() -> new ResourceNotFoundException("RoleEntity", "Id", roleId));

        roleEntity.setName(roleDto.getName());
        // Update other properties as needed

        RoleEntity updatedRole = roleRepository.save(roleEntity);
        return modelMapper.map(updatedRole, RoleDto.class);
    }

    @Override
    public RoleDto getRoleById(Integer roleId) {
        RoleEntity roleEntity = roleRepository.findById(roleId)
                .orElseThrow(() -> new ResourceNotFoundException("RoleEntity", "Id", roleId));
        return modelMapper.map(roleEntity, RoleDto.class);
    }

    @Override
    public RoleDto getRoleByName(String name) {
        RoleEntity roleEntity = roleRepository.findRoleByName(name);
        if (roleEntity == null) {
            throw new ResourceNotFoundException("RoleEntity", "Name", 0);
        }
        return modelMapper.map(roleEntity, RoleDto.class);
    }

    @Override
    public List<RoleDto> getAllRoles() {
        List<RoleEntity> roleEntities = roleRepository.findAll();
        return roleEntities.stream()
                .map(roleEntity -> modelMapper.map(roleEntity, RoleDto.class))
                .collect(Collectors.toList());
    }

    @Override
    public void deleteRole(Integer roleId) {
        RoleEntity roleEntity = roleRepository.findById(roleId)
                .orElseThrow(() -> new ResourceNotFoundException("RoleEntity", "Id", roleId));
        roleRepository.delete(roleEntity);
    }
    
    @Override
    public boolean roleExists(String name) {
        RoleEntity existingRole = roleRepository.findRoleByName(name);
        return existingRole != null;
    }
}