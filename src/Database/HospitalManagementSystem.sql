create database project;

use project;

CREATE TABLE Patient (
    id INT PRIMARY KEY auto_increment,
    name VARCHAR(255) NOT NULL,
    email VARCHAR(255),
    phone INT,
    dob DATE,
    address VARCHAR(255),
    CONSTRAINT chk_patient_email CHECK (email LIKE '%_@__%.__%'), 
    CONSTRAINT chk_patient_phone CHECK (phone BETWEEN 1000000000 AND 9999999999) 
);

CREATE TABLE Doctor (
    id INT PRIMARY KEY auto_increment,
    name VARCHAR(255) NOT NULL,
    specialization VARCHAR(255),
    email VARCHAR(255),
    phone INT,
    CONSTRAINT chk_doctor_email CHECK (email LIKE '%_@__%.__%'), 
    CONSTRAINT chk_doctor_phone CHECK (phone BETWEEN 1000000000 AND 9999999999) 
);

CREATE TABLE Users (
    id INT PRIMARY KEY auto_increment,
    username VARCHAR(255),
    password varchar(255),
    role VARCHAR(255),
    CONSTRAINT chk_user_role CHECK (role IN ('ADMIN', 'DOCTOR', 'PATIENT'))
);

CREATE TABLE Appointment (
    id INT PRIMARY KEY auto_increment,
    patient_id INT,
    doctor_id INT,
    appointmentDate DATE,
    status VARCHAR(50),
    CONSTRAINT fk_appointment_patient FOREIGN KEY (patient_id) REFERENCES Patient(id),
    CONSTRAINT fk_appointment_doctor FOREIGN KEY (doctor_id) REFERENCES Doctor(id),
    CONSTRAINT chk_appointment_status CHECK (status IN ('PENDING', 'CONFIRMED', 'COMPLETED', 'CANCELLED'))
);

-- ======================================================================================

CREATE TABLE users (
    id INT AUTO_INCREMENT PRIMARY KEY,
    username VARCHAR(255) NOT NULL UNIQUE,
    password VARCHAR(255) NOT NULL
);

CREATE TABLE roles (
    id INT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(255) NOT NULL UNIQUE
);

CREATE TABLE permissions (
    id INT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(255) NOT NULL UNIQUE
);

CREATE TABLE role_permissions (
	id int not null primary key auto_increment,
    role_id INT NOT NULL,
    permission_id INT NOT NULL,
    CONSTRAINT fk_role_permissions_role FOREIGN KEY (role_id) REFERENCES roles(id),
    CONSTRAINT fk_role_permissions_permission FOREIGN KEY (permission_id) REFERENCES permissions(id)
);

CREATE TABLE user_roles (
	id int not null auto_increment primary key,
    user_id INT NOT NULL,
    role_id INT NOT NULL,
    CONSTRAINT fk_user_roles_user FOREIGN KEY (user_id) REFERENCES users(id),
    CONSTRAINT fk_user_roles_role FOREIGN KEY (role_id) REFERENCES roles(id)
);

CREATE TABLE doctors (
    id INT AUTO_INCREMENT PRIMARY KEY,
    user_id INT NOT NULL UNIQUE,
    name VARCHAR(255) NOT NULL,
    specialization VARCHAR(255),
    email VARCHAR(255),
    phone BIGINT,
    CONSTRAINT fk_doctors_user FOREIGN KEY (user_id) REFERENCES users(id)
);

CREATE TABLE patients (
    id INT AUTO_INCREMENT PRIMARY KEY,
    user_id INT NOT NULL UNIQUE,
    name VARCHAR(255) NOT NULL,
    email VARCHAR(255),
    phone BIGINT,
    dob DATE,
    address VARCHAR(255),
    CONSTRAINT fk_patients_user FOREIGN KEY (user_id) REFERENCES users(id)
);

CREATE TABLE appointments (
    id INT AUTO_INCREMENT PRIMARY KEY,
    patient_id INT NOT NULL,
    doctor_id INT NOT NULL,
    appointment_date DATE NOT NULL,
    status VARCHAR(20) NOT NULL,
    CONSTRAINT fk_appointments_patient FOREIGN KEY (patient_id) REFERENCES patients(id),
    CONSTRAINT fk_appointments_doctor FOREIGN KEY (doctor_id) REFERENCES doctors(id)
);

CREATE INDEX idx_appointments_doctor_id ON appointments(doctor_id);
CREATE INDEX idx_appointments_patient_id ON appointments(patient_id);


select * FROM appointments;
select * FROM USERS;
select * FROM PERMISSIONS;
select * FROM role_permissions;
select * FROM user_roles;
select * FROM doctors;
SELECT * FROM PATIENTS;
SELECT * FROM ROLES;


DROP TABLE appointments;
DROP TABLE USERS;
DROP TABLE PERMISSIONS;
DROP TABLE role_permissions;
DROP TABLE user_roles;
DROP TABLE doctors;
DROP TABLE PATIENTS;
DROP TABLE ROLES;

show tables;
