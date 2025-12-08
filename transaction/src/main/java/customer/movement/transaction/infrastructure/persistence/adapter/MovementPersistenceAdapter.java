package customer.movement.transaction.infrastructure.persistence.adapter;

import customer.movement.transaction.domain.model.Movement;
import customer.movement.transaction.domain.ports.out.MovementRepositoryPort;
import customer.movement.transaction.infrastructure.persistence.mapper.MovementMapper;
import customer.movement.transaction.infrastructure.persistence.repository.MovementJpaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.sql.Timestamp;
import java.util.List;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class MovementPersistenceAdapter implements MovementRepositoryPort {
    
    private final MovementJpaRepository movementJpaRepository;
    private final MovementMapper movementMapper;

    @Override
    public Mono<Movement> save(Movement movement) {
        return Mono.just(movementMapper.toDomain(
                movementJpaRepository.save(movementMapper.toEntity(movement))
        ));
    }

    @Override
    public Mono<Movement> findById(int id) {
        return Mono.justOrEmpty(
                movementJpaRepository.findById(id)
                        .map(movementMapper::toDomain)
        );
    }

    @Override
    public Flux<Movement> findAll() {
        return Flux.fromIterable(
                movementJpaRepository.findAll()
                        .stream()
                        .map(movementMapper::toDomain)
                        .collect(Collectors.toList())
        );
    }

    @Override
    public List<Movement> findByAccountNumberAndDateBetween(int accountNumber, Timestamp startDate, Timestamp endDate) {
        return movementJpaRepository.findByAccountNumberAndDateBetween(accountNumber, startDate, endDate)
                .stream()
                .map(movementMapper::toDomain)
                .collect(Collectors.toList());
    }
}
