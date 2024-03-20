package boraldan.shop.domen.Cart;

import lombok.Data;
import org.springframework.stereotype.Component;

/**
 * класс Lot для добавления количества в корзину
 */
@Data
public class Lot {
    private int id;
    private int lot;
}
