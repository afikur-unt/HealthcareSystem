package com.afikur.healthcare.repository;

import com.afikur.healthcare.model.Patient;
import com.afikur.healthcare.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface PatientRepository extends JpaRepository<Patient, Long> {
    Optional<Patient> findByUser(User user);
}