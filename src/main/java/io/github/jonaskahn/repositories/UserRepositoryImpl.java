package io.github.jonaskahn.repositories;

import io.github.jonaskahn.assistant.query.JpaQueryExecutor;
import io.github.jonaskahn.entities.User;
import io.github.jonaskahn.entities.enums.Status;
import io.github.jonaskahn.services.user.UserDto;
import io.jooby.Context;
import jakarta.inject.Inject;
import jakarta.persistence.EntityManager;
import org.apache.commons.lang3.ObjectUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

public class UserRepositoryImpl extends BaseRepositoryImpl<User, Long> implements UserRepository {

    private static final Logger log = LoggerFactory.getLogger(UserRepositoryImpl.class);

    private final EntityManager entityManager;

    @Inject
    public UserRepositoryImpl(EntityManager entityManager, Context context) {
        super(entityManager, User.class, context);
        this.entityManager = entityManager;
    }

    @Override
    public Optional<User> findByUsernameOrEmail(String username, String email) {
        var query = entityManager.createQuery("select u from User u where username = :username or email = :email", User.class);
        query.setParameter("username", username);
        query.setParameter("email", email);
        try {
            return Optional.of(query.getSingleResult());
        } catch (Exception e) {
            log.warn("Could not find the users with given username or email", e);
            return Optional.empty();
        }
    }

    @Override
    public Optional<User> findActivatedUserByPreferredUsername(Long preferredUsername) {
        var query = entityManager.createQuery(
                "select u from User u where preferredUsername = :preferredUsername and status = :status",
                User.class
        );
        query.setParameter("preferredUsername", preferredUsername);
        query.setParameter("status", Status.ACTIVATED);
        try {
            return Optional.of(query.getSingleResult());
        } catch (Exception e) {
            log.warn("Could not find the users with given preferredUsername", e);
            return Optional.empty();
        }
    }

    @Override
    public Boolean existsByUsernameOrEmail(String username, String email) {
        if (ObjectUtils.allNull(username, email)) return false;
        var sql = new StringBuilder("select 1 from users u where 1=1 ");
        var params = new HashMap<String, Object>();
        if (username != null) {
            sql.append(" and u.username = :username");
            params.put("username", username);
        }
        if (email != null) {
            sql.append(" and u.email = :email");
            params.put("email", email);
        }
        var query = entityManager.createQuery("select exists (" + sql + ")", Boolean.class);
        params.forEach(query::setParameter);
        return query.getSingleResult();
    }

    @Override
    public Optional<UserDto> findCustomActivatedUserByPreferredUsername(Long preferredUsername) {
        var query = entityManager.createNativeQuery("select * from users where preferred_username = :preferredUsername and status = :status");
        try {
            var result = JpaQueryExecutor.builder(UserDto.class)
                    .with(query, Map.of(
                            "preferredUsername", preferredUsername,
                            "status", Status.Code.ACTIVATED)
                    )
                    .getSingleResult();
            return Optional.of(result);
        } catch (Exception e) {
            log.warn("Could not find the users with given preferredUsername", e);
            return Optional.empty();
        }
    }
}
