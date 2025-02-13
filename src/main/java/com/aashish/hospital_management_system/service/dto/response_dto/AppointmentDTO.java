package com.aashish.hospital_management_system.service.dto.response_dto;

import com.aashish.hospital_management_system.entity.Appointment;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDate;

@Data
@AllArgsConstructor
public class AppointmentDTO {
    private Integer id;
    private Integer patientId;
    private Integer doctorId;
    private LocalDate appointmentDate;
    private Appointment.Status status;
    private String doctorName;
    private String patientName;
}
