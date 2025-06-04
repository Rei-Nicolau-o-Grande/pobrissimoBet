package bet.pobrissimo.controller.v1;

import bet.pobrissimo.config.AuthenticatedCurrentUser;
import bet.pobrissimo.dtos.ticket.SearchTicket;
import bet.pobrissimo.dtos.ticket.TicketResponse;
import bet.pobrissimo.service.PdfService;
import bet.pobrissimo.service.TicketService;
import net.sf.jasperreports.engine.JRException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

@RestController
@RequestMapping("api/v1/ticket")
public class TicketController {

    private final TicketService ticketService;
    private final PdfService pdfService;

    public TicketController(TicketService ticketService,
                            PdfService pdfService) {
        this.ticketService = ticketService;
        this.pdfService = pdfService;
    }

    @PostMapping("/generate-pdf")
    @PreAuthorize("hasRole('ROLE_Player')")
    public ResponseEntity<byte[]> generatePdf(@RequestBody SearchTicket searchTicket) throws JRException, IOException {
        String username = AuthenticatedCurrentUser.getUserName();
        String dateTimeNow = LocalDateTime.now().format(DateTimeFormatter.ofPattern("dd/MM/yyyy-HH:mm:ss"));
        String filename = String.format("PobrissimoBet-Tickets-%s-%s.pdf", username, dateTimeNow);

        byte[] pdfBytes = pdfService.generateTicketsPDF(searchTicket);

        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "inline; filename=" + filename)
                .contentType(MediaType.APPLICATION_PDF)
                .body(pdfBytes);
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
