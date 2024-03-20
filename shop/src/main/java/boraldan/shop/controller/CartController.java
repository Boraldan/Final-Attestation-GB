package boraldan.shop.controller;

import boraldan.shop.domen.Cart.Cart;
import boraldan.shop.domen.car.Car;
import boraldan.shop.domen.order.Coupon;
import boraldan.shop.domen.order.Orders;
import boraldan.shop.domen.order.Status;
import boraldan.shop.dto.PayDTO;
import boraldan.shop.controller.feign.BankFeign;
import boraldan.shop.security.PersonDetails;
import boraldan.shop.service.*;
import boraldan.shop.util.CouponValid;
import feign.FeignException;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatusCode;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 *  класс CartController является контроллером, который обрабатывает запросы, связанные с корзиной покупок
 */
@Controller
@RequiredArgsConstructor
public class CartController {

    private final HttpSession session; // Сессия HTTP для работы с сеансами
    private final CartService cartService; // Сервис для работы с корзиной покупок
    private final OrderServiceImpl orderServiceImpl; // Сервис для обработки заказов
    private final EmailService emailService; // Сервис для отправки электронной почты
    private final CouponValid couponValid; // Валидатор для проверки купонов
    private final SellerService sellerService; // Сервис для работы с продавцами
    private final BankFeign bankFeign; // Клиент Feign для взаимодействия с банком

    /**
     * Отображает страницу корзины покупок с возможностью удаления товаров из неё.
     * Динамические страницы формируются с использованием Thymeleaf.
     * @param idCar идентификатор товара для удаления из корзины (опционально)
     * @param model модель для передачи данных в представление
     * @return страница корзины покупок
     */

    @GetMapping("/cart")
    public String shop(@RequestParam(value = "car", required = false) Integer idCar, Model model) {
        if (idCar != null) {
            cartService.dellItem(idCar);
            return "redirect:/cart";
        }
        session.setAttribute("oldCarList", cartService.cloneCart());
        model.addAttribute("coupon", new Coupon());
        return "cart";
    }
    /**
     * Проверяет правильность количества товаров в корзине.
     * При некорректных данных перенаправляет на страницу корзины с сообщением об ошибке.
     * @param cart модель корзины покупок
     * @return страница корзины покупок или страница с сообщением об ошибке
     */
    @PostMapping("/cart")
    public String checkLot(@ModelAttribute("cart") Cart cart) {
        List<Car> oldCarList = (List<Car>) session.getAttribute("oldCarList");
        if (cartService.checkFalseLot(cart, oldCarList)) {
            return "redirect:/cart";
        }
        cartService.updateLotCart(cart, oldCarList);
        return "redirect:/cart";
    }

    /**
     * Проверяет введенный купон на корректность.
     * Если купон неверный, возвращает на страницу корзины с сообщением об ошибке.
     * @param coupon объект купона для проверки
     * @param bindingResult результат валидации формы
     * @return страница корзины покупок или страница с сообщением об ошибке
     */
    @PostMapping("/cart/coupon")
    public String checkCoupon(@ModelAttribute("coupon") @Valid Coupon coupon, BindingResult bindingResult) {
        coupon = couponValid.validateCoupon(coupon, bindingResult);
        if (bindingResult.hasErrors()) {
            return "cart";
        }
        Cart cart = (Cart) session.getAttribute("cart");
        cart.setCoupon(coupon);
        return "redirect:/cart";
    }
    /**
     * Отображает страницу оформления заказа с данными о продавце, пользователе и объекте для оплаты.
     * @param model модель для передачи данных в представление
     * @param personDetails данные пользователя
     * @return страница оформления заказа
     */
    @GetMapping("/checkout")
    public String checkout(Model model, @AuthenticationPrincipal PersonDetails personDetails) {
        Cart cart = (Cart) session.getAttribute("cart");
        if (cart.getCarList().isEmpty()) {
            return "redirect:/cart";
        }
        model.addAttribute("seller", sellerService.findById(1).get());
        model.addAttribute("person", personDetails.getPerson());
        model.addAttribute("payDTO", new PayDTO());
        return "/checkout";
    }
    /**
     * Создает и сохраняет заказ на основе содержимого корзины.
     * Отправляет уведомление о заказе по электронной почте.
     * @param model модель для передачи данных в представление
     * @return страница с подтверждением заказа
     */
    @GetMapping("/thankyou")
    public String creatOrder(Model model) {
        Cart cart = (Cart) session.getAttribute("cart");
        if (cart.getCarList().isEmpty()) {
            return "redirect:/cart";
        }
        Orders orders = orderServiceImpl.saveOrdersCar(cart);
        model.addAttribute("Order", orders);
        emailService.sendMessageThread(orders);

        cart.getCarList().clear();
        cart.setCoupon(null);
        return "/thankyou";
    }

    /**
     * Создает и сохраняет заказ, а затем пытается выполнить оплату через сервис банка.
     * Отправляет уведомление о заказе по электронной почте.
     * @param model модель для передачи данных в представление
     * @param payDTO объект для оплаты
     * @return страница с подтверждением заказа
     */
    @PostMapping("/thankyou")
    public String creatAndPayOrder(Model model, @ModelAttribute("payDTO") PayDTO payDTO) {
        Cart cart = (Cart) session.getAttribute("cart");
        if (cart.getCarList().isEmpty()) {
            return "redirect:/cart";
        }
        Orders orders = orderServiceImpl.saveOrdersCar(cart);
        try {
            HttpStatusCode httpStatus = bankFeign.transferShop(payDTO).getStatusCode();
            if (httpStatus.is2xxSuccessful()) {
                orders.setStatus(Status.PAID);
            }
        } catch (FeignException e) {
            System.out.println(e.getMessage());
        }
        emailService.sendMessageThread(orders);
        model.addAttribute("Order", orders);

        cart.getCarList().clear();
        cart.setCoupon(null);
        return "/thankyou";
    }
    /**
     * Отменяет заказ по его идентификатору.
     * Перенаправляет на страницу аккаунта пользователя.
     * @param id идентификатор заказа для отмены
     * @return страница аккаунта пользователя
     */
    @GetMapping("/orders/cancel")
    public String canselOrders(@RequestParam(value = "id", required = false) Integer id) {
        if (id != null) {
            orderServiceImpl.cancelOrders(id);
            return "redirect:/auth/account";
        }
        return "redirect:/auth/account";
    }

}
