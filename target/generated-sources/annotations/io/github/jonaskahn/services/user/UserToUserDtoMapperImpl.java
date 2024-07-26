package io.github.jonaskahn.services.user;

import io.github.jonaskahn.entities.User;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.processing.Generated;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2024-07-26T17:18:17+0700",
    comments = "version: 1.5.5.Final, compiler: javac, environment: Java 21.0.3 (Amazon.com Inc.)"
)
public class UserToUserDtoMapperImpl implements UserToUserDtoMapper {

    @Override
    public UserDto userToUserDto(User user) {
        if ( user == null ) {
            return null;
        }

        UserDto.UserDtoBuilder userDto = UserDto.builder();

        userDto.id( user.getId() );
        userDto.username( user.getUsername() );
        userDto.email( user.getEmail() );
        userDto.fullName( user.getFullName() );
        userDto.status( user.getStatus() );
        List<String> list = user.getRoles();
        if ( list != null ) {
            userDto.roles( new ArrayList<String>( list ) );
        }
        userDto.createdAt( user.getCreatedAt() );
        userDto.createdBy( user.getCreatedBy() );
        userDto.updatedAt( user.getUpdatedAt() );
        userDto.updatedBy( user.getUpdatedBy() );

        return userDto.build();
    }
}
