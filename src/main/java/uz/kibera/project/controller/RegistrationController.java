//package uz.kibera.project.controller;
//
//import io.swagger.annotations.ApiOperation;
//import io.swagger.annotations.ApiParam;
//import org.springframework.data.domain.Page;
//import org.springframework.data.domain.Pageable;
//import org.springframework.http.MediaType;
//import org.springframework.http.ResponseEntity;
//import org.springframework.security.access.prepost.PreAuthorize;
//import org.springframework.web.bind.annotation.*;
//import org.springframework.web.multipart.MultipartFile;
//import uz.kibera.project.dao.entity.Role;
//import uz.kibera.project.dto.UserDto;
//
//import java.util.List;
//import java.util.stream.Collectors;
//@RestController
//@RequestMapping("/registration")
//public class RegistrationController {
//
//    @ApiOperation(value = "Create request for registration staff")
//    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
//    @PostMapping("/verification/request")
//    public VerificationToken createUserRegistrationRequest(@RequestBody UserRegistrationDto userRegistrationDto) {
//        return mapper.from(adminService.createUserRegistrationRequest(userRegistrationDto, lang));
//    }
//
//    @ApiOperation(value = "List of Roles")
//    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
//    @GetMapping("/roles")
//    public List<Role> getRoles() {
//        return List.of(Role.ADMIN, Role.MANAGER, Role.VIEWER);
//    }
//
//    @ApiOperation(value = "List of Staff")
//    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
//    @GetMapping("/list/staff")
//    public Page<UserDto> getStaffs(Pageable pageable) {
//        return userService.getUsers(pageable).map(userMapper::toDto);
//    }
//
//    @ApiOperation(value = "List of Clients")
//    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
//    @GetMapping("/list/clients")
//    public Page<UserDto> getClients(Pageable pageable) {
//        return userService.getClients(pageable).map(userMapper::toDto);
//    }
//
//    @ApiOperation(value = "Block user")
//    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
//    @PostMapping("/block")
//    public void setBlock(@RequestParam Long userId) {
//        adminService.setUserLockedType(userId, true);
//    }
//
//    @ApiOperation(value = "Unblock user")
//    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
//    @PostMapping("/unblock")
//    public void setUnBlock(@RequestParam Long userId) {
//        adminService.setUserLockedType(userId, false);
//    }
//
//    @ApiOperation(value = "Delete user")
//    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
//    @DeleteMapping("/{id}")
//    public void delete(@PathVariable Long id) {
//        userService.deleteUser(id);
//    }
//
//    @ApiOperation(value = "Update staff")
//    @PreAuthorize("hasAnyAuthority('ROLE_ADMIN')")
//    @PutMapping("/update/{id}")
//    public UserDto updateStaff(@Valid @RequestBody StaffDto staffDto, @ApiParam(required = true, name = "staff id", value = "Staff identifier.")
//    @PathVariable(name = "id") Long userId) {
//        return userMapper.toDto(userService.updateStaff(userId, staffDto));
//    }
//
//    @ApiOperation(value = "List of Trusted Devices")
//    @GetMapping("/list/trusted-device/{username}")
//    public List<TrustedDeviceDto> fetchTrustedDevices(@PathVariable @Phone String username) {
//        return trustedDeviceService.fetchTrustedDevices(username).stream().map(trustedDeviceMapper::toDto).collect(Collectors.toList());
//    }
//
//    @ApiOperation(value = "Delete Trusted Device")
//    @DeleteMapping("/trusted-device/{id}")
//    public void deleteTrustedDevice(@PathVariable Long id) {
//        trustedDeviceService.deleteTrustedDevice(id);
//    }
//
//    @ApiOperation(value = "Set new password")
//    @PostMapping(value = "/password")
//    public void setPassword(@RequestParam @Phone String login, @RequestBody UserPasswordDto userPasswordDto) {
//        userService.setNewPassword(login, userPasswordDto);
//    }
//
//    @PostMapping(value = "/profile/image", consumes = MediaType.MULTIPART_FORM_DATA_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
//    public ResponseEntity<FileObjectResponse> setUserProfileImage(@UserId Long userId, @RequestPart("file") @NotNull MultipartFile multipartFile) {
//        return ResponseEntity.ok(userService.setProfileImage(userId, multipartFile));
//    }
//
//    @GetMapping("/check/profile/image")
//    public ResponseEntity<FileObjectResponse> checkProfileImage(@UserId Long userId) {
//        return ResponseEntity.ok(userService.checkProfileImage(userId));
//    }
//
//}
