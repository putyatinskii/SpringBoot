package ru.hospital.app.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AppointmentDto {

    private RecordDto recordDto;

    private String description;

    private Boolean isSick;

    private Boolean isClosed;

    private Boolean isVisited;
}
