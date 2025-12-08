package customer.movement.transaction;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

import customer.movement.transaction.domain.model.Account;
import customer.movement.transaction.domain.model.Movement;
import customer.movement.transaction.domain.ports.out.AccountRepositoryPort;
import customer.movement.transaction.domain.ports.out.MovementRepositoryPort;
import customer.movement.transaction.application.usecases.MovementUseCaseImpl;

@ExtendWith(MockitoExtension.class)
public class MovementServiceTest {

    @Mock
    private MovementRepositoryPort movementRepositoryPort;

    @Mock
    private AccountRepositoryPort accountRepositoryPort;

    @InjectMocks
    private MovementUseCaseImpl movementUseCase;

    private Account account;
    private Movement movement;

    @BeforeEach
    void setUp() {
        account = Account.builder()
                .number(123)
                .type("Ahorros")
                .initialBalance(5000.0)
                .status(true)
                .clientIdentification(1)
                .build();

        movement = Movement.builder()
                .accountNumber(123)
                .type("credito")
                .amount(1000.0)
                .status(true)
                .build();
    }

    @Test
    void testCreateMovementSuccess() {
        when(accountRepositoryPort.findById(anyInt())).thenReturn(Mono.just(account));
        when(movementRepositoryPort.findAll()).thenReturn(Flux.empty());
        when(accountRepositoryPort.save(any(Account.class))).thenReturn(Mono.just(account));
        when(movementRepositoryPort.save(any(Movement.class))).thenAnswer(invocation -> {
            Movement savedMovement = invocation.getArgument(0);
            return Mono.just(savedMovement);
        });

        Mono<Movement> result = movementUseCase.createMovement(movement);

        assertNotNull(result);
        result.subscribe(mv -> {
            assertEquals(6000.0, mv.getBalance());
            assertEquals(123, mv.getAccountNumber());
            assertEquals("credito", mv.getType());
        });

        verify(accountRepositoryPort, times(1)).findById(123);
        verify(movementRepositoryPort, times(1)).save(any(Movement.class));
    }

    @Test
    void testCreateMovementInsufficientBalance() {
        movement = Movement.builder()
                .accountNumber(123)
                .type("debito")
                .amount(6000.0)
                .status(true)
                .build();

        when(accountRepositoryPort.findById(anyInt())).thenReturn(Mono.just(account));
        when(movementRepositoryPort.findAll()).thenReturn(Flux.empty());

        Mono<Movement> result = movementUseCase.createMovement(movement);

        result.subscribe(
            mv -> fail("Expected an IllegalArgumentException"),
            error -> assertTrue(error instanceof IllegalArgumentException)
        );

        verify(accountRepositoryPort, times(1)).findById(123);
        verify(movementRepositoryPort, never()).save(any(Movement.class));
    }

    @Test
    void testCreateMovementAccountNotFound() {
        when(accountRepositoryPort.findById(anyInt())).thenReturn(Mono.empty());

        Mono<Movement> result = movementUseCase.createMovement(movement);

        result.subscribe(
            mv -> fail("Expected an IllegalArgumentException"),
            error -> assertTrue(error instanceof IllegalArgumentException)
        );

        verify(accountRepositoryPort, times(1)).findById(123);
        verify(movementRepositoryPort, never()).save(any(Movement.class));
    }
}
