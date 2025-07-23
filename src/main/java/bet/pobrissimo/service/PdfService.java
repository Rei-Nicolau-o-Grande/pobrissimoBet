package bet.pobrissimo.service;

import bet.pobrissimo.dtos.ticket.SearchTicket;
import bet.pobrissimo.enums.ResultBetEnum;
import bet.pobrissimo.exception.exceptions.ValidationException;
import bet.pobrissimo.model.Ticket;
import bet.pobrissimo.repository.TicketRepository;
import bet.pobrissimo.repository.specifications.TicketSpecifications;
import net.sf.jasperreports.engine.*;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.ClassPathResource;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.util.StreamUtils;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.math.BigDecimal;
import java.text.NumberFormat;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

@Service
public class PdfService {

    private final Logger logger = LoggerFactory.getLogger(PdfService.class);

    private final TicketRepository ticketRepository;

    public PdfService(TicketRepository ticketRepository) {
        this.ticketRepository = ticketRepository;
    }

    public byte[] generateTicketsPDF(SearchTicket searchTicket) throws IOException, JRException {
        this.validateSearchTicket(searchTicket);

        Specification<Ticket> ticketsSpec = TicketSpecifications.searchTicket(searchTicket);
        List<Ticket> tickets = ticketRepository.findAll(ticketsSpec);

//        File jrxmlFile = ResourceUtils.getFile("classpath:templatesPDF/JasperTemplateTickets.jrxml");
        ClassPathResource resource = new ClassPathResource("templatesPDF/JasperTemplateTickets.jrxml");

        InputStream jrxmlInputStream = resource.getInputStream();
        JasperReport jasperReport = JasperCompileManager.compileReport(jrxmlInputStream);

        BufferedImage logo;
        try (InputStream imgInputStream = new ClassPathResource("templatesPDF/images/pobrissimo-bet-logo.png").getInputStream()) {
            byte[] logoBytes = StreamUtils.copyToByteArray(imgInputStream);
            logo = ImageIO.read(new ByteArrayInputStream(logoBytes));
            if (logo == null) {
                throw new IOException("A imagem do logo não pôde ser lida. Verifique o formato e o conteúdo do arquivo.");
            }
        }

        Long lossesBet = tickets.stream()
                .filter(ticket -> ticket.getResultBet() == ResultBetEnum.LOSER)
                .count();
        Long winningsBet = tickets.stream()
                .filter(ticket -> ticket.getResultBet() == ResultBetEnum.WINNER)
                .count();

        int countBet = tickets.size();

        BigDecimal lossesAmount = tickets.stream()
                .filter(ticket -> ticket.getResultBet() == ResultBetEnum.LOSER)
                .map(Ticket::getAmount)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        BigDecimal winningsAmount = tickets.stream()
                .filter(ticket -> ticket.getResultBet() == ResultBetEnum.WINNER)
                .map(Ticket::getAmount)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        BigDecimal totalAmount = winningsAmount.subtract(lossesAmount);

        Locale localeBR = Locale.of("pt", "BR");
        NumberFormat currencyFormatter = NumberFormat.getCurrencyInstance(localeBR);

        String lossesAmountFormatCurrency = currencyFormatter.format(lossesAmount);
        String winningsAmountFormatCurrency = currencyFormatter.format(winningsAmount);
        String totalAmountFormatCurrency = currencyFormatter.format(totalAmount);

        JRBeanCollectionDataSource dataSource = new JRBeanCollectionDataSource(tickets);

        Map<String, Object> parameters = new HashMap<>();
        parameters.put("logo", logo);
        parameters.put("Data_Table_Source", dataSource);
        parameters.put("ContagemTickets", countBet);
        parameters.put("ContagemTicketsPerdas", lossesBet);
        parameters.put("ContagemTicketsGanhos", winningsBet);
        parameters.put("PerdasDinheiro", lossesAmountFormatCurrency);
        parameters.put("GanhosDinheiro", winningsAmountFormatCurrency);
        parameters.put("TotalDinheiro", totalAmountFormatCurrency);

        try (ByteArrayOutputStream baos = new ByteArrayOutputStream()) {
            JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport, parameters, new JREmptyDataSource());
            JasperExportManager.exportReportToPdfStream(jasperPrint, baos);
            logger.info("PDF generado com sucesso.");
            return baos.toByteArray();
        }
    }

    public void validateSearchTicket(SearchTicket searchTicket) {
        this.starDateGreaterThanOrEqualEndDateValidate(searchTicket.startDate(), searchTicket.endDate());
    }

    public void starDateGreaterThanOrEqualEndDateValidate(LocalDateTime startDate, LocalDateTime endDate) {
        if (startDate != null && endDate != null) {
            if (startDate.isAfter(endDate)) throw new ValidationException(
                    HttpStatus.UNPROCESSABLE_ENTITY,
                    HttpStatus.UNPROCESSABLE_ENTITY.value(),
                    HttpStatus.UNPROCESSABLE_ENTITY.getReasonPhrase(),
                    "A data de início não pode ser maior que a data de fim."
            );

            if (startDate.isEqual(endDate)) throw new ValidationException(
                    HttpStatus.UNPROCESSABLE_ENTITY,
                    HttpStatus.UNPROCESSABLE_ENTITY.value(),
                    HttpStatus.UNPROCESSABLE_ENTITY.getReasonPhrase(),
                    "A data de início não pode ser igual que a data de fim."
            );
        }
    }

}
