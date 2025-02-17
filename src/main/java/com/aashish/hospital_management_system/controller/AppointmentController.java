package com.aashish.hospital_management_system.controller;

import com.aashish.hospital_management_system.constants.UserPermissions;
import com.aashish.hospital_management_system.service.AppointmentService;
import com.aashish.hospital_management_system.service.dto.AppointmentDTO;
import com.aashish.hospital_management_system.service.dto.response.PaginatedResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/appointments")
public class AppointmentController {

    private final AppointmentService appointmentService;

    @Autowired
    public AppointmentController(AppointmentService appointmentService) {
        this.appointmentService = appointmentService;
    }

    // Book an appointment (Only users with 'book_appointment' permission can book appointments)
    @PostMapping("/bookAppointment")
    @PreAuthorize("hasAuthority('" + UserPermissions.WRITE_APPOINTMENT + "')")
    public ResponseEntity<String> bookAppointment(@RequestBody AppointmentDTO appointment) {
        String bookedAppointment = appointmentService.bookAppointment(appointment);
        return ResponseEntity.ok(bookedAppointment);
    }

    @GetMapping("/getAllAppointments")
    @PreAuthorize("hasAuthority('" + UserPermissions.READ_ALL_APPOINTMENTS + "')")
    public PaginatedResponse<AppointmentDTO> getAllAppointments(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        Pageable pageable = PageRequest.of(page, size);
        return appointmentService.getAllAppointments(pageable);
    }

    @GetMapping("/{patientId}/getAppointment")
    @PreAuthorize("hasAuthority('" + UserPermissions.READ_ALL_APPOINTMENTS + "') or hasAuthority('" + UserPermissions.READ_OWN_APPOINTMENT + "')")
    public PaginatedResponse<AppointmentDTO> getAppointmentsByPatientId(
            @PathVariable Integer patientId,
            @PageableDefault Pageable pageable) {
        return appointmentService.getAppointmentsByPatientId(patientId, pageable);
    }

    // Approve an appointment (Only users with 'approve_appointment' permission can approve appointments)
    @PutMapping("/{id}/approve")
    @PreAuthorize("hasAuthority('" + UserPermissions.ACTION_APPROVE_APPOINTMENT + "')")
    public ResponseEntity<String> approveAppointment(@PathVariable Integer id) {
        String approvedAppointment = appointmentService.approveAppointment(id);
        return ResponseEntity.ok(approvedAppointment);
    }

    // Cancel an appointment (Only users with 'cancel_own_appointment' or 'cancel_all_appointments' permission can cancel it)
    @PutMapping("/{id}/cancel")
    @PreAuthorize("hasAuthority('" + UserPermissions.ACTION_CANCEL_OWN_APPOINTMENT + "') or hasAuthority('" + UserPermissions.ACTION_CANCEL_ALL_APPOINTMENTS + "')")
    public ResponseEntity<String> cancelAppointment(@PathVariable Integer id) {
        String cancelledAppointment = appointmentService.cancelAppointment(id);
        return ResponseEntity.ok(cancelledAppointment);
    }
}