package bet.pobrissimo.core.service;

import bet.pobrissimo.core.dtos.transaction.TransactionResponseDto;
import bet.pobrissimo.core.enums.GameNames;
import bet.pobrissimo.core.model.Ticket;
import bet.pobrissimo.core.repository.TicketRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import static bet.pobrissimo.core.enums.ResultBetEnum.LOSER;
import static bet.pobrissimo.core.enums.ResultBetEnum.WINNER;

@Service
public class TicketService {

    private final TicketRepository ticketRepository;

    public TicketService(TicketRepository ticketRepository) {
        this.ticketRepository = ticketRepository;
    }

    @Transactional
    public void createTicket(TransactionResponseDto transactionResponse, GameNames gameName, long multiplier) {
        Ticket ticket = new Ticket(
                gameName,
                transactionResponse.value(),
                multiplier,
                multiplier > 0 ? WINNER : LOSER,
                transactionResponse.id()
        );
        ticketRepository.save(ticket);

    }
}
