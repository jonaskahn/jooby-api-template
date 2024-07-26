package io.github.jonaskahn.repositories;

import io.github.jonaskahn.entities.BaseEntity;

import java.util.Optional;

public interface BaseRepository<Entity extends BaseEntity, ID> {

    void create(Entity entity);

    void update(Entity entity);

    void delete(Entity entity);

    void deleteById(ID id);

    Optional<Entity> findById(ID id);
}
