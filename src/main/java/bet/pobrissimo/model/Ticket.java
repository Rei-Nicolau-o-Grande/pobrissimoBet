package bet.pobrissimo.model;

import bet.pobrissimo.enums.GameNames;
import bet.pobrissimo.enums.ResultBetEnum;
import jakarta.persistence.*;
import org.springframework.data.annotation.CreatedDate;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Objects;
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

    private LocalDateTime createdAt;

    @PrePersist
    public void prePersist() {
        this.createdAt = LocalDateTime.now();
    }

    public Ticket() {
    }

    public Ticket(UUID id, GameNames nameGame, BigDecimal amount, Long multiplier, ResultBetEnum resultBet, Transaction transactionId) {
        this.id = id;
        this.nameGame = nameGame;
        this.amount = amount;
        this.multiplier = multiplier;
        this.resultBet = resultBet;
        this.transactionId = transactionId;
    }

    public Ticket(GameNames gameName, BigDecimal value, long multiplier, ResultBetEnum resultBet, UUID transactionId) {
        this.nameGame = gameName;
        this.amount = value;
        this.multiplier = multiplier;
        this.resultBet = resultBet;
        this.transactionId = new Transaction();
        this.transactionId.setId(transactionId);
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public GameNames getNameGame() {
        return nameGame;
    }

    public void setNameGame(GameNames nameGame) {
        this.nameGame = nameGame;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public Long getMultiplier() {
        return multiplier;
    }

    public void setMultiplier(Long multiplier) {
        this.multiplier = multiplier;
    }

    public ResultBetEnum getResultBet() {
        return resultBet;
    }

    public void setResultBet(ResultBetEnum resultBet) {
        this.resultBet = resultBet;
    }

    public Transaction getTransactionId() {
        return transactionId;
    }

    public void setTransactionId(Transaction transactionId) {
        this.transactionId = transactionId;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        Ticket ticket = (Ticket) o;
        return Objects.equals(id, ticket.id) && nameGame == ticket.nameGame && Objects.equals(amount, ticket.amount) && Objects.equals(multiplier, ticket.multiplier) && resultBet == ticket.resultBet && Objects.equals(transactionId, ticket.transactionId) && Objects.equals(createdAt, ticket.createdAt);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, nameGame, amount, multiplier, resultBet, transactionId, createdAt);
    }

    @Override
    public String toString() {
        return "Ticket{" +
                "id=" + id +
                ", nameGame=" + nameGame +
                ", amount=" + amount +
                ", multiplier=" + multiplier +
                ", resultBet=" + resultBet +
                ", transactionId=" + transactionId +
                ", createdAt=" + createdAt +
                '}';
    }
}
