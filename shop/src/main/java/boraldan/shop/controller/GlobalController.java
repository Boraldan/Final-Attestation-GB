package boraldan.shop.controller;

import boraldan.shop.domen.Cart.Cart;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * Глобальный контроллер, предоставляющий общие модели для всех контроллеров.
 */
@ControllerAdvice
@RequiredArgsConstructor
public class GlobalController {

    private final HttpSession session;
    /**
     * Добавляет модель корзины в каждый запрос.
     *
     * @return Корзина, сохранённая в сессии
     */
    @ModelAttribute("cart")
    public Cart addCart() {
        return (Cart) session.getAttribute("cart");
    }
}
