package bet.pobrissimo.service;

import bet.pobrissimo.dtos.transaction.TransactionRequestDto;
import bet.pobrissimo.dtos.transaction.TransactionResponseDto;
import bet.pobrissimo.enums.TransactionEnum;
import bet.pobrissimo.model.Transaction;
import bet.pobrissimo.repository.TransactionRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class TransactionService {

    private final WalletService walletService;
    private final TransactionRepository transactionRepository;

    public TransactionService(WalletService walletService,
                              TransactionRepository transactionRepository) {
        this.walletService = walletService;
        this.transactionRepository = transactionRepository;
    }

    @Transactional
    public TransactionResponseDto createTransactionDeposit(String walletId, TransactionRequestDto dto) {
        this.walletService.deposit(walletId, dto.value());
        Transaction transaction = this.transactionRepository.save(
                new Transaction(walletId, dto.value(), TransactionEnum.DEPOSIT));

        return new TransactionResponseDto(
                transaction.getId(),
                transaction.getValue(),
                transaction.getType().name(),
                transaction.getCreatedAt(),
                transaction.getWallet().toMyWalletResponseDto()
        );
    }

    @Transactional
    public TransactionResponseDto createTransactionWithDraw(String walletId, TransactionRequestDto dto) {
        this.walletService.withdraw(walletId, dto.value());
        Transaction transaction = this.transactionRepository.save(
                new Transaction(walletId, dto.value(), TransactionEnum.WITHDRAW));

        return new TransactionResponseDto(
                transaction.getId(),
                transaction.getValue(),
                transaction.getType().name(),
                transaction.getCreatedAt(),
                transaction.getWallet().toMyWalletResponseDto()
        );
    }

}
