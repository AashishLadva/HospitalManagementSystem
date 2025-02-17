package com.aashish.hospital_management_system.controller;

import com.aashish.hospital_management_system.constants.UserPermissions;
import com.aashish.hospital_management_system.service.PatientService;
import com.aashish.hospital_management_system.service.dto.PatientDTO;
import com.aashish.hospital_management_system.service.dto.response.PaginatedResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/patients")
public class PatientController {

    private final PatientService patientService;

    @Autowired
    public PatientController(PatientService patientService) {
        this.patientService = patientService;
    }

    // Add a new patient (Only users with 'add_patient' permission can add patients)
    @PostMapping("/addPatient")
    @PreAuthorize("hasAuthority('" + UserPermissions.WRITE_PATIENTS + "')")
    public ResponseEntity<String> addPatient(@RequestBody PatientDTO patient) {
        String savedPatient = patientService.addPatient(patient);
        return ResponseEntity.ok(savedPatient);
    }

    // Get a patient by ID (Accessible to users with 'view_own_patient' or 'view_all_patients' permission)
    @GetMapping("/{patientId}/getPatient")
    @PreAuthorize("hasAuthority('" + UserPermissions.READ_OWN_PATIENTS + "') or hasAuthority('" + UserPermissions.READ_ALL_PATIENTS + "')")
    public ResponseEntity<PatientDTO> getPatientById(@PathVariable Integer patientId) {
        return ResponseEntity.ok(patientService.getPatientById(patientId));
    }

    // Get all patients (Accessible to users with 'view_all_patients' permission)
    @GetMapping("/getAllPatients")
    @PreAuthorize("hasAuthority('" + UserPermissions.READ_ALL_PATIENTS + "')")
    public ResponseEntity<PaginatedResponse<PatientDTO>> getAllPatients(
            @PageableDefault(size = 10) Pageable pageable) {
        return ResponseEntity.ok(patientService.getAllPatients(pageable));
    }

    // Remove a patient by ID (Only users with 'delete_patient' permission can remove patients)
    @DeleteMapping("/{id}/deletePatient")
    @PreAuthorize("hasAuthority('" + UserPermissions.WRITE_PATIENTS + "')")
    public ResponseEntity<String> removePatient(@PathVariable Integer id) {
        String response = patientService.removePatient(id);
        return ResponseEntity.ok(response);
    }

    // Update a patient (Only users with 'update_own_patient' or 'update_all_patients' permission can update it)
    @PutMapping("/{id}/updatePatient")
    @PreAuthorize("hasAuthority('" + UserPermissions.WRITE_OWN_PATIENTS + "') or hasAuthority('" + UserPermissions.WRITE_PATIENTS + "')")
    public ResponseEntity<String> updatePatient(@PathVariable Integer id, @RequestBody PatientDTO patient) {
        String updatedPatient = patientService.updatePatient(id, patient);
        return ResponseEntity.ok(updatedPatient);
    }
}