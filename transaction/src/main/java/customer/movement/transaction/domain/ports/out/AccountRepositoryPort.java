package customer.movement.transaction.domain.ports.out;

import customer.movement.transaction.domain.model.Account;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;

public interface AccountRepositoryPort {
    Mono<Account> save(Account account);
    Mono<Account> findById(int id);
    Mono<Void> deleteById(int id);
    Mono<Boolean> existsById(int id);
    Flux<Account> findAll();
    List<Account> findByClientIdentification(int clientIdentification);
}
