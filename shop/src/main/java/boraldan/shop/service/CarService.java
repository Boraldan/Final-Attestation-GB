package boraldan.shop.service;

import boraldan.shop.domen.car.Car;
import boraldan.shop.repository.CarRepo;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.CacheManager;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;



import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
/**
 * Сервис, предоставляющий методы для работы с автомобилями.
 */
@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class CarService {

    private final CarRepo carRepo;
    /**
     * Получает все автомобили.
     * @param sortByYear указывает, нужно ли сортировать автомобили по году производства
     * @return список всех автомобилей
     */
    public List<Car> allCar(boolean sortByYear) {
        if (sortByYear) {
            return carRepo.findAll(Sort.by("year"));
        } else {
            return carRepo.findAll();
        }
    }

    /**
     * Находит автомобиль по его идентификатору.
     * @param id идентификатор автомобиля
     * @return найденный автомобиль, если существует
     */
    public Optional<Car> findById(int id) {
        return carRepo.findById(id);
    }

    /**
     * Получает все автомобили без пагинации.
     * @return список всех автомобилей
     */
    public List<Car> findAll(){
        return carRepo.findAll();
    }

    /**
     * Получает список автомобилей с пагинацией.
     * @param page номер страницы
     * @param carPerPage количество автомобилей на странице
     * @param sortByYear указывает, нужно ли сортировать автомобили по году производства
     * @return список автомобилей на указанной странице
     */
    public List<Car> findWithPagination(Integer page, Integer carPerPage, boolean sortByYear) {
        if (sortByYear) {
            return carRepo.findVolumeNotNull(PageRequest.of(page, carPerPage, Sort.by("year"))).getContent();
        } else {
            return carRepo.findVolumeNotNull(PageRequest.of(page, carPerPage, Sort.by("id"))).getContent();
        }
    }

    /**
     * Получает список всех автомобилей с пагинацией.
     * @param page номер страницы
     * @param carPerPage количество автомобилей на странице
     * @param sortByYear указывает, нужно ли сортировать автомобили по году производства
     * @return список автомобилей на указанной странице
     */
    public List<Car> findWithPaginationAll(Integer page, Integer carPerPage, boolean sortByYear) {
        if (sortByYear) {
            return carRepo.findAll(PageRequest.of(page, carPerPage, Sort.by("year"))).getContent();
        } else {
            return carRepo.findAll(PageRequest.of(page, carPerPage, Sort.by("id"))).getContent();
        }
    }

    /**
     * Уменьшает количество доступных автомобилей в базе данных на указанное количество.
     * @param dbCar автомобиль в базе данных
     * @param lot количество автомобилей, которое нужно убрать
     */
    @Transactional
    public void minusVolumeDB(Car dbCar, int lot) {
        dbCar.setVolume(dbCar.getVolume() - lot);
        carRepo.save(dbCar);
    }

    /**
     * Увеличивает количество доступных автомобилей в базе данных на указанное количество.
     * @param car автомобиль, который нужно добавить
     */
    @Transactional
    public void addVolumeDB(Car car) {
        Car dbCar = carRepo.findById(car.getId()).orElse(null);
        if (dbCar != null) {
            dbCar.setVolume(dbCar.getVolume() + car.getVolume());
            carRepo.save(dbCar);
        }
    }

    /**
     * Сохраняет автомобиль в базе данных.
     * @param car автомобиль, который нужно сохранить
     * @return сохраненный автомобиль
     */
    @Transactional
    public Car save(Car car) {
        return carRepo.save(car);
    }

    /**
     * Получает топ-3 автомобилей.
     * @return список топ-3 автомобилей
     */
    public List<Car> getTop3() {
        return carRepo.findTop3(PageRequest.of(0, 3));
    }
    /**
     * Вычисляет количество страниц для пагинации в зависимости от количества автомобилей.
     * @param sizePage количество автомобилей на странице
     * @param carVolume количество доступных автомобилей
     * @return количество страниц пагинации
     */
    public int countPageByVolume(int sizePage, int carVolume) {
        int size = carRepo.findByVolumeGreaterThan(carVolume).size();
        return (int) Math.ceil((double) size / sizePage);
    }

    /**
     * Вычисляет количество страниц для пагинации всех автомобилей.
     * @param sizePage количество автомобилей на странице
     * @return количество страниц пагинации
     */
    public int countPageAll(int sizePage) {
        int size = carRepo.findAll().size();
        return (int) Math.ceil((double) size / sizePage);
    }

    /**
     * Находит количество автомобилей с объемом больше указанного.
     * @param volume минимальный объем автомобилей
     * @return количество автомобилей с указанным объемом
     */
    public int findByVolumeGreaterThan(int volume) {
        return carRepo.findByVolumeGreaterThan(volume).size();
    }

    /**
     * Удаляет автомобиль по его идентификатору.
     * @param id идентификатор автомобиля
     */
    @Transactional
    public void deleteById(int id) {
        carRepo.deleteById(id);
    }

    /**
     * Возвращает путь к изображению.
     * @param pathImg путь к изображению
     * @return путь к изображению
     */
    public String getImgPath(String pathImg) {
        if (pathImg.contains("resources\\static")) {
            pathImg = pathImg.replace('\\', '/').replace("\"", "");
            return pathImg.substring(pathImg.indexOf("resources/static") + "resources/static".length()).strip();
        }
        return "/image/1.jpg";
    }

}
