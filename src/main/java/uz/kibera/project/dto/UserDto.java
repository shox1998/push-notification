package uz.kibera.project.dto;

import lombok.Data;
import uz.kibera.project.dao.entity.Role;

@Data
public class UserDto {
    private long id;
    private String username;
    private Role role;
    private String firstName;
    private String lastName;
//    private UserStatus status;
    private Boolean locked;
}
