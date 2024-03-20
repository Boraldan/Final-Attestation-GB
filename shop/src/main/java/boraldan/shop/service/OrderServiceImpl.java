package boraldan.shop.service;

import boraldan.shop.domen.Cart.Cart;
import boraldan.shop.domen.car.Car;
import boraldan.shop.domen.order.Orders;
import boraldan.shop.domen.order.OrdersCar;
import boraldan.shop.domen.order.Status;
import boraldan.shop.domen.person.Person;
import boraldan.shop.repository.OrdersCarRepo;
import boraldan.shop.repository.OrdersRepo;
import boraldan.shop.security.PersonDetails;
import boraldan.shop.service.interfaces.OrderService;
import lombok.RequiredArgsConstructor;
import org.hibernate.Hibernate;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
/**
 * Сервис для работы с заказами.
 */
@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class OrderServiceImpl implements OrderService {

    private final OrdersRepo ordersRepo;
    private final OrdersCarRepo ordersCarRepo;
    private final CarService carService;

    /**
     * Находит заказ по его идентификатору.
     *
     * @param id идентификатор заказа
     * @return заказ, если найден, иначе пустое значение
     */
    public Optional<Orders> findById(int id) {
        return ordersRepo.findById(id);
    }

    /**
     * Возвращает список всех заказов.
     *
     * @return список заказов
     */
    public List<OrdersCar> findAll() {
        return ordersCarRepo.findAll();
    }

    /**
     * Находит все позиции заказа по идентификатору заказа.
     *
     * @param id идентификатор заказа
     * @return список позиций заказа
     */
    public List<OrdersCar> findByOrderId(int id) {
        return ordersCarRepo.findOrdersCarByOrdersId(id);
    }

    /**
     * Сохраняет заказ вместе с позициями из корзины.
     *
     * @param cart корзина с товарами
     * @return сохраненный заказ
     */
    @Transactional
    public Orders saveOrdersCar(Cart cart) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String role = authentication.getAuthorities().stream().toList().get(0).toString();
        // --------------------создаем Orders
        Orders orders = new Orders();
        if (!role.equals("ROLE_ANONYMOUS")) {
            Person person = ((PersonDetails) authentication.getPrincipal()).getPerson();
            orders.setCreatAt(LocalDateTime.now());
            orders.setStatus(Status.CREATED);
            orders.setPerson(person);
            if (cart.getCoupon() != null) orders.setCoupon(cart.getCoupon());
            orders.setCars(cart.getCarList());
            orders = ordersRepo.save(orders);

            for (Car car : cart.getCarList()) {
                // --- помечаем Car как купленные, чтобы не удалять из базы:  boolean purchased
                Car updateCar = carService.findById(car.getId()).get();
                updateCar.setPurchased(true);
                carService.save(updateCar);

                //  ---  записываем lot в таблицу OrdersCar
                OrdersCar ordersCar = ordersCarRepo.findOrdersCarByOrdersAndCar(orders, car).get();
                ordersCar.setLot(car.getVolume());
                ordersCarRepo.save(ordersCar);
            }
        }
        return orders;
    }

    /**
     * Устанавливает количество товара в каждом заказе.
     *
     * @param list список заказов
     * @return список заказов с установленными количествами товара
     */
    public List<Orders> setLotCarList(List<Orders> list) {
        for (Orders orders : list) {
           setLotCar(orders);
        }
        return list;
    }
    /**
     * Устанавливает количество товара в заказе.
     *
     * @param orders заказ
     * @return заказ с установленным количеством товара
     */
    public Orders setLotCar(Orders orders) {
        int i = 0;
        for (Car car : orders.getCars()) {
            car.setVolume(ordersCarRepo.findOrdersCarByOrdersIdAndCarId(orders.getId(), car.getId()).get().getLot());
            try {
                Car cloneCar = car.clone();
                orders.getCars().set(i++, cloneCar);
                System.out.println(cloneCar);
            } catch (CloneNotSupportedException e) {
                throw new RuntimeException(e);
            }
        }
        return orders;
    }

    /**
     * Отменяет заказ и возвращает товары на склад.
     *
     * @param id идентификатор заказа
     */
    @Transactional
    public void cancelOrders(int id) {
        Orders orders = ordersRepo.findById(id).orElse(null);
        if (orders != null) {
            Hibernate.initialize(orders.getCars());
            orders.setStatus(Status.CANCELLED);
            ordersRepo.save(orders);
            for (Car car : orders.getCars()) {
                car.setVolume(car.getVolume() + ordersCarRepo.findOrdersCarByOrdersIdAndCarId(orders.getId(), car.getId()).get().getLot());
                carService.save(car);
            }
        }
    }

}