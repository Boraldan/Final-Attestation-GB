package boraldan.shop.repository;

import boraldan.shop.domen.person.Person;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


import java.util.Optional;

/**
 * Репозиторий для доступа к данным о пользователях.
 */
@Repository
public interface PersonRepo extends JpaRepository<Person, Integer> {

    /**
     * Находит пользователя по его имени.
     *
     * @param name имя пользователя
     * @return опциональный объект пользователя
     */
    Optional<Person> findByName(String name);

    /**
     * Находит пользователя по его идентификатору.
     *
     * @param id идентификатор пользователя
     * @return опциональный объект пользователя
     */
    Optional<Person> findById(int id);

    /**
     * Находит пользователя по его адресу электронной почты.
     *
     * @param email адрес электронной почты пользователя
     * @return опциональный объект пользователя
     */
    Optional<Person> findByEmail(String email);

    /**
     * Находит пользователя по его карте.
     *
     * @param card номер карты пользователя
     * @return опциональный объект пользователя
     */
    Optional<Person> findByCard(Long card);

    /**
     * Находит пользователя по его номеру телефона.
     *
     * @param phone номер телефона пользователя
     * @return опциональный объект пользователя
     */
    Optional<Person> findByPhone(Long phone);
}