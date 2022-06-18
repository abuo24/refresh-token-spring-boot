package uz.zako.lesson62.entity;

import lombok.*;
import uz.zako.lesson62.entity.base.AbstractEntity;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.Date;
import java.util.List;


@EqualsAndHashCode(callSuper = true)
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Entity(name = "refresh_token")
public class RefreshToken extends AbstractEntity {

//    @NotNull(message = "refresh_token must not be null")
    @Column(name = "refresh_token")
    private String refreshToken;

    @ManyToOne(fetch = FetchType.LAZY)
    private User user;

    private Date expiredTime;

}
