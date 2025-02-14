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
import com.aashish.hospital_management_system.service.dto.AppointmentDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class AppointmentService {

    private final AppointmentRepository appointmentRepository;
    private final PatientRepository patientRepository;
    private final DoctorRepository doctorRepository;

    // Book an appointment
    @Transactional
    public String bookAppointment(AppointmentDTO appointmentDTO) {
        if (appointmentDTO == null) {
            throw new BadRequestException("Appointment object is null");
        }
        if (appointmentDTO.getAppointmentDate() == null) {
            appointmentDTO.setAppointmentDate(LocalDate.now());
        }

        // Validate Patient and Doctor existence
        Patient patient = patientRepository.findById(appointmentDTO.getPatientId())
                .orElseThrow(() -> new NotFoundException(ExceptionCommonMessages.PATIENT_NOT_FOUND));
        Doctor doctor = doctorRepository.findById(appointmentDTO.getDoctorId())
                .orElseThrow(() -> new NotFoundException(ExceptionCommonMessages.DOCTOR_NOT_FOUND));

        // Check if appointment already exists
        boolean isBooked = appointmentRepository.existsByPatientAndDoctorAndAppointmentDate(
                patient, doctor, appointmentDTO.getAppointmentDate());
        if (isBooked) {
            throw new BadRequestException("Appointment is already booked.");
        }

        Appointment appointment = new Appointment();
        appointment.setAppointmentDate(appointmentDTO.getAppointmentDate());
        appointment.setStatus(Appointment.Status.PENDING);
        appointment.setDoctor(doctor);
        appointment.setPatient(patient);
        appointmentRepository.save(appointment);
        return "Appointment booked successfully.";
    }

    // Get all appointments
    @Transactional(readOnly = true)
    public List<AppointmentDTO> getAllAppointments() {
        List<Appointment> allAppointments = appointmentRepository.findAll();
        List<AppointmentDTO> appointmentDTOS = new ArrayList<>();
        allAppointments.forEach(appointment -> appointmentDTOS.add(new AppointmentDTO(appointment)));
        return appointmentDTOS;
    }

    // Get appointments by patient ID
    @Transactional(readOnly = true)
    public List<AppointmentDTO> getAppointmentsByPatientId(Integer patientId) {
        List<Appointment> allAppointments = appointmentRepository.findAllByPatientId(patientId);
        if (allAppointments.isEmpty()) {
            throw new NotFoundException(ExceptionCommonMessages.APPOINTMENT_NOT_FOUND);
        }
        List<AppointmentDTO> appointmentDTOS = new ArrayList<>();
        allAppointments.forEach(appointment -> appointmentDTOS.add(new AppointmentDTO(appointment)));
        return appointmentDTOS;
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
