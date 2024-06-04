package bet.pobrissimo.core.repository;

import bet.pobrissimo.core.model.Wallet;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface WalletRepository extends JpaRepository<Wallet, UUID> {

    @Query("SELECT w.amount FROM Wallet w WHERE w.user.id = :userId")
    BigDecimal getAmountByUserId(UUID userId);

    Optional<Wallet> findById(UUID id);

}
