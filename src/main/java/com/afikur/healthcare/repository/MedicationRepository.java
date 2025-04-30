package com.afikur.healthcare.repository;

import com.afikur.healthcare.model.Medication;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MedicationRepository extends JpaRepository<Medication, Long> {
}