package boraldan.shop.controller.exception;

import feign.FeignException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
/**
 * Обработчик исключений Feign для обработки исключений, возникающих при использовании Feign клиента.
 */
@ControllerAdvice
public class FeignExceptionHandler {
    /**
     * Метод для обработки исключений FeignException.
     * Возвращает ResponseEntity с подходящим статусом и сообщением об ошибке.
     * @param e Исключение типа FeignException
     * @return ResponseEntity с сообщением об ошибке и соответствующим статусом HTTP
     */
    @ExceptionHandler(FeignException.class)
    public ResponseEntity<Object> handleFeignException(FeignException e) {
        // возвращение ResponseEntity с подходящим статусом и сообщением об ошибке
        return new ResponseEntity<>("Feign exception occurred: " + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
