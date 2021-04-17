package io.beka.service;

public interface CoreService<T, DTO> {
    DTO convertEntityToDto(T t, DTO dto);

    T convertDtoToEntity(T t, DTO dto);
}
