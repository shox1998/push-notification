package uz.kibera.project.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import uz.kibera.project.dto.AccessTokenResponse;
import uz.kibera.project.dto.RegistrationRequest;
import uz.kibera.project.dto.UpdatingUserDto;
import uz.kibera.project.dto.UserResponse;
import uz.kibera.project.service.UserService;

@RestController
@PreAuthorize("hasAuthority('ADMIN')")
@RequestMapping("/admin")
@RequiredArgsConstructor
public class AdminController {
    private final UserService userService;

    @GetMapping(value = "/list/users", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Page<UserResponse>>  fetchUsers(@PageableDefault(sort = "username") Pageable pageable) {
        return ResponseEntity.ok(userService.fetchUsers(pageable));
    }

    @PostMapping("/registration")
    public ResponseEntity<AccessTokenResponse> registration(@RequestBody RegistrationRequest registrationRequest)  {
        return ResponseEntity.ok(userService.register(registrationRequest));
    }

    @PostMapping("/{id}/block-unblock")
    public ResponseEntity<Void> makeDisableOrEnable(@PathVariable Long id) {
        userService.makeDisableOrEnable(id);
        return ResponseEntity.ok().build();
    }
}
