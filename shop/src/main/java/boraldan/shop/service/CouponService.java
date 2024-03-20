package boraldan.shop.service;

import boraldan.shop.domen.order.Coupon;
import boraldan.shop.repository.CouponRepo;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


import java.util.Optional;

/**
 * Сервисный класс для работы с купонами.
 */
@Service
@RequiredArgsConstructor
public class CouponService {

    private final CouponRepo couponRepo;

    /**
     * Поиск действующего купона по его имени.
     *
     * @param name имя купона
     * @return действующий купон с указанным именем, если найден, в противном случае пустое значение Optional
     */
    public Optional<Coupon> findValidCoupon(String name){
        return couponRepo.findFirstByNameAndValid(name, true);
    }
}