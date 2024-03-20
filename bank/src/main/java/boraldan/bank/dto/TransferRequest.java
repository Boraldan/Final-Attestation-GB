package boraldan.bank.dto;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class TransferRequest {

  // Идентификатор счета отправителя перевода
  private long senderAccountId;

  // Идентификатор счета получателя перевода
  private long receiverAccountId;

  // Сумма перевода
  private BigDecimal amount;

}
