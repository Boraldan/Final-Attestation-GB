package boraldan.shop;

import boraldan.shop.service.SessionEventService;
import org.modelmapper.ModelMapper;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.Bean;
import org.springframework.security.core.session.SessionRegistry;
import org.springframework.security.core.session.SessionRegistryImpl;
/**
 * Приложение помечено аннотацией @EnableFeignClients, чтобы включить поддержку клиента Feign
 * для выполнения HTTP-запросов.
 */
@SpringBootApplication
@EnableFeignClients
public class ShopApplication {

	public static void main(String[] args) {
		SpringApplication.run(ShopApplication.class, args);
	}


}
