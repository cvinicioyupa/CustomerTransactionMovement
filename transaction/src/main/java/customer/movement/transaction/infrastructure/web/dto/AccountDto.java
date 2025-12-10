package customer.movement.transaction.infrastructure.web.dto;

import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AccountDto {
    
    @NotNull(message = "El número de cuenta no puede ser nulo")
    @Min(value = 1, message = "El número de cuenta debe ser mayor a 0")
    private int number;
    
    @NotBlank(message = "El tipo de cuenta no puede estar vacío")
    @Size(min = 3, max = 50, message = "El tipo de cuenta debe tener entre 3 y 50 caracteres")
    private String type;
    
    @NotNull(message = "El saldo inicial no puede ser nulo")
    @DecimalMin(value = "0.0", inclusive = false, message = "El saldo inicial debe ser mayor a 0")
    private double initialBalance;
    
    @NotNull(message = "El estado no puede ser nulo")
    private boolean status;
    
    @NotNull(message = "La identificación del cliente no puede ser nula")
    @Min(value = 1, message = "La identificación del cliente debe ser mayor a 0")
    private int clientIdentification;
}
