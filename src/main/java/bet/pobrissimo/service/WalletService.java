package bet.pobrissimo.service;

import bet.pobrissimo.dtos.wallet.MyWalletResponseDto;
import bet.pobrissimo.exception.exceptions.WalletUserNotFound;
import bet.pobrissimo.model.User;
import bet.pobrissimo.model.Wallet;
import bet.pobrissimo.repository.TransactionRepository;
import bet.pobrissimo.repository.WalletRepository;
import bet.pobrissimo.config.AccessControlService;
import bet.pobrissimo.config.AuthenticatedCurrentUser;
import bet.pobrissimo.exception.exceptions.EntityNotFoundException;
import bet.pobrissimo.exception.exceptions.TransactionWithDrawException;
import bet.pobrissimo.util.ValidateConvertStringToUUID;
import org.springframework.http.HttpStatus;
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
        Wallet wallet = this.walletRepository.findByUserId(userId).orElseThrow(
                () -> new WalletUserNotFound(
                        HttpStatus.NOT_FOUND,
                        HttpStatus.NOT_FOUND.value(),
                        HttpStatus.NOT_FOUND.getReasonPhrase(),
                        "Wallet do usuário logado não encontrada."
                ));
        BigDecimal amount = this.getAmountByUserId(userId);
        return new MyWalletResponseDto(wallet.getId(), amount);
    }

    @Transactional(readOnly = true)
    public BigDecimal getAmountByUserId(UUID userId) {
        return this.walletRepository.getAmountByUserId(userId);
    }

    @Transactional(readOnly = true)
    public Wallet findWalletById(String walletId) {
        UUID walletUUID = ValidateConvertStringToUUID.validate(walletId, "Wallet não encontrada.");

        return this.walletRepository.findById(walletUUID)
                .orElseThrow(() -> new EntityNotFoundException(
                        HttpStatus.NOT_FOUND,
                        HttpStatus.NOT_FOUND.value(),
                        HttpStatus.NOT_FOUND.getReasonPhrase(),
                        "Wallet não encontrada."
                ));
    }

    @Transactional(readOnly = true)
    protected void validateScaleValue(BigDecimal value) {
        if (value.precision() > 15 && value.scale() > 2) {
            throw new TransactionWithDrawException(
                    HttpStatus.BAD_REQUEST,
                    HttpStatus.BAD_REQUEST.value(),
                    HttpStatus.BAD_REQUEST.getReasonPhrase(),
                    "Valor inválido."
            );
        }
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
        this.validateScaleValue(value);
        if (value.doubleValue() > wallet.getAmount().doubleValue()) {
            throw new TransactionWithDrawException(
                    HttpStatus.BAD_REQUEST,
                    HttpStatus.BAD_REQUEST.value(),
                    HttpStatus.BAD_REQUEST.getReasonPhrase(),
                    "Saldo insuficiente.");
        }
        wallet.setAmount(wallet.getAmount().subtract(value));
        this.walletRepository.save(wallet);
    }
}
