package customer.movement.transaction.infrastructure.persistence.mapper;

import customer.movement.transaction.domain.model.Account;
import customer.movement.transaction.infrastructure.persistence.entity.AccountEntity;
import org.springframework.stereotype.Component;

@Component
public class AccountMapper {
    
    public AccountEntity toEntity(Account account) {
        if (account == null) {
            return null;
        }
        return AccountEntity.builder()
                .number(account.getNumber())
                .type(account.getType())
                .initialBalance(account.getInitialBalance())
                .status(account.isStatus())
                .clientIdentification(account.getClientIdentification())
                .build();
    }
    
    public Account toDomain(AccountEntity entity) {
        if (entity == null) {
            return null;
        }
        return Account.builder()
                .number(entity.getNumber())
                .type(entity.getType())
                .initialBalance(entity.getInitialBalance())
                .status(entity.isStatus())
                .clientIdentification(entity.getClientIdentification())
                .build();
    }
}
