package customer.movement.transaction.infrastructure.web.dto;

import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MovementDto {
    
    private int id;
    
    private Date date;
    
    @NotNull(message = "El número de cuenta no puede ser nulo")
    @Min(value = 1, message = "El número de cuenta debe ser mayor a 0")
    private int accountNumber;
    
    @NotBlank(message = "El tipo de movimiento no puede estar vacío")
    @Pattern(regexp = "^(debito|credito)$", message = "El tipo de movimiento debe ser 'debito' o 'credito'")
    private String type;
    
    @NotNull(message = "El monto no puede ser nulo")
    @DecimalMin(value = "0.01", message = "El monto debe ser mayor a 0.01")
    private double amount;
    
    private double balance;
    
    @NotNull(message = "El estado no puede ser nulo")
    private boolean status;
}
