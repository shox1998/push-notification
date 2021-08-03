package uz.kibera.project.controller;

import javax.validation.Valid;

import java.util.List;
import java.util.UUID;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import uz.kibera.project.dao.entity.Role;
import uz.kibera.project.dto.AccessTokenResponse;
import uz.kibera.project.dto.PushDto;
import uz.kibera.project.dto.PushRequest;
import uz.kibera.project.dto.RegistrationRequest;
import uz.kibera.project.dto.UserResponse;
//import uz.kibera.project.service.NotificationService;
import uz.kibera.project.service.NotificationService;
import uz.kibera.project.service.UserService;

@RestController
@CrossOrigin(allowedHeaders = "*", origins = "*")
@PreAuthorize("hasAuthority('ADMIN')")
@RequestMapping("/admin")
@RequiredArgsConstructor
public class AdminController {
    private final UserService userService;
    private NotificationService notificationService;

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

    @GetMapping(value = "/fetch/roles", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<Role>> fetchRoles() {
        return ResponseEntity.ok(List.of(Role.ADMIN, Role.OPERATOR));
    }

    @PostMapping(value = "/create/push", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Void> createPushNotification(@Valid @RequestBody PushRequest pushRequest) {
        notificationService.createPush(pushRequest);
        return ResponseEntity.ok().build();
    }

    @GetMapping(value = "/fetch/all/pushes", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Page<PushDto>> fetchAllPushes(@PageableDefault Pageable pageable) {
        return ResponseEntity.ok(notificationService.fetchAllPushes(pageable));
    }

    @DeleteMapping("/delete/push/{id}")
    public ResponseEntity<Void> deletePush(@PathVariable UUID id) {
        notificationService.deletePush(id);
        return ResponseEntity.ok().build();
    }
}
