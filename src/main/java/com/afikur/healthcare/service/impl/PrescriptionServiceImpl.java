package com.afikur.healthcare.service.impl;

import com.afikur.healthcare.model.Prescription;
import com.afikur.healthcare.repository.PrescriptionRepository;
import com.afikur.healthcare.service.PrescriptionService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.Paragraph;
import com.itextpdf.layout.element.Table;
import com.itextpdf.layout.properties.UnitValue;

import java.io.ByteArrayOutputStream;
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

    @Override
    public Prescription getPrescriptionById(Long id) {
        return prescriptionRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Prescription not found"));
    }

    @Override
    public Prescription updatePrescription(Long id, Prescription prescriptionDetails) {
        Prescription prescription = prescriptionRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Prescription not found"));

        prescription.setPatient(prescriptionDetails.getPatient());
        prescription.setIssuedDate(prescriptionDetails.getIssuedDate());

        // Update medications: clear existing and add new ones
        prescription.getMedications().clear();
        if (prescriptionDetails.getMedications() != null) {
            prescriptionDetails.getMedications().forEach(med -> {
                med.setPrescription(prescription);
                prescription.getMedications().add(med);
            });
        }

        return prescriptionRepository.save(prescription);
    }

    @Override
    public void deletePrescription(Long id) {
        Prescription prescription = prescriptionRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Prescription not found"));
        prescriptionRepository.delete(prescription);
    }

    @Override
    public byte[] generatePrescriptionPdf(Long prescriptionId) {
        Prescription prescription = getPrescriptionById(prescriptionId);
        ByteArrayOutputStream baos = new ByteArrayOutputStream();

        try {
            PdfWriter writer = new PdfWriter(baos);
            PdfDocument pdf = new PdfDocument(writer);
            Document document = new Document(pdf);

            // Title
            document.add(new Paragraph("Prescription Report")
                    .setBold()
                    .setFontSize(16));

            // Patient Details
            document.add(new Paragraph("\nPatient Details")
                    .setBold()
                    .setFontSize(12));
            document.add(new Paragraph("Name: " + prescription.getPatient().getUser().getName()));
            document.add(new Paragraph("Date of Birth: " + prescription.getPatient().getDateOfBirth()));
            document.add(new Paragraph("Medical History: " + (prescription.getPatient().getMedicalHistory() != null ? prescription.getPatient().getMedicalHistory() : "N/A")));

            // Prescription Details
            document.add(new Paragraph("\nPrescription Details")
                    .setBold()
                    .setFontSize(12));
            document.add(new Paragraph("Issued Date: " + prescription.getIssuedDate()));
            document.add(new Paragraph("Prescribing Doctor: " + prescription.getDoctor().getName()));

            // Medications Table
            document.add(new Paragraph("\nMedications")
                    .setBold()
                    .setFontSize(12));
            Table table = new Table(UnitValue.createPercentArray(new float[]{50, 50}));
            table.setWidth(UnitValue.createPercentValue(100));
            table.addHeaderCell("Medication");
            table.addHeaderCell("Dosage");
            for (var med : prescription.getMedications()) {
                table.addCell(med.getName());
                table.addCell(med.getDosage());
            }
            document.add(table);

            document.close();
        } catch (Exception e) {
            throw new RuntimeException("Error generating PDF", e);
        }

        return baos.toByteArray();
    }
}