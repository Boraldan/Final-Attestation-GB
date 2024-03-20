package boraldan.shop.service;

import boraldan.shop.domen.person.Person;
import boraldan.shop.repository.PersonRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


/**
 * Сервис для выполнения операций регистрации пользователей.
 */
@Service
@Transactional(readOnly = true)
public class RegistrationService {

    private final PersonRepo personRepo;
    private final PasswordEncoder passwordEncoder;

    /**
     * Конструктор сервиса регистрации.
     *
     * @param personRepo      репозиторий для работы с пользователями
     * @param passwordEncoder компонент для кодирования паролей
     */
    @Autowired
    public RegistrationService(PersonRepo personRepo, PasswordEncoder passwordEncoder) {
        this.personRepo = personRepo;
        this.passwordEncoder = passwordEncoder;
    }

    /**
     * Регистрирует нового пользователя.
     *
     * @param person данные нового пользователя
     */
    @Transactional
    public void registration(Person person) {
        // Кодирование пароля перед сохранением пользователя
        person.setPassword(passwordEncoder.encode(person.getPassword()));
        // Установка роли пользователя
        person.setRole("ROLE_USER");
        // Сохранение пользователя
        personRepo.save(person);
    }
}