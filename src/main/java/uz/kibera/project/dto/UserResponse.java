package uz.kibera.project.dto;

import lombok.Data;
import uz.kibera.project.dao.entity.Role;

@Data
public class UserResponse {
    private Long id;
    private String username;
    private String email;
    private String firstName;
    private String lastName;
    private Role role;
    private Boolean locked;
    private Long regionId;
}
