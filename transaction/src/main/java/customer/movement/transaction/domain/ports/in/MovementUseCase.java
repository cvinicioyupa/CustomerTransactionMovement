package customer.movement.transaction.domain.ports.in;

import customer.movement.transaction.domain.model.Movement;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface MovementUseCase {
    Mono<Movement> createMovement(Movement movement);
    Mono<Movement> getMovementById(int id);
    Flux<Movement> getAllMovements();
}
