package bet.pobrissimo.core.repository;

import bet.pobrissimo.core.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface UserRepository extends JpaRepository<User, UUID>, JpaSpecificationExecutor<User> {

//    @Query("SELECT u.email FROM User u WHERE u.email = :email")
    Optional<User> findByEmail(String email);

    Optional<User> findById(UUID userId);

    Optional<User> findByUsername(String username);

}
