package com.afikur.healthcare.controller;

import com.afikur.healthcare.model.Medication;
import com.afikur.healthcare.model.Patient;
import com.afikur.healthcare.model.Prescription;
import com.afikur.healthcare.model.User;
import com.afikur.healthcare.service.PatientService;
import com.afikur.healthcare.service.PrescriptionService;
import com.afikur.healthcare.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;

@Controller
@RequestMapping("/doctor")
@RequiredArgsConstructor
public class DoctorController {

    private final PatientService patientService;
    private final PrescriptionService prescriptionService;
    private final UserService userService;

    @GetMapping("/patients")
    public String getPatientManagement(Model model) {
        model.addAttribute("patients", patientService.getAllPatients());
        model.addAttribute("patient", new Patient());
        model.addAttribute("users", userService.getUsersByRole("ROLE_PATIENT"));
        return "doctor/patient-management";
    }

    @PostMapping("/patients")
    public String createPatient(@ModelAttribute Patient patient) {
        patientService.createPatient(patient);
        return "redirect:/doctor/patients";
    }

    @GetMapping("/patients/edit/{id}")
    public String editPatient(@PathVariable Long id, Model model) {
        model.addAttribute("patient", patientService.getPatientById(id));
        model.addAttribute("users", userService.getUsersByRole("ROLE_PATIENT"));
        return "doctor/patient-management";
    }


    @PostMapping("/patients/update")
    public String updatePatient(@ModelAttribute Patient patient) {
        patientService.updatePatient(patient.getId(), patient);
        return "redirect:/doctor/patients";
    }

    @GetMapping("/prescriptions")
    public String getPrescriptionManagement(Model model) {
        model.addAttribute("prescriptions", prescriptionService.getAllPrescriptions());
        model.addAttribute("prescription", new Prescription());
        model.addAttribute("patients", patientService.getAllPatients());
        return "doctor/prescription-management";
    }


    private User getLoggedinUser(Principal principal) {
        String email = principal.getName();
        User doctor = userService.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("Doctor not found"));
        return doctor;
    }

    @PostMapping("/prescriptions")
    public String createPrescription(@ModelAttribute Prescription prescription,
                                     @RequestParam("medicationNames") List<String> medicationNames,
                                     @RequestParam("dosages") List<String> dosages,
                                     Principal principal) {
        // Create medications from form data
        for (int i = 0; i < medicationNames.size(); i++) {
            Medication medication = new Medication();
            medication.setName(medicationNames.get(i));
            medication.setDosage(dosages.get(i));
            prescription.getMedications().add(medication);
        }
        User doctor = getLoggedinUser(principal);
        prescription.setDoctor(doctor);

        prescriptionService.createPrescription(prescription);
        return "redirect:/doctor/prescriptions";
    }
}
