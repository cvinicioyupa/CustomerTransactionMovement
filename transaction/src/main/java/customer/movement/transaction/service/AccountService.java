package customer.movement.transaction.service;

import org.springframework.stereotype.Service;

import customer.movement.transaction.model.Account;
import customer.movement.transaction.repository.AccountRepository;


import reactor.core.publisher.Mono;
import reactor.core.publisher.Flux;



@Service
public class AccountService {
    private final AccountRepository accountRepository;

   
    public AccountService(AccountRepository accountRepository) {
        this.accountRepository = accountRepository;
    }

    public Mono<Account> create(Account account) {
        return Mono.just(accountRepository.save(account));
    }

    public Mono<Account> getById(int id) {
        return Mono.justOrEmpty(accountRepository.findById(id));
    }

    public Mono<Account> update(int id, Account account) {
        if (accountRepository.existsById(id)) {
            return Mono.just(accountRepository.save(account));
        } else {
            return Mono.empty();
        }
    }

    public Mono<Void> delete(int id) {
        accountRepository.deleteById(id);
        return Mono.empty();
    }

    public Flux<Account> getAll() {
        return Flux.fromIterable(accountRepository.findAll());
    }
}
