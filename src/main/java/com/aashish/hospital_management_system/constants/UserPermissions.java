package com.aashish.hospital_management_system.constants;

public final class UserPermissions {

    public static final String WRITE_APPOINTMENT = "WRITE_APPOINTMENT";
    public static final String READ_ALL_APPOINTMENTS = "READ_ALL_APPOINTMENTS";
    public static final String READ_OWN_APPOINTMENT = "READ_OWN_APPOINTMENT";
    public static final String ACTION_APPROVE_APPOINTMENT = "ACTION_APPROVE_APPOINTMENT";
    public static final String ACTION_CANCEL_OWN_APPOINTMENT = "ACTION_CANCEL_OWN_APPOINTMENT";
    public static final String ACTION_CANCEL_ALL_APPOINTMENTS = "ACTION_CANCEL_ALL_APPOINTMENTS";

    public static final String WRITE_DOCTOR = "WRITE_DOCTOR";
    public static final String READ_ALL_DOCTOR = "READ_ALL_DOCTOR";
    public static final String READ_DOCTOR = "READ_DOCTOR";
    
    public static final String WRITE_PATIENTS = "WRITE_PATIENTS";
    public static final String READ_OWN_PATIENTS = "READ_OWN_PATIENTS";
    public static final String READ_ALL_PATIENTS = "READ_ALL_PATIENTS";
    public static final String WRITE_OWN_PATIENTS = "WRITE_OWN_PATIENTS";

    public static final String READ_PERMISSIONS = "READ_PERMISSIONS";
    public static final String READ_ALL_PERMISSIONS = "READ_ALL_PERMISSIONS";
    public static final String WRITE_PERMISSIONS = "WRITE_PERMISSIONS";

    public static final String WRITE_ROLES = "WRITE_ROLES";
    public static final String READ_ROLES = "READ_ROLES";

    public static final String WRITE_ROLES_PERMISSIONS = "WRITE_ROLES_PERMISSIONS";
    public static final String READ_ALL_ROLES_PERMISSIONS = "READ_ALL_ROLES_PERMISSIONS";

    public static final String READ_USER = "READ_USER";
    public static final String READ_ALL_USERS = "READ_ALL_USERS";

    public static final String WRITE_USER_ROLES = "WRITE_USER_ROLES";
    public static final String READ_ALL_USER_ROLES = "READ_ALL_USER_ROLES";

    private UserPermissions() {
        // No instances
    }

}
