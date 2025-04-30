-- Index on user.email for login and lookup
CREATE INDEX idx_user_email ON "user" (email);

-- Index on patient.user_id for patient lookups
CREATE INDEX idx_patient_user_id ON patient (user_id);

-- Indexes on prescription.patient_id and doctor_id for filtering
CREATE INDEX idx_prescription_patient_id ON prescription (patient_id);
CREATE INDEX idx_prescription_doctor_id ON prescription (doctor_id);
CREATE INDEX idx_prescription_issued_date ON prescription (issued_date);

-- Index on medication.prescription_id for medication retrieval
CREATE INDEX idx_medication_prescription_id ON medication (prescription_id);
