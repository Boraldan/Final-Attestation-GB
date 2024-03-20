package boraldan.shop.domen.order;

import lombok.Data;

import java.io.Serializable;

/**
 * внешние ключи для таблицы OrdersCar
 */
@Data
public class IdOrdersCar  implements Serializable {

    private int orders;
    private int car;

}