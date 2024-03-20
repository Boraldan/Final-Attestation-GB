package boraldan.shop.service;


import boraldan.shop.domen.order.Orders;
import boraldan.shop.domen.person.Person;
import boraldan.shop.repository.PersonRepo;
import boraldan.shop.security.PersonDetails;
import boraldan.shop.service.interfaces.OrderService;
import lombok.RequiredArgsConstructor;
import org.hibernate.Hibernate;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;



import java.util.*;
/**
 * Сервис для управления операциями с пользователями.
 */
@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class PersonService {

    private final PersonRepo personRepo;
    private final OrderService orderService;

    /**
     * Находит пользователя по его имени.
     *
     * @param name имя пользователя
     * @return пользователь
     */
    public Optional<Person> findByName(String name) {
        return personRepo.findByName(name);
    }

    /**
     * Находит пользователя по его идентификатору.
     *
     * @param id идентификатор пользователя
     * @return пользователь
     */
    public Person findById(int id) {
        Person person = personRepo.findById(id).get();
        System.out.println("findById ---> " + person.getOrders());
        person.setOrders(orderService.setLotCarList(person.getOrders()));
        return person;
    }

    /**
     * Находит пользователя по его email.
     *
     * @param email email пользователя
     * @return пользователь
     */
    public Optional<Person> findByEmail(String email) {
        return personRepo.findByEmail(email);
    }

    /**
     * Находит пользователя по его карте.
     *
     * @param card номер карты пользователя
     * @return пользователь
     */
    public Optional<Person> findByCard(Long card) {
        return personRepo.findByCard(card);
    }

    /**
     * Находит пользователя по его номеру телефона.
     *
     * @param phone номер телефона пользователя
     * @return пользователь
     */
    public Optional<Person> findByPhone(Long phone) {
        return personRepo.findByPhone(phone);
    }

    /**
     * Получает всех пользователей.
     *
     * @return список всех пользователей
     */
    public List<Person> allPerson() {
        return personRepo.findAll();
    }

    /**
     * Сохраняет пользователя.
     *
     * @param person пользователь для сохранения
     */
    @Transactional
    public void save(Person person) {
        personRepo.save(person);
    }

    /**
     * Получает информацию о пользователе для страницы аккаунта.
     *
     * @return пользователь
     */
    public Person accountPage() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String role = authentication.getAuthorities().stream().toList().get(0).toString();
        if (role.equals("ROLE_USER")) {
            Person person = ((PersonDetails) authentication.getPrincipal()).getPerson();
            person = findById(person.getId());
            person.getOrders().sort(Comparator.comparing(Orders::getCreatAt));
            return person;
        }
        if (role.equals("ROLE_ADMIN")) {
            return ((PersonDetails) authentication.getPrincipal()).getPerson();
        }
        return null;
    }

    /**
     * Получает всех пользователей с пагинацией.
     *
     * @param page           номер страницы
     * @param personPerPage количество пользователей на странице
     * @return список пользователей на указанной странице
     */
    public List<Person> findWithPaginationAll(Integer page, Integer personPerPage) {
        return personRepo.findAll(PageRequest.of(page, personPerPage, Sort.by("id"))).getContent();

    }

    /**
     * Получает количество страниц с пагинацией.
     *
     * @return список страниц
     */
    public List<Integer> pagesPageableAll() {
        List<Integer> pages = new ArrayList<>();
        int size = personRepo.findAll().size();
        if (size >= 15) {
            if (size % 15 == 0) {
                size = size / 15;
            } else {
                size = size / 15 + 1;
            }
            for (int i = 0; i < size; i++) {
                pages.add(i);
            }
            return pages;
        }
        pages.add(0);
        return pages;
    }

    /**
     * Удаляет пользователя по его идентификатору.
     *
     * @param id идентификатор пользователя для удаления
     */
    @Transactional
    public void deleteById(int id) {
        personRepo.deleteById(id);
    }

    /**
     * Редактирует информацию о пользователе.
     *
     * @param editPerson редактируемый пользователь
     */
    @Transactional
    public void editPersonAccount(Person editPerson) {
        if (!getRole().equals("ROLE_ANONYMOUS")) {
            checkFieldAndSave(editPerson);
        }
    }

    /**
     * Редактирует информацию о пользователе администратором.
     *
     * @param editPerson редактируемый пользователь
     */
    @Transactional
    public void editPersonAdmin(Person editPerson) {
        checkFieldAndSave(editPerson);
    }

    /**
     * Проверяет и сохраняет измененные поля пользователя.
     *
     * @param editPerson редактируемый пользователь
     */
    private void checkFieldAndSave(Person editPerson) {
        Person person = findById(editPerson.getId());
        Hibernate.initialize(person.getOrders());

        if (!person.getName().equals(editPerson.getName())) {
            person.setName(editPerson.getName());
        }
        if (!person.getAge().equals(editPerson.getAge())) {
            person.setAge(editPerson.getAge());
        }
        if (!person.getCard().equals(editPerson.getCard())) {
            person.setCard(editPerson.getCard());
        }
        if (!person.getPhone().equals(editPerson.getPhone())) {
            person.setPhone(editPerson.getPhone());
        }
        if (!person.getEmail().equals(editPerson.getEmail())) {
            person.setEmail(editPerson.getEmail());
        }
        personRepo.save(person);
    }

    /**
     * Получает роль текущего пользователя из контекста безопасности.
     *
     * @return роль текущего пользователя
     */
    private String getRole() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        return authentication.getAuthorities().stream().toList().get(0).toString();
    }
}
