package ru.hospital.app.service;

import ru.hospital.app.dto.AppointmentDto;
import ru.hospital.app.dto.RecordDto;
import ru.hospital.app.dto.UserDto;
import ru.hospital.app.model.Speciality;

import java.util.List;
import java.util.UUID;

public interface UserService {
    void addUser(UserDto userDto);

    UserDto findByLogin(String name);

    List<UserDto> getAllUsers();

    void updateUser(UserDto userDto, String login);

    void deleteUser(String login);

    List<RecordDto> checkFreeTime(String doctorName, Speciality doctorType, Integer period);

    void createRecord(String login, UUID recordId);

    List<AppointmentDto> getFutureAppointments(String login);

    List<AppointmentDto> getPastAppointments(String login, String doctorName, Speciality doctorType);

}
