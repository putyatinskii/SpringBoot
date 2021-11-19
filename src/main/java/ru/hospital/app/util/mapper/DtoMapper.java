package ru.hospital.app.util.mapper;

import java.util.Optional;

public interface DtoMapper<D, E> {
    Optional<D> toDto(E entity);

    Optional<E> toEntity(D dto);
}
