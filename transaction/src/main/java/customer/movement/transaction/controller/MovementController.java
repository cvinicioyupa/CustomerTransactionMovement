package customer.movement.transaction.controller;


import org.springframework.web.bind.annotation.*;

import customer.movement.transaction.model.Movement;
import customer.movement.transaction.service.MovementService;
import reactor.core.publisher.Mono;
import reactor.core.publisher.Flux;



@RestController
@RequestMapping("/api/v1/movements")
public class MovementController {
    private final MovementService movementService;


    public MovementController(MovementService movementService) {
        this.movementService = movementService;
    }

    @PostMapping
    public Mono<Movement> createMovement(@RequestBody Movement movement) {
        return movementService.create(movement);
    }

    @GetMapping("/{id}")
    public Mono<Movement> getMovement(@PathVariable int id) {
     
        return movementService.getById(id);
    }

  

    @GetMapping
    public Flux<Movement> listAllMovements() {
        return movementService.getAll();
    }
}
