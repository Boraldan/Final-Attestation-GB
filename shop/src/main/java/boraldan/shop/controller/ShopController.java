package boraldan.shop.controller;

import boraldan.shop.domen.Cart.Lot;
import boraldan.shop.domen.car.Car;
import boraldan.shop.controller.feign.BankFeign;
import boraldan.shop.service.CarService;
import boraldan.shop.service.CartService;
import boraldan.shop.service.FileGateway;
import boraldan.shop.util.LotValidator;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;


import java.util.List;

/**
 * класс ShopController является контроллером, отвечающим за обработку запросов, связанных с функциональностью магазина
 */
@Controller
@RequiredArgsConstructor
@RequestMapping("/shop")
public class ShopController {

    private final CarService carService; // Сервис для работы с автомобилями
    private final CartService cartService; // Сервис для работы с корзиной покупок
    private final LotValidator lotValidator; // Валидатор для проверки лотов
    private final FileGateway fileGateway; // Шлюз для записи данных в файлы

    /**
     * Метод для отображения страницы магазина с автомобилями с возможностью пагинации.
     *
     * @param model   объект Model для передачи данных в представление
     * @param page    номер страницы (опционально)
     * @param byYear  флаг сортировки по году (опционально)
     * @return страница магазина с каталогом товаров
     */
    @GetMapping
    public String shop(Model model,
                       @RequestParam(value = "page", required = false) Integer page,
                       @RequestParam(value = "byYear", required = false) boolean byYear) {

        int sizePage = 8;
        if (page == null) {
            List<Car> carList = carService.findWithPagination(0, sizePage, byYear);
            model.addAttribute("cars", carList);
            model.addAttribute("numpages", carService.countPageByVolume(sizePage, 0));
            model.addAttribute("currentPage", 5);
        } else {
            List<Car> carList = carService.findWithPagination(page-1, sizePage, byYear);
            model.addAttribute("cars", carList);
            model.addAttribute("numpages", carService.countPageByVolume(sizePage, 0));
            model.addAttribute("currentPage", page);
        }
        return "shop";
    }

    /**
     * Метод для отображения страницы с информацией об автомобиле.
     *
     * @param model объект Model для передачи данных в представление
     * @param id    ID автомобиля
     * @return страница с информацией об автомобиле
     */
    @GetMapping("/car/{id}")
    public String getCar(Model model, @PathVariable("id") int id) {
        Car car = carService.findById(id).orElse(null);
        if (car != null) {
            model.addAttribute("car", car);
            Lot lot = new Lot();
            lot.setLot(1);
            model.addAttribute("lot", lot);
            return "car";
        }
        return "redirect:/shop";
    }
    /**
     * Метод для добавления автомобиля в корзину покупок.
     *
     * @param lot            объект Lot с информацией о лоте
     * @param id             ID автомобиля
     * @param model          объект Model для передачи данных в представление
     * @param bindingResult  объект BindingResult для проверки ошибок валидации
     * @return  страница с информацией об автомобиле
     */
    @PostMapping("/car/{id}")
    public String addToCart(@ModelAttribute("lot") Lot lot, @PathVariable("id") int id, Model model,
                            BindingResult bindingResult) {
        lot.setId(id);
        Car validCar = lotValidator.validateLot(lot, bindingResult);
        if (bindingResult.hasErrors()) {
            model.addAttribute("car", validCar);
            return "car";
        }
        try {
            cartService.addItem(validCar, lot.getLot());
        } catch (CloneNotSupportedException e) {
            throw new RuntimeException(e);
        }


        fileGateway.writeToFile(validCar.getMaker() + ".txt", validCar.getTitle());
        return "redirect:/shop/car/" + id;
    }


}
