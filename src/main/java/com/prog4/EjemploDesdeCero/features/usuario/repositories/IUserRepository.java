package com.prog4.EjemploDesdeCero.features.usuario.repositories;

import org.springframework.data.repository.CrudRepository;
import com.prog4.EjemploDesdeCero.features.usuario.models.User;
import java.util.Optional;


public interface IUserRepository extends CrudRepository<User, Long> {
    Optional<User> findByUsername(String username);
}
