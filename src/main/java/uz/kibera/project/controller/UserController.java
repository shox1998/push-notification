package uz.kibera.project.controller;

import javax.annotation.PostConstruct;
import javax.servlet.ServletRequest;
import javax.validation.Valid;

import com.google.common.collect.ImmutableSet;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import uz.kibera.project.dao.entity.User;
import uz.kibera.project.dao.repository.UserRepository;
import uz.kibera.project.dto.AccessTokenResponse;
import uz.kibera.project.dto.AuthenticationRequest;
import uz.kibera.project.dto.RegistrationRequest;
import uz.kibera.project.service.UserService;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
@Slf4j
public class UserController {
    private final UserService userService;
    @PostConstruct
    public void init() {
        SecurityContextHolder.clearContext();
    }

    @PostMapping(value = "/login", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<AccessTokenResponse> authenticateAndGetAccessToken(@Valid @RequestBody AuthenticationRequest authenticationRequest) {
        return ResponseEntity.ok(userService.authenticate(authenticationRequest));
    }

    @PreAuthorize("hasAuthority('ADMIN')")
    @GetMapping("/secured")
    public String testSecurity() {
        System.out.println("This method secured");
        return "Secured";
    }

    @PostMapping("/registration")
    public ResponseEntity<AccessTokenResponse> registration(@RequestBody RegistrationRequest registrationRequest)  {
        return ResponseEntity.ok(userService.register(registrationRequest));
    }
}
