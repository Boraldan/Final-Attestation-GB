package boraldan.shop.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.integration.annotation.ServiceActivator;
import org.springframework.integration.annotation.Transformer;
import org.springframework.integration.channel.DirectChannel;
import org.springframework.integration.core.GenericTransformer;
import org.springframework.integration.file.FileWritingMessageHandler;
import org.springframework.integration.file.support.FileExistsMode;
import org.springframework.messaging.MessageChannel;

import java.io.File;
import java.time.LocalDateTime;

/**
 * класс IntegrationConfig определяет конфигурацию для интеграционных компонентов Spring Integration
 */
@Configuration
public class IntegrationConfig {
    // Создание канала для передачи текстовых сообщений
    @Bean
    public MessageChannel textInputChanel() {
        return new DirectChannel();
    }
    // Создание канала для записи файлов
    @Bean
    public MessageChannel fileWriterChanel() {
        return new DirectChannel();
    }
    // Создание преобразователя для обработки текстовых сообщений
    @Bean
    @Transformer(inputChannel = "textInputChanel", outputChannel = "fileWriterChanel")
    public GenericTransformer<String, String> mainTransformer() {
        return text -> {
            LocalDateTime creat = LocalDateTime.now();
            String addCar = "Модель %s была добавлена в корзину %d.%d.%d". formatted(text,
                    creat.getDayOfMonth(), creat.getMonthValue(), creat.getYear());
            return addCar;
        };
    }
    // Создание обработчика сообщений для записи в файл
    @Bean
    @ServiceActivator(inputChannel = "fileWriterChanel")
    public FileWritingMessageHandler messageHandler() {
        FileWritingMessageHandler handler =
                new FileWritingMessageHandler(new File(
                        "shop/src/main/resources/files"));
        handler.setExpectReply(false);
        handler.setFileExistsMode(FileExistsMode.APPEND);
        handler.setAppendNewLine(true);

        return handler;
    }
}
