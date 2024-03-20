package boraldan.shop.service;


import boraldan.shop.domen.ContactEmail;
import boraldan.shop.domen.order.Orders;
import lombok.RequiredArgsConstructor;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;


import java.io.File;
/**
 * Сервисный класс для отправки электронных писем.
 */
@Service
@RequiredArgsConstructor
public class EmailService {

    private final JavaMailSender emailSender;

    /**
     * Отправляет простое текстовое сообщение о заказе Покупателю.
     *
     * @param orders информация о заказе
     */
    private void sendSimpleMessage(Orders orders) {
        String text = "%s. \nОжидайте отправки заказа.".formatted(orders.infoOrder());
        SimpleMailMessage message = new SimpleMailMessage();
//        message.setFrom("born17121982@gmail.com");
        message.setTo(orders.getPerson().getEmail());
        message.setSubject(orders.infoOrder());
        message.setText(text);
        emailSender.send(message);
    }

    /**
     * Отправляет сообщение по электронной почте с вопросом от гостя.
     *
     * @param contactEmail информация о вопросе от гостя
     */
    private void sendContactEmailMessage(ContactEmail contactEmail) {
        String text = contactEmail.infoContactEmail();
        SimpleMailMessage message = new SimpleMailMessage();
//        message.setFrom("born17121982@gmail.com");
        message.setTo("boraldantest@yandex.ru");
        message.setSubject("Вопрос от Гостя");
        message.setText(text);
        emailSender.send(message);
    }


//    public void sendWithAttachment(String to, String subject, String text,
//                                   String pathToAttachment) throws MessagingException {
//        MimeMessage message = emailSender.createMimeMessage();
//        MimeMessageHelper helper = new MimeMessageHelper(message, true);
//        helper.setFrom("born17121982@gmail.com");
//        helper.setTo(to);
//        helper.setSubject(subject);
//        helper.setText(text);
//
//        FileSystemResource file = new FileSystemResource(new File(pathToAttachment));
//        helper.addAttachment("Invoice", file);
//
//        emailSender.send(message);
//    }

    /**
     * Запускает новый поток для отправки простого текстового сообщения о заказе.
     *
     * @param orders информация о заказе
     */
    public void sendMessageThread(Orders orders){
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                sendSimpleMessage(orders);
            }
        });
        thread.start();
    }

    /**
     * Запускает новый поток для отправки сообщения по электронной почте с вопросом от гостя.
     *
     * @param contactEmail информация о вопросе от гостя
     */
    public void sendContactEmailThread(ContactEmail contactEmail){
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                sendContactEmailMessage(contactEmail);
            }
        });
        thread.start();
    }


}