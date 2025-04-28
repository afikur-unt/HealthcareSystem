package com.afikur.healthcare.advice;

import com.afikur.healthcare.dto.UserDetailModel;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ModelAttribute;

@ControllerAdvice
public class GlobalControllerAdvice {

    @ModelAttribute
    public void addUserDetailsToModel(Model model) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth != null && auth.getPrincipal() instanceof UserDetailModel userDetail) {

            model.addAttribute("email", userDetail.getUsername());
            model.addAttribute("name", userDetail.getName());
        }
    }
}
