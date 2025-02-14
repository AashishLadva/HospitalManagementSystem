package com.aashish.hospital_management_system.service.dto;

import com.aashish.hospital_management_system.entity.Appointment;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true) // Ignore unknown fields in the request
public class AppointmentDTO {
    private Integer id;
    private Integer patientId;
    private Integer doctorId;
    private LocalDate appointmentDate;

    @JsonInclude(JsonInclude.Include.NON_NULL) // Exclude null fields in the response
    @JsonProperty(access = JsonProperty.Access.READ_ONLY) // Prevent this field from being set in requests
    private Appointment.Status status;

    private String doctorName;
    private String patientName;

    public AppointmentDTO(Appointment appointment) {
        this.id = appointment.getId();
        this.patientId = appointment.getPatient().getId();
        this.doctorId = appointment.getDoctor().getId();
        this.appointmentDate = appointment.getAppointmentDate();
        this.status = appointment.getStatus();
        this.doctorName = appointment.getDoctor().getName();
        this.patientName = appointment.getPatient().getName();
    }
}

