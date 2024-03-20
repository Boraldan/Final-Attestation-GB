package boraldan.shop.service;

import boraldan.shop.domen.person.Person;
import boraldan.shop.repository.PersonRepo;
import boraldan.shop.security.PersonDetails;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;



import java.util.Optional;
/**
 * Сервис для загрузки информации о пользователе по его имени пользователя.
 */
@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class PersonDetailsService implements UserDetailsService {

    private final PersonRepo personRepo;

    /**
     * Загружает информацию о пользователе по его имени.
     *
     * @param username имя пользователя
     * @return информация о пользователе
     * @throws UsernameNotFoundException если пользователь не найден
     */
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Optional<Person> person = personRepo.findByName(username);
        if (person.isEmpty()) throw new UsernameNotFoundException("User not found");
        return new PersonDetails(person.get());
    }
}
