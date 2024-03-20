package boraldan.shop.repository;

import boraldan.shop.domen.person.Seller;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * Репозиторий для доступа к данным о продавцах.
 */
@Repository
public interface SellerRepo extends JpaRepository<Seller, Integer> {
}