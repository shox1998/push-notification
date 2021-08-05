package uz.kibera.project.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import uz.kibera.project.dao.entity.User;
import uz.kibera.project.dto.UpdatingUserDto;
import uz.kibera.project.dto.UserResponse;
import uz.kibera.project.mapper.annotation.UpdateEntity;

@Mapper
public interface UserMapper {
    @UpdateEntity
    @Mapping(target = "username", ignore = true)
    @Mapping(target = "password", ignore = true)
    @Mapping(target = "locked", ignore = true)
    @Mapping(target = "email", ignore = true)
    @Mapping(target = "role", ignore = true)
    void updateEntity(@MappingTarget User user, UpdatingUserDto updatingUserDto);

    UserResponse toUserResponse(User user);
}
