package bet.pobrissimo.repository;

import bet.pobrissimo.enums.GameNames;
import bet.pobrissimo.enums.ResultBetEnum;
import bet.pobrissimo.model.Ticket;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface TicketRepository extends JpaRepository<Ticket, UUID>, JpaSpecificationExecutor<Ticket> {

    List<Ticket> findTop30ByOrderByCreatedAtDesc();

    List<Ticket> findTop30ByNameGameOrderByCreatedAtDesc(GameNames nameGame);

    List<Ticket> findTop30ByNameGameAndResultBetOrderByAmountDesc(GameNames gameNames, ResultBetEnum resultBetEnum);
}
