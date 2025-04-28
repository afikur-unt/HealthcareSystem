package com.afikur.healthcare.config;

import com.afikur.healthcare.model.Role;
import com.afikur.healthcare.model.User;
import com.afikur.healthcare.repository.RoleRepository;
import com.afikur.healthcare.repository.UserRepository;
import jakarta.transaction.Transactional;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.*;

@Configuration
public class DataSeeder {
    @Bean
    CommandLineRunner seedDatabase(UserRepository userRepository, RoleRepository roleRepository, PasswordEncoder passwordEncoder) {
        return new CommandLineRunner() {
            @Override
            @Transactional
            public void run(String... args) throws Exception {
                DataSeeder.this.seedData(userRepository, roleRepository, passwordEncoder);
            }
        };
    }

    public void seedData(UserRepository userRepository, RoleRepository roleRepository, PasswordEncoder passwordEncoder) {
        String[] roleNames = {"ROLE_ADMIN", "ROLE_DOCTOR", "ROLE_PATIENT"};

        Map<String, Role> rolesMap = createRoles(roleRepository, roleNames);

        List<User> users = Arrays.asList(
                new User("Admin User", "admin@healthcare.com", "12345", true, Collections.singletonList(rolesMap.get("ROLE_ADMIN"))),
                new User("Dr. Jessica", "doctor@healthcare.com", "12345", true, Collections.singletonList(rolesMap.get("ROLE_DOCTOR"))),
                new User("Mary Cooper", "patient@healthcare.com", "12345", true, Collections.singletonList(rolesMap.get("ROLE_PATIENT")))
        );

        createUsersWithRoles(userRepository, passwordEncoder, users);
    }

    private static void createUsersWithRoles(UserRepository userRepository, PasswordEncoder passwordEncoder, List<User> users) {
        for (User user : users) {
            if (userRepository.findByEmailIgnoreCase(user.getEmail()).isEmpty()) {
                user.setPassword(passwordEncoder.encode(user.getPassword())); // encode password
                user.setRoles(Collections.singletonList(user.getRoles().get(0))); // wrap role in a list
                userRepository.save(user);
            }
        }
    }

    private static Map<String, Role> createRoles(RoleRepository roleRepository, String[] roleNames) {
        Map<String, Role> rolesMap = new HashMap<>();

        for (String roleName : roleNames) {
            Role role = roleRepository.findByName(roleName);
            if (role == null) {
                role = new Role();
                role.setName(roleName);
                role = roleRepository.save(role);
            }
            rolesMap.put(roleName, role);
        }
        return rolesMap;
    }
}
