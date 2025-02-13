package com.aashish.hospital_management_system.controller;

import com.aashish.hospital_management_system.constants.UserPermissions;
import com.aashish.hospital_management_system.entity.Doctor;
import com.aashish.hospital_management_system.service.DoctorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/doctors")
public class DoctorController {

    private final DoctorService doctorService;

    @Autowired
    public DoctorController(DoctorService doctorService) {
        this.doctorService = doctorService;
    }

    // Add a new doctor (Only users with 'add_doctor' permission can add doctors)
    @PostMapping("/addDoctor")
    @PreAuthorize("hasAuthority('" + UserPermissions.WRITE_DOCTOR + "')")
    public ResponseEntity<String> addDoctor(@RequestBody Doctor doctor) {
        String savedDoctor = doctorService.addDoctor(doctor);
        return ResponseEntity.ok(savedDoctor);
    }

    // Get all doctors (Accessible to users with 'view_all_doctors' permission)
    @GetMapping("/getAllDoctors")
    @PreAuthorize("hasAuthority('" + UserPermissions.READ_ALL_DOCTOR + "')")
    public ResponseEntity<List<Doctor>> getAllDoctors() {
        List<Doctor> doctors = (List<Doctor>) doctorService.getAllDoctors();
        return ResponseEntity.ok(doctors);
    }

    // Get a doctor by ID (Accessible to users with 'view_all_doctors' permission)
    @GetMapping("/{id}/getDoctor")
    @PreAuthorize("hasAuthority('" + UserPermissions.READ_DOCTOR + "') or hasAuthority('" + UserPermissions.READ_ALL_DOCTOR + "')")
    public ResponseEntity<Doctor> getDoctorById(@PathVariable Integer id) {
        Doctor doctor = doctorService.getDoctorById(id);
        return ResponseEntity.ok(doctor);
    }

    // Update a doctor (Only users with 'update_doctor' permission can update doctors)
    @PutMapping("/{id}/updateDoctor")
    @PreAuthorize("hasAuthority('" + UserPermissions.WRITE_DOCTOR + "')")
    public ResponseEntity<Doctor> updateDoctor(@PathVariable Integer id, @RequestBody Doctor updatedDoctor) {
        Doctor updated = doctorService.updateDoctor(id, updatedDoctor);
        return ResponseEntity.ok(updated);
    }

    // Delete a doctor by ID (Only users with 'delete_doctor' permission can delete doctors)
    @DeleteMapping("/{id}/deleteDoctor")
    @PreAuthorize("hasAuthority('" + UserPermissions.WRITE_DOCTOR + "')")
    public ResponseEntity<String> deleteDoctor(@PathVariable Integer id) {
        String response = doctorService.deleteDoctor(id);
        return ResponseEntity.ok(response);
    }
}