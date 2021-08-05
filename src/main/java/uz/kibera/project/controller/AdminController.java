package uz.kibera.project.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import uz.kibera.project.dao.entity.Role;
import uz.kibera.project.dto.*;
import uz.kibera.project.mapper.NotificationMapper;
import uz.kibera.project.mapper.UserMapper;
import uz.kibera.project.service.NotificationService;
import uz.kibera.project.service.UserService;

import javax.validation.Valid;
import java.util.List;
import java.util.UUID;

@RestController
@CrossOrigin(allowedHeaders = "*", origins = "*")
@PreAuthorize("hasAuthority('ADMIN')")
@RequestMapping("/admin")
@RequiredArgsConstructor
public class AdminController {
    private final UserService userService;
    private final NotificationService notificationService;
    private final UserMapper userMapper;
    private final NotificationMapper notificationMapper;

    @GetMapping(value = "/list/users", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Page<UserResponse>> fetchUsers(@PageableDefault(sort = "username") Pageable pageable) {
        return ResponseEntity.ok(userService.fetchUsers(pageable));
    }
    @PutMapping(value = "/update/user/{id}")
    public ResponseEntity<UserResponse> updateUser(@PathVariable Long id, @Valid @RequestBody UpdatingUserRequest userRequest) {
        return ResponseEntity.ok(userService.updateUser(id, userRequest));
    }
    @PostMapping("/registration")
    public ResponseEntity<AccessTokenResponse> registration(@Valid @RequestBody RegistrationRequest registrationRequest) {
        return ResponseEntity.ok(userService.register(registrationRequest));
    }

    @DeleteMapping("/delete/user/{id}")
    public ResponseEntity<Void> deleteUser(@PathVariable Long id) {
        userService.deleteUser(id);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/{id}/block-unblock")
    public ResponseEntity<Void> makeDisableOrEnable(@PathVariable Long id) {
        userService.makeDisableOrEnable(id);
        return ResponseEntity.ok().build();
    }
    @GetMapping("/fetch-user/{id}")
    public ResponseEntity<UserResponse> fetchById(@PathVariable Long id) {
        return ResponseEntity.ok(userMapper.toUserResponse(userService.fetchUser(id)));
    }

    @GetMapping(value = "/fetch/roles", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<Role>> fetchRoles() {
        return ResponseEntity.ok(List.of(Role.ADMIN, Role.OPERATOR));
    }

    @PreAuthorize("hasAnyAuthority('ADMIN', 'OPERATOR')")
    @PostMapping(value = "/create/push", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Void> createPushNotification(@Valid @RequestBody PushRequest pushRequest) {
        notificationService.createPush(pushRequest);
        return ResponseEntity.ok().build();
    }

    @PreAuthorize("hasAnyAuthority('ADMIN', 'OPERATOR')")
    @GetMapping("/fetch-push/{id}")
    public ResponseEntity<PushDto> fetchPushById(@PathVariable UUID id) {
        return ResponseEntity.ok(notificationMapper.toPushDto(notificationService.fetchPush(id)));
    }

    @PreAuthorize("hasAnyAuthority('ADMIN', 'OPERATOR')")
    @GetMapping(value = "/fetch/all/pushes", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Page<PushDto>> fetchAllPushes(@PageableDefault Pageable pageable) {
        return ResponseEntity.ok(notificationService.fetchAllPushes(pageable));
    }

    @PreAuthorize("hasAnyAuthority('ADMIN', 'OPERATOR')")
    @DeleteMapping("/delete/push/{id}")
    public ResponseEntity<Void> deletePush(@PathVariable UUID id) {
        notificationService.deletePush(id);
        return ResponseEntity.ok().build();
    }

    @PreAuthorize("hasAnyAuthority('ADMIN', 'OPERATOR')")
    @PutMapping("/update/push/{id}")
    public ResponseEntity<PushDto> updatePush(@PathVariable UUID id, @Valid @RequestBody PushRequest pushRequest) {
        return ResponseEntity.ok(notificationService.updatePush(pushRequest, id));
    }

    @PreAuthorize("hasAnyAuthority('ADMIN', 'OPERATOR')")
    @PostMapping(value = "/create/notice", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Void> createNoticeNotification(@Valid @RequestBody NoticeRequest noticeRequest) {
        notificationService.createNotice(noticeRequest);
        return ResponseEntity.ok().build();
    }

    @PreAuthorize("hasAnyAuthority('ADMIN', 'OPERATOR')")
    @GetMapping("/fetch-notice/{id}")
    public ResponseEntity<NoticeDto> fetchNoticeById(@PathVariable UUID id) {
        return ResponseEntity.ok(notificationMapper.toNoticeDto(notificationService.fetchNotice(id)));
    }
    @PreAuthorize("hasAnyAuthority('ADMIN', 'OPERATOR')")
    @PostMapping(value = "/upload", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<String> uploadFile(@RequestParam("image") MultipartFile multipartFile) {
        return ResponseEntity.ok(notificationService.uploadFile(multipartFile));
    }

    @PreAuthorize("hasAnyAuthority('ADMIN', 'OPERATOR')")
    @GetMapping(value = "/fetch/all/notices", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Page<NoticeDto>> fetchAllNotices(@PageableDefault Pageable pageable) {
        return ResponseEntity.ok(notificationService.fetchAllNotices(pageable));
    }

    @PreAuthorize("hasAnyAuthority('ADMIN', 'OPERATOR')")
    @DeleteMapping("/delete/notice/{id}")
    public ResponseEntity<Void> deleteNotice(@PathVariable UUID id) {
        notificationService.deleteNotice(id);
        return ResponseEntity.ok().build();
    }

    @PreAuthorize("hasAnyAuthority('ADMIN', 'OPERATOR')")
    @PutMapping("/update/notice/{id}")
    public ResponseEntity<NoticeDto> updateNotice(@PathVariable UUID id, @Valid @RequestBody NoticeRequest noticeRequest) {
        return ResponseEntity.ok(notificationService.updateNotice(noticeRequest, id));
    }

}
