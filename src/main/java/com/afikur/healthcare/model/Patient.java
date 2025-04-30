package com.afikur.healthcare.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;

@Entity
@Getter
@Setter
@NoArgsConstructor
public class Patient {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate dateOfBirth;
    private String medicalHistory;

    @OneToOne
    @JoinColumn(name = "user_id")
    private User user;

    public Patient(LocalDate dateOfBirth, String medicalHistory) {
        this.dateOfBirth = dateOfBirth;
        this.medicalHistory = medicalHistory;
    }
}