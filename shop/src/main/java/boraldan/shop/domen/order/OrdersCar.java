package boraldan.shop.domen.order;

import boraldan.shop.domen.car.Car;
import jakarta.persistence.*;
import jakarta.validation.constraints.Min;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

/**
 * Таблица @ManyToMany между Orders  и Car
 */
@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "Orders_Car")
// аннотацию @IdClass, чтобы обозначить составной ключ из orders_id и car_id для JPA Repo
@IdClass(IdOrdersCar.class)
public class OrdersCar {

    // Заказ, к которому относится данная запись
    @Id
    @ManyToOne
    private Orders orders;

    // Автомобиль, который присутствует в данном заказе
    @Id
    @ManyToOne
    private Car car;

    // Количество автомобилей данной модели в заказе
    @Column(name = "lot")
    @Min(value = 1, message = "Lot should be greater than 0")
    private Integer lot;

    @Override
    public String toString() {
        return "OrdersCar{" +
                "orders=" + orders +
                ", car=" + car +
                ", lot=" + lot +
                '}';
    }
}
