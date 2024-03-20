package boraldan.shop.service.interfaces;

import boraldan.shop.domen.Cart.Cart;
import boraldan.shop.domen.order.Orders;
import boraldan.shop.domen.order.OrdersCar;

import java.util.List;
import java.util.Optional;
/**
 * Интерфейс, определяющий методы для работы с заказами.
 */
public interface OrderService {

    /**
     * Находит заказ по его идентификатору.
     * @param id идентификатор заказа
     * @return заказ, если найден, иначе - пустое значение Optional
     */
    Optional<Orders> findById(int id);

    /**
     * Возвращает список всех заказов.
     * @return список всех заказов
     */
    List<OrdersCar> findAll();

    /**
     * Находит все товары в заказе по идентификатору заказа.
     * @param id идентификатор заказа
     * @return список всех товаров в заказе
     */
    List<OrdersCar> findByOrderId(int id);

    /**
     * Сохраняет заказ в базе данных на основе информации из корзины.
     * @param cart корзина с товарами, для которых создается заказ
     * @return сохраненный заказ
     */
    Orders saveOrdersCar(Cart cart);

    /**
     * Устанавливает номера лотов для всех товаров в списке заказов.
     * @param list список заказов
     * @return список заказов с установленными номерами лотов для товаров
     */
    List<Orders> setLotCarList(List<Orders> list);

    /**
     * Устанавливает номера лотов для всех товаров в заказе.
     * @param orders заказ
     * @return заказ с установленными номерами лотов для товаров
     */
    Orders setLotCar(Orders orders);

    /**
     * Отменяет заказ по его идентификатору.
     * @param id идентификатор заказа
     */
    void cancelOrders(int id);
}