
package customer.movement.transaction.service;

import org.springframework.stereotype.Service;

import customer.movement.transaction.model.Movement;
import customer.movement.transaction.repository.AccountRepository;
import customer.movement.transaction.repository.MovementRepository;


import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import lombok.extern.slf4j.Slf4j;
import java.util.Comparator;

@Service
@Slf4j
public class MovementService {
    private final MovementRepository movementRepository;
    private final AccountRepository accountRepository;

  
    public MovementService(MovementRepository movementRepository, AccountRepository accountRepository) {
        this.movementRepository = movementRepository;
        this.accountRepository = accountRepository;
    }

 /* 
    public Mono<Movement> create(Movement movement) {
        log.info("Attempting to create movement for account number: {}", movement.getAccountNumber());

        if (movement.getValue() <= 0) {
            log.error("Movement value must be greater than zero.");
            return Mono.error(new IllegalArgumentException("The value of a movement must be greater than zero"));
        }

        return Mono.justOrEmpty(accountRepository.findById(movement.getAccountNumber()))
            .flatMap(account -> {
                return Flux.fromIterable(movementRepository.findAll())
                    .filter(m -> m.getAccountNumber() == movement.getAccountNumber())
                    .collectList()
                    .flatMap(movements -> {
                        return movements.stream()
                            .max(Comparator.comparing(Movement::getId))
                            .map(lastMovement -> {
                                double newBalance = lastMovement.getBalance();
        
                                if ("debito".equalsIgnoreCase(movement.getType())) {
                                    if (newBalance < movement.getValue()) {
                                        log.error("Insufficient balance for account number: {}", movement.getAccountNumber());
                                        return Mono.<Movement>error(new IllegalArgumentException("Saldo no disponible"));
                                    }
                                    newBalance -= movement.getValue();
                                } else if ("credito".equalsIgnoreCase(movement.getType())) {
                                    newBalance += movement.getValue();
                                }
        
                                movement.setBalance(newBalance);
                            
                                account.setInitialBalance(newBalance);
                                accountRepository.save(account);
                                log.info("Movement recorded successfully for account number: {}", movement.getAccountNumber());
                                return Mono.justOrEmpty(movementRepository.save(movement));
                            })
                            .orElseGet(() -> {
                                log.error("No previous movements found for account number: {}", movement.getAccountNumber());
                                return Mono.<Movement>error(new IllegalArgumentException("No previous movements found for account number: " + movement.getAccountNumber()));
                            });
                    });
            })
            .switchIfEmpty(
                Mono.error(new IllegalArgumentException("Account does not exist with number: " + movement.getAccountNumber()))
            );
    }
*/

public Mono<Movement> create(Movement movement) {
    log.info("Creacion del movimiento: {}", movement.getAccountNumber());

    if (movement.getValue() <= 0) {
        log.error("El monto tiene que ser mayor a 0.");
        return Mono.error(new IllegalArgumentException("El monto tiene que ser mayor a 0"));
    }

    return Mono.justOrEmpty(accountRepository.findById(movement.getAccountNumber()))
        .flatMap(account -> {
            return Flux.fromIterable(movementRepository.findAll())
                .filter(m -> m.getAccountNumber() == movement.getAccountNumber())
                .collectList()
                .flatMap(movements -> {
                    // Si no existe el movimientos previo traigo el saldo inicial de la cuenta
                    double newBalance = movements.stream()
                        .max(Comparator.comparing(Movement::getId))
                        .map(Movement::getBalance)
                        .orElse(account.getInitialBalance());

                    if ("debito".equalsIgnoreCase(movement.getType())) {
                        if (newBalance < movement.getValue()) {
                            log.error("La cuenta no tiene el saldo suficiente: {}", movement.getAccountNumber());
                            return Mono.<Movement>error(new IllegalArgumentException("Saldo no disponible"));
                        }
                        newBalance -= movement.getValue();
                    } else if ("credito".equalsIgnoreCase(movement.getType())) {
                        newBalance += movement.getValue();
                    }

                    movement.setBalance(newBalance);
                    movement.setDate(java.sql.Timestamp.valueOf(java.time.LocalDateTime.now()));
                    account.setInitialBalance(newBalance);
                    accountRepository.save(account);
                    log.info("El movimiento se registro con exito: {}", movement.getAccountNumber());
                    return Mono.justOrEmpty(movementRepository.save(movement));
                });
        })
        .switchIfEmpty(
            Mono.error(new IllegalArgumentException("La cuenta no existe: " + movement.getAccountNumber()))
        );
}



    public Mono<Movement> getById(int id) {
        return Mono.justOrEmpty(movementRepository.findById(id));
    }

    public Mono<Movement> update(int id, Movement movement) {
        if (movementRepository.existsById(id)) {
            return Mono.just(movementRepository.save(movement));
        } else {
            return Mono.empty();
        }
    }

    public Mono<Void> delete(int id) {
        movementRepository.deleteById(id);
        return Mono.empty();
    }

    public Flux<Movement> getAll() {
        return Flux.fromIterable(movementRepository.findAll());
    }
}
