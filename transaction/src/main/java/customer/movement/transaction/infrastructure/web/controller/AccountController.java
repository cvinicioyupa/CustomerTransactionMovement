package customer.movement.transaction.infrastructure.web.controller;

import customer.movement.transaction.domain.ports.in.AccountUseCase;
import customer.movement.transaction.infrastructure.web.dto.AccountDto;
import customer.movement.transaction.infrastructure.web.mapper.AccountDtoMapper;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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
    public Mono<ResponseEntity<AccountDto>> createAccount(@Valid @RequestBody AccountDto accountDto) {
        return accountUseCase.createAccount(accountDtoMapper.toDomain(accountDto))
                .map(account -> new ResponseEntity<>(accountDtoMapper.toDto(account), HttpStatus.CREATED));
    }

    @GetMapping("/{id}")
    public Mono<ResponseEntity<AccountDto>> getAccount(@PathVariable int id) {
        return accountUseCase.getAccountById(id)
                .map(account -> new ResponseEntity<>(accountDtoMapper.toDto(account), HttpStatus.OK))
                .defaultIfEmpty(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    @PutMapping("/{id}")
    public Mono<ResponseEntity<AccountDto>> updateAccount(@PathVariable int id, @Valid @RequestBody AccountDto accountDto) {
        return accountUseCase.updateAccount(id, accountDtoMapper.toDomain(accountDto))
                .map(account -> new ResponseEntity<>(accountDtoMapper.toDto(account), HttpStatus.OK))
                .defaultIfEmpty(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    @DeleteMapping("/{id}")
    public Mono<ResponseEntity<Void>> deleteAccount(@PathVariable int id) {
        return accountUseCase.deleteAccount(id)
                .then(Mono.just(new ResponseEntity<Void>(HttpStatus.NO_CONTENT)))
                .defaultIfEmpty(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }
}
