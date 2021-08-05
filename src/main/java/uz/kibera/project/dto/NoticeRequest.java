package uz.kibera.project.dto;

import lombok.Data;
import uz.kibera.project.dao.entity.types.NoticeType;

import javax.validation.constraints.NotBlank;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
public class NoticeRequest {
    @NotBlank
    private String title;

    @NotBlank
    private String content;

    private LocalDate fromDate;
    private LocalDate toDate;

    private NoticeType noticeType;

}
