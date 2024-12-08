package com.bsuir.backendAipos7.service.mapper.impl;

import com.bsuir.backendAipos7.entity.Cat;
import com.bsuir.backendAipos7.service.dto.CatDto;
import com.bsuir.backendAipos7.service.mapper.DataMapper;
import org.springframework.stereotype.Component;

@Component
public class DataMapperImpl implements DataMapper {
    @Override
    public Cat toEntity(CatDto dto) {
        Cat entity = new Cat();
        entity.setWeight(dto.getWeight());
        entity.setName(dto.getName());
        entity.setAge(dto.getAge());
        return entity;
    }

    @Override
    public CatDto toDto(Cat entity) {
        CatDto dto = new CatDto();
        dto.setId(entity.getId());
        dto.setWeight(entity.getWeight());
        dto.setAge(entity.getAge());
        dto.setName(entity.getName());
        return dto;
    }
}
