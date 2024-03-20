package boraldan.shop.domen.Cart;

import boraldan.shop.domen.car.Car;
import boraldan.shop.domen.order.Coupon;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import org.springframework.web.context.annotation.SessionScope;



import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
public class Cart {

    private List<Car> carList = new ArrayList<>();

    private Coupon coupon;  // Купон, применяемый к корзине

    /**
     * Метод для подсчета общего количества предметов в корзине.
     *
     * @return общее количество предметов в корзине
     */
    public int itemsCart() {
        return carList.stream().mapToInt(Car::getVolume).sum();
    }

    /**
     * Метод для подсчета общей суммы товаров в корзине без учета скидок.
     *
     * @return общая сумма товаров в корзине без учета скидок
     */
    public double subTotalSumCart() {
        double sum = 0;
        for (Car car : carList) sum += car.getPrice() * car.getVolume();
        return sum;
    }
    /**
     * Метод для подсчета общей суммы товаров в корзине с учетом скидок, примененных через купон.
     *
     * @return общая сумма товаров в корзине с учетом скидок
     */
    public double totalSumCart() {
        double sum = 0;
        for (Car car : carList) sum += car.getPrice() * car.getVolume();
        if (coupon != null) sum -= sum * (coupon.getDiscount().doubleValue() / 100);
        return sum;
    }


    @Override
    public String
    toString() {
        return "Cart{" +
                "carList=" + carList +
                '}';
    }

}
