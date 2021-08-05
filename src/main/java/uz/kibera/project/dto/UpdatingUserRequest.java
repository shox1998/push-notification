package uz.kibera.project.dto;

import lombok.Data;
import uz.kibera.project.dao.entity.Role;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;

@Data
public class UpdatingUserRequest {
    private String firstName;
    private String lastName;

    private String password;

    private Role role;
}
