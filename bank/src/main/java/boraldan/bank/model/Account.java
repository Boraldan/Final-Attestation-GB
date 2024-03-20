package boraldan.bank.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.Data;

import java.math.BigDecimal;

@Data
@Entity
public class Account {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    // Имя держателя счета
    private String name;

    // Номер кредитной карты, связанный с аккаунтом
    private Long card;

    // Сумма денежных средств на счете
    private BigDecimal amount;

}
