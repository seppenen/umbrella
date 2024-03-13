package org.umbrella;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.umbrella.dto.UserResponseDto;
import org.umbrella.dto.EntrepreneurDto;
import org.umbrella.service.EntrepreneurServiceInterface;

import java.util.List;

@RestController
public class UmbrellaEndpoints {

    private final UmbrellaFacade umbrellaFacade;
    private final EntrepreneurServiceInterface entrepreneurService;

    public UmbrellaEndpoints(UmbrellaFacade umbrellaFacade, EntrepreneurServiceInterface entrepreneurService) {
        this.umbrellaFacade = umbrellaFacade;
        this.entrepreneurService = entrepreneurService;
    }

    /**
     * Retrieves the list of users from the UserService.
     *
     * @return ResponseEntity<List < UserResponseDto>> - the list of users as a ResponseEntity containing a List of UserResponseDto objects
     */
    @GetMapping("/getAllUsers")
    public ResponseEntity<List<UserResponseDto>> fetchUsersFromUserService() {
        List<UserResponseDto> userServiceUsers = umbrellaFacade.getUsersFromUserService();
        return ResponseEntity.ok(userServiceUsers);
    }

    /**
     * Removes an entrepreneur by their ID.
     *
     * @param id the ID of the entrepreneur to be removed
     * @return a ResponseEntity with no content
     */
    @DeleteMapping("/entrepreneurs/{id}")
    public ResponseEntity removeEntrepreneurByID(@PathVariable Long id) {
        entrepreneurService.deleteEntrepreneur(id);
        return ResponseEntity.noContent().build();
    }

    /**
     * Retrieves an EntrepreneurDto object by ID.
     *
     * @param id the ID of the entrepreneur to retrieve
     * @return a ResponseEntity containing the retrieved EntrepreneurDto object
     */
    @GetMapping("/entrepreneurs/{id}")
    public ResponseEntity<EntrepreneurDto> getEntrepreneur(@PathVariable Long id) {
        var result = entrepreneurService.getEntrepreneur(id);
        return ResponseEntity.ok(result);
    }

    /**
     * Retrieves a list of EntrepreneurDto objects.
     *
     * @return ResponseEntity<List < EntrepreneurDto>> - the list of entrepreneurs as a ResponseEntity containing a List of EntrepreneurDto objects
     */
    @GetMapping("/entrepreneurs")
    public ResponseEntity<List<EntrepreneurDto>> getEntrepreneurs() {
        var result = entrepreneurService.getEntrepreneurs();
        return ResponseEntity.ok(result);
    }

    /**
     * Creates a new entrepreneur.
     *
     * @param entrepreneurDto the EntrepreneurDto object representing the entrepreneur to be created
     * @return a ResponseEntity containing the created EntrepreneurDto object
     */
    @PostMapping("/entrepreneurs")
    public ResponseEntity<EntrepreneurDto> createEntrepreneur(@RequestBody EntrepreneurDto entrepreneurDto) {
        var result = entrepreneurService.createEntrepreneur(entrepreneurDto);
        return ResponseEntity.ok(result);
    }

    /**
     * Retrieves the health status of the application.
     *
     * @return ResponseEntity - the response entity indicating the health status of the application
     */
    @GetMapping("/health")
    public ResponseEntity getHealth() {
        return ResponseEntity.ok("Application is running");
    }
}
