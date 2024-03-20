package org.umbrella.service.impl;

import jakarta.persistence.EntityNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.umbrella.entity.EntrepreneurEntity;
import org.umbrella.repository.EntrepreneurRepository;
import org.umbrella.service.EntrepreneurServiceInterface;
import org.umbrella.service.LoggerService;

import java.text.MessageFormat;
import java.util.List;
import java.util.Optional;

@Service
public class EntrepreneurService implements EntrepreneurServiceInterface {
    private static final String CREATED_MESSAGE_FORMAT = "Created entrepreneur with id: {0}";
    private static final String NOT_FOUND_MESSAGE = "Entrepreneur not found";
    private static final String DELETED_MESSAGE_FORMAT = "Deleted entrepreneur with id: {0}";
    private final EntrepreneurRepository entrepreneurRepository;
    private final LoggerService loggerService;

    public EntrepreneurService( EntrepreneurRepository entrepreneurRepository, LoggerService loggerService) {
        this.entrepreneurRepository = entrepreneurRepository;
        this.loggerService = loggerService;
    }

    /**
     * Creates a new Entrepreneur with the provided details.
     *
     * @param entrepreneurEntityToPersist  The EntrepreneurDto containing the details of the Entrepreneur to create.
     * @return The created EntrepreneurEntity.
     */
    @Override
    public EntrepreneurEntity persistEntrepreneurEntity(EntrepreneurEntity entrepreneurEntityToPersist) {
        EntrepreneurEntity createdEntity = entrepreneurRepository.save(entrepreneurEntityToPersist);
        String logMessage = MessageFormat.format(CREATED_MESSAGE_FORMAT, createdEntity.getId());
        loggerService.logInfo(logMessage, HttpStatus.CREATED, createdEntity);
        return createdEntity;
    }
    /**
     * Retrieves a list of all Entrepreneurs.
     *
     * @return A list of EntrepreneurEntity objects representing the Entrepreneurs.
     */
    @Override
    public List<EntrepreneurEntity> getAllEntrepreneurs() {
        return entrepreneurRepository.findAll();
    }

    /**
     * Retrieves the EntrepreneurDto object for the given id.
     *
     * @param id The id of the Entrepreneur.
     * @return The EntrepreneurEntity object representing the Entrepreneur.
     * @throws EntityNotFoundException if the Entrepreneur with the given id does not exist.
     */
    @Override
    public EntrepreneurEntity getEntrepreneur(Long id) {
        return findEntityOrThrow(id)
                .orElseThrow(this::throwNotFound);
    }

    @Override
    public EntrepreneurEntity updateEntrepreneur(EntrepreneurEntity entrepreneur) {
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
    public void deleteEntrepreneur(Long id) {
        var entrepreneur = findEntityOrThrow(id)
                .orElseThrow(this::throwNotFound);
        entrepreneur.setDeleted(true);
        entrepreneurRepository.save(entrepreneur);
        String message = MessageFormat.format(DELETED_MESSAGE_FORMAT, entrepreneur.getId());
        loggerService.logInfo(message, HttpStatus.OK, entrepreneur);
    }

    private Optional<EntrepreneurEntity> findEntityOrThrow(Long id) {
        return entrepreneurRepository.findById(id);
    }

    private RuntimeException throwNotFound() {
        return new EntityNotFoundException(NOT_FOUND_MESSAGE);
    }
}


