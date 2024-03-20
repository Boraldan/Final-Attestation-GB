package boraldan.shop.domen.car;

import boraldan.shop.domen.order.Orders;
import jakarta.persistence.*;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;
import org.springframework.cache.annotation.EnableCaching;


import java.util.*;


@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "Car")
public class Car implements Cloneable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private int id;

    @NotEmpty
    @Size(min = 2, message = "Должно быть более 2 символов")
    @Column(name = "maker")
    private String maker;

    @NotEmpty
    @Size(min = 2, message = "Должно быть более 2 символов")
    @Column(name = "model")
    private String model;

    @Min(1880)
    @Column(name = "prod_year")
    private int year;

    @Column(name = "typecar", columnDefinition = "typecar")
    @Enumerated(EnumType.STRING)
    private TypeCar typeCar;

    @Column(name = "gearbox", columnDefinition = "gearbox")
    @Enumerated(EnumType.STRING)
    private Gearbox gearbox;

    @Column(name = "engine")
    private double engine;

    @Column(name = "fuel", columnDefinition = "fuel")
    @Enumerated(EnumType.STRING)
    private Fuel fuel;

    @Column(name = "colour", columnDefinition = "colour")
    @Enumerated(EnumType.STRING)
    private Colour colour;

    @Column(name = "img")
    private String img;

    @Column(name = "price")
    private double price;

    @Column(name = "volume")
    private int volume;

    @Column(name = "purchased")
    private boolean purchased;

    @Transient
    private boolean inStock;

    // Список заказов, в которых участвует данный автомобиль
    @ManyToMany
    @JoinTable(name = "orders_car",
            joinColumns = @JoinColumn(name = "car_id"),
            inverseJoinColumns = @JoinColumn(name = "orders_id"))
    public List<Orders> orders = new ArrayList<>();

    /**
     * Метод, определяющий наличие автомобиля в наличии.
     *
     * @return true, если автомобиль в наличии, в противном случае - false
     */
    public boolean isInStock() {
        if (volume > 0) inStock = true;
        return inStock;
    }

    /**
     * Метод для получения заголовка автомобиля, содержащего марку, модель и год выпуска.
     *
     * @return Заголовок автомобиля
     */
    public String getTitle() {
        return maker + " " + model + " " + year;
    }

    @Override
    public String toString() {
        return "Car{" +
                "id=" + id +
                ", volume=" + volume  +
                '}';
    }

    /**
     * Переопределенный метод clone() для создания копии объекта Car.
     *
     * @return Копия объекта Car
     * @throws CloneNotSupportedException если клонирование не поддерживается
     */
    @Override
    public Car clone() throws CloneNotSupportedException {
        return (Car) super.clone();
    }

}
