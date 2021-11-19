package ru.hospital.app.util.mapper.impl;

import org.modelmapper.Converter;
import org.modelmapper.ModelMapper;
import org.modelmapper.spi.MappingContext;
import org.springframework.stereotype.Component;
import ru.hospital.app.dto.AppointmentDto;
import ru.hospital.app.model.Appointment;

/**
 * @author dputiatinskii
 */

@Component
public class AppoitmentDtoMapper extends AbstractDtoMapper<AppointmentDto, Appointment> {

    private final RecordDtoMapper recordDtoMapper;

    public AppoitmentDtoMapper(ModelMapper mapper, RecordDtoMapper recordDtoMapper) {
        super(AppointmentDto.class, Appointment.class, mapper);
        this.recordDtoMapper = recordDtoMapper;
        mapper.addConverter(toDtoConverter());
    }

    private Converter<Appointment, AppointmentDto> toDtoConverter() {
        return new Converter<>() {
            @Override
            public AppointmentDto convert(MappingContext<Appointment, AppointmentDto> context) {
                Appointment appointment = context.getSource();
                return new AppointmentDto(
                        recordDtoMapper.toDto(appointment.getRecord()).orElse(null),
                        appointment.getDescription(),
                        appointment.getIsSick(),
                        appointment.getIsClosed(),
                        appointment.getIsVisited()
                );
            }
        };
    }
}
