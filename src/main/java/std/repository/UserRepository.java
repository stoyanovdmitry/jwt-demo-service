package std.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import std.entitty.User;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {

    Optional<User> findUserByUsername(String username);
}
