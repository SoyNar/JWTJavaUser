package org.nar.jwtproyect.Repository;

import org.nar.jwtproyect.Entities.Role;
import org.nar.jwtproyect.Entities.User;
import org.springframework.data.repository.CrudRepository;

import java.util.Optional;

public interface RoleRepository extends CrudRepository<Role,Long> {
    // query method
    // devuelve un optional
    Optional<Role> findByName(String name);
}
