package customer.movement.transaction.infrastructure.persistence.adapter;

import customer.movement.transaction.domain.model.Account;
import customer.movement.transaction.domain.ports.out.AccountRepositoryPort;
import customer.movement.transaction.infrastructure.persistence.mapper.AccountMapper;
import customer.movement.transaction.infrastructure.persistence.repository.AccountJpaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class AccountPersistenceAdapter implements AccountRepositoryPort {
    
    private final AccountJpaRepository accountJpaRepository;
    private final AccountMapper accountMapper;

    @Override
    public Mono<Account> save(Account account) {
        return Mono.just(accountMapper.toDomain(
                accountJpaRepository.save(accountMapper.toEntity(account))
        ));
    }

    @Override
    public Mono<Account> findById(int id) {
        return Mono.justOrEmpty(
                accountJpaRepository.findById(id)
                        .map(accountMapper::toDomain)
        );
    }

    @Override
    public Mono<Void> deleteById(int id) {
        accountJpaRepository.deleteById(id);
        return Mono.empty();
    }

    @Override
    public Mono<Boolean> existsById(int id) {
        return Mono.just(accountJpaRepository.existsById(id));
    }

    @Override
    public Flux<Account> findAll() {
        return Flux.fromIterable(
                accountJpaRepository.findAll()
                        .stream()
                        .map(accountMapper::toDomain)
                        .collect(Collectors.toList())
        );
    }

    @Override
    public List<Account> findByClientIdentification(int clientIdentification) {
        return accountJpaRepository.findByClientIdentification(clientIdentification)
                .stream()
                .map(accountMapper::toDomain)
                .collect(Collectors.toList());
    }
}
