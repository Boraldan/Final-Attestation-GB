package boraldan.shop.domen;


import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import lombok.Getter;

//   для этих полей th:field="*{fName}"  th:errors="*{fName}"  в thymeleaf нужны обычные гетеры и сетеры.
//   от  Lombok не работают
public class ContactEmail {

    @NotEmpty(message = "Имя не должно быть пустым")
    @Size(min = 2, max = 100, message = "Имя должно быть от 2 до 20 символов длиной")
    private String fName;

    @NotEmpty(message = "Не должно быть пустым")
    @Size(min = 2, max = 100, message = "Должно быть от 2 до 20 символов длиной")
    private String lName;

    @NotEmpty(message = "Не должно быть пустым")
    @Email
    private String emailGuest;

    @NotEmpty(message = "Не должно быть пустым")
    @Size(min = 2, max = 100, message = "Должно быть от 5 до 100 символов длиной")
    private String message;

    public String getfName() {
        return fName;
    }

    public void setfName(String fName) {
        this.fName = fName;
    }

    public String getlName() {
        return lName;
    }

    public void setlName(String lName) {
        this.lName = lName;
    }

    public String getEmailGuest() {
        return emailGuest;
    }

    public void setEmailGuest(String emailGuest) {
        this.emailGuest = emailGuest;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    @Override
    public String toString() {
        return "ContactEmail{" +
                "fName='" + fName + '\'' +
                ", lName='" + lName + '\'' +
                ", emailGuest='" + emailGuest + '\'' +
                ", message='" + message + '\'' +
                '}';
    }

    public String infoContactEmail() {
        return "%s %s\n%s\n%s".formatted(fName, lName, emailGuest, message);

    }
}
