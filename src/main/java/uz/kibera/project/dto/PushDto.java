package uz.kibera.project.dto;

import javax.validation.constraints.NotBlank;

import lombok.Data;
import uz.kibera.project.dao.entity.types.PushType;

@Data
public class PushDto {
    @NotBlank
    private String title;

    @NotBlank
    private String content;

    private PushType pushType;
}
