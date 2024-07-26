package io.github.jonaskahn.services.user;


import io.github.jonaskahn.constants.Jwt;
import io.github.jonaskahn.controllers.user.UserRegisterRequest;
import io.github.jonaskahn.entities.User;
import io.github.jonaskahn.entities.enums.Status;
import io.github.jonaskahn.exception.ShouldNeverOccurException;
import io.github.jonaskahn.repositories.UserRepository;
import io.github.jonaskahn.services.authen.PasswordEncoder;
import io.hypersistence.tsid.TSID;
import io.jooby.Context;
import jakarta.inject.Inject;
import lombok.RequiredArgsConstructor;
import org.pac4j.core.profile.UserProfile;

import java.util.Optional;

@RequiredArgsConstructor(onConstructor = @__(@Inject))
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;

    private final PasswordEncoder passwordEncoder;

    private final Context context;

    @Override
    public void createUser(UserRegisterRequest request) {
        if (userRepository.existsByUsernameOrEmail(request.username(), request.email())) {
            throw new UserExistException();
        }
        var newUser = User.builder()
                .email(request.email())
                .username(request.username())
                .fullName(request.name())
                .password(passwordEncoder.encode(request.password()))
                .preferredUsername(TSID.fast().toLong())
                .status(Status.ACTIVATED)
                .build();
        userRepository.create(newUser);
    }

    @Override
    public UserDto getCurrentUserInfo() {
        var preferredUsername = Optional.ofNullable(context.getUser())
                .map(UserProfile.class::cast)
                .map(u -> u.getAttribute(Jwt.Attribute.UID).toString())
                .map(Long::valueOf)
                .orElseThrow(ShouldNeverOccurException::new);
        var user = userRepository.findActivatedUserByPreferredUsername(preferredUsername).orElseThrow(UserNotFoundException::new);
        return UserToUserDtoMapper.INSTANCE.userToUserDto(user);
    }

    @Override
    public UserDto getCurrentUserInfoWithExecutor() {
        var preferredUsername = Optional.ofNullable(context.getUser())
                .map(UserProfile.class::cast)
                .map(u -> u.getAttribute(Jwt.Attribute.UID).toString())
                .map(Long::valueOf)
                .orElseThrow(ShouldNeverOccurException::new);
        return userRepository.findCustomActivatedUserByPreferredUsername(preferredUsername).orElseThrow(UserNotFoundException::new);
    }
}
