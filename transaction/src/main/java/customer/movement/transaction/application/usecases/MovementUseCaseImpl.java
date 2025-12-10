package customer.movement.transaction.application.usecases;

import customer.movement.transaction.domain.model.Account;
import customer.movement.transaction.domain.model.Movement;
import customer.movement.transaction.domain.ports.in.MovementUseCase;
import customer.movement.transaction.domain.ports.out.AccountRepositoryPort;
import customer.movement.transaction.domain.ports.out.MovementRepositoryPort;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.Comparator;

@Service
@Slf4j
@RequiredArgsConstructor
public class MovementUseCaseImpl implements MovementUseCase {
    
    private final MovementRepositoryPort movementRepositoryPort;
    private final AccountRepositoryPort accountRepositoryPort;

    @Override
    public Mono<Movement> createMovement(Movement movement) {
        log.info("Creacion del movimiento: {}", movement.getAccountNumber());

        return validateMovementAmount(movement)
                .flatMap(validMovement -> findAccount(validMovement.getAccountNumber())
                    .flatMap(account -> calculateNewBalance(account, validMovement)
                        .flatMap(newBalance -> updateAccountAndSaveMovement(account, validMovement, newBalance))
                    )
                );
    }

    private Mono<Movement> validateMovementAmount(Movement movement) {
        if (movement.getAmount() <= 0) {
            log.error("El monto tiene que ser mayor a 0.");
            return Mono.error(new IllegalArgumentException("El monto tiene que ser mayor a 0"));
        }
        return Mono.just(movement);
    }

    private Mono<Account> findAccount(int accountNumber) {
        return accountRepositoryPort.findById(accountNumber)
                .switchIfEmpty(Mono.error(
                    new IllegalArgumentException("La cuenta no existe: " + accountNumber)
                ));
    }

    private Mono<Double> calculateNewBalance(Account account, Movement movement) {
        return movementRepositoryPort.findAll()
                .filter(m -> m.getAccountNumber() == movement.getAccountNumber())
                .collectList()
                .flatMap(movements -> {
                    double currentBalance = movements.stream()
                        .max(Comparator.comparing(Movement::getId))
                        .map(Movement::getBalance)
                        .orElse(account.getInitialBalance());

                    return validateAndApplyMovement(currentBalance, movement);
                });
    }

    private Mono<Double> validateAndApplyMovement(double currentBalance, Movement movement) {
        if ("debito".equalsIgnoreCase(movement.getType())) {
            return validateSufficientBalance(currentBalance, movement.getAmount())
                    .map(valid -> currentBalance - movement.getAmount());
        } else if ("credito".equalsIgnoreCase(movement.getType())) {
            return Mono.just(currentBalance + movement.getAmount());
        } else {
            return Mono.error(new IllegalArgumentException("Tipo de movimiento inválido: " + movement.getType()));
        }
    }

    private Mono<Boolean> validateSufficientBalance(double currentBalance, double movementAmount) {
        if (currentBalance < movementAmount) {
            log.error("La cuenta no tiene el saldo suficiente. Balance: {}, Monto: {}", currentBalance, movementAmount);
            return Mono.error(new IllegalArgumentException("Saldo no disponible"));
        }
        return Mono.just(true);
    }

    private Mono<Movement> updateAccountAndSaveMovement(Account account, Movement movement, Double newBalance) {
        movement.setBalance(newBalance);
        movement.setDate(java.sql.Timestamp.valueOf(java.time.LocalDateTime.now()));
        
        Account updatedAccount = buildUpdatedAccount(account, newBalance);
        
        return accountRepositoryPort.save(updatedAccount)
                .flatMap(savedAccount -> {
                    log.info("El movimiento se registro con exito: {}", movement.getAccountNumber());
                    return movementRepositoryPort.save(movement);
                });
    }

    private Account buildUpdatedAccount(Account account, Double newBalance) {
        return Account.builder()
                .number(account.getNumber())
                .type(account.getType())
                .initialBalance(newBalance)
                .status(account.isStatus())
                .clientIdentification(account.getClientIdentification())
                .build();
    }

    @Override
    public Mono<Movement> getMovementById(int id) {
        return movementRepositoryPort.findById(id);
    }

    @Override
    public Flux<Movement> getAllMovements() {
        return movementRepositoryPort.findAll();
    }
}
