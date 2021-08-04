package uz.kibera.project.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
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

    @Mapping(target = "createdDate", dateFormat = "dd-MM-yyyy hh:mm:ss a", source = "created")
    PushDto toPushDto(Push push);

    @CreateNewEntity
    @Mapping(target = "imageUrl", source = "fileName")
    @Mapping(target = "toDate", ignore = true)
    @Mapping(target = "fromDate", ignore = true)
    Notice toNewNoticeEntity(NoticeRequest noticeRequest);

    @Mapping(target = "fromDate", dateFormat = "dd-MM-yyyy")
    @Mapping(target = "toDate", dateFormat = "dd-MM-yyyy")
    NoticeDto toNoticeDto(Notice notice);

    @UpdateEntity
    void updatePush(@MappingTarget Push updatablePush, PushRequest pushRequest);

    @UpdateEntity
    @Mapping(target = "toDate", ignore = true)
    @Mapping(target = "fromDate", ignore = true)
    void updateNotice(@MappingTarget Notice updatableNotice, NoticeRequest noticeRequest);
}
