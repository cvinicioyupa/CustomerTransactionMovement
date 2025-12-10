package customer.movement.transaction.infrastructure.web.controller;

import customer.movement.transaction.domain.ports.in.MovementUseCase;
import customer.movement.transaction.infrastructure.web.dto.MovementDto;
import customer.movement.transaction.infrastructure.web.mapper.MovementDtoMapper;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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
    public Mono<ResponseEntity<MovementDto>> createMovement(@Valid @RequestBody MovementDto movementDto) {
        return movementUseCase.createMovement(movementDtoMapper.toDomain(movementDto))
                .map(movement -> new ResponseEntity<>(movementDtoMapper.toDto(movement), HttpStatus.CREATED));
    }

    @GetMapping("/{id}")
    public Mono<ResponseEntity<MovementDto>> getMovement(@PathVariable int id) {
        return movementUseCase.getMovementById(id)
                .map(movement -> new ResponseEntity<>(movementDtoMapper.toDto(movement), HttpStatus.OK))
                .defaultIfEmpty(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    @GetMapping
    public Flux<MovementDto> listAllMovements() {
        return movementUseCase.getAllMovements()
                .map(movementDtoMapper::toDto);
    }
}
