package com.afikur.healthcare.service.impl;

import com.afikur.healthcare.repository.PatientRepository;
import com.afikur.healthcare.repository.PrescriptionRepository;
import com.afikur.healthcare.repository.UserRepository;
import com.afikur.healthcare.service.AdminService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AdminServiceImpl implements AdminService {
    private final UserRepository userRepository;
    private final PatientRepository patientRepository;
    private final PrescriptionRepository prescriptionRepository;

    @Override
    public long getTotalUsers() {
        return userRepository.count();
    }

    @Override
    public long getTotalPatients() {
        return patientRepository.count();
    }

    @Override
    public long getTotalPrescriptions() {
        return prescriptionRepository.count();
    }
}
