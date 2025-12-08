package customer.movement.transaction.application.usecases;

import customer.movement.transaction.domain.model.Account;
import customer.movement.transaction.domain.ports.in.AccountUseCase;
import customer.movement.transaction.domain.ports.out.AccountRepositoryPort;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
@RequiredArgsConstructor
public class AccountUseCaseImpl implements AccountUseCase {
    
    private final AccountRepositoryPort accountRepositoryPort;

    @Override
    public Mono<Account> createAccount(Account account) {
        return accountRepositoryPort.save(account);
    }

    @Override
    public Mono<Account> getAccountById(int id) {
        return accountRepositoryPort.findById(id);
    }

    @Override
    public Mono<Account> updateAccount(int id, Account account) {
        return accountRepositoryPort.existsById(id)
                .flatMap(exists -> {
                    if (exists) {
                        return accountRepositoryPort.save(account);
                    } else {
                        return Mono.empty();
                    }
                });
    }

    @Override
    public Mono<Void> deleteAccount(int id) {
        return accountRepositoryPort.deleteById(id);
    }

    @Override
    public Flux<Account> getAllAccounts() {
        return accountRepositoryPort.findAll();
    }
}
