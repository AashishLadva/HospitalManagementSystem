package com.aashish.hospital_management_system.service.dto;

import com.aashish.hospital_management_system.entity.Doctor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class DoctorDTO {

    private Integer id;
    private Integer userId;
    private String name;
    private String specialization;

    public DoctorDTO(Doctor doctor) {
        this.id = doctor.getId();
        this.userId = doctor.getUser().getId();
        this.name = doctor.getName();
        this.specialization = doctor.getSpecialization();
    }
}