package org.nar.jwtproyect.Repository;

import org.nar.jwtproyect.Entities.User;
import org.springframework.data.repository.CrudRepository;

import java.util.Optional;

public interface UserRepository extends CrudRepository<User,Long> {
    // metodo personalizado query method
    boolean existsByUsername(String username);
    Optional<User> findByUsername(String username);
}
