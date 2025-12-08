package customer.movement.transaction.infrastructure.web.mapper;

import customer.movement.transaction.domain.model.Movement;
import customer.movement.transaction.infrastructure.web.dto.MovementDto;
import org.springframework.stereotype.Component;

@Component
public class MovementDtoMapper {
    
    public Movement toDomain(MovementDto dto) {
        if (dto == null) {
            return null;
        }
        return Movement.builder()
                .id(dto.getId())
                .date(dto.getDate())
                .type(dto.getType())
                .amount(dto.getAmount())
                .balance(dto.getBalance())
                .status(dto.isStatus())
                .accountNumber(dto.getAccountNumber())
                .build();
    }
    
    public MovementDto toDto(Movement movement) {
        if (movement == null) {
            return null;
        }
        return MovementDto.builder()
                .id(movement.getId())
                .date(movement.getDate())
                .type(movement.getType())
                .amount(movement.getAmount())
                .balance(movement.getBalance())
                .status(movement.isStatus())
                .accountNumber(movement.getAccountNumber())
                .build();
    }
}
