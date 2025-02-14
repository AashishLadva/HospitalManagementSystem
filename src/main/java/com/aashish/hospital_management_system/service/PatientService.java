package com.aashish.hospital_management_system.service;

import com.aashish.hospital_management_system.configuration.exception.BadRequestException;
import com.aashish.hospital_management_system.configuration.exception.NotFoundException;
import com.aashish.hospital_management_system.constants.ExceptionCommonMessages;
import com.aashish.hospital_management_system.entity.Patient;
import com.aashish.hospital_management_system.entity.User;
import com.aashish.hospital_management_system.repository.PatientRepository;
import com.aashish.hospital_management_system.repository.UserRepository;
import com.aashish.hospital_management_system.service.dto.PatientDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class PatientService {

    private final PatientRepository patientRepository;
    private final UserRepository userRepository;

    // Add a new patient
    @Transactional
    public String addPatient(PatientDTO patientDTO) {
        if (patientDTO == null || patientDTO.getUserId() == null) {
            throw new BadRequestException("Invalid patient object or missing user ID.");
        }

        User user = userRepository.findById(patientDTO.getUserId())
                .orElseThrow(() -> new NotFoundException("User with given ID does not exist"));

        if (!user.getRoleNames().contains("ROLE_PATIENT")) {
            throw new BadRequestException("User does not have the 'ROLE_PATIENT' role");
        }
        Patient patient = new Patient();

        patient.setDob(patientDTO.getDob());
        patient.setName(patientDTO.getName());
        patient.setAddress(patientDTO.getAddress());
        patient.setUser(user);

        patientRepository.save(patient);

        return "Patient added successfully";
    }

    // Get a patient by ID
    @Transactional(readOnly = true)
    public PatientDTO getPatientById(Integer id) {
        Patient patient = patientRepository.findById(id)
                .orElseThrow(() -> new NotFoundException(ExceptionCommonMessages.PATIENT_NOT_FOUND));
        return new PatientDTO(patient);
    }

    // Get all patients
    @Transactional(readOnly = true)
    public List<PatientDTO> getAllPatients() {
        List<Patient> allPatients = patientRepository.findAll();
        List<PatientDTO> patientDTOs = new ArrayList<>();
        allPatients.forEach(patientDTO -> patientDTOs.add(new PatientDTO(patientDTO)));
        return patientDTOs;
    }

    // Remove a patient by ID
    @Transactional
    public String removePatient(Integer id) {
        if (!patientRepository.existsById(id)) {
            throw new NotFoundException(ExceptionCommonMessages.PATIENT_NOT_FOUND);
        }

        patientRepository.deleteById(id);
        return "Patient removed successfully";
    }

    // Update a patient's details
    @Transactional
    public String updatePatient(Integer id, PatientDTO updatedPatient) {
        if (updatedPatient == null) {
            throw new BadRequestException("Patient object is null");
        }

        Patient existingPatient = patientRepository.findById(id)
                .orElseThrow(() -> new NotFoundException(ExceptionCommonMessages.PATIENT_NOT_FOUND));

        // Update non-null fields using Optional.ofNullable()
        Optional.ofNullable(updatedPatient.getName()).ifPresent(existingPatient::setName);
        Optional.ofNullable(updatedPatient.getDob()).ifPresent(existingPatient::setDob);
        Optional.ofNullable(updatedPatient.getAddress()).ifPresent(existingPatient::setAddress);

        patientRepository.save(existingPatient);

        return "Patient updated successfully";
    }
}
