package bet.pobrissimo.core.model;

import bet.pobrissimo.core.enums.GameNames;
import bet.pobrissimo.core.enums.ResultBetEnum;
import jakarta.persistence.*;

import java.math.BigDecimal;
import java.util.UUID;

@Entity
@Table(name = "tickets", schema = "game")
public class Ticket {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Enumerated(EnumType.STRING)
    private GameNames nameGame;

    private BigDecimal amount;

    private Long multiplier;

    @Enumerated(EnumType.STRING)
    private ResultBetEnum resultBet;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "transaction_id")
    private Transaction transactionId;
}
