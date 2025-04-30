package com.afikur.healthcare.service.impl;

import com.afikur.healthcare.model.Patient;
import com.afikur.healthcare.model.User;
import com.afikur.healthcare.repository.PatientRepository;
import com.afikur.healthcare.repository.UserRepository;
import com.afikur.healthcare.service.PatientService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class PatientServiceImpl implements PatientService {

    private final PatientRepository patientRepository;
    private final UserRepository userRepository;

    @Override
    public Patient createPatient(Patient patient) {
        User user = userRepository.findById(patient.getUser().getId())
                .orElseThrow(() -> new RuntimeException("User not found"));
        if (!user.getRoles().stream().anyMatch(role -> role.getName().equals("ROLE_PATIENT"))) {
            throw new RuntimeException("User must have ROLE_PATIENT");
        }
        // Check if user is already associated with a patient
        if (patientRepository.findByUser(user).isPresent()) {
            throw new RuntimeException("User is already a patient");
        }
        return patientRepository.save(patient);
    }

    @Override
    public Patient updatePatient(Long id, Patient patientDetails) {
        Patient patient = patientRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Patient not found"));
        patient.setDateOfBirth(patientDetails.getDateOfBirth());
        patient.setMedicalHistory(patientDetails.getMedicalHistory());
        return patientRepository.save(patient);
    }

    @Override
    public List<Patient> getAllPatients() {
        return patientRepository.findAll();
    }

    @Override
    public Patient getPatientById(Long id) {
        return patientRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Patient not found"));
    }
}
