-- Stored Procedure: Get Patient Prescriptions
CREATE OR REPLACE PROCEDURE get_patient_prescriptions(
    IN p_patient_id BIGINT,
    OUT p_result refcursor
)
LANGUAGE plpgsql
AS $$
BEGIN
OPEN p_result FOR
SELECT p.id, p.issued_date, u.name AS doctor_name, pt.user_id, m.name AS medication_name, m.dosage
FROM prescription p
         JOIN patient pt ON p.patient_id = pt.id
         JOIN "user" u ON p.doctor_id = u.id
         LEFT JOIN medication m ON p.id = m.prescription_id
WHERE p.patient_id = p_patient_id
ORDER BY p.issued_date DESC;
END;
$$;

-- Stored Procedure: Insert Prescription with Medications
CREATE OR REPLACE PROCEDURE insert_prescription(
    IN p_issued_date DATE,
    IN p_patient_id BIGINT,
    IN p_doctor_id BIGINT,
    IN p_medications JSONB
)
LANGUAGE plpgsql
AS $$
DECLARE
v_prescription_id BIGINT;
    v_med JSONB;
BEGIN
    -- Insert Prescription
INSERT INTO prescription (issued_date, patient_id, doctor_id)
VALUES (p_issued_date, p_patient_id, p_doctor_id)
    RETURNING id INTO v_prescription_id;

-- Insert Medications
FOR v_med IN SELECT * FROM jsonb_array_elements(p_medications)
                               LOOP
    INSERT INTO medication (name, dosage, prescription_id)
             VALUES (
                 (v_med->>'name')::TEXT,
                 (v_med->>'dosage')::TEXT,
                 v_prescription_id
                 );
END LOOP;

COMMIT;
EXCEPTION
    WHEN OTHERS THEN
        ROLLBACK;
        RAISE EXCEPTION 'Error inserting prescription: %', SQLERRM;
END;
$$;