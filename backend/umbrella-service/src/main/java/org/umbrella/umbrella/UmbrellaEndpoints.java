package org.umbrella.umbrella;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.umbrella.umbrella.dto.EntrepreneurDto;
import org.umbrella.umbrella.dto.UserResponseDto;
import org.umbrella.umbrella.service.EntrepreneurServiceInterface;

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
     * Retrieves the list of users from the User Service.
     *
     * @return the ResponseEntity with the list of users as UserResponseDto objects
     */
    @GetMapping("/getAllUsers")
    public ResponseEntity<List<UserResponseDto>> fetchUsersFromUserService() {
        List<UserResponseDto> userServiceUsers = umbrellaFacade.getUsersFromUserService();
        return ResponseEntity.ok(userServiceUsers);
    }

    @DeleteMapping("/removeEntrepreneurById/{id}")
    public ResponseEntity removeEntrepreneurByID(@PathVariable Long id) {
        entrepreneurService.deleteEntrepreneur(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/getEntrepreneurById")
    public ResponseEntity<EntrepreneurDto> getEntrepreneur(@RequestParam Long id) {
        var result = entrepreneurService.getEntrepreneur(id);
        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    @GetMapping("/getAllEntrepreneurs")
    public ResponseEntity<List<EntrepreneurDto>> getEntrepreneurs() {
        var result = entrepreneurService.getEntrepreneurs();
        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    @PostMapping("/createEntrepreneur")
    public ResponseEntity<EntrepreneurDto> createEntrepreneur(@RequestBody EntrepreneurDto entrepreneurDto) {
        var result = entrepreneurService.createEntrepreneur(entrepreneurDto);
        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    @GetMapping("/health")
    public ResponseEntity getHealth() {
        return ResponseEntity.ok("Application is running");
    }
}
