package uz.kibera.project.controller;

import javax.validation.Valid;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import uz.kibera.project.dto.AccessTokenResponse;
import uz.kibera.project.dto.AuthenticationRequest;
import uz.kibera.project.dto.UpdatingUserDto;
import uz.kibera.project.dto.UserResponse;
import uz.kibera.project.dto.UserResponseWithAccessToken;
import uz.kibera.project.service.UserService;

@RestController
@CrossOrigin(allowedHeaders = "*", origins = "*")
@RequestMapping("/user")
@RequiredArgsConstructor
@Slf4j
public class UserController {
    private final UserService userService;

    @PostMapping(value = "/login", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<UserResponseWithAccessToken> authenticateAndGetAccessToken(@Valid @RequestBody AuthenticationRequest authenticationRequest) {
        log.info("aaa");
        return ResponseEntity.ok(userService.authenticate(authenticationRequest));
    }
    @PreAuthorize("hasAnyAuthority('ADMIN', 'OPERATOR')")
    @PutMapping(value = "/update/{id}", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<UserResponse> updateUser(@PathVariable Long id, @Valid @RequestBody UpdatingUserDto updatingUserDto) {
        return ResponseEntity.ok(userService.updateUserEntity(id, updatingUserDto));
    }

}
