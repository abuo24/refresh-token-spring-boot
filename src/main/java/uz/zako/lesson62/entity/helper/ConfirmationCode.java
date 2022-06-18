package uz.zako.lesson62.entity.helper;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import uz.zako.lesson62.entity.User;
import uz.zako.lesson62.entity.base.AbstractEntity;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.ManyToOne;
import java.io.Serializable;
import java.util.Date;


@Entity
@NoArgsConstructor
@AllArgsConstructor
@Data
public class ConfirmationCode extends AbstractEntity implements Serializable {

    private int code;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    private User user;

    private Date expiredAt;

}
