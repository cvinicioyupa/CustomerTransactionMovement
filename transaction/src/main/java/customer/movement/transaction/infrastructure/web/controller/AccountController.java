package customer.movement.transaction.infrastructure.web.controller;

import customer.movement.transaction.domain.ports.in.AccountUseCase;
import customer.movement.transaction.infrastructure.web.dto.AccountDto;
import customer.movement.transaction.infrastructure.web.mapper.AccountDtoMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/api/v1/accounts")
@RequiredArgsConstructor
public class AccountController {
    
    private final AccountUseCase accountUseCase;
    private final AccountDtoMapper accountDtoMapper;

    @GetMapping
    public Flux<AccountDto> listAllAccounts() {
        return accountUseCase.getAllAccounts()
                .map(accountDtoMapper::toDto);
    }

    @PostMapping
    public Mono<AccountDto> createAccount(@RequestBody AccountDto accountDto) {
        return accountUseCase.createAccount(accountDtoMapper.toDomain(accountDto))
                .map(accountDtoMapper::toDto);
    }

    @GetMapping("/{id}")
    public Mono<AccountDto> getAccount(@PathVariable int id) {
        return accountUseCase.getAccountById(id)
                .map(accountDtoMapper::toDto);
    }

    @PutMapping("/{id}")
    public Mono<AccountDto> updateAccount(@PathVariable int id, @RequestBody AccountDto accountDto) {
        return accountUseCase.updateAccount(id, accountDtoMapper.toDomain(accountDto))
                .map(accountDtoMapper::toDto);
    }

    @DeleteMapping("/{id}")
    public Mono<Void> deleteAccount(@PathVariable int id) {
        return accountUseCase.deleteAccount(id);
    }
}
