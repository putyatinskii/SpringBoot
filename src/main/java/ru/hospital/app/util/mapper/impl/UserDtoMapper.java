package ru.hospital.app.util.mapper.impl;

import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;
import ru.hospital.app.dto.UserDto;
import ru.hospital.app.model.User;

@Component
public class UserDtoMapper extends AbstractDtoMapper<UserDto, User> {
    public UserDtoMapper(ModelMapper modelMapper) {
        super(UserDto.class, User.class, modelMapper);
    }
}
