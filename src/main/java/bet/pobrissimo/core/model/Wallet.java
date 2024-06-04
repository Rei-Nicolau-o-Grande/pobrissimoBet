package bet.pobrissimo.core.model;

import jakarta.persistence.*;

import java.math.BigDecimal;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

@Entity
@Table(schema = "bank", name = "wallets")
public class Wallet {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(precision = 19, scale = 4)
    private BigDecimal amount;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    @OneToMany(mappedBy = "wallet")
    private List<Transaction> transactions;

    public Wallet(UUID id, BigDecimal amount, User user, List<Transaction> transactions) {
        this.id = id;
        this.amount = amount;
        this.user = user;
        this.transactions = transactions;
    }

    public Wallet(User user, BigDecimal amount) {
        this.setUser(user);
        this.setAmount(amount);
    }

    public Wallet() {
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public List<Transaction> getTransactions() {
        return transactions;
    }

    public void setTransactions(List<Transaction> transactions) {
        this.transactions = transactions;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Wallet wallet = (Wallet) o;
        return Objects.equals(id, wallet.id) && Objects.equals(amount, wallet.amount) && Objects.equals(user, wallet.user) && Objects.equals(transactions, wallet.transactions);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, amount, user, transactions);
    }

    @Override
    public String toString() {
        return "Wallet{" +
                "id=" + id +
                ", amount=" + amount +
                ", user=" + user +
                ", transactions=" + transactions +
                '}';
    }
}
