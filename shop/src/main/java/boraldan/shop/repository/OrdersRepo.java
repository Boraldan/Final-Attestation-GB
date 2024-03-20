package boraldan.shop.repository;

import boraldan.shop.domen.order.Orders;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


import java.util.Optional;

/**
 * Репозиторий для доступа к данным о заказах.
 */
@Repository
public interface OrdersRepo extends JpaRepository<Orders, Integer> {

    /**
     * Находит заказ по его идентификатору.
     *
     * @param id идентификатор заказа
     * @return опциональный объект заказа
     */
    Optional<Orders> findById(int id);

}