package org.umbrella;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.umbrella.dto.EntrepreneurDto;
import org.umbrella.dto.UserResponseDto;
import org.umbrella.service.JwtService;

import java.util.List;

@RestController
@RequestMapping("/api/v1")
public class UmbrellaEndpoints {

    private final UmbrellaFacade umbrellaFacade;
    private final JwtService jwtService;


    public UmbrellaEndpoints(UmbrellaFacade umbrellaFacade, JwtService jwtService) {
        this.umbrellaFacade = umbrellaFacade;
        this.jwtService = jwtService;

    }

    /**
     * Retrieves the list of users from the UserService.
     *
     * @return ResponseEntity<List < UserResponseDto>> - the list of users as a ResponseEntity containing a List of UserResponseDto objects
     */
    @GetMapping("/getAllUsers")
    public ResponseEntity<List<UserResponseDto>> fetchUsersFromUserService() {
        return ResponseEntity.ok(umbrellaFacade.getUsersFromUserService());
    }

    @PostMapping("/refresh-token/{uuid}")
    public ResponseEntity<String> refreshToken(@PathVariable String uuid) {
        //TODO: Implement the logic to refresh the token
       String token = jwtService.GenerateToken("0000");
       return ResponseEntity.ok(token);
    }

    /**
     * Removes an entrepreneur by their ID.
     *
     * @param id the ID of the entrepreneur to be removed
     * @return a ResponseEntity with no content
     */
    @DeleteMapping("/entrepreneur/{id}")
    public ResponseEntity<Void> deleteEntrepreneur(@PathVariable("id") Long entrepreneurId) {
        umbrellaFacade.deleteEntrepreneur(entrepreneurId);
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
       EntrepreneurDto result = umbrellaFacade.getEntrepreneur(id);
        return ResponseEntity.ok(result);
    }

    /**
     * Retrieves a list of EntrepreneurDto objects.
     *
     * @return ResponseEntity<List < EntrepreneurDto>> - the list of entrepreneurs as a ResponseEntity containing a List of EntrepreneurDto objects
     */
    @GetMapping("/entrepreneurs")
    public ResponseEntity<List<EntrepreneurDto>> getEntrepreneurs() {
        var result = umbrellaFacade.getEntrepreneurs();
        return ResponseEntity.ok(result);
    }

    /**
     * Creates a new entrepreneur.
     *
     * @param entrepreneurDto the EntrepreneurDto object representing the entrepreneur to be created
     * @return a ResponseEntity containing the created EntrepreneurDto object
     */
    @PostMapping("/entrepreneur")
    public ResponseEntity<EntrepreneurDto> createEntrepreneurEntity(@RequestBody EntrepreneurDto entrepreneurDto) {
        EntrepreneurDto createdEntrepreneur = umbrellaFacade.createAndReturnEntrepreneur(entrepreneurDto);
        return ResponseEntity.ok(createdEntrepreneur);
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
