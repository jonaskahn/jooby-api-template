package io.github.jonaskahn.services.user;

import io.github.jonaskahn.entities.User;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper
public interface UserToUserDtoMapper {
    UserToUserDtoMapper INSTANCE = Mappers.getMapper(UserToUserDtoMapper.class);

    UserDto userToUserDto(User user);
}
