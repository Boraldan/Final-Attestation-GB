package boraldan.bank.service;

import boraldan.bank.model.Account;
import boraldan.bank.repository.AccountRepository;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;

@AllArgsConstructor
@Service
public class TransferService {
    // Репозиторий для доступа к данным счетов

    private final AccountRepository accountRepository;

    /**
     * Переводит денежные средства между счетами.
     * @param idSender   Идентификатор счета отправителя.
     * @param idReceiver Идентификатор счета получателя.
     * @param amount     Сумма для перевода.
     */
    @Transactional
    public void transferMoney(long idSender, long idReceiver, BigDecimal amount) {
        Account sender = accountRepository.findById(idSender).get();
        Account receiver = accountRepository.findById(idReceiver).get();

        sender.setAmount(sender.getAmount().subtract(amount));
        receiver.setAmount(receiver.getAmount().add(amount));

        accountRepository.save(sender);
        accountRepository.save(receiver);
    }
    /**
     * Осуществляет покупку с переводом денежных средств.
     * @param bayerCard  Номер кредитной карты покупателя.
     * @param sellerCard Номер кредитной карты продавца.
     * @param amount     Сумма для перевода.
     * @return           Ответ о выполнении операции.
     */
    @Transactional
    public ResponseEntity<?> transferShop(long bayerCard, long sellerCard, BigDecimal amount) {
        try {
            Account sender = accountRepository.findByCard(bayerCard);
            Account receiver = accountRepository.findByCard(sellerCard);

            sender.setAmount(sender.getAmount().subtract(amount));
            receiver.setAmount(receiver.getAmount().add(amount));

            accountRepository.save(sender);
            accountRepository.save(receiver);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Bad Request: ");
        }
        return ResponseEntity.ok().build();
    }
    /**
     * Получаем список всех счетов в Банке
     * @return Список всех счетов.
     */
    public List<Account> getAllAccounts() {
        return accountRepository.findAll();
    }
}
