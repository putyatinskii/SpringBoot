package ru.hospital.app.controller;

import ru.hospital.app.dto.AppointmentDto;
import ru.hospital.app.dto.RecordDto;
import ru.hospital.app.dto.UserDto;
import ru.hospital.app.model.Speciality;

import java.util.List;
import java.util.UUID;

public interface UserController {

    void signUpForUser(UserDto user);

    UserDto getUserInfo();

    List<UserDto> getAllUsers();

    void updateUser(UserDto userDto);

    void deleteUser();

    List<RecordDto> checkFreeTime(String doctorName, Speciality doctorType, Integer period);

    void createRecord(UUID recordId);

    List<AppointmentDto> getFutureAppointments();

    List<AppointmentDto> getPastAppointments(String doctorName, String doctorType);
}
