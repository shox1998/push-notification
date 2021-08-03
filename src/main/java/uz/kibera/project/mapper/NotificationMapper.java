package uz.kibera.project.mapper;

import org.mapstruct.Mapper;
import uz.kibera.project.dao.entity.Push;
import uz.kibera.project.dto.PushDto;
import uz.kibera.project.dto.PushRequest;

@Mapper
public interface NotificationMapper {
    @CreateNewEntity
    Push toNewPushEntity(PushRequest pushRequest);

    PushDto toPushDto(Push push);
}
