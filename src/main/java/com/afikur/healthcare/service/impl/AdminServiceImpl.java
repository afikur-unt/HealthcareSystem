package com.afikur.healthcare.service.impl;

import com.afikur.healthcare.model.Medication;
import com.afikur.healthcare.repository.PatientRepository;
import com.afikur.healthcare.repository.PrescriptionRepository;
import com.afikur.healthcare.repository.UserRepository;
import com.afikur.healthcare.service.AdminService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.Period;
import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class AdminServiceImpl implements AdminService {
    private final UserRepository userRepository;
    private final PatientRepository patientRepository;
    private final PrescriptionRepository prescriptionRepository;

    @Override
    public long getTotalUsers() {
        return userRepository.count();
    }

    @Override
    public long getTotalPatients() {
        return patientRepository.count();
    }

    @Override
    public long getTotalPrescriptions() {
        return prescriptionRepository.count();
    }


    @Override
    public Map<String, Object> getDashboardAnalytics() {
        Map<String, Object> analytics = new HashMap<>();

        // Patient Distribution by Age Group
        Map<String, Long> ageDistribution = patientRepository.findAll().stream()
                .collect(Collectors.groupingBy(
                        patient -> {
                            int age = Period.between(patient.getDateOfBirth(), LocalDate.now()).getYears();
                            if (age < 18) return "0-17";
                            else if (age < 30) return "18-29";
                            else if (age < 50) return "30-49";
                            else return "50+";
                        },
                        Collectors.counting()
                ));
        List<String> ageLabels = Arrays.asList("0-17", "18-29", "30-49", "50+");
        List<Long> ageCounts = ageLabels.stream()
                .map(label -> ageDistribution.getOrDefault(label, 0L))
                .collect(Collectors.toList());
        analytics.put("ageLabels", ageLabels);
        analytics.put("ageCounts", ageCounts);

        // Prescription Count by Month
        int currentYear = LocalDate.now().getYear();
        Map<Integer, Long> prescriptionByMonth = prescriptionRepository.findAll().stream()
                .filter(p -> p.getIssuedDate().getYear() == currentYear)
                .collect(Collectors.groupingBy(
                        p -> p.getIssuedDate().getMonthValue(),
                        Collectors.counting()
                ));
        List<String> monthLabels = Arrays.asList("Jan", "Feb", "Mar", "Apr", "May", "Jun", "Jul", "Aug", "Sep", "Oct", "Nov", "Dec");
        List<Long> prescriptionCounts = new ArrayList<>(Collections.nCopies(12, 0L));
        prescriptionByMonth.forEach((month, count) -> prescriptionCounts.set(month - 1, count));
        analytics.put("monthLabels", monthLabels);
        analytics.put("prescriptionCounts", prescriptionCounts);

        // Medication Frequency (Top 5)
        Map<String, Long> medicationFrequency = prescriptionRepository.findAll().stream()
                .flatMap(p -> p.getMedications().stream())
                .collect(Collectors.groupingBy(
                        Medication::getName,
                        Collectors.counting()
                ));
        List<Map.Entry<String, Long>> topMedications = medicationFrequency.entrySet().stream()
                .sorted(Map.Entry.<String, Long>comparingByValue().reversed())
                .limit(5)
                .collect(Collectors.toList());
        List<String> medicationLabels = topMedications.stream()
                .map(Map.Entry::getKey)
                .collect(Collectors.toList());
        List<Long> medicationCounts = topMedications.stream()
                .map(Map.Entry::getValue)
                .collect(Collectors.toList());
        analytics.put("medicationLabels", medicationLabels);
        analytics.put("medicationCounts", medicationCounts);

        // Doctor Prescription Load
        Map<String, Long> doctorPrescriptionLoad = prescriptionRepository.findAll().stream()
                .collect(Collectors.groupingBy(
                        p -> p.getDoctor().getName(),
                        Collectors.counting()
                ));
        List<String> doctorLabels = doctorPrescriptionLoad.keySet().stream()
                .sorted()
                .collect(Collectors.toList());
        List<Long> doctorCounts = doctorLabels.stream()
                .map(label -> doctorPrescriptionLoad.getOrDefault(label, 0L))
                .collect(Collectors.toList());
        analytics.put("doctorLabels", doctorLabels);
        analytics.put("doctorCounts", doctorCounts);

        return analytics;
    }
}
