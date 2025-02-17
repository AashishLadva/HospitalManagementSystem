package com.aashish.hospital_management_system.service;

import com.aashish.hospital_management_system.configuration.exception.BadRequestException;
import com.aashish.hospital_management_system.configuration.exception.NotFoundException;
import com.aashish.hospital_management_system.constants.ExceptionCommonMessages;
import com.aashish.hospital_management_system.entity.Doctor;
import com.aashish.hospital_management_system.entity.User;
import com.aashish.hospital_management_system.repository.DoctorRepository;
import com.aashish.hospital_management_system.repository.UserRepository;
import com.aashish.hospital_management_system.service.dto.DoctorDTO;
import com.aashish.hospital_management_system.service.dto.response.PaginatedResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
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
    public String addDoctor(DoctorDTO doctorDTO) {
        if (doctorDTO == null || doctorDTO.getUserId() == null) {
            throw new BadRequestException("Invalid doctor object or missing user ID.");
        }

        User user = userRepository.findById(doctorDTO.getUserId())
                .orElseThrow(() -> new NotFoundException("User with given ID does not exist"));

        if (!user.getRoleNames().contains("ROLE_DOCTOR")) {
            throw new BadRequestException("User does not have the 'ROLE_DOCTOR' role");
        }
        Doctor doctor = new Doctor();
        doctor.setName(doctorDTO.getName());
        doctor.setSpecialization(doctorDTO.getSpecialization());
        doctor.setUser(user);

        doctorRepository.save(doctor);

        return "Doctor added successfully";
    }

    // Get all doctors
    @Transactional(readOnly = true)
    public PaginatedResponse<DoctorDTO> getAllDoctors(Pageable pageable) {
        Page<Doctor> doctorPage = doctorRepository.findAll(pageable); // Fetch paginated data

        Page<DoctorDTO> doctorDTOPage = doctorPage.map(DoctorDTO::new);

        return PaginatedResponse.fromPage(doctorDTOPage);
    }


    // Update a doctor
    @Transactional
    public String updateDoctor(Integer doctorId, DoctorDTO updatedDoctor) {
        if (updatedDoctor == null) {
            throw new BadRequestException("Doctor object is null");
        }

        Doctor existingDoctor = doctorRepository.findById(doctorId)
                .orElseThrow(() -> new NotFoundException(ExceptionCommonMessages.DOCTOR_NOT_FOUND));

        // Ensure user ID is not changed
        if (!existingDoctor.getUser().getId().equals(updatedDoctor.getUserId())) {
            throw new BadRequestException("Cannot change the user ID of an existing doctor");
        }

        // Update non-null fields using Optional.ofNullable()
        Optional.ofNullable(updatedDoctor.getName()).ifPresent(existingDoctor::setName);
        Optional.ofNullable(updatedDoctor.getSpecialization()).ifPresent(existingDoctor::setSpecialization);

        doctorRepository.save(existingDoctor);

        return "Doctor updated successfully";
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
    public DoctorDTO getDoctorById(Integer id) {
        Doctor doctor = doctorRepository.findById(id)
                .orElseThrow(() -> new NotFoundException(ExceptionCommonMessages.DOCTOR_NOT_FOUND));
        return new DoctorDTO(doctor);
    }
}
