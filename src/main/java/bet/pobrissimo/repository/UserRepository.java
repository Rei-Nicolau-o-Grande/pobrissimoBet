package bet.pobrissimo.repository;

import bet.pobrissimo.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface UserRepository extends JpaRepository<User, UUID>, JpaSpecificationExecutor<User> {

    Optional<User> findByEmail(String email);

    Optional<User> findById(UUID userId);

    Optional<User> findByUsername(String username);

    boolean existsByEmail(String value);

    boolean existsByUsername(String value);
}
