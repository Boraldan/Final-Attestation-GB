package boraldan.shop.repository;

import boraldan.shop.domen.order.Coupon;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


import java.util.Optional;

/**
 * Репозиторий для доступа к данным о купонах.
 */
@Repository
public interface CouponRepo extends JpaRepository<Coupon, Integer> {

    /**
     * Находит первый купон по имени и состоянию валидности.
     *
     * @param name   имя купона
     * @param valid  флаг валидности купона
     * @return опциональный объект купона
     */
    Optional<Coupon> findFirstByNameAndValid(String name, boolean valid);
}