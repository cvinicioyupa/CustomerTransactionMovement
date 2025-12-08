package customer.movement.transaction.domain.ports.in;

import customer.movement.transaction.domain.model.Account;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface AccountUseCase {
    Mono<Account> createAccount(Account account);
    Mono<Account> getAccountById(int id);
    Mono<Account> updateAccount(int id, Account account);
    Mono<Void> deleteAccount(int id);
    Flux<Account> getAllAccounts();
}
