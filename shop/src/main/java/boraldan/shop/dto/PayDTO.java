package boraldan.shop.dto;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.NonNull;

@Data
public class PayDTO {

    private Long bayerCard;
    private Long sellerCard;
    private Double amount;

}
