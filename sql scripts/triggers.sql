-- Create Audit Table
CREATE TABLE prescription_audit (
    audit_id BIGSERIAL PRIMARY KEY,
    prescription_id BIGINT,
    action VARCHAR(50),
    action_date TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    patient_id BIGINT,
    doctor_id BIGINT
);

-- Create Trigger Function
CREATE OR REPLACE FUNCTION log_prescription_insert()
RETURNS TRIGGER AS $$
BEGIN
INSERT INTO prescription_audit (prescription_id, action, patient_id, doctor_id)
VALUES (NEW.id, 'INSERT', NEW.patient_id, NEW.doctor_id);
RETURN NEW;
END;
$$ LANGUAGE plpgsql;

-- Create Trigger
CREATE TRIGGER prescription_insert_trigger
    AFTER INSERT ON prescription
    FOR EACH ROW
    EXECUTE FUNCTION log_prescription_insert();
