package boraldan.bank.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.NonNull;

import java.math.BigDecimal;

@Data
public class PayDTO {
    // Номер кредитной карты покупателя
    @NotNull
    private Long bayerCard;

    // Номер кредитной карты продавца
    @NotNull
    private Long sellerCard;

    // Сумма платежа
    @NotNull
    private BigDecimal amount;
}
