package com.aashish.hospital_management_system.service;

import com.aashish.hospital_management_system.configuration.exception.BadRequestException;
import com.aashish.hospital_management_system.configuration.exception.NotFoundException;
import com.aashish.hospital_management_system.constants.ExceptionCommonMessages;
import com.aashish.hospital_management_system.entity.Doctor;
import com.aashish.hospital_management_system.entity.User;
import com.aashish.hospital_management_system.repository.DoctorRepository;
import com.aashish.hospital_management_system.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class DoctorService {

    private final DoctorRepository doctorRepository;
    private final UserRepository userRepository;

    // Add a new doctor
    @Transactional
    public String addDoctor(Doctor doctor) {
        if (doctor == null || doctor.getUser() == null || doctor.getUser().getId() == null) {
            throw new BadRequestException("Invalid doctor object or missing user ID.");
        }

        User user = userRepository.findById(doctor.getUser().getId())
                .orElseThrow(() -> new NotFoundException("User with given ID does not exist"));

        if (!user.getRoleNames().contains("DOCTOR")) {
            throw new BadRequestException("User does not have the 'DOCTOR' role");
        }

        doctorRepository.save(doctor);
        return "Doctor added successfully";
    }

    // Get all doctors
    @Transactional(readOnly = true)
    public Iterable<Doctor> getAllDoctors() {
        return doctorRepository.findAll();
    }

    // Update a doctor
    @Transactional
    public Doctor updateDoctor(Integer doctorId, Doctor updatedDoctor) {
        if (updatedDoctor == null) {
            throw new BadRequestException("Doctor object is null");
        }

        Doctor existingDoctor = doctorRepository.findById(doctorId)
                .orElseThrow(() -> new NotFoundException(ExceptionCommonMessages.DOCTOR_NOT_FOUND));

        // Ensure user ID is not changed
        if (!existingDoctor.getUser().getId().equals(updatedDoctor.getUser().getId())) {
            throw new BadRequestException("Cannot change the user ID of an existing doctor");
        }

        // Update non-null fields using Optional.ofNullable()
        Optional.ofNullable(updatedDoctor.getName()).ifPresent(existingDoctor::setName);
        Optional.ofNullable(updatedDoctor.getSpecialization()).ifPresent(existingDoctor::setSpecialization);

        return doctorRepository.save(existingDoctor);
    }

    // Delete a doctor by ID
    @Transactional
    public String deleteDoctor(Integer doctorId) {
        if (!doctorRepository.existsById(doctorId)) {
            throw new NotFoundException(ExceptionCommonMessages.DOCTOR_NOT_FOUND);
        }

        doctorRepository.deleteById(doctorId);
        return "Doctor deleted successfully";
    }

    // Get a doctor by ID
    @Transactional(readOnly = true)
    public Doctor getDoctorById(Integer id) {
        return doctorRepository.findById(id)
                .orElseThrow(() -> new NotFoundException(ExceptionCommonMessages.DOCTOR_NOT_FOUND));
    }
}
