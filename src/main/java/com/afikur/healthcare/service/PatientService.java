package com.afikur.healthcare.service;

import com.afikur.healthcare.model.Patient;

import java.util.List;

public interface PatientService {
    Patient createPatient(Patient patient);

    Patient updatePatient(Long id, Patient patientDetails);

    List<Patient> getAllPatients();

    Patient getPatientById(Long id);
}
