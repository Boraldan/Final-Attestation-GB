package boraldan.shop.controller.feign;


import boraldan.shop.dto.PayDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
/**
 * Интерфейс Feign клиента для взаимодействия с удалённым сервисом "bank".
 */
@FeignClient(name = "bank")
public interface BankFeign {
    /**
     * Метод для отправки GET запроса к сервису "bank" для получения тестовых данных.
     *
     * @return строка данных, полученных от сервиса "bank"
     */
    @GetMapping
    String getMany();
    /**
     * Метод для отправки POST запроса к сервису "bank" для осуществления транзакции в магазине.
     *
     * @param payDTO объект, содержащий информацию о транзакции
     * @return ResponseEntity, содержащий результат выполнения запроса
     */
    @PostMapping("/transfershop")
    ResponseEntity<?> transferShop(@RequestBody PayDTO payDTO);
}
