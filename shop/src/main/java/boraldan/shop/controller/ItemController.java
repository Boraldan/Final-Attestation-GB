package boraldan.shop.controller;

import boraldan.shop.domen.car.Car;
import boraldan.shop.dto.NewCarDTO;
import boraldan.shop.service.CarService;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;


import java.util.List;

/**
 * Контроллер для работы с товарами через кабинет администратора.
 */
@Controller
@AllArgsConstructor
@RequestMapping("/item")
public class ItemController {

    // Инструмент для преобразования объектов между классами
    private final ModelMapper modelMapper;
    // Сервис для работы с объектами типа Car (автомобили)
    private final CarService carService;

    /**
     * Метод для отображения списка автомобилей с пагинацией.
     *
     * @param model  объект Model для передачи данных в представление
     * @param page   номер страницы (опционально)
     * @param byYear флаг сортировки по году (опционально)
     * @return страница со списком автомобилей
     */
    @GetMapping
    public String carList(Model model,
                          @RequestParam(value = "page", required = false) Integer page,
                          @RequestParam(value = "byYear", required = false) boolean byYear) {

        int sizePage = 15;
        if (page == null) {
            List<Car> carList = carService.findWithPaginationAll(0, sizePage, byYear);
            model.addAttribute("cars", carList);
            model.addAttribute("numpages", carService.countPageAll(sizePage));
            model.addAttribute("currentPage", 5);
        } else {
            List<Car> carList = carService.findWithPaginationAll(page - 1, sizePage, byYear);
            model.addAttribute("cars", carList);
            model.addAttribute("numpages", carService.countPageAll(sizePage));
            model.addAttribute("currentPage", page);
        }
        return "item/items";
    }

    /**
     * Метод для отображения страницы редактирования автомобиля по его идентификатору.
     *
     * @param id    идентификатор автомобиля
     * @param model объект Model для передачи данных в представление
     * @return страница редактирования автомобиля
     */
    @GetMapping("/edit/{id}")
    public String editCar(@PathVariable("id") int id, Model model) {
        model.addAttribute("car", carService.findById(id).get());
        return "item/edit";
    }

    /**
     * Метод для обработки POST запроса на редактирование автомобиля.
     *
     * @param car объект Car, представляющий отредактированные данные автомобиля
     * @return страница со списком автомобилей после успешного редактирования
     */
    @PostMapping("/edit")
    public String editCarPost(@ModelAttribute("car") Car car) {
        if (!car.getImg().equals(carService.findById(car.getId()).get().getImg())) {
            car.setImg(carService.getImgPath(car.getImg()));
        }
        carService.save(car);
        return "redirect:/item";
    }

    /**
     * Метод для обработки DELETE запроса на удаление автомобиля по его идентификатору.
     *
     * @param id идентификатор автомобиля
     * @return страница со списком автомобилей после успешного удаления
     */
    @DeleteMapping("/{id}")
    public String deleteCar(@PathVariable("id") int id) {
        System.out.println(id);
        carService.deleteById(id);
        return "redirect:/item";
    }

    /**
     * Метод для отображения страницы создания нового автомобиля.
     *
     * @param model объект Model для передачи данных в представление
     * @return страница создания нового автомобиля
     */
    @GetMapping("/new")
    public String newItem(Model model) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String role = authentication.getAuthorities().stream().toList().get(0).toString();
        if (role.equals("ROLE_ADMIN")) {
            model.addAttribute("newCar", new Car());
            return "item/new";
        }
        return "redirect:auth/login";
    }

    /**
     * Метод для обработки POST запроса на создание нового автомобиля.
     *
     * @param newCar        объект NewCarDTO, содержащий данные о новом автомобиле
     * @param bindingResult объект BindingResult для проверки ошибок валидации
     * @return страница создания нового автомобиля после успешного создания
     */
    @PostMapping("/new")
    public String newItemPost(@ModelAttribute("newCar") @Valid NewCarDTO newCar, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return "item/new";
        }
        newCar.setImg(carService.getImgPath(newCar.getImg()));
        Car car = convertToCar(newCar);
        carService.save(car);
        return "redirect:/item/new";
    }

    /**
     * Метод для преобразования объекта NewCarDTO в объект Car.
     *
     * @param carDTO объект NewCarDTO, который необходимо преобразовать
     * @return объект Car, созданный на основе данных из NewCarDTO
     */
    private Car convertToCar(NewCarDTO carDTO) {
        return modelMapper.map(carDTO, Car.class);
    }

    /**
     * Метод для преобразования объекта Car в объект NewCarDTO.
     *
     * @param car объект Car, который необходимо преобразовать
     * @return объект NewCarDTO, созданный на основе данных из Car
     */
    private NewCarDTO convertToCarDTO(Car car) {
        return modelMapper.map(car, NewCarDTO.class);
    }


}
