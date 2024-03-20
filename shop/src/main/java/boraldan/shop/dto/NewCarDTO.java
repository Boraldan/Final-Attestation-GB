package boraldan.shop.dto;


import boraldan.shop.domen.car.Colour;
import boraldan.shop.domen.car.Fuel;
import boraldan.shop.domen.car.Gearbox;
import boraldan.shop.domen.car.TypeCar;
import jakarta.validation.constraints.*;
import lombok.Data;


@Data
public class NewCarDTO {

    @NotEmpty
    @Size(min = 2, message = "Должно быть более 2 символов")
    private String maker;

    @NotEmpty
    @Size(min = 2, message = "Должно быть более 2 символов")
    private String model;

    @Min(1880)
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
