package boraldan.shop.controller;

import boraldan.shop.domen.person.Person;
import boraldan.shop.dto.PersonDTO;
import boraldan.shop.security.PersonDetails;
import boraldan.shop.service.PersonService;
import boraldan.shop.service.RegistrationService;
import boraldan.shop.util.PersonDTOValid;
import boraldan.shop.util.PersonValidator;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.context.annotation.SessionScope;
/**
 * Контроллер, отвечающий за обработку запросов, связанных с аутентификацией и регистрацией пользователей.
 */
@Controller
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {

    private final RegistrationService registrationService;
    private final PersonValidator personValidator;
    private final PersonDTOValid personDTOValid;
    private final PersonService personService;
    private final ModelMapper modelMapper;

    /**
     * Метод для отображения страницы входа.
     * @return Представление страницы входа
     */
    @GetMapping("/login")
    public String loginPage() {
        if (!getRole().equals("ROLE_ANONYMOUS")) {
            return "redirect:/auth/account";
        }
        return "auth/login";
    }
    /**
     * Метод для отображения страницы регистрации.
     * @param person объект Person для передачи в представление
     * @return Представление страницы регистрации
     */
    @GetMapping("/registration")
    public String registrationPage(@ModelAttribute("person") Person person) {
        if (!getRole().equals("ROLE_ANONYMOUS")) {
            return "redirect:/auth/login";
        }
        return "auth/registration";
    }
    /**
     * Метод для выполнения регистрации пользователя.
     * @param person объект Person, представляющий данные пользователя
     * @param bindingResult объект BindingResult для проверки ошибок валидации
     * @return перенаправление на страницу входа после успешной регистрации
     */
    @PostMapping("/registration")
    public String performRegistration(@ModelAttribute("person") @Valid Person person, BindingResult bindingResult) {
        personValidator.validate(person, bindingResult);
        if (bindingResult.hasErrors()) {
            return "/auth/registration";
        }
        registrationService.registration(person);
        return "redirect:/auth/login";
    }

    /**
     * Метод для отображения страницы аккаунта пользователя.
     * @param model объект Model для передачи данных в представление
     * @return Строка, представляющая имя представления страницы аккаунта пользователя
     */
    @GetMapping("/account")
    public String accountPage(Model model) {
        Person person = personService.accountPage();
        if (person != null && person.getRole().equals("ROLE_USER")) {
            model.addAttribute("person", person);
            return "auth/account";
        }
        if (person != null && person.getRole().equals("ROLE_ADMIN")) {
            model.addAttribute("person", person);
            return "redirect:/admin";
        }
        return "auth/login";
    }
    /**
     * Метод для отображения страницы изменения данных аккаунта пользователя.
     *
     * @param model объект Model для передачи данных в представление
     * @return Представление с полями изменения данных аккаунта пользователя
     */
    @GetMapping("/account/alter")
    public String accountAlter(Model model) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String role = authentication.getAuthorities().stream().toList().get(0).toString();
        if (!role.equals("ROLE_ANONYMOUS")) {
            Person person = ((PersonDetails) authentication.getPrincipal()).getPerson();
            PersonDTO personDTO = convertToPersonDTO(person);
            model.addAttribute("personDTO", personDTO);
            return "auth/alter";
        }
        return "auth/account";
    }
    /**
     * Метод для обработки POST запроса на изменение данных аккаунта пользователя.
     *
     * @param personDTO объект PersonDTO, представляющий данные пользователя для изменения
     * @param bindingResult объект BindingResult для проверки ошибок валидации
     * @return  перенаправление на страницу аккаунта пользователя после успешного изменения данных
     */
    @PostMapping("/account/alter")
    public String postAccountAlter(@ModelAttribute("personDTO") @Valid PersonDTO personDTO, BindingResult bindingResult) {
        personDTOValid.validateDTO(personDTO, bindingResult);
        if (bindingResult.hasErrors()) {
            return "/auth/alter";
        }
        personService.editPersonAccount(convertToPerson(personDTO));
        return "redirect:/auth/account";
    }
    // Преобразование объекта PersonDTO в объект Person
    private Person convertToPerson(PersonDTO personDTO) {
        return modelMapper.map(personDTO, Person.class);
    }
    // Преобразование объекта Person в объект PersonDTO
    private PersonDTO convertToPersonDTO(Person person) {
        return modelMapper.map(person, PersonDTO.class);
    }

    /**
     * Получение роли текущего пользователя из контекста безопасности
     *
     * @return Роль текущего пользователя в виде String
     */
    private String getRole() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        return authentication.getAuthorities().stream().toList().get(0).toString();
    }

}
