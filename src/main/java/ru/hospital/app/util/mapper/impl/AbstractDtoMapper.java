package ru.hospital.app.util.mapper.impl;

import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import ru.hospital.app.util.mapper.DtoMapper;

import java.util.Optional;

@RequiredArgsConstructor
public class AbstractDtoMapper<D, E> implements DtoMapper<D, E> {
    private final Class<D> dtoClass;
    private final Class<E> entityClass;

    protected final ModelMapper mapper;

    @Override
    public Optional<D> toDto(E entity) {
        return Optional.ofNullable(entity).isPresent()
                ? Optional.of(this.mapper.map(entity, this.dtoClass)) : Optional.empty();
        //return mapper.map(entity, this.dtoClass);
    }

    @Override
    public Optional<E> toEntity(D dto) {
        return Optional.ofNullable(dto).isPresent()
                ? Optional.of(this.mapper.map(dto, this.entityClass)) : Optional.empty();
        //return mapper.map(dto, this.entityClass);
    }
}
