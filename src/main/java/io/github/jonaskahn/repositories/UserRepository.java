package io.github.jonaskahn.repositories;

import com.google.inject.ImplementedBy;
import io.github.jonaskahn.entities.User;
import io.github.jonaskahn.services.user.UserDto;

import java.util.Optional;

@ImplementedBy(UserRepositoryImpl.class)
public interface UserRepository extends BaseRepository<User, Long> {

    Optional<User> findByUsernameOrEmail(String username, String email);

    Optional<User> findActivatedUserByPreferredUsername(Long preferredUsername);

    Boolean existsByUsernameOrEmail(String username, String email);

    Optional<UserDto> findCustomActivatedUserByPreferredUsername(Long preferredUsername);
}
