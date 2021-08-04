package uz.kibera.project.dto;

import lombok.Data;
import uz.kibera.project.dao.entity.Role;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;

@Data
public class UpdatingUserRequest {
    private String firsName;
    private String lastName;
    @NotBlank
    @Email
    private String email;

    @NotBlank
    private String password;

    private Role role;
}
