package boraldan.shop.service;

import boraldan.shop.domen.Cart.Cart;
import boraldan.shop.domen.car.Car;
import jakarta.servlet.http.HttpSessionEvent;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.web.session.HttpSessionEventPublisher;

/**
 * Сервис для обработки событий сессии.
 */
public class SessionEventService extends HttpSessionEventPublisher {

    @Autowired
    private CarService carService;

    /**
     * Вызывается при создании сессии.
     * Инициализирует корзину покупок и устанавливает максимальное время неактивности сессии.
     *
     * @param event событие создания сессии
     */
    @Override
    public void sessionCreated(HttpSessionEvent event) {
        event.getSession().setAttribute("cart", new Cart());
        System.out.println("Открываем сессию MyHttpS --> " + event.getSession().getId());
        event.getSession().setMaxInactiveInterval(500);   // время указывается в секундах

        super.sessionCreated(event);
    }

    /**
     * Вызывается при уничтожении сессии.
     * Возвращает товары из корзины покупок на склад.
     *
     * @param event событие уничтожения сессии
     */
    @Override
    public void sessionDestroyed(HttpSessionEvent event) {
        System.out.println("Закрываем сессию MyHttpS --> " + event.getSession().getId());
        Cart cart = (Cart) event.getSession().getAttribute("cart");
        if (cart != null && !cart.getCarList().isEmpty()) {
            for (Car car : cart.getCarList()) {
                carService.addVolumeDB(car);
            }
        }
        super.sessionDestroyed(event);
    }

}