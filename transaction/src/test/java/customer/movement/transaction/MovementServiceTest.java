
package customer.movement.transaction;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import reactor.core.publisher.Mono;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;
import java.util.Optional;
import customer.movement.transaction.model.Account;
import customer.movement.transaction.model.Movement;
import customer.movement.transaction.repository.AccountRepository;
import customer.movement.transaction.repository.MovementRepository;
import customer.movement.transaction.service.MovementService;

@ExtendWith(MockitoExtension.class)
public class MovementServiceTest {

    @Mock
    private MovementRepository movementRepository;

    @Mock
    private AccountRepository accountRepository;

    @InjectMocks
    private MovementService movementService;

    private Account account;
    private Movement movement;

    @BeforeEach
    void setUp() {
        account = new Account();
        account.setNumber(123);
        account.setInitialBalance(5000.0);

        movement = new Movement();
        movement.setAccountNumber(123);
        movement.setType("credito");
        movement.setValue(1000.0);
    }

    @Test
    void testCreateMovementSuccess() {
        when(accountRepository.findById(anyInt())).thenReturn(Optional.of(account));
        when(movementRepository.save(any(Movement.class))).thenReturn(movement);

        Mono<Movement> result = movementService.create(movement);

        assertNotNull(result);
        result.subscribe(mv -> {
            assertEquals(6000.0, mv.getBalance());
            assertEquals(123, mv.getAccountNumber());
            assertEquals("credito", mv.getType());
        });

        verify(accountRepository, times(1)).findById(123);
        verify(movementRepository, times(1)).save(movement);
    }

    @Test
    void testCreateMovementInsufficientBalance() {
        movement.setType("debito");
        movement.setValue(6000.0);

        when(accountRepository.findById(anyInt())).thenReturn(Optional.of(account));

        Mono<Movement> result = movementService.create(movement);

        result.subscribe(
            mv -> fail("Expected an IllegalArgumentException"),
            error -> assertTrue(error instanceof IllegalArgumentException)
        );

        verify(accountRepository, times(1)).findById(123);
        verify(movementRepository, never()).save(any(Movement.class));
    }

    @Test
    void testCreateMovementAccountNotFound() {
        when(accountRepository.findById(anyInt())).thenReturn(Optional.empty());

        Mono<Movement> result = movementService.create(movement);

        result.subscribe(
            mv -> fail("Expected an IllegalArgumentException"),
            error -> assertTrue(error instanceof IllegalArgumentException)
        );

        verify(accountRepository, times(1)).findById(123);
        verify(movementRepository, never()).save(any(Movement.class));
    }
}
