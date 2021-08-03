package uz.kibera.project.dto;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

import lombok.Data;
import uz.kibera.project.dao.entity.types.PushType;

import java.time.LocalDateTime;
import java.util.UUID;

@Data
public class PushDto {
    @NotNull
    private UUID id;

    @NotBlank
    private String title;

    @NotBlank
    private String content;

    private PushType pushType;

    private String createdDate;
}
