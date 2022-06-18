package uz.zako.lesson62.entity;

import lombok.*;
import uz.zako.lesson62.entity.base.AbstractEntity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.ManyToMany;
import javax.validation.constraints.NotBlank;
import java.util.List;

@EqualsAndHashCode(callSuper = true, exclude = {"roles"})
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Entity(name = "users")
public class User extends AbstractEntity {

    @Column(name = "full_name", length = 50)
    private String fullName;

    @NotBlank(message = "phone is not blank")
    @Column(name = "phone", unique = true, nullable = false)
    private String phone;

    @NotBlank(message = "username is not blank")
    @Column(name = "username", unique = true, nullable = false)
    private String username;

    private String password;

    @ToString.Exclude
    @ManyToMany(fetch = FetchType.EAGER)
    private List<Role> roles;

}
