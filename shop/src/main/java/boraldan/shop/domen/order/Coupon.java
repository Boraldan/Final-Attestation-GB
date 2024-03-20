package boraldan.shop.domen.order;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import jdk.jfr.Timestamp;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "Coupon")
public class Coupon {

    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

//    @Size(min = 2, max = 100, message = "Имя должно быть от 2 до 20 символов длиной")
    @Column(name = "name")
    private String name;

    @Column(name = "discount")
    private Integer discount;

    @Column(name = "valid")
    private boolean valid;

    @Column(name = "creat_at")
    @Timestamp
    private LocalDateTime creatAt;

    @OneToMany(mappedBy = "coupon")
    private List<Orders> orders;

    @Override
    public String toString() {
        return "Coupon{" +
                "name='" + name + '\'' +
                ", valid=" + valid +
                '}';
    }
}
