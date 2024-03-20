package boraldan.shop.controller;

import boraldan.shop.domen.ContactEmail;
import boraldan.shop.service.CarService;
import boraldan.shop.service.EmailService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.session.SessionRegistry;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

/**
 *  класса StartController отвечает за обработку запросов, связанных с различными дополнительными страницами веб-приложения
 */
@Controller
@RequiredArgsConstructor
public class StartController {

    private final CarService carService; // Сервис для работы с автомобилями
    private final EmailService emailService; // Сервис для отправки электронной почты

    /**
     * Метод для отображения главной страницы со списком топ-3 автомобилей.
     *
     * @param model объект Model для передачи данных в представление
     * @return главная страницы
     */
    @GetMapping
    public String index(Model model) {
        model.addAttribute("cars", carService.getTop3());
        return "index";
    }

    /**
     * Метод для отображения страницы "О нас".
     *
     * @return  страница "О нас"
     */
    @GetMapping("/about")
    public String about() {
        return "about";
    }

    /**
     * Метод для отображения страницы "Контакты" с формой для отправки электронного письма.
     *
     * @param model объект Model для передачи данных в представление
     * @return страница "Контакты"
     */
    @GetMapping("/contact")
    public String contactGet(Model model) {
        model.addAttribute("contactEmail", new ContactEmail());
        return "contact";
    }
    /**
     * Метод для обработки отправки электронного письма через форму на странице "Контакты".
     *
     * @param contactEmail  объект ContactEmail с данными формы
     * @param bindingResult объект BindingResult для проверки ошибок валидации
     * @return  страница "Контакты"
     */
    @PostMapping("/contact")
    public String contactPost(@ModelAttribute("contactEmail") @Valid ContactEmail contactEmail, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return "contact";
        }
        emailService.sendContactEmailThread(contactEmail);
        return "redirect:/contact";
    }

    /**
     * Метод для отображения страницы "Услуги".
     *
     * @return страница "Услуги"
     */
    @GetMapping("/services")
    public String services() {
        return "services";
    }

    /**
     * Метод для отображения страницы администратора.
     *
     * @return страница администратора
     */
    @GetMapping("/admin")
    public String adminPage(){
        return "admin/main";
    }

}
