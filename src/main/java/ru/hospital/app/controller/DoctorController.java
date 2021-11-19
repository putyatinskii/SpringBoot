package ru.hospital.app.controller;

import ru.hospital.app.dto.AppointmentDto;
import ru.hospital.app.dto.DoctorDto;
import ru.hospital.app.dto.RecordDto;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

public interface DoctorController {

    void signUpForDoctor(DoctorDto doctorDto);

    DoctorDto getDoctorInfo();

    List<DoctorDto> getAllDoctors();

    void updateDoctor(DoctorDto doctorDto);

    void deleteDoctor();

    List<RecordDto> getTodayRecords();

    List<RecordDto> getUserRecords(UUID userId);

    void updateAppointment(UUID id, AppointmentDto appointmentDto);

    void createRecords(LocalDateTime dateTime);
}
