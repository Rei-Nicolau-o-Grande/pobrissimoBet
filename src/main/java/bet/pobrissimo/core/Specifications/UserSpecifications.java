package bet.pobrissimo.core.Specifications;

import bet.pobrissimo.core.model.User;
import jakarta.persistence.criteria.Join;
import jakarta.persistence.criteria.Predicate;
import org.springframework.data.jpa.domain.Specification;

public class UserSpecifications {
    public static Specification<User> searchByCriteria(String username, String email, Boolean isActive, String role) {
        return (root, query, criteriaBuilder) -> {
            Predicate predicate = criteriaBuilder.conjunction();

            if (username != null && !username.isEmpty()) {
                predicate = criteriaBuilder.and(predicate,
                        criteriaBuilder.like(criteriaBuilder.lower(
                                root.get("username")),
                                "%" + username.toLowerCase() + "%"));
            }
            if (email != null && !email.isEmpty()) {
                predicate = criteriaBuilder.and(predicate,
                        criteriaBuilder.like(criteriaBuilder.lower(
                                root.get("email")),
                                "%" + email.toLowerCase() + "%"));
            }
            if (isActive != null) {
                predicate = criteriaBuilder.and(predicate,
                        criteriaBuilder.equal(
                                root.get("isActive"),
                                isActive));
            }
            if (role != null && !role.isEmpty()) {
                Join<Object, Object> rolesJoin = root.join("roles");
                predicate = criteriaBuilder.and(predicate,
                        criteriaBuilder.equal(
                                rolesJoin.get("name"),
                                role));
            }

            return predicate;
        };
    }
}
