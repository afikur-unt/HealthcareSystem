package com.afikur.healthcare.controller;
import com.afikur.healthcare.model.User;
import com.afikur.healthcare.service.PrescriptionService;
import com.afikur.healthcare.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import java.security.Principal;

@Controller
@RequestMapping("/patient")
@RequiredArgsConstructor
public class PatientController {

    private final PrescriptionService prescriptionService;
    private final UserService userService;

    @GetMapping("/portal")
    public String getPatientPortal(Model model, Principal principal) {
        User loggedinUser = getLoggedinUser(principal);
        model.addAttribute("prescriptions", prescriptionService.getPrescriptionsByPatient(loggedinUser.getId()));
        return "patient/portal";
    }

    private User getLoggedinUser(Principal principal) {
        String email = principal.getName();
        User user = userService.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("Doctor not found"));
        return user;
    }
}