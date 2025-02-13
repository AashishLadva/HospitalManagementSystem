package com.aashish.hospital_management_system.controller;

import com.aashish.hospital_management_system.constants.UserPermissions;
import com.aashish.hospital_management_system.entity.Patient;
import com.aashish.hospital_management_system.service.PatientService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

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
    public ResponseEntity<String> addPatient(@RequestBody Patient patient) {
        String savedPatient = patientService.addPatient(patient);
        return ResponseEntity.ok(savedPatient);
    }

    // Get a patient by ID (Accessible to users with 'view_own_patient' or 'view_all_patients' permission)
    @GetMapping("/{id}/getPatient")
    @PreAuthorize("hasAuthority('" + UserPermissions.READ_OWN_PATIENTS + "') or hasAuthority('" + UserPermissions.READ_ALL_PATIENTS + "')")
    public ResponseEntity<Patient> getPatientById(@PathVariable Integer id) {
        Patient patient = patientService.getPatientById(id);
        return ResponseEntity.ok(patient);
    }

    // Get all patients (Accessible to users with 'view_all_patients' permission)
    @GetMapping("/getAllPatients")
    @PreAuthorize("hasAuthority('" + UserPermissions.READ_ALL_PATIENTS + "')")
    public ResponseEntity<List<Patient>> getAllPatients() {
        List<Patient> patients = (List<Patient>) patientService.getAllPatients();
        return ResponseEntity.ok(patients);
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
    public ResponseEntity<String> updatePatient(@PathVariable Integer id, @RequestBody Patient patient) {
        String updatedPatient = patientService.updatePatient(id, patient);
        return ResponseEntity.ok(updatedPatient);
    }
}