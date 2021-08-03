package uz.kibera.project.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import uz.kibera.project.dao.entity.Notice;
import uz.kibera.project.dao.entity.Push;
import uz.kibera.project.dto.NoticeDto;
import uz.kibera.project.dto.NoticeRequest;
import uz.kibera.project.dto.PushDto;
import uz.kibera.project.dto.PushRequest;

@Mapper
public interface NotificationMapper {
    @CreateNewEntity
    Push toNewPushEntity(PushRequest pushRequest);

    @Mapping(target = "createdDate",  dateFormat = "dd-mm-yyyy hh:mm:ss a", source = "created")
    PushDto toPushDto(Push push);

    @CreateNewEntity
    @Mapping(target = "imageUrl", source = "fileName")
    @Mapping(target = "toDate", dateFormat = "yyyy-mm-dd")
    @Mapping(target = "fromDate", dateFormat = "yyyy-mm-dd")
    Notice toNewNoticeEntity(NoticeRequest noticeRequest);

    @Mapping(target = "fromDate",  dateFormat = "dd-mm-yyyy hh:mm:ss a")
    @Mapping(target = "toDate",  dateFormat = "dd-mm-yyyy hh:mm:ss a")
    NoticeDto toNoticeDto(Notice notice);
}
