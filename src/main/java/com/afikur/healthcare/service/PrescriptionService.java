package com.afikur.healthcare.service;

import com.afikur.healthcare.model.Prescription;

import java.util.List;

public interface PrescriptionService {
    Prescription createPrescription(Prescription prescription);

    List<Prescription> getPrescriptionsByPatient(Long patientId);

    List<Prescription> getAllPrescriptions();

    Prescription getPrescriptionById(Long id);

    Prescription updatePrescription(Long id, Prescription prescriptionDetails);

    void deletePrescription(Long id);

    byte[] generatePrescriptionPdf(Long prescriptionId);
}
