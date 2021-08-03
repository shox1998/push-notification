package uz.kibera.project.dto;

import lombok.Builder;
import lombok.Data;

@Builder
@Data
public class NotificationDto {
    private String title;
    private String content;
    private String imageUrl;
}
