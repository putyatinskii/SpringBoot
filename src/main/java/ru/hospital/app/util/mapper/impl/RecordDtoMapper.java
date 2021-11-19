package ru.hospital.app.util.mapper.impl;

import org.modelmapper.Converter;
import org.modelmapper.ModelMapper;
import org.modelmapper.spi.MappingContext;
import org.springframework.stereotype.Component;
import ru.hospital.app.dto.RecordDto;
import ru.hospital.app.model.Record;

@Component
public class RecordDtoMapper extends AbstractDtoMapper<RecordDto, Record> {

    private final DoctorDtoMapper doctorDtoMapper;

    private final UserDtoMapper userDtoMapper;

    public RecordDtoMapper(ModelMapper mapper, DoctorDtoMapper doctorDtoMapper, UserDtoMapper userDtoMapper) {
        super(RecordDto.class, Record.class, mapper);
        this.doctorDtoMapper = doctorDtoMapper;
        this.userDtoMapper = userDtoMapper;
        mapper.addConverter(toDtoConverter());
    }

    private Converter<Record, RecordDto> toDtoConverter() {
        return new Converter<>() {
            @Override
            public RecordDto convert(MappingContext<Record, RecordDto> context) {
                Record record = context.getSource();
                return new RecordDto(record.getId(),
                        userDtoMapper.toDto(record.getUser()).orElse(null),
                        doctorDtoMapper.toDto(record.getDoctor()).orElse(null),
                        record.getTimeOfAppointment());
            }
        };
    }
}
