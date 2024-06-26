package org.umbrella;

import lombok.AllArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;
import org.umbrella.client.UserServiceClient;
import org.umbrella.dto.EntrepreneurDto;
import org.umbrella.dto.UserResponseDto;
import org.umbrella.entity.EntrepreneurEntity;
import org.umbrella.service.EntrepreneurService;

import java.util.List;
import java.util.stream.Collectors;

@Component
@AllArgsConstructor
public class UmbrellaFacade {

    private final UserServiceClient userServiceClient;
    private final ModelMapper mapper;
    private final EntrepreneurService entrepreneurService;

    /**
     * Retrieves the list of users from the User Service.
     *
     * @return The list of users as a List of UserResponseDto objects.
     */
    public List<UserResponseDto> getUsersFromUserService() {

        //TODO: Implement this method
        return mapToDto(userServiceClient.getUsers("token"), UserResponseDto.class);
    }

    /**
     * Retrieves a list of EntrepreneurDto objects.
     *
     * @return the list of entrepreneurs as a List of EntrepreneurDto objects
     */
    public List<EntrepreneurDto> getEntrepreneurs() {
        return mapToDto(entrepreneurService.getAllEntrepreneurs(), EntrepreneurDto.class);
    }

    /**
     * Retrieves an EntrepreneurDto object by ID.
     *
     * @param id the ID of the entrepreneur to retrieve
     * @return the retrieved EntrepreneurDto object
     */
    public EntrepreneurDto getEntrepreneur(Long id) {
        EntrepreneurEntity entrepreneur = entrepreneurService.getEntrepreneur(id);
        return mapToSingleDto(entrepreneur, EntrepreneurDto.class);
    }

    /**
     * Creates and returns an EntrepreneurDto object.
     *
     * @param entrepreneurDtoToCreate the EntrepreneurDto object representing the entrepreneur to be created
     *
     * @return the created EntrepreneurDto object
     */
    public EntrepreneurDto createAndReturnEntrepreneur(EntrepreneurDto entrepreneurDtoToCreate) {
        EntrepreneurEntity entityToPersist = mapToSingleEntity(entrepreneurDtoToCreate, EntrepreneurEntity.class);
        EntrepreneurEntity persistedEntity = entrepreneurService.persistEntrepreneurEntity(entityToPersist);
        return mapToSingleDto(persistedEntity, EntrepreneurDto.class);
    }

    public void deleteEntrepreneur(Long id) {
        entrepreneurService.deleteEntrepreneur(id);
    }

    private <T, U> List<U> mapToDto(List<T> entityList, Class<U> dtoClass) {
        return entityList.stream()
                .map(entity -> mapper.map(entity, dtoClass))
                .collect(Collectors.toList());
    }

    private <T, U> U mapToSingleDto(T entity, Class<U> dtoClass) {
        return mapper.map(entity, dtoClass);
    }

    private <T, U> U mapToSingleEntity(T dto, Class<U> entityClass) {
        return mapper.map(dto, entityClass);
    }

}
