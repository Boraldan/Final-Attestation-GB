package boraldan.shop.domen.order;

import boraldan.shop.domen.car.Car;
import boraldan.shop.domen.person.Person;
import jakarta.persistence.*;
import jdk.jfr.Timestamp;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.cache.annotation.EnableCaching;


import java.time.LocalDateTime;
import java.util.*;

@Cacheable(false)
@Getter
@Setter
@NoArgsConstructor
@EqualsAndHashCode
@Entity
@Table(name = "Orders")
public class Orders {

    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @ManyToOne
    @JoinColumn(name = "person_id", referencedColumnName = "id")
    private Person person;

    @ManyToOne
    @JoinColumn(name = "coupon_id", referencedColumnName = "id")
    private Coupon coupon;

    @Column(name = "creat_at")
    @Timestamp
    private LocalDateTime creatAt;

    @Column(name = "status")
    @Enumerated(EnumType.STRING)
    private Status status;

    // Список автомобилей в заказе
    @ManyToMany
    @JoinTable(name = "orders_car",
            joinColumns = @JoinColumn(name = "orders_id"),
            inverseJoinColumns = @JoinColumn(name = "car_id"))
    public List<Car> cars = new ArrayList<>();

    /**
     * Метод для подсчета общей суммы товаров в заказе без учета скидок.
     *
     * @return общая сумма товаров в заказе без учета скидок
     */
    public double subTotalSumOrders() {
        double sum = 0;
        for (Car car : cars) sum += car.getPrice() * car.getVolume();
        return sum;
    }

    /**
     * Метод для подсчета общей суммы товаров в заказе с учетом скидок, примененных через купон.
     *
     * @return общая сумма товаров в заказе с учетом скидок
     */
    public double totalSumOrders() {
        double sum = 0;
        for (Car car : cars) sum += car.getPrice() * car.getVolume();
        if (coupon != null) sum -= sum * (coupon.getDiscount().doubleValue() / 100);
        return sum;
    }
    /**
     * Метод для получения информации о заказе в виде строки.
     *
     * @return информация о заказе
     */
    public String infoOrder() {
        return "Заказ №%d от %d.%d.%d".formatted(id, creatAt.getDayOfMonth(), creatAt.getMonthValue(), creatAt.getYear());
    }

    @Override
    public String toString() {
        return "Orders{" +
                "id=" + id +
                ", coupon=" + coupon +
                ", status=" + status +
                '}';
    }
}
