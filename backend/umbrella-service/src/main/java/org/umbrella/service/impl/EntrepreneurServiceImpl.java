package org.umbrella.service.impl;

import jakarta.persistence.EntityNotFoundException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.umbrella.entity.EntrepreneurEntity;
import org.umbrella.repository.EntrepreneurRepository;

import java.util.List;
import java.util.Optional;

@Service
public class EntrepreneurServiceImpl implements org.umbrella.service.EntrepreneurService {
    private static final String CREATED_MESSAGE_FORMAT = "Created entrepreneur with id: {}";
    private static final String NOT_FOUND_MESSAGE = "Entrepreneur not found";
    private static final String DELETED_MESSAGE_FORMAT = "Deleted entrepreneur with id: {}";
    private final EntrepreneurRepository entrepreneurRepository;
    private static final Logger logger = LoggerFactory.getLogger(EntrepreneurServiceImpl.class);


    public EntrepreneurServiceImpl(EntrepreneurRepository entrepreneurRepository) {
        this.entrepreneurRepository = entrepreneurRepository;

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
        logger.info(CREATED_MESSAGE_FORMAT, createdEntity.getId());
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
        logger.info(DELETED_MESSAGE_FORMAT, entrepreneur.getId());
    }

    private Optional<EntrepreneurEntity> findEntityOrThrow(Long id) {
        return entrepreneurRepository.findById(id);
    }

    private RuntimeException throwNotFound() {
        return new EntityNotFoundException(NOT_FOUND_MESSAGE);
    }
}


