package bet.pobrissimo.service;

import bet.pobrissimo.dtos.transaction.TransactionResponseDto;
import bet.pobrissimo.enums.GameNames;
import bet.pobrissimo.model.Ticket;
import bet.pobrissimo.repository.TicketRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import static bet.pobrissimo.enums.ResultBetEnum.LOSER;
import static bet.pobrissimo.enums.ResultBetEnum.WINNER;

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
