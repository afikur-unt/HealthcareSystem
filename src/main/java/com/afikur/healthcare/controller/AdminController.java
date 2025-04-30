package com.afikur.healthcare.controller;

import com.afikur.healthcare.service.AdminService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping()
@RequiredArgsConstructor
public class AdminController {

    private final AdminService adminService;

    @GetMapping("/dashboard")
    public String getDashboard(Model model) {
        model.addAttribute("totalUsers", adminService.getTotalUsers());
        model.addAttribute("totalPatients", adminService.getTotalPatients());
        model.addAttribute("totalPrescriptions", adminService.getTotalPrescriptions());
        model.addAllAttributes(adminService.getDashboardAnalytics());
        return "admin/dashboard";
    }
}