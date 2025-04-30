package com.afikur.healthcare.service.impl;

import com.afikur.healthcare.model.Prescription;
import com.afikur.healthcare.repository.PrescriptionRepository;
import com.afikur.healthcare.service.PrescriptionService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class PrescriptionServiceImpl implements PrescriptionService {

    private final PrescriptionRepository prescriptionRepository;

    @Override
    public Prescription createPrescription(Prescription prescription) {
        if (prescription.getMedications() != null) {
            prescription.getMedications().forEach(med -> med.setPrescription(prescription));
        }
        return prescriptionRepository.save(prescription);
    }

    @Override
    public List<Prescription> getPrescriptionsByPatient(Long patientId) {
        return prescriptionRepository.findByPatientId(patientId);
    }

    @Override
    public List<Prescription> getAllPrescriptions() {
        return prescriptionRepository.findAll();
    }
}