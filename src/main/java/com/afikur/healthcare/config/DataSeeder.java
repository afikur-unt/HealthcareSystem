package com.afikur.healthcare.config;

import com.afikur.healthcare.model.Medication;
import com.afikur.healthcare.model.Patient;
import com.afikur.healthcare.model.Prescription;
import com.afikur.healthcare.model.Role;
import com.afikur.healthcare.model.User;
import com.afikur.healthcare.repository.MedicationRepository;
import com.afikur.healthcare.repository.PatientRepository;
import com.afikur.healthcare.repository.PrescriptionRepository;
import com.afikur.healthcare.repository.RoleRepository;
import com.afikur.healthcare.repository.UserRepository;
import jakarta.transaction.Transactional;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.time.LocalDate;
import java.util.*;

@Configuration
public class DataSeeder {

    @Bean
    CommandLineRunner seedDatabase(
            UserRepository userRepository,
            PatientRepository patientRepository,
            RoleRepository roleRepository,
            PrescriptionRepository prescriptionRepository,
            MedicationRepository medicationRepository,
            PasswordEncoder passwordEncoder) {
        return new CommandLineRunner() {
            @Override
            @Transactional
            public void run(String... args) throws Exception {
                DataSeeder.this.seedData(
                        userRepository,
                        patientRepository,
                        roleRepository,
                        prescriptionRepository,
                        medicationRepository,
                        passwordEncoder);
            }
        };
    }

    public void seedData(
            UserRepository userRepository,
            PatientRepository patientRepository,
            RoleRepository roleRepository,
            PrescriptionRepository prescriptionRepository,
            MedicationRepository medicationRepository,
            PasswordEncoder passwordEncoder) {

        // Create Roles
        String[] roleNames = {"ROLE_ADMIN", "ROLE_PATIENT", "ROLE_DOCTOR"};
        Map<String, Role> rolesMap = createRoles(roleRepository, roleNames);

        // Create Users
        List<User> users = Arrays.asList(
                new User("John Doe", "john.doe@example.com", "password123", true, Arrays.asList(rolesMap.get("ROLE_PATIENT"), rolesMap.get("ROLE_ADMIN"))),
                new User("Jane Smith", "jane.smith@example.com", "password123", true, Collections.singletonList(rolesMap.get("ROLE_PATIENT"))),
                new User("Dr. Alice Brown", "alice.brown@example.com", "doctor123", true, Collections.singletonList(rolesMap.get("ROLE_DOCTOR"))),
                new User("Dr. Bob Wilson", "bob.wilson@example.com", "doctor123", true, Collections.singletonList(rolesMap.get("ROLE_DOCTOR"))),
                new User("Michael Lee", "michael.lee@example.com", "password123", true, Collections.singletonList(rolesMap.get("ROLE_PATIENT"))),
                new User("Sarah Davis", "sarah.davis@example.com", "password123", true, Collections.singletonList(rolesMap.get("ROLE_PATIENT"))),
                new User("Emily Clark", "emily.clark@example.com", "password123", true, Collections.singletonList(rolesMap.get("ROLE_PATIENT")))
        );

        createUsersWithRoles(userRepository, passwordEncoder, users);

        // Create Patients
        List<Patient> patients = Arrays.asList(
                new Patient(LocalDate.of(1990, 5, 15), "Asthma, seasonal allergies"),
                new Patient(LocalDate.of(1985, 8, 22), "Hypertension, no known allergies"),
                new Patient(LocalDate.of(1978, 11, 30), "Type 2 diabetes, hyperlipidemia"),
                new Patient(LocalDate.of(1995, 3, 12), "Migraine, no known allergies"),
                new Patient(LocalDate.of(2000, 7, 25), "Anxiety, eczema")
        );

        createPatients(patientRepository, userRepository, patients, Arrays.asList(
                "john.doe@example.com",
                "jane.smith@example.com",
                "michael.lee@example.com",
                "sarah.davis@example.com",
                "emily.clark@example.com"
        ));

        // Create Prescriptions
        List<Prescription> prescriptions = createPrescriptions(prescriptionRepository, patientRepository, userRepository);
        createMedications(medicationRepository, prescriptions);
    }

    private static Map<String, Role> createRoles(RoleRepository roleRepository, String[] roleNames) {
        Map<String, Role> rolesMap = new HashMap<>();

        for (String roleName : roleNames) {
            Role role = roleRepository.findByName(roleName)
                    .orElseGet(() -> {
                        Role newRole = new Role();
                        newRole.setName(roleName);
                        return roleRepository.save(newRole);
                    });
            rolesMap.put(roleName, role);
        }
        return rolesMap;
    }

    private static void createUsersWithRoles(UserRepository userRepository, PasswordEncoder passwordEncoder, List<User> users) {
        for (User user : users) {
            if (userRepository.findByEmailIgnoreCase(user.getEmail()).isEmpty()) {
                user.setPassword(passwordEncoder.encode(user.getPassword()));
                user.setRoles(new ArrayList<>(user.getRoles()));
                userRepository.save(user);
            }
        }
    }

