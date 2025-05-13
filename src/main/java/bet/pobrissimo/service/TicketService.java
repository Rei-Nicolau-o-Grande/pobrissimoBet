package bet.pobrissimo.service;

import bet.pobrissimo.dtos.ticket.TicketResponse;
import bet.pobrissimo.dtos.transaction.TransactionResponseDto;
import bet.pobrissimo.enums.GameNames;
import bet.pobrissimo.model.Ticket;
import bet.pobrissimo.repository.TicketRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static bet.pobrissimo.enums.GameNames.BURRINHO_FORTUNE;
import static bet.pobrissimo.enums.GameNames.RODA_RODA_PICANHA;
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

    @Transactional(readOnly = true)
    public List<TicketResponse> getTicketsWinnerGameBurrinhoFortune() {
        return ticketRepository.findTop30ByNameGameAndResultBetOrderByAmountDesc(BURRINHO_FORTUNE, WINNER).stream()
                .map(ticket -> new TicketResponse(
                        ticket.getId(),
                        null,
                        ticket.getAmount(),
                        ticket.getMultiplier(),
                        ticket.getResultBet().toString(),
                        null,
                        ticket.getCreatedAt()
                ))
                .toList();
    }

    @Transactional(readOnly = true)
    public List<TicketResponse> getTicketsWinnerGameRodaRodaPicanha() {
        return ticketRepository.findTop30ByNameGameAndResultBetOrderByAmountDesc(RODA_RODA_PICANHA, WINNER).stream()
                .map(ticket -> new TicketResponse(
                        ticket.getId(),
                        null,
                        ticket.getAmount(),
                        ticket.getMultiplier(),
                        ticket.getResultBet().toString(),
                        null,
                        ticket.getCreatedAt()
                ))
                .toList();
    }
}
