package uz.kibera.project.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import uz.kibera.project.dto.UserDto;
import uz.kibera.project.service.UserService;

@RestController
@RequestMapping("/admin")
@RequiredArgsConstructor
public class AdminController {
    private final UserService userService;

    @GetMapping(value = "/list/users", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Page<UserDto>>  fetchUsers(@PageableDefault(sort = "username") Pageable pageable) {
        return ResponseEntity.ok(userService.fetchUsers(pageable));
    }


}
