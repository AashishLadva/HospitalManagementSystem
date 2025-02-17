package com.aashish.hospital_management_system.repository;

import com.aashish.hospital_management_system.entity.Appointment;
import com.aashish.hospital_management_system.entity.Doctor;
import com.aashish.hospital_management_system.entity.Patient;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.Optional;

@Repository
public interface AppointmentRepository extends JpaRepository<Appointment, Integer> {

    Optional<Appointment> findByPatientIdAndDoctorIdAndAppointmentDate(Integer id, Integer id1, LocalDate appointmentDate);

    boolean existsByPatientAndDoctorAndAppointmentDate(Patient patient, Doctor doctor, LocalDate appointmentDate);

    Page<Appointment> findAllByPatientId(Integer patientId, Pageable pageable);

    Optional<Appointment> findByPatientId(Integer id);
}
