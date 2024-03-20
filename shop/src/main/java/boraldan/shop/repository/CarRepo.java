package boraldan.shop.repository;

import boraldan.shop.domen.car.Car;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;


import java.util.List;
/**
 * Репозиторий для доступа к данным об автомобилях.
 */
@Repository
public interface CarRepo extends JpaRepository<Car, Integer> {

    /**
     * Извлекает список топ 3 автомобилей с ненулевым объемом, используя пагинацию.
     *
     * @param pageable информация о странице и сортировке
     * @return список топ 3 автомобилей
     */
    @Query("SELECT car FROM Car car WHERE car.volume > 0")
    List<Car> findTop3(Pageable pageable);

    /**
     * Извлекает список автомобилей с объемом больше заданного.
     *
     * @param volume минимальный объем автомобиля
     * @return список автомобилей с объемом больше заданного
     */
    List<Car> findByVolumeGreaterThan(int volume);

    /**
     * Извлекает страницу автомобилей с ненулевым объемом, используя пагинацию.
     *
     * @param pageable информация о странице и сортировке
     * @return страница автомобилей с ненулевым объемом
     */
    @Query("SELECT car FROM Car car WHERE car.volume > 0")
    Page<Car> findVolumeNotNull(Pageable pageable);
}
