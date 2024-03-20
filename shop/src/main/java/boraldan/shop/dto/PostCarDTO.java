package boraldan.shop.dto;

import boraldan.shop.domen.car.Colour;
import boraldan.shop.domen.car.Fuel;
import boraldan.shop.domen.car.Gearbox;
import boraldan.shop.domen.car.TypeCar;
import lombok.Data;


@Data
public class PostCarDTO {

    private int id;
    private String maker;
    private String model;
    private int year;
    private TypeCar typeCar;
    private Gearbox gearbox;
    private double engine;
    private Fuel fuel;
    private Colour colour;
    private String img;
    private double price;
    private int volume;
}
