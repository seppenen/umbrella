package org.umbrella.umbrella;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.umbrella.umbrella.dto.UserResponseDto;

import java.util.List;

@RestController
public class UmbrellaEndpoints {

    private final UmbrellaFacade umbrellaFacade;

    public UmbrellaEndpoints(UmbrellaFacade umbrellaFacade) {
        this.umbrellaFacade = umbrellaFacade;

    }

    /**
     * Retrieves the list of users from the User Service.
     *
     * @return the ResponseEntity with the list of users as UserResponseDto objects
     */
    @GetMapping("/userServiceUsers")
    public ResponseEntity<List<UserResponseDto>> fetchUsersFromUserService() {
        List<UserResponseDto> userServiceUsers = umbrellaFacade.getUsersFromUserService();
        return ResponseEntity.ok(userServiceUsers);
    }

    @GetMapping("/health")
    public ResponseEntity getHealth() {
        return ResponseEntity.ok("Application is running");
    }
}
