package bet.pobrissimo.model;

import bet.pobrissimo.enums.TransactionEnum;
import jakarta.persistence.*;
import org.hibernate.annotations.CreationTimestamp;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.Objects;
import java.util.UUID;

@Entity
@Table(schema = "bank", name = "transactions")
public class Transaction {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(precision = 15, scale = 2)
    private BigDecimal value;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "wallet_id")
    private Wallet wallet;

    @Enumerated(EnumType.STRING)
    private TransactionEnum type;

    @CreationTimestamp
    private Instant createdAt;

    public Transaction(UUID id, BigDecimal value, Wallet wallet, TransactionEnum type) {
        this.id = id;
        this.value = value;
        this.wallet = wallet;
        this.type = type;
    }

    public Transaction() {
    }

    public Transaction(String walletId, BigDecimal value, TransactionEnum type) {
        this.value = value;
        this.type = type;
        this.wallet = new Wallet();
        this.wallet.setId(UUID.fromString(walletId));
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public BigDecimal getValue() {
        return value;
    }

    public void setValue(BigDecimal value) {
        this.value = value;
    }

    public Wallet getWallet() {
        return wallet;
    }

    public void setWallet(Wallet wallet) {
        this.wallet = wallet;
    }

    public TransactionEnum getType() {
        return type;
    }

    public void setType(TransactionEnum type) {
        this.type = type;
    }

    public Instant getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Instant createdAt) {
        this.createdAt = createdAt;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        Transaction that = (Transaction) o;
        return Objects.equals(id, that.id) && Objects.equals(value, that.value) && Objects.equals(wallet, that.wallet) && type == that.type && Objects.equals(createdAt, that.createdAt);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, value, wallet, type, createdAt);
    }

    @Override
    public String toString() {
        return "Transaction{" +
                "id=" + id +
                ", value=" + value +
                ", wallet=" + wallet +
                ", type=" + type +
                ", createdAt=" + createdAt +
                '}';
    }
}
