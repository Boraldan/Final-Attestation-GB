package boraldan.shop.util;

import boraldan.shop.domen.person.Person;
import boraldan.shop.dto.PersonDTO;
import boraldan.shop.service.PersonService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;


/**
 * Валидатор для объектов типа PersonDTO.
 */
@Component
@RequiredArgsConstructor
public class PersonDTOValid implements Validator {

    private final PersonService personService;

    /**
     * Поддерживает ли валидатор данный класс объекта для проверки.
     *
     * @param clazz класс объекта
     * @return true, если класс объекта совпадает с классом PersonDTO, иначе false
     */
    @Override
    public boolean supports(Class<?> clazz) {
        return PersonDTO.class.equals(clazz);
    }
    @Override
    public void validate(Object target, Errors errors) {
    }

    /**
     * Проверяет поля DTO на уникальность в базе данных.
     * Если валидация не пройдена, в объект errors добавляется соответствующее сообщение об ошибке.
     *
     * @param target объект типа PersonDTO, который проверяется
     * @param errors объект для записи ошибок в процессе валидации
     */
    public void validateDTO(Object target, Errors errors) {
        PersonDTO personDTO = (PersonDTO) target;
        Person person = personService.findById(personDTO.getId());
        if (personService.findByCard(personDTO.getCard()).isPresent() && personDTO.getId() != person.getId()) {
            errors.rejectValue("card", "", "Пользователь с такой картой существует");
        }
        if (personService.findByPhone(personDTO.getPhone()).isPresent() && personDTO.getId() != person.getId()) {
            errors.rejectValue("phone", "", "Пользователь с таким телефоном существует");
        }
        if (personService.findByEmail(personDTO.getEmail()).isPresent() && personDTO.getId() != person.getId()) {
            errors.rejectValue("email", "", "Пользователь с таким email существует");
        }
    }
}
