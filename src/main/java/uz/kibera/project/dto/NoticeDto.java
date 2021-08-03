package uz.kibera.project.dto;

import lombok.Data;
import uz.kibera.project.dao.entity.types.NoticeType;

@Data
public class NoticeDto {
    private String title;
    private String content;
    private NoticeType noticeType;
    private String imageUrl;
    private String fromDate;
    private String toDate;

}
