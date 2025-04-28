package com.afikur.healthcare.service.impl;

import com.afikur.healthcare.dto.UserDto;
import com.afikur.healthcare.model.Role;
import com.afikur.healthcare.model.User;
import com.afikur.healthcare.repository.RoleRepository;
import com.afikur.healthcare.repository.UserRepository;
import com.afikur.healthcare.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public void saveUser(UserDto userDto) {
        User user = new User();
        user.setName(userDto.getName());
        user.setEmail(userDto.getEmail());
        user.setEnabled(true);
        user.setPassword(passwordEncoder.encode(userDto.getPassword()));

        Role role = roleRepository.findByName("ROLE_USER");

        if (role == null) {
            role = checkRoleExists();
        }
        user.setRoles(Arrays.asList(role));
        userRepository.save(user);

    }

    @Override
    public Optional<User> findByEmail(String email) {
        return userRepository.findByEmailIgnoreCase(email);
    }

    @Override
    public List<UserDto> findAllUsers() {
        List<User> users = userRepository.findAll();
        List<UserDto> userDtoList = new ArrayList<>();
        for (User user : users) {
            UserDto userDto = new UserDto();
            userDto.setId(user.getId());
            userDto.setName(user.getName());
            userDto.setEmail(user.getEmail());
            userDto.setEnabled(user.isEnabled());
            userDtoList.add(userDto);
        }
        return userDtoList;
    }

    private Role checkRoleExists() {
        Role role = new Role();
        role.setName("ROLE_USER");
        return roleRepository.save(role);
    }
}
