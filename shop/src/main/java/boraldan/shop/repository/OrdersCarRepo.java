package boraldan.shop.repository;

import boraldan.shop.domen.car.Car;
import boraldan.shop.domen.order.IdOrdersCar;
import boraldan.shop.domen.order.Orders;
import boraldan.shop.domen.order.OrdersCar;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


import java.util.List;
import java.util.Optional;

/**
 * Репозиторий для доступа к данным о заказах автомобилей.
 */
@Repository
public interface OrdersCarRepo extends JpaRepository<OrdersCar, IdOrdersCar> {

    /**
     * Находит список заказов автомобилей по идентификатору заказа.
     *
     * @param id идентификатор заказа
     * @return список заказов автомобилей
     */
    List<OrdersCar> findOrdersCarByOrdersId(int id);

    /**
     * Находит заказ автомобиля по заказу и автомобилю.
     *
     * @param orders заказ
     * @param car    автомобиль
     * @return опциональный объект заказа автомобиля
     */
    Optional<OrdersCar> findOrdersCarByOrdersAndCar(Orders orders, Car car);

    /**
     * Находит заказ автомобиля по идентификаторам заказа и автомобиля.
     *
     * @param idOrder идентификатор заказа
     * @param idCar   идентификатор автомобиля
     * @return опциональный объект заказа автомобиля
     */
    Optional<OrdersCar> findOrdersCarByOrdersIdAndCarId(int idOrder, int idCar);

}