    private static void createPatients(PatientRepository patientRepository, UserRepository userRepository, List<Patient> patients, List<String> patientEmails) {
        for (int i = 0; i < patients.size(); i++) {
            String email = patientEmails.get(i);
            Optional<User> user = userRepository.findByEmailIgnoreCase(email);
            if (user.isPresent() && patientRepository.findByUser(user.get()).isEmpty()) {
                Patient patient = patients.get(i);
                patient.setUser(user.get());
                patientRepository.save(patient);
            }
        }
    }

    private static List<Prescription> createPrescriptions(PrescriptionRepository prescriptionRepository, PatientRepository patientRepository, UserRepository userRepository) {
        List<Prescription> prescriptions = new ArrayList<>();
        String[][] prescriptionData = {
                {"2025-03-01", "john.doe@example.com", "alice.brown@example.com"},
                {"2025-04-10", "john.doe@example.com", "bob.wilson@example.com"},
                {"2025-04-15", "jane.smith@example.com", "alice.brown@example.com"},
                {"2024-12-01", "michael.lee@example.com", "alice.brown@example.com"},
                {"2025-01-20", "michael.lee@example.com", "bob.wilson@example.com"},
                {"2025-02-15", "sarah.davis@example.com", "bob.wilson@example.com"},
                {"2025-03-10", "sarah.davis@example.com", "alice.brown@example.com"},
                {"2024-11-05", "emily.clark@example.com", "alice.brown@example.com"},
                {"2025-02-20", "emily.clark@example.com", "bob.wilson@example.com"},
                {"2025-04-01", "jane.smith@example.com", "bob.wilson@example.com"}
        };

        for (String[] data : prescriptionData) {
            LocalDate issuedDate = LocalDate.parse(data[0]);
            String patientEmail = data[1];
            String doctorEmail = data[2];

            Optional<User> patientUser = userRepository.findByEmailIgnoreCase(patientEmail);
            Optional<User> doctorUser = userRepository.findByEmailIgnoreCase(doctorEmail);

            if (patientUser.isPresent() && doctorUser.isPresent()) {
                Optional<Patient> patient = patientRepository.findByUser(patientUser.get());
                if (patient.isPresent()) {
                    Prescription prescription = new Prescription();
                    prescription.setIssuedDate(issuedDate);
                    prescription.setPatient(patient.get());
                    prescription.setDoctor(doctorUser.get());
                    prescriptions.add(prescriptionRepository.save(prescription));
                }
            }
        }
        return prescriptions;
    }

    private static void createMedications(MedicationRepository medicationRepository, List<Prescription> prescriptions) {
        String[][][] medicationData = {
                // Prescription 1 (John Doe, Asthma/Allergies)
                {{"Albuterol", "2 puffs every 4-6 hours"}, {"Loratadine", "10 mg daily"}},
                // Prescription 2 (John Doe, Other Needs)
                {{"Lisinopril", "10 mg daily"}, {"Ibuprofen", "200 mg as needed"}},
                // Prescription 3 (Jane Smith, Hypertension)
                {{"Amlodipine", "5 mg daily"}, {"Metoprolol", "25 mg twice daily"}},
                // Prescription 4 (Michael Lee, Diabetes/Hyperlipidemia)
                {{"Metformin", "500 mg twice daily"}, {"Atorvastatin", "20 mg daily"}, {"Aspirin", "81 mg daily"}},
                // Prescription 5 (Michael Lee, Diabetes Follow-up)
                {{"Insulin Glargine", "20 units daily"}, {"Rosuvastatin", "10 mg daily"}},
                // Prescription 6 (Sarah Davis, Migraine)
                {{"Sumatriptan", "50 mg as needed"}, {"Propranolol", "40 mg twice daily"}, {"Acetaminophen", "500 mg as needed"}},
                // Prescription 7 (Sarah Davis, Migraine Follow-up)
                {{"Topiramate", "25 mg twice daily"}, {"Ibuprofen", "400 mg as needed"}},
                // Prescription 8 (Emily Clark, Anxiety)
                {{"Sertraline", "50 mg daily"}, {"Hydroxyzine", "25 mg as needed"}, {"Lorazepam", "0.5 mg as needed"}},
                // Prescription 9 (Emily Clark, Eczema)
                {{"Hydrocortisone", "1% cream twice daily"}, {"Cetirizine", "10 mg daily"}, {"Triamcinolone", "0.1% ointment daily"}},
                // Prescription 10 (Jane Smith, Hypertension Follow-up)
                {{"Hydrochlorothiazide", "25 mg daily"}, {"Losartan", "50 mg daily"}, {"Aspirin", "81 mg daily"}}
        };

        for (int i = 0; i < prescriptions.size(); i++) {
            Prescription prescription = prescriptions.get(i);
            String[][] medications = medicationData[i];
            for (String[] medData : medications) {
                Medication medication = new Medication();
                medication.setName(medData[0]);
                medication.setDosage(medData[1]);
                medication.setPrescription(prescription);
                medicationRepository.save(medication);
            }
        }
    }
}