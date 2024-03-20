package boraldan.bank.controller;

import java.util.List;

import boraldan.bank.dto.PayDTO;
import boraldan.bank.util.PayDTOValid;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import boraldan.bank.dto.TransferRequest;
import boraldan.bank.model.Account;
import boraldan.bank.service.TransferService;

@AllArgsConstructor
@RestController
public class AccountController {
    // Сервис для выполнения операций по переводу денег
    private final TransferService transferService;
    // Класс для передачи и валидации данных о платеже
    private final PayDTOValid payDTOValid;
    /**
     * Возвращает сообщение "Give me your many!" и выводит в консоль информацию о том, что банк обращается к приложению.
     * @return Строка с сообщением "Give me your many!"
     */
    @GetMapping
    public String getMany(){
        System.out.println("Банк пишет нам.");
        return "Give me your many!";
    }
    /**
     * Выполняет операцию покупки между покупателем и продавцом с использованием сервиса перевода.
     * @param payDTO Объект, содержащий информацию о платеже
     * @return ResponseEntity с результатом операции покупки или статусом ошибки в случае некорректных данных платежа
     */
    @PostMapping("/transfershop")
    public ResponseEntity<?> transferShop(@RequestBody PayDTO payDTO) {
        if (payDTOValid.falsePayDTO(payDTO)) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Bad Request: ");
        }
        return transferService.transferShop(payDTO.getBayerCard(), payDTO.getSellerCard(), payDTO.getAmount());
    }
    /**
     * Обработчик HTTP POST запроса на перевод денег.
     * Выполняет операцию перевода денег между двумя счетами с использованием сервиса перевода.
     * @param request Объект, содержащий информацию о платеже
     */
    @PostMapping("/transfer")
    public void transferMoney(@RequestBody TransferRequest request) {
        transferService.transferMoney(request.getSenderAccountId(), request.getReceiverAccountId(), request.getAmount());
    }
    /**
     * Возвращает список всех счетов с помощью сервиса перевода.
     * @return Список всех счетов
     */
    @GetMapping("/accounts")
    public List<Account> getAllAccounts() {
        return transferService.getAllAccounts();
    }
}
