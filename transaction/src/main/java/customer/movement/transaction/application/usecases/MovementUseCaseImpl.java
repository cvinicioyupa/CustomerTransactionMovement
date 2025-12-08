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

        if (movement.getAmount() <= 0) {
            log.error("El monto tiene que ser mayor a 0.");
            return Mono.error(new IllegalArgumentException("El monto tiene que ser mayor a 0"));
        }

        return accountRepositoryPort.findById(movement.getAccountNumber())
            .flatMap(account -> {
                return movementRepositoryPort.findAll()
                    .filter(m -> m.getAccountNumber() == movement.getAccountNumber())
                    .collectList()
                    .flatMap(movements -> {
                        // Si no existe movimiento previo traigo el saldo inicial de la cuenta
                        double newBalance = movements.stream()
                            .max(Comparator.comparing(Movement::getId))
                            .map(Movement::getBalance)
                            .orElse(account.getInitialBalance());

                        if ("debito".equalsIgnoreCase(movement.getType())) {
                            if (newBalance < movement.getAmount()) {
                                log.error("La cuenta no tiene el saldo suficiente: {}", movement.getAccountNumber());
                                return Mono.<Movement>error(new IllegalArgumentException("Saldo no disponible"));
                            }
                            newBalance -= movement.getAmount();
                        } else if ("credito".equalsIgnoreCase(movement.getType())) {
                            newBalance += movement.getAmount();
                        }

                        movement.setBalance(newBalance);
                        movement.setDate(java.sql.Timestamp.valueOf(java.time.LocalDateTime.now()));
                        
                        Account updatedAccount = Account.builder()
                                .number(account.getNumber())
                                .type(account.getType())
                                .initialBalance(newBalance)
                                .status(account.isStatus())
                                .clientIdentification(account.getClientIdentification())
                                .build();
                        
                        return accountRepositoryPort.save(updatedAccount)
                                .flatMap(savedAccount -> {
                                    log.info("El movimiento se registro con exito: {}", movement.getAccountNumber());
                                    return movementRepositoryPort.save(movement);
                                });
                    });
            })
            .switchIfEmpty(
                Mono.error(new IllegalArgumentException("La cuenta no existe: " + movement.getAccountNumber()))
            );
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
