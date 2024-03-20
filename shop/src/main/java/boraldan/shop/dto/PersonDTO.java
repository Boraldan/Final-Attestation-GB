package boraldan.shop.dto;

import jakarta.persistence.Column;
import jakarta.validation.constraints.*;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Data
public class PersonDTO {

    private int id;

    @NotEmpty(message = "Имя не должно быть пустым")
    @Size(min = 2, max = 100, message = "Имя должно быть от 2 до 100 символов длиной")
    @Column(name = "name")
    private String name;

    @NotNull
    @Min(value = 0, message = "Минимальный возраст от 0")
    @Max(value = 130, message = "Максимальный возраст до 130")
    @Column(name = "age")
    private Integer age;

    @NotNull
    @Column(name = "card")
    private Long card;

    @NotNull
    @Column(name = "phone")
    private Long phone;

    @NotEmpty(message = "Не должно быть пустым")
    @Email
    @Column(name = "email")
    private String email;
}
