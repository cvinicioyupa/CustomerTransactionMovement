package customer.movement.transaction.controller;


import org.springframework.web.bind.annotation.*;

import customer.movement.transaction.model.Account;
import customer.movement.transaction.service.AccountService;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/api/v1/accounts")
public class AccountController {
    private final AccountService accountService;

  @GetMapping 
  public Flux<Account> listAllAccounts() 
  { 
    return accountService.getAll();
 }


    public AccountController(AccountService accountService) {
        this.accountService = accountService;
    }

    @PostMapping
    public Mono<Account> createAccount(@RequestBody Account account) {
        return accountService.create(account);
    }

    @GetMapping("/{id}")
    public Mono<Account> getAccount(@PathVariable int id) {
        return accountService.getById(id);
    }

    @PutMapping("/{id}")
    public Mono<Account> updateAccount(@PathVariable int id, @RequestBody Account account) {
        return accountService.update(id, account);
    }

    @DeleteMapping("/{id}")
    public Mono<Void> deleteAccount(@PathVariable int id) {
        return accountService.delete(id);
    }
}
