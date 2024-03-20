package boraldan.shop.service;

import org.springframework.integration.annotation.MessagingGateway;
import org.springframework.integration.file.FileHeaders;
import org.springframework.messaging.handler.annotation.Header;
/**
 * Интерфейс шлюза для записи данных в файл.
 * Отправляет данные в канал "textInputChanel" для дальнейшей обработки.
 */
@MessagingGateway(defaultRequestChannel = "textInputChanel")
public interface FileGateway {
    /**
     * Записывает данные в файл.
     *
     * @param filename имя файла
     * @param data     данные для записи
     */
    void writeToFile(@Header(FileHeaders.FILENAME) String filename, String data);
}
