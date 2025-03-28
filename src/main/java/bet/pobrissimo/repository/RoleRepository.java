package bet.pobrissimo.repository;

import bet.pobrissimo.model.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RoleRepository extends JpaRepository<Role, Long> {

//    @Query("SELECT r.name FROM Role r WHERE r.name = :role")
    Role findByName(String role);
}
