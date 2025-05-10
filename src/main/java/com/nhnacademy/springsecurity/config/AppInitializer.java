package com.nhnacademy.springsecurity.config;


import com.nhnacademy.springsecurity.role.domain.Role;
import com.nhnacademy.springsecurity.role.repository.RoleRepository;
import org.springframework.boot.ApplicationRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class AppInitializer {

    @Bean
    ApplicationRunner initRoles(RoleRepository roleRepository){
        return args -> {
            if(roleRepository.count()==0){
                roleRepository.save(new Role("ROLE_USER"));
                roleRepository.save(new Role( "ROLE_ADMIN"));
            }
        };
    }
}
