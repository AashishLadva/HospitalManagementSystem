package com.aashish.hospital_management_system.service.dto;

import com.aashish.hospital_management_system.entity.Patient;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@NoArgsConstructor
public class PatientDTO {

    private Integer id;

    private Integer userId;

    private String name;

    private LocalDate dob;

    private String address;

    public PatientDTO(Patient patient) {
        this.id = patient.getId();
        this.userId = patient.getUser().getId();
        this.name = patient.getName();
        this.dob = patient.getDob();
        this.address = patient.getAddress();
    }
}