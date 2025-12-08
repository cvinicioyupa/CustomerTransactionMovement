package customer.movement.transaction.infrastructure.web.mapper;

import customer.movement.transaction.domain.model.Account;
import customer.movement.transaction.infrastructure.web.dto.AccountDto;
import org.springframework.stereotype.Component;

@Component
public class AccountDtoMapper {
    
    public Account toDomain(AccountDto dto) {
        if (dto == null) {
            return null;
        }
        return Account.builder()
                .number(dto.getNumber())
                .type(dto.getType())
                .initialBalance(dto.getInitialBalance())
                .status(dto.isStatus())
                .clientIdentification(dto.getClientIdentification())
                .build();
    }
    
    public AccountDto toDto(Account account) {
        if (account == null) {
            return null;
        }
        return AccountDto.builder()
                .number(account.getNumber())
                .type(account.getType())
                .initialBalance(account.getInitialBalance())
                .status(account.isStatus())
                .clientIdentification(account.getClientIdentification())
                .build();
    }
}
