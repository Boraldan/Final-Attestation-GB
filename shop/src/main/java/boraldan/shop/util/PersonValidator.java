package boraldan.shop.util;

import boraldan.shop.domen.person.Person;
import boraldan.shop.service.PersonService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

/**
 * Валидатор для объектов типа Person.
 */
@Component
@RequiredArgsConstructor
public class PersonValidator implements Validator {
    private final PersonService personService;

    /**
     * Поддерживает ли валидатор данный класс объекта для проверки.
     *
     * @param clazz класс объекта
     * @return true, если класс объекта совпадает с классом Person, иначе false
     */
    @Override
    public boolean supports(Class<?> clazz) {
        return Person.class.equals(clazz);
    }

    /**
     * Проверяет объект типа Person на корректность.
     * Если валидация не пройдена, в объект errors добавляются соответствующие сообщения об ошибках.
     *
     * @param target объект, который нужно проверить
     * @param errors объект для записи ошибок в процессе валидации
     */
    @Override
    public void validate(Object target, Errors errors) {
        Person person = (Person) target;
        if (personService.findByPhone(person.getPhone()).isPresent())
            errors.rejectValue("phone", "", "Пользователь с таким телефоном существует");

        if (personService.findByEmail(person.getEmail()).isPresent())
            errors.rejectValue("email", "", "Пользователь с таким email существует");

        if (personService.findByCard(person.getCard()).isPresent())
            errors.rejectValue("card", "", "Пользователь с такой картой существует");
    }

}

