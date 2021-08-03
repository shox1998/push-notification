package uz.kibera.project.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class UserResponseWithAccessToken {
    private UserResponse userResponse;
    private String accessToken;
}
