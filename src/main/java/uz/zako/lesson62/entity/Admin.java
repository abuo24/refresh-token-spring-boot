package uz.zako.lesson62.entity;

import lombok.*;

import javax.persistence.Entity;

@EqualsAndHashCode(callSuper = true, exclude = {})
@ToString
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Entity
//@RequiredArgsConstructor
public class Admin extends User {

    private String bio;

}
