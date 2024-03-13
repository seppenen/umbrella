package org.umbrella.service;

import jakarta.persistence.EntityNotFoundException;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.umbrella.dto.EntrepreneurDto;
import org.umbrella.entity.EntrepreneurEntity;
import org.umbrella.repository.EntrepreneurRepository;

import java.text.MessageFormat;
import java.util.List;
import java.util.Optional;

@Service
public class EntrepreneurService implements EntrepreneurServiceInterface {
    private static final String CREATED_MESSAGE_FORMAT = "Created entrepreneur with id: {0}";
    private static final String NOT_FOUND_MESSAGE = "Entrepreneur not found";
    private static final String DELETED_MESSAGE_FORMAT = "Deleted entrepreneur with id: {0}";
    private final ModelMapper mapper;
    private final EntrepreneurRepository entrepreneurRepository;
    private final LoggerService loggerService;

    public EntrepreneurService(ModelMapper mapper, EntrepreneurRepository entrepreneurRepository, LoggerService loggerService) {
        this.mapper = mapper;
        this.entrepreneurRepository = entrepreneurRepository;
        this.loggerService = loggerService;
    }


    /**
     * Creates a new Entrepreneur with the provided details.
     *
     * @param entrepreneurDto  The EntrepreneurDto containing the details of the Entrepreneur to create.
     * @return The created EntrepreneurDto.
     */
    @Override
    public EntrepreneurDto createEntrepreneur(EntrepreneurDto entrepreneurDto) {
        var entityToPersist = mapDtoToEntity(entrepreneurDto);
        var persistedEntity = entrepreneurRepository.save(entityToPersist);
        String message = MessageFormat.format(CREATED_MESSAGE_FORMAT, persistedEntity.getId());
        loggerService.logInfo(message, HttpStatus.CREATED, persistedEntity);
        return mapEntityToDto(persistedEntity);
    }

    /**
     * Retrieves a list of all Entrepreneurs.
     *
     * @return A list of EntrepreneurDto objects representing the Entrepreneurs.
     */
    @Override
    public List<EntrepreneurDto> getEntrepreneurs() {
        return entrepreneurRepository.findAll().stream()
                .map(this::mapEntityToDto)
                .toList();
    }

    /**
     * Retrieves the EntrepreneurDto object for the given id.
     *
     * @param id The id of the Entrepreneur.
     * @return The EntrepreneurDto object representing the Entrepreneur.
     * @throws EntityNotFoundException if the Entrepreneur with the given id does not exist.
     */
    @Override
    public EntrepreneurDto getEntrepreneur(Long id) {
        return findEntityOrThrow(id).map(this::mapEntityToDto)
                .orElseThrow(this::throwNotFound);
    }

    @Override
    public EntrepreneurDto updateEntrepreneur(EntrepreneurEntity entrepreneur) {
        //TODO: implement
        return null;
    }

    /**
     * Deletes an Entrepreneur with the specified id by setting the 'deleted' flag to true.
     * Also logs an info message with the deleted Entrepreneur information.
     *
     * @param id The id of the Entrepreneur to delete.
     * @return The updated EntrepreneurEntity after deletion.
     */
    @Override
    public EntrepreneurEntity deleteEntrepreneur(Long id) {
        var entrepreneur = findEntityOrThrow(id)
                .orElseThrow(this::throwNotFound);
        entrepreneur.setDeleted(true);
        String message = MessageFormat.format(DELETED_MESSAGE_FORMAT, entrepreneur.getId());
        loggerService.logInfo(message, HttpStatus.OK, entrepreneur);

        return entrepreneurRepository.save(entrepreneur);
    }

    private Optional<EntrepreneurEntity> findEntityOrThrow(Long id) {
        return entrepreneurRepository.findById(id);
    }

    private RuntimeException throwNotFound() {
        return new EntityNotFoundException(NOT_FOUND_MESSAGE);
    }

    private EntrepreneurDto mapEntityToDto(EntrepreneurEntity entity) {
        return mapper.map(entity, EntrepreneurDto.class);
    }

    private EntrepreneurEntity mapDtoToEntity(EntrepreneurDto dto) {
        return mapper.map(dto, EntrepreneurEntity.class);
    }
}
