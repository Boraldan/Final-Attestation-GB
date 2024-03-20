package boraldan.shop.util;

import boraldan.shop.domen.order.Coupon;
import boraldan.shop.service.CouponService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;
/**
 * Валидатор для проверки объектов типа Coupon.
 */
@Component
@RequiredArgsConstructor
public class CouponValid implements Validator {

    private final CouponService couponService;

    /**
     * Определяет, поддерживает ли этот валидатор заданный класс.
     *
     * @param clazz класс, который требуется проверить
     * @return true, если валидатор поддерживает указанный класс; в противном случае false
     */
    @Override
    public boolean supports(Class<?> clazz) {
       return Coupon.class.equals(clazz);
    }

    @Override
    public void validate(Object target, Errors errors) {
    }
    /**
     * Проверяет, является ли указанный купон действительным.
     *
     * @param target объект типа Coupon
     * @param errors объект, содержащий информацию об ошибках валидации
     * @return объект типа Coupon, если купон действителен; в противном случае null
     */
    public Coupon validateCoupon(Object target, Errors errors) {
        Coupon coupon = (Coupon) target;
        Coupon validCoupon =  couponService.findValidCoupon(coupon.getName()).orElse(null);
        if (validCoupon == null ) {
            errors.rejectValue("name", "", "Этот купон недействителен ");
            return coupon;
        }
        return validCoupon;
    }

}
