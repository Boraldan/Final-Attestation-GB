package boraldan.bank.util;


import boraldan.bank.dto.PayDTO;
import org.springframework.stereotype.Component;

@Component
public class PayDTOValid {
    /**
     * Проверяет корректность данных платежной информации.
     * @param payDTO объект с данными платежной информации
     * @return true, если данные некорректны (одно или несколько полей равны null), иначе false
     */
    public boolean falsePayDTO(PayDTO payDTO) {
        return payDTO.getBayerCard() == null || payDTO.getSellerCard() == null || payDTO.getAmount() == null;
    }
}
