package bet.pobrissimo.core.repository;

import bet.pobrissimo.core.model.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface RoleRepository extends JpaRepository<Role, Long> {

//    @Query("SELECT r.name FROM Role r WHERE r.name = :role")
    Role findByName(String role);
}
