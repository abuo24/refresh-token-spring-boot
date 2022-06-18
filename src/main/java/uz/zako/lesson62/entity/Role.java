package uz.zako.lesson62.entity;

import lombok.*;
import org.springframework.security.core.GrantedAuthority;
import uz.zako.lesson62.entity.base.AbstractEntity;

import javax.persistence.Entity;


@EqualsAndHashCode(callSuper = true, exclude = {})
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Entity
public class Role extends AbstractEntity implements GrantedAuthority {

    private String name;

    @Override
    public String getAuthority() {
        return name;
    }

}
