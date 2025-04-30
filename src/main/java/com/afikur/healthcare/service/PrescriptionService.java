package com.afikur.healthcare.service;

import com.afikur.healthcare.model.Prescription;

import java.util.List;

public interface PrescriptionService {
    Prescription createPrescription(Prescription prescription);

    List<Prescription> getPrescriptionsByPatient(Long patientId);

    List<Prescription> getAllPrescriptions();
}
