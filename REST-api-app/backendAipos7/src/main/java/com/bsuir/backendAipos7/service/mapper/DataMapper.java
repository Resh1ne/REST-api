package com.bsuir.backendAipos7.service.mapper;

import com.bsuir.backendAipos7.entity.Cat;
import com.bsuir.backendAipos7.service.dto.CatDto;

public interface DataMapper {
    Cat toEntity(CatDto dto);
    CatDto toDto(Cat entity);
}
