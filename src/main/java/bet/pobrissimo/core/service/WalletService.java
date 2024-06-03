package bet.pobrissimo.core.service;

import bet.pobrissimo.core.dto.wallet.MyWalletResponseDto;
import bet.pobrissimo.core.model.User;
import bet.pobrissimo.core.model.Wallet;
import bet.pobrissimo.core.repository.TransactionRepository;
import bet.pobrissimo.core.repository.WalletRepository;
import bet.pobrissimo.infra.config.AuthenticatedCurrentUser;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.UUID;

@Service
public class WalletService {

    private final WalletRepository walletRepository;
    private final TransactionRepository transactionRepository;

    public WalletService(WalletRepository walletRepository,
                         TransactionRepository transactionRepository) {
        this.walletRepository = walletRepository;
        this.transactionRepository = transactionRepository;
    }

    @Transactional
    public void create(User user) {
        Wallet wallet = new Wallet(user, BigDecimal.ZERO);
        user.setWallet(wallet);
        this.walletRepository.save(wallet);
    }

    @Transactional(readOnly = true)
    public MyWalletResponseDto getMyWallet() {
        UUID userId = AuthenticatedCurrentUser.getUserId();
        BigDecimal amount = this.getAmountByUserId(userId);
        return new MyWalletResponseDto(amount);
    }

    @Transactional(readOnly = true)
    public BigDecimal getAmountByUserId(UUID userId) {
        return this.walletRepository.getAmountByUserId(userId);
    }
}
