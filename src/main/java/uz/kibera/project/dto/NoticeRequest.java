package uz.kibera.project.dto;

import lombok.Data;

import javax.validation.constraints.NotBlank;

@Data
public class NoticeRequest {
    @NotBlank
    private String title;

    @NotBlank
    private String content;

    private String fromDate;
    private String toDate;

    private String fileName;

}
