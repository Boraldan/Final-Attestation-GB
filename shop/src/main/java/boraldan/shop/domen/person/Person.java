package boraldan.shop.domen.person;

import boraldan.shop.domen.order.Orders;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.CacheConcurrencyStrategy;


import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "person")
public class Person {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
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

    @Column(name = "role")
    private String role;

    @NotEmpty(message = "Не должно быть пустым")
    @Column(name = "password")
    private String password;

    @OneToMany(mappedBy = "person")
    private List<Orders> orders;

    public String infoPerson() {
        return "id%d %s  %s".formatted(id, name, email);
    }

    @Override
    public String toString() {
        return "Person{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", age=" + age +
                ", card=" + card +
                ", phone=" + phone +
                ", email='" + email + '\'' +
                ", role='" + role + '\'' +
                ", password='" + password + '\'' +
                '}';
    }
}
