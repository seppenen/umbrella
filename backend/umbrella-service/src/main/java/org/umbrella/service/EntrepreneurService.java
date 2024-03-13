package org.umbrella.service;

import jakarta.persistence.EntityNotFoundException;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.umbrella.entity.EntrepreneurEntity;
import org.umbrella.repository.EntrepreneurRepository;
import org.umbrella.dto.EntrepreneurDto;

import java.util.List;
import java.util.Optional;

@Service
public class EntrepreneurService implements EntrepreneurServiceInterface{

    private static final String CREATED_MESSAGE = "Created entrepreneur with id: %s";
    private final ModelMapper mapper;
    private final EntrepreneurRepository entrepreneurRepository;
    private final LoggerService loggerService;

    public EntrepreneurService(ModelMapper mapper, EntrepreneurRepository entrepreneurRepository, LoggerService loggerService) {
        this.mapper = mapper;
        this.entrepreneurRepository = entrepreneurRepository;
        this.loggerService = loggerService;
    }

    @Override
    public EntrepreneurDto createEntrepreneur(EntrepreneurDto entrepreneurDto) {
        var entityToPersist = mapper.map(entrepreneurDto, EntrepreneurEntity.class);
        var persistedEntity = entrepreneurRepository.save(entityToPersist);
        loggerService.logInfo(CREATED_MESSAGE, HttpStatus.OK, persistedEntity);
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
        loggerService.logInfo("Deleted entrepreneur with id: %s", HttpStatus.OK, entrepreneur);
        return entrepreneurRepository.save(entrepreneur);
    }

    private EntrepreneurDto mapEntityToDto(EntrepreneurEntity entity) {
        return mapper.map(entity, EntrepreneurDto.class);
    }

}
