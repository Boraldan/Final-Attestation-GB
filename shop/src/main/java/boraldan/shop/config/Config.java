package boraldan.shop.config;

import boraldan.shop.service.SessionEventService;
import org.modelmapper.ModelMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpHeaders;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.web.client.RestTemplate;

/**
 * класс Config содержит конфигурацию Spring.
 */
@Configuration
public class Config {
    // Создание бина RestTemplate для выполнения HTTP-запросов
    @Bean
    public RestTemplate template(){
        return new RestTemplate();
    };

    // Создание бина HttpHeaders для работы с HTTP-заголовками
    @Bean
    public HttpHeaders headers()
    {
        return new HttpHeaders();
    }

    // Создание бина ModelMapper для преобразования объектов между различными типами
    @Bean
    public ModelMapper modelMapper() {
        return new ModelMapper();
    }

    // Создание бина SessionEventService для обработки событий сессии
    @Bean
    public SessionEventService sessionEventService() {
        return new SessionEventService();
    }

}
