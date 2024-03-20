package boraldan.shop.service;

import boraldan.shop.domen.person.Seller;
import boraldan.shop.repository.SellerRepo;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


import java.util.List;
import java.util.Optional;

/**
 * Сервис для выполнения операций с продавцами.
 */
@Service
@AllArgsConstructor
public class SellerService {

    private final SellerRepo sellerRepo;

    /**
     * Находит продавца по указанному идентификатору.
     *
     * @param id идентификатор продавца
     * @return объект Optional, содержащий найденного продавца, если такой есть, иначе пустой
     */
    public Optional<Seller> findById(int id){
        return sellerRepo.findById(id);
    }
}