package com.afikur.healthcare.controller;

import com.afikur.healthcare.dto.UserDto;
import com.afikur.healthcare.model.User;
import com.afikur.healthcare.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

import java.util.Optional;

@Controller
@RequiredArgsConstructor
public class AuthController {
    private final UserService userService;

    @GetMapping("/login")
    public String login() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.isAuthenticated()
                && !(authentication instanceof AnonymousAuthenticationToken)) {
            return "redirect:/dashboard";
        }
        return "login";
    }

    @GetMapping("/register")
    public String showRegistrationForm(Model model) {

        UserDto userDto = new UserDto();
        model.addAttribute("user", userDto);
        return "register";

    }

    @PostMapping("/register/save")
    public String registration(@Valid @ModelAttribute("user") UserDto userDto, BindingResult result, Model model) { //Model attribute is used to extract model object which is a form data.


        Optional<User> existingUser = userService.findByEmail(userDto.getEmail());

        if (existingUser.isPresent()) {
            result.rejectValue("email", null, "There is already an account existed with this email");
        }

        if (result.hasErrors()) {
            model.addAttribute("user", userDto);
            return "register";
        }

        userService.saveUser(userDto);
        return "redirect:/register?success";

    }
}

