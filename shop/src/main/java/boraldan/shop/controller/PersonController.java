package boraldan.shop.controller;

import boraldan.shop.domen.person.Person;
import boraldan.shop.dto.PersonDTO;
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
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;


/**
 * Контроллер для управления пользователями интернет-магазина.
 */
@Controller
@RequestMapping("/person")
@RequiredArgsConstructor
public class PersonController {
    // Сервис для выполнения операций с объектами Person
    private final PersonService personService;
    // Сервис для выполнения операций регистрации пользователей
    private final RegistrationService registrationService;
    // Валидатор для проверки данных объекта Person
    private final PersonValidator personValidator;
    // Валидатор для проверки данных объекта PersonDTO
    private final PersonDTOValid personDTOValid;
    // Инструмент для преобразования объектов между классами
    private final ModelMapper modelMapper;

    /**
     * Метод для отображения списка людей с пагинацией.
     *
     * @param model объект Model для передачи данных в представление
     * @param page  номер страницы (опционально)
     * @return  страница со списком людей
     */
    @GetMapping
    public String mainPeople(Model model,
                             @RequestParam(value = "page", required = false) Integer page) {
        if (page == null) {
            model.addAttribute("people", personService.findWithPaginationAll(0, 15));
            model.addAttribute("numpages", personService.pagesPageableAll());
        } else {
            model.addAttribute("people", personService.findWithPaginationAll(page, 15));
            model.addAttribute("numpages", personService.pagesPageableAll());
        }
        return "person/people";
    }

    /**
     * Метод для отображения страницы создания нового человека.
     *
     * @param model объект Model для передачи данных в представление
     * @return  страница создания нового человека
     */
    @GetMapping("/new")
    public String newPerson(Model model) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String role = authentication.getAuthorities().stream().toList().get(0).toString();
        if (role.equals("ROLE_ADMIN")) {
            model.addAttribute("newPerson", new Person());
            return "person/new";
        }
        return "redirect:auth/login";
    }

    /**
     * Метод для создания нового человека на основе предоставленных данных.
     *
     * @param newPerson     объект Person, содержащий данные нового человека
     * @param bindingResult объект BindingResult для проверки ошибок валидации
     * @return страница списка людей после создания человека
     */
    @PostMapping("/new")
    public String newPersonPost(@ModelAttribute("newPerson") @Valid Person newPerson, BindingResult bindingResult) {
        personValidator.validate(newPerson, bindingResult);
        if (bindingResult.hasErrors()) {
            return "person/new";
        }
        registrationService.registration(newPerson);
        return "redirect:/person";
    }

    /**
     * Метод для отображения страницы редактирования информации о человеке по его ID.
     *
     * @param id    ID человека для редактирования
     * @param model объект Model для передачи данных в представление
     * @return  страница редактирования информации о человеке
     */
    @GetMapping("/edit/{id}")
    public String editPerson(@PathVariable("id") int id, Model model) {
        PersonDTO personDTO = convertToPersonDTO(personService.findById(id));
        model.addAttribute("personDTO", personDTO);
        return "person/edit";
    }

    /**
     * Метод для обновления информации о человеке после редактирования.
     *
     * @param personDTO     объект PersonDTO, содержащий обновлённые данные о человеке
     * @param bindingResult объект BindingResult для проверки ошибок валидации
     * @return  страница списка людей после успешного редактирования
     */
    @PostMapping("/edit")
    public String editPersonPost(@ModelAttribute("personDTO") @Valid PersonDTO personDTO, BindingResult bindingResult) {
        personDTOValid.validateDTO(personDTO, bindingResult);
        if (bindingResult.hasErrors()) {
            return "person/edit";
        }
        Person person = convertToPerson(personDTO);
        personService.editPersonAdmin(person);
        return "redirect:/person";
    }
    /**
     * Метод для удаления человека по его ID.
     *
     * @param id ID человека для удаления
     * @return  страница списка людей после удаления человека
     */
    @DeleteMapping("/{id}")
    public String deleteCar(@PathVariable("id") int id) {
        personService.deleteById(id);
        return "redirect:/person";
    }

    /**
     * Метод для преобразования объекта PersonDTO в объект Person.
     *
     * @param personDTO объект PersonDTO, который необходимо преобразовать
     * @return объект Person, созданный на основе данных из PersonDTO
     */
    private Person convertToPerson(PersonDTO personDTO) {
        return modelMapper.map(personDTO, Person.class);
    }

    /**
     * Метод для преобразования объекта Person в объект PersonDTO.
     *
     * @param person объект Person, который необходимо преобразовать
     * @return объект PersonDTO, созданный на основе данных из Person
     */
    private PersonDTO convertToPersonDTO(Person person) {
        return modelMapper.map(person, PersonDTO.class);
    }
}