package ru.hospital.app.util.mapper.impl;

import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;
import ru.hospital.app.dto.DoctorDto;
import ru.hospital.app.model.Doctor;

@Component
public class DoctorDtoMapper extends AbstractDtoMapper<DoctorDto, Doctor> {
    public DoctorDtoMapper(ModelMapper mapper) {
        super(DoctorDto.class, Doctor.class, mapper);
    }
}
