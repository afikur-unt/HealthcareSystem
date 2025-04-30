package com.afikur.healthcare.controller;
import com.afikur.healthcare.service.PrescriptionService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/patient")
@RequiredArgsConstructor
public class PatientController {

    private final PrescriptionService prescriptionService;

    @GetMapping("/portal/{patientId}")
    public String getPatientPortal(@PathVariable Long patientId, Model model) {
        model.addAttribute("prescriptions", prescriptionService.getPrescriptionsByPatient(patientId));
        return "patient/portal";
    }
}