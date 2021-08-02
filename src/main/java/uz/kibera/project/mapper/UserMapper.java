package uz.kibera.project.mapper;

import org.mapstruct.Mapper;
import uz.kibera.project.dao.entity.User;
import uz.kibera.project.dto.UserDto;

@Mapper
public interface UserMapper {
    UserDto tUserDto(User user);
}
