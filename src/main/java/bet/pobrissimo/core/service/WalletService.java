package bet.pobrissimo.core.service;

import bet.pobrissimo.core.dtos.wallet.MyWalletResponseDto;
import bet.pobrissimo.core.model.User;
import bet.pobrissimo.core.model.Wallet;
import bet.pobrissimo.core.repository.TransactionRepository;
import bet.pobrissimo.core.repository.WalletRepository;
import bet.pobrissimo.infra.config.AccessControlService;
import bet.pobrissimo.infra.config.AuthenticatedCurrentUser;
import bet.pobrissimo.infra.exception.EntityNotFoundException;
import bet.pobrissimo.infra.exception.InvalidUUIDException;
import bet.pobrissimo.infra.exception.TransactionWithDrawException;
import bet.pobrissimo.infra.util.ValidateConvertStringToUUID;
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

    @Transactional(readOnly = true)
    public Wallet findWalletById(String walletId) {
        UUID walletUUID = ValidateConvertStringToUUID.validate(walletId, "Wallet não encontrada.");

        return this.walletRepository.findById(walletUUID)
                .orElseThrow(() -> new EntityNotFoundException("Wallet não encontrada."));
    }

    @Transactional
    public void deposit(String walletId, BigDecimal value) {
        var wallet = this.findWalletById(walletId);
        AccessControlService.checkPermission(wallet.getUser().getId().toString());
        wallet.setAmount(wallet.getAmount().add(value));
        this.walletRepository.save(wallet);
    }

    @Transactional
    public void withdraw(String walletId, BigDecimal value) {
        var wallet = this.findWalletById(walletId);
        AccessControlService.checkPermission(wallet.getUser().getId().toString());
        if (value.doubleValue() > wallet.getAmount().doubleValue()) {
            throw new TransactionWithDrawException("Saldo insuficiente.");
        }
        wallet.setAmount(wallet.getAmount().subtract(value));
        this.walletRepository.save(wallet);
    }
}
