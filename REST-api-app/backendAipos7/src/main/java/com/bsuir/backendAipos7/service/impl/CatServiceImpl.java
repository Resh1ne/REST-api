package com.bsuir.backendAipos7.service.impl;

import com.bsuir.backendAipos7.entity.Cat;
import com.bsuir.backendAipos7.repository.CatRepository;
import com.bsuir.backendAipos7.service.CatService;
import com.bsuir.backendAipos7.service.dto.CatDto;
import com.bsuir.backendAipos7.service.mapper.DataMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CatServiceImpl implements CatService {
    private final CatRepository catRepository;
    private final DataMapper dataMapper;

    @Override
    public CatDto create(CatDto dto) {
        Cat cat = dataMapper.toEntity(dto);
        Cat createdCat = catRepository.save(cat);
        return dataMapper.toDto(createdCat);
    }

    @Override
    public CatDto getById(Long id) {
        return catRepository
                .findById(id.intValue())
                .map(dataMapper::toDto)
                .orElseThrow();
    }

    @Override
    public List<CatDto> getAll() {
        return catRepository
                .findAll()
                .stream()
                .map(dataMapper::toDto)
                .toList();
    }

    @Override
    public CatDto update(CatDto dto) {
        Cat cat = dataMapper.toEntity(dto);
        cat.setId(dto.getId());
        Cat createdCat = catRepository.save(cat);
        return dataMapper.toDto(createdCat);
    }

    @Override
    public void delete(Long id) {
        catRepository.deleteById(id.intValue());
    }
}
