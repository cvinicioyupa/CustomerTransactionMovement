package customer.movement.transaction.infrastructure.web.controller;

import customer.movement.transaction.domain.ports.in.MovementUseCase;
import customer.movement.transaction.infrastructure.web.dto.MovementDto;
import customer.movement.transaction.infrastructure.web.mapper.MovementDtoMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/api/v1/movements")
@RequiredArgsConstructor
public class MovementController {
    
    private final MovementUseCase movementUseCase;
    private final MovementDtoMapper movementDtoMapper;

    @PostMapping
    public Mono<MovementDto> createMovement(@RequestBody MovementDto movementDto) {
        return movementUseCase.createMovement(movementDtoMapper.toDomain(movementDto))
                .map(movementDtoMapper::toDto);
    }

    @GetMapping("/{id}")
    public Mono<MovementDto> getMovement(@PathVariable int id) {
        return movementUseCase.getMovementById(id)
                .map(movementDtoMapper::toDto);
    }

    @GetMapping
    public Flux<MovementDto> listAllMovements() {
        return movementUseCase.getAllMovements()
                .map(movementDtoMapper::toDto);
    }
}
