package customer.movement.transaction.domain.ports.out;

import customer.movement.transaction.domain.model.Movement;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.sql.Timestamp;
import java.util.List;

public interface MovementRepositoryPort {
    Mono<Movement> save(Movement movement);
    Mono<Movement> findById(int id);
    Flux<Movement> findAll();
    List<Movement> findByAccountNumberAndDateBetween(int accountNumber, Timestamp startDate, Timestamp endDate);
}
