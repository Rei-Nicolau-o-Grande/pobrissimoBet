package bet.pobrissimo.repository.specifications;

import bet.pobrissimo.config.AuthenticatedCurrentUser;
import bet.pobrissimo.dtos.ticket.SearchTicket;
import bet.pobrissimo.model.Ticket;
import jakarta.persistence.criteria.Predicate;
import org.springframework.data.jpa.domain.Specification;

import java.util.UUID;

public class TicketSpecifications {

    public static Specification<Ticket> searchTicket(SearchTicket searchTicket) {
        return (root, query, criteriaBuilder) -> {
            Predicate predicate = criteriaBuilder.conjunction();

            UUID userId = AuthenticatedCurrentUser.getUserId();

            predicate = criteriaBuilder.and(
                predicate,
                criteriaBuilder.equal(root.get("user").get("id"), userId));


            if (searchTicket.nameGame() != null && !searchTicket.nameGame().isEmpty()) {
                predicate = criteriaBuilder.and(
                        predicate,
                        criteriaBuilder.equal(root.get("nameGame"),
                                searchTicket.nameGame()
                        )
                );
            }

            if (searchTicket.resultBet() != null && !searchTicket.resultBet().isBlank()) {
                predicate = criteriaBuilder.and(
                        predicate,
                        criteriaBuilder.equal(
                                root.get("resultBet"),
                                searchTicket.resultBet()
                        )
                );
            }

            if (searchTicket.multiplier() != null && !searchTicket.multiplier().isBlank()) {
                predicate = criteriaBuilder.and(
                        predicate,
                        criteriaBuilder.equal(
                                root.get("multiplier"),
                                searchTicket.multiplier()
                        )
                );
            }

            if (searchTicket.startDate() != null) {
                predicate = criteriaBuilder.and(
                        predicate,
                        criteriaBuilder.greaterThanOrEqualTo(
                                root.get("createdAt"),
                                searchTicket.startDate()
                        )
                );
            }

            if (searchTicket.endDate() != null) {
                predicate = criteriaBuilder.and(
                        predicate,
                        criteriaBuilder.lessThanOrEqualTo(
                                root.get("createdAt"),
                                searchTicket.endDate()
                        )
                );
            }

            query.orderBy(criteriaBuilder.desc(root.get("createdAt")));

            return predicate;
        };
    }
}
