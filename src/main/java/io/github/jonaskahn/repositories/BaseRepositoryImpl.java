package io.github.jonaskahn.repositories;

import io.github.jonaskahn.entities.BaseEntity;
import io.jooby.Context;
import jakarta.persistence.EntityManager;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import org.pac4j.core.profile.UserProfile;

import java.util.Optional;

@RequiredArgsConstructor(access = AccessLevel.PUBLIC)
public class BaseRepositoryImpl<Entity extends BaseEntity, ID> implements BaseRepository<Entity, ID> {
    private final EntityManager entityManager;
    private final Class<Entity> clazz;
    private final Context context;


    public void create(Entity e) {
        e.setCreatedBy(getCurrentLoggedInUserId());
        e.setUpdatedBy(getCurrentLoggedInUserId());
        entityManager.persist(e);
    }

    private Long getCurrentLoggedInUserId() {
        return Optional.ofNullable(context.getUser())
                .map(UserProfile.class::cast)
                .map(UserProfile::getId)
                .map(Long::valueOf)
                .orElse(0L);
    }

    public void update(Entity e) {
        e.setUpdatedBy(getCurrentLoggedInUserId());
        entityManager.persist(e);
    }

    public void delete(Entity e) {
        entityManager.remove(e);
    }

    public void deleteById(ID id) {
        findById(id).ifPresent(entityManager::remove);
    }

    public Optional<Entity> findById(ID id) {
        return Optional.ofNullable(entityManager.find(clazz, id));
    }
}
