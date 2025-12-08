package customer.movement.transaction.infrastructure.persistence.mapper;

import customer.movement.transaction.domain.model.Movement;
import customer.movement.transaction.infrastructure.persistence.entity.MovementEntity;
import org.springframework.stereotype.Component;

@Component
public class MovementMapper {
    
    public MovementEntity toEntity(Movement movement) {
        if (movement == null) {
            return null;
        }
        return MovementEntity.builder()
                .id(movement.getId())
                .date(movement.getDate())
                .type(movement.getType())
                .amount(movement.getAmount())
                .balance(movement.getBalance())
                .status(movement.isStatus())
                .accountNumber(movement.getAccountNumber())
                .build();
    }
    
    public Movement toDomain(MovementEntity entity) {
        if (entity == null) {
            return null;
        }
        return Movement.builder()
                .id(entity.getId())
                .date(entity.getDate())
                .type(entity.getType())
                .amount(entity.getAmount())
                .balance(entity.getBalance())
                .status(entity.isStatus())
                .accountNumber(entity.getAccountNumber())
                .build();
    }
}
