package ru.hospital.app.service;

import ru.hospital.app.dto.AppointmentDto;
import ru.hospital.app.dto.DoctorDto;
import ru.hospital.app.dto.RecordDto;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

public interface DoctorService {
    void addDoctor(DoctorDto doctor);

    void updateDoctor(DoctorDto doctorDto, String login);

    void deleteDoctor(String login);

    List<DoctorDto> getAllDoctors();

    DoctorDto findByLogin(String login);

    List<RecordDto> getTodayRecords(String login);

    List<RecordDto> getUserRecords(String login, UUID userId);

    void updateAppointment(UUID id, AppointmentDto appointmentDto);

    void createRecords(String login, LocalDateTime dateTime);
}
