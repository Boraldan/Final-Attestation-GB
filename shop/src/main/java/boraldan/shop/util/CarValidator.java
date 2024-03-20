package boraldan.shop.util;

import boraldan.shop.domen.car.Car;
import boraldan.shop.service.CarService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

/**
 * Валидатор для проверки объектов типа Car.
 */
@Component
@RequiredArgsConstructor
public class CarValidator implements Validator {

    private final CarService carService;

    /**
     * Определяет, поддерживает ли этот валидатор заданный класс.
     *
     * @param clazz класс, который требуется проверить
     * @return true, если валидатор поддерживает указанный класс; в противном случае false
     */
    @Override
    public boolean supports(Class<?> clazz) {
        return Car.class.equals(clazz);
    }


    @Override
    public void validate(Object target, Errors errors) {
    }

    /**
     * Проверяет, доступное количество для данного автомобиля
     *
     * @param target объект типа Car
     * @param lot    количество товара в корзине
     * @return true, если указанный лот превышает доступное количество для данного автомобиля; в противном случае false
     */
    public boolean falsePlusLotInCart(Object target, int lot) {
        Car car = (Car) target;
        Car validCar = carService.findById(car.getId()).get();
        return validCar.getVolume() < lot;
    }

}
