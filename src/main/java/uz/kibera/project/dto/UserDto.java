package uz.kibera.project.dto;

import lombok.Data;
import uz.kibera.project.dao.entity.Role;

@Data
public class UserDto {
    private long id;
    private String login;
    private Role role;
    private UserStatus status;
    private Boolean locked;
}
