package org.umbrella.umbrella.service;

import jakarta.persistence.EntityNotFoundException;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.umbrella.umbrella.dto.EntrepreneurDto;
import org.umbrella.umbrella.entity.EntrepreneurEntity;
import org.umbrella.umbrella.repository.EntrepreneurRepository;

import java.util.List;
import java.util.Optional;

@Service
public class EntrepreneurService implements EntrepreneurServiceInterface{

    private final ModelMapper mapper;
    private final EntrepreneurRepository entrepreneurRepository;

    public EntrepreneurService(ModelMapper mapper, EntrepreneurRepository entrepreneurRepository) {
        this.mapper = mapper;
        this.entrepreneurRepository = entrepreneurRepository;
    }

    @Override
    public EntrepreneurDto createEntrepreneur(EntrepreneurDto entrepreneurDto) {
        var entityToPersist = mapper.map(entrepreneurDto, EntrepreneurEntity.class);
        var persistedEntity = entrepreneurRepository.save(entityToPersist);
        return mapper.map(persistedEntity, EntrepreneurDto.class);
    }

    @Override
    public List<EntrepreneurDto> getEntrepreneurs() {
        List<EntrepreneurEntity> entrepreneurEntities = entrepreneurRepository.findAll();
        return entrepreneurEntities.stream()
                .map(entrepreneurEntity -> mapper.map(entrepreneurEntity, EntrepreneurDto.class))
                .toList();
    }

    @Override
    public EntrepreneurDto getEntrepreneur(Long id) {
        Optional<EntrepreneurEntity> entrepreneurEntity = entrepreneurRepository.findById(id);
        return entrepreneurEntity.map(this::mapEntityToDto)
                .orElseThrow(() -> new EntityNotFoundException("Entrepreneur not found"));
    }

    @Override
    public EntrepreneurDto updateEntrepreneur(EntrepreneurEntity entrepreneur) {

        //TODO: implement
        return null;
    }

    @Override
    public EntrepreneurEntity deleteEntrepreneur(Long id) {
        var entrepreneur = entrepreneurRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Entrepreneur not found"));
        entrepreneur.setDeleted(true);
        return entrepreneurRepository.save(entrepreneur);
    }

    private EntrepreneurDto mapEntityToDto(EntrepreneurEntity entity) {
        return mapper.map(entity, EntrepreneurDto.class);
    }

}
