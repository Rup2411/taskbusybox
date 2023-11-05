package com.authorization.taskbusybox;

import java.util.List;
import java.util.Optional;
import java.util.Set;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.password.PasswordEncoder;

import com.authorization.taskbusybox.config.AppConstants;
import com.authorization.taskbusybox.entities.RoleEntity;
import com.authorization.taskbusybox.entities.UserEntity;
import com.authorization.taskbusybox.repositories.RoleRepository;
import com.authorization.taskbusybox.repositories.UserRepository;

@SpringBootApplication
public class AuthorizationApplication implements CommandLineRunner {

	@Autowired
	RoleRepository roleRepository;
	
	@Autowired
	UserRepository userRepository;
	
	@Autowired
	PasswordEncoder passwordEncoder;
	
	public static void main(String[] args) {
		SpringApplication.run(AuthorizationApplication.class, args);
	}

	@Bean
	public ModelMapper modelMapper() {
		
		return new ModelMapper();
	}

	@Override
	public void run(String... args) throws Exception {
		
		try {
			RoleEntity adminRole = roleRepository.findById(AppConstants.ROLE_ADMIN)
                    .orElseGet(() -> {
                        RoleEntity role = new RoleEntity();
                        role.setId(AppConstants.ROLE_ADMIN);
                        role.setName("ROLE_ADMIN");
                        return roleRepository.save(role);
                    });

            Optional<UserEntity> adminUserOptional = userRepository.findByEmail("admin@example.com");
            if (adminUserOptional.isEmpty()) {
                UserEntity defaultAdminUser = new UserEntity();
                defaultAdminUser.setEmail("admin@example.com");
                defaultAdminUser.setPassword(passwordEncoder.encode("Rupesh@6291"));
                defaultAdminUser.setContactNumber("9038961502");
                defaultAdminUser.setFirstName("ADMIN");
                defaultAdminUser.setLastName(" ");
                defaultAdminUser.setRoles(Set.of(adminRole));
                userRepository.save(defaultAdminUser);
	            }
	            
			RoleEntity role1 = new RoleEntity();
			
			role1.setId(AppConstants.ROLE_USER);
			role1.setName("ROLE_USER");
			
			RoleEntity role2 = new RoleEntity();
			
			role2.setId(AppConstants.ROLE_MANAGER);
			role2.setName("ROLE_MANAGER");
			
			List<RoleEntity> roleEntities = List.of(adminRole,role1,role2);
			
			List<RoleEntity> result = this.roleRepository.saveAll(roleEntities);
			
			result.forEach(r -> {
				System.out.println(r.getName());
			});
		}
		catch (Exception e) {
			
			e.printStackTrace();
		}
	}
}
