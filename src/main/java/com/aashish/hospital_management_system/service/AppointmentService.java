package com.aashish.hospital_management_system.service;

import com.aashish.hospital_management_system.configuration.exception.BadRequestException;
import com.aashish.hospital_management_system.configuration.exception.NotFoundException;
import com.aashish.hospital_management_system.constants.ExceptionCommonMessages;
import com.aashish.hospital_management_system.entity.Appointment;
import com.aashish.hospital_management_system.entity.Doctor;
import com.aashish.hospital_management_system.entity.Patient;
import com.aashish.hospital_management_system.repository.AppointmentRepository;
import com.aashish.hospital_management_system.repository.DoctorRepository;
import com.aashish.hospital_management_system.repository.PatientRepository;
import com.aashish.hospital_management_system.service.dto.response_dto.AppointmentDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;

@Service
@RequiredArgsConstructor
public class AppointmentService {

    private final AppointmentRepository appointmentRepository;
    private final PatientRepository patientRepository;
    private final DoctorRepository doctorRepository;

    // Book an appointment
    @Transactional
    public String bookAppointment(Appointment appointment) {
        if (appointment == null) {
            throw new BadRequestException("Appointment object is null");
        }
        if (appointment.getAppointmentDate() == null) {
            appointment.setAppointmentDate(LocalDate.now());
        }

        // Validate Patient and Doctor existence
        Patient patient = patientRepository.findById(appointment.getPatient().getId())
                .orElseThrow(() -> new NotFoundException(ExceptionCommonMessages.PATIENT_NOT_FOUND));
        Doctor doctor = doctorRepository.findById(appointment.getDoctor().getId())
                .orElseThrow(() -> new NotFoundException(ExceptionCommonMessages.DOCTOR_NOT_FOUND));

        // Check if appointment already exists
        boolean isBooked = appointmentRepository.existsByPatientAndDoctorAndAppointmentDate(
                patient, doctor, appointment.getAppointmentDate());
        if (isBooked) {
            throw new BadRequestException("Appointment is already booked.");
        }

        appointment.setStatus(Appointment.Status.PENDING);
        appointmentRepository.save(appointment);
        return "Appointment booked successfully.";
    }

    // Get all appointments
    @Transactional(readOnly = true)
    public List<AppointmentDTO> getAllAppointments() {
        return appointmentRepository.findAll().stream()
                .map(appointment -> new AppointmentDTO(
                        appointment.getId(),
                        appointment.getPatient().getId(),
                        appointment.getDoctor().getId(),
                        appointment.getAppointmentDate(),
                        appointment.getStatus(),
                        appointment.getDoctor().getName(),
                        appointment.getPatient().getName()
                ))
                .toList();
    }

    // Get appointments by patient ID
    @Transactional(readOnly = true)
    public List<AppointmentDTO> getAppointmentsByPatientId(Integer patientId) {
        List<Appointment> appointments = appointmentRepository.findAllByPatientId(patientId);
        if (appointments.isEmpty()) {
            throw new NotFoundException(ExceptionCommonMessages.APPOINTMENT_NOT_FOUND);
        }

        return appointments.stream()
                .map(appointment -> new AppointmentDTO(
                        appointment.getId(),
                        appointment.getPatient().getId(),
                        appointment.getDoctor().getId(),
                        appointment.getAppointmentDate(),
                        appointment.getStatus(),
                        appointment.getDoctor().getName(),
                        appointment.getPatient().getName()
                ))
                .toList();
    }

    // Approve an appointment
    @Transactional
    public String approveAppointment(Integer appointmentId) {
        Appointment appointment = appointmentRepository.findById(appointmentId)
                .orElseThrow(() -> new NotFoundException(ExceptionCommonMessages.APPOINTMENT_NOT_FOUND));
        appointment.setStatus(Appointment.Status.CONFIRMED);
        appointmentRepository.save(appointment);
        return "Appointment approved successfully.";
    }

    // Cancel an appointment
    @Transactional
    public String cancelAppointment(Integer appointmentId) {
        Appointment appointment = appointmentRepository.findById(appointmentId)
                .orElseThrow(() -> new NotFoundException(ExceptionCommonMessages.APPOINTMENT_NOT_FOUND));
        appointment.setStatus(Appointment.Status.CANCELLED);
        appointmentRepository.save(appointment);
        return "Appointment cancelled successfully.";
    }
}
