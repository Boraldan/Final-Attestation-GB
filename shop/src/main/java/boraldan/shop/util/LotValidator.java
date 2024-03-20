package boraldan.shop.util;

import boraldan.shop.domen.Cart.Lot;
import boraldan.shop.domen.car.Car;
import boraldan.shop.service.CarService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

/**
 * Валидатор для объектов типа Lot.
 */
@Component
@RequiredArgsConstructor
public class LotValidator implements Validator {

    private final CarService carService;

    /**
     * Поддерживает ли валидатор данный класс объекта для проверки.
     *
     * @param clazz класс объекта
     * @return true, если класс объекта совпадает с классом Lot, иначе false
     */
    @Override
    public boolean supports(Class<?> clazz) {
        return Lot.class.equals(clazz);
    }

    @Override
    public void validate(Object target, Errors errors) {
    }

    /**
     * Проверяет, может ли быть добавлено указанное количество товара в корзину.
     * Если валидация не пройдена, в объект errors добавляется соответствующее сообщение об ошибке.
     *
     * @param target объект типа Lot, который проверяется
     * @param errors объект для записи ошибок в процессе валидации
     * @return объект типа Car, который прошел валидацию
     */
    public Car validateLot(Object target, Errors errors) {
        Lot lot = (Lot) target;
        Car validCar = carService.findById(lot.getId()).get();
        if (validCar.getVolume() < lot.getLot() || lot.getLot() < 1) {
            errors.rejectValue("lot", "", "Нет такого количества");
            return validCar;
        }
        return validCar;
    }
}
