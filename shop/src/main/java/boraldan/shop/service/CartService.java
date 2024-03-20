package boraldan.shop.service;

import boraldan.shop.domen.Cart.Cart;
import boraldan.shop.domen.car.Car;
import boraldan.shop.util.CarValidator;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;


import java.util.ArrayList;
import java.util.List;

/**
 * Сервис для работы с Корзиной
 */
@Service
@RequiredArgsConstructor
public class CartService {

    private final HttpSession session;  //  объект HttpSession для работы с сеансом пользователя
    private final CarService carService;  //  сервис для работы с объектами типа Car
    private final CarValidator carValidator;  //  валидатор для проверки корректности данных автомобилей


    /**
     * Клонирует содержимое корзины из сеанса пользователя.
     * @return список склонированных объектов Car
     */
    public List<Car> cloneCart() {
        Cart cart = (Cart) session.getAttribute("cart");
        List<Car> oldCarList = new ArrayList<>();
        for (Car car : cart.getCarList()) {
            try {
                oldCarList.add((Car) car.clone());
            } catch (CloneNotSupportedException e) {
                e.printStackTrace();
            }
        }
        return oldCarList;
    }

    /**
     * Добавляет элемент в корзину.
     * @param dbCar объект Car, который будет добавлен в корзину
     * @param lot количество товара
     * @throws CloneNotSupportedException если клонирование не поддерживается
     */
    public void addItem(Car dbCar, int lot) throws CloneNotSupportedException {
        Cart cart = (Cart) session.getAttribute("cart");
        Car car = dbCar.clone();
        car.setVolume(lot);
        if (cart.getCarList().stream().noneMatch(x -> x.getId() == dbCar.getId())) {
            cart.getCarList().add(car);
            carService.minusVolumeDB(dbCar, car.getVolume());
        } else {
            for (Car el : cart.getCarList()) {
                if (el.getId() == car.getId()) {
                    el.setVolume(el.getVolume() + car.getVolume());
                    carService.minusVolumeDB(dbCar, car.getVolume());
                }
            }
        }
        session.setAttribute("cart", cart);
    }

    /**
     * Удаляет элемент из корзины.
     * @param idCar идентификатор товара, который будет удален
     */
    public void dellItem(int idCar) {
        Cart cart = (Cart) session.getAttribute("cart");
        Car car = cart.getCarList().stream().filter(el -> el.getId() == idCar).findFirst().get();
        carService.addVolumeDB(car);
        cart.getCarList().remove(car);
        session.setAttribute("cart", cart);
    }

    /**
     * Проверяет корректность значений количества товаров в корзине.
     * @param cart текущая корзина
     * @param oldCarList список старых значений товаров в корзине
     * @return true, если найдены ошибки, иначе false
     */
    public boolean checkFalseLot(Cart cart, List<Car> oldCarList) {
        for (int i = 0; i < cart.getCarList().size(); i++) {
            if (cart.getCarList().get(i).getVolume() < 1) {
                cart.setCarList(oldCarList);
                return true;
            }
            if (cart.getCarList().get(i).getVolume() > oldCarList.get(i).getVolume()) {
                int lot = cart.getCarList().get(i).getVolume() - oldCarList.get(i).getVolume();
                if (carValidator.falsePlusLotInCart(cart.getCarList().get(i), lot)) {
                    cart.setCarList(oldCarList);
                    return true;
                }
            } else if (cart.getCarList().get(i).getVolume() < oldCarList.get(i).getVolume()) {
                int lot = cart.getCarList().get(i).getVolume() - oldCarList.get(i).getVolume();
                if (oldCarList.get(i).getVolume() < Math.abs(lot)) {
                    cart.setCarList(oldCarList);
                    return true;
                }
            }
        }
        return false;
    }

    /**
     * Обновляет количество товаров в базе данных на основе изменений в корзине.
     * @param cart текущая корзина
     * @param oldCarList список старых значений товаров в корзине
     */
    public void updateLotCart(Cart cart, List<Car> oldCarList) {
        for (int i = 0; i < cart.getCarList().size(); i++) {
            if (cart.getCarList().get(i).getVolume() != oldCarList.get(i).getVolume()) {
                int lot = cart.getCarList().get(i).getVolume() - oldCarList.get(i).getVolume();
                Car car = carService.findById(cart.getCarList().get(i).getId()).orElse(null);
                if (car != null) {
                    car.setVolume(car.getVolume() - lot);
                    carService.save(car);
                }
            }
        }
    }

}
