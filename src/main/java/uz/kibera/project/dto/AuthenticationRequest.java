package uz.kibera.project.dto;

import lombok.Data;
import uz.kibera.project.dao.entity.Role;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Data
public class AuthenticationRequest {

    @NotBlank
    private String username;

    @NotBlank
    private String password;
}
