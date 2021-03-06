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
import uz.kibera.project.mapper.annotation.CreateNewEntity;
import uz.kibera.project.mapper.annotation.UpdateEntity;

@Mapper
public interface NotificationMapper {
    @CreateNewEntity
    Push toNewPushEntity(PushRequest pushRequest);

    @Mapping(target = "createdDate", dateFormat = "dd-MM-yyyy hh:mm:ss a", source = "created")
    PushDto toPushDto(Push push);

    @CreateNewEntity
    @Mapping(target = "toDate", dateFormat = "dd-MM-yyyy")
    @Mapping(target = "fromDate", dateFormat = "dd-MM-yyyy")
    Notice toNewNoticeEntity(NoticeRequest noticeRequest);

    @Mapping(target = "fromDate", dateFormat = "dd-MM-yyyy")
    @Mapping(target = "toDate", dateFormat = "dd-MM-yyyy")
    NoticeDto toNoticeDto(Notice notice);

    @UpdateEntity
    void updatePush(@MappingTarget Push updatablePush, PushRequest pushRequest);

    @UpdateEntity
    @Mapping(target = "toDate", dateFormat = "dd-MM-yyyy")
    @Mapping(target = "fromDate", dateFormat = "dd-MM-yyyy")
    void updateNotice(@MappingTarget Notice updatableNotice, NoticeRequest noticeRequest);
}
