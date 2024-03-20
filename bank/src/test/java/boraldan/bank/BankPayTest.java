package boraldan.bank;

import boraldan.bank.model.Account;
import boraldan.bank.repository.AccountRepository;
import boraldan.bank.service.TransferService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;

import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
public class BankPayTest {

    @Mock
    private AccountRepository  accountRepository;

    @InjectMocks
    private TransferService transferService;

    @Test
    public void transactionExpectCorrect(){
        Long source = 11111L;
        Account sourceAccount = new Account();
        sourceAccount.setCard(source);
        BigDecimal sourceBalance = new BigDecimal(1000);
        sourceAccount.setAmount(sourceBalance);

        Long destination = 55555L;
        Account destinationAccount = new Account();
        destinationAccount.setCard(destination);
        BigDecimal destinationBalance = new BigDecimal(0);
        destinationAccount.setAmount(destinationBalance);
        BigDecimal sum = new BigDecimal(10);

        given(accountRepository.findByCard(source))
                .willReturn(sourceAccount);
        given(accountRepository.findByCard(destination))
                .willReturn(destinationAccount);
        transferService.transferShop(sourceAccount.getCard(), destinationAccount.getCard(), sum);

        verify(accountRepository).findByCard(source);
        verify(accountRepository).findByCard(destination);
        verify(accountRepository).save(sourceAccount);
        verify(accountRepository).save(destinationAccount);

        Assertions.assertEquals(sourceBalance.subtract(sum),
                sourceAccount.getAmount());
        Assertions.assertEquals(destinationBalance.add(sum),
                destinationAccount.getAmount());
    }
}
