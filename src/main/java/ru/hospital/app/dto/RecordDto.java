package ru.hospital.app.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RecordDto {

    private UUID id;

    private UserDto userDto;

    private DoctorDto doctorDto;

    private LocalDateTime timeOfAppointment;
}
