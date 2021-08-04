package uz.kibera.project.dto;

import lombok.Data;
import uz.kibera.project.dao.entity.types.NoticeType;

import java.util.UUID;

@Data
public class NoticeDto {
    private UUID id;
    private String title;
    private String content;
    private NoticeType noticeType;
    private String imageUrl;
    private String fromDate;
    private String toDate;

}
