package com.afikur.healthcare.service;


import com.afikur.healthcare.dto.UserDto;
import com.afikur.healthcare.model.User;

import java.util.List;
import java.util.Optional;

public interface UserService {
    void saveUser(UserDto userDto);

    Optional<User> findByEmail(String email);

    List<UserDto> findAllUsers();
}
