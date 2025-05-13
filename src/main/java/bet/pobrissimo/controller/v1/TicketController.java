package bet.pobrissimo.controller.v1;

import bet.pobrissimo.dtos.ticket.TicketResponse;
import bet.pobrissimo.service.TicketService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("api/v1/ticket")
public class TicketController {

    private final TicketService ticketService;

    public TicketController(TicketService ticketService) {
        this.ticketService = ticketService;
    }

    @GetMapping("/burrinho-fortune")
    @PreAuthorize("hasRole('ROLE_Admin') or hasRole('ROLE_Player')")
    public ResponseEntity<List<TicketResponse>> ticketsGameBurrinhoFortune() {
        List<TicketResponse> tickets = ticketService.getTicketsWinnerGameBurrinhoFortune();
        return ResponseEntity.ok(tickets);
    }

    @GetMapping("/roda-roda-picanha")
    @PreAuthorize("hasRole('ROLE_Admin') or hasRole('ROLE_Player')")
    public ResponseEntity<List<TicketResponse>> ticketsGameRodaRodaPicanha() {
        List<TicketResponse> tickets = ticketService.getTicketsWinnerGameRodaRodaPicanha();
        return ResponseEntity.ok(tickets);
    }
}
