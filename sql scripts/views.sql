-- View: Prescription Details
CREATE OR REPLACE VIEW prescription_details_view AS
SELECT
    p.id AS prescription_id,
    p.issued_date,
    pt.id AS patient_id,
    pt.user_id AS patient_user_id,
    u1.name AS patient_name,
    u2.name AS doctor_name,
    m.name AS medication_name,
    m.dosage
FROM prescription p
         JOIN patient pt ON p.patient_id = pt.id
         JOIN "user" u1 ON pt.user_id = u1.id
         JOIN "user" u2 ON p.doctor_id = u2.id
         LEFT JOIN medication m ON p.id = m.prescription_id
ORDER BY p.issued_date DESC;