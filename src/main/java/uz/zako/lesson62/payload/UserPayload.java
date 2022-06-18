package uz.zako.lesson62.payload;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserPayload {

    private Long id;
    private String fullName;

    private String phone;

    private String username;

    private String password;

    private Date createdAt;

    public UserPayload(Long id, String fullName, String phone, String username) {
        this.id = id;
        this.fullName = fullName;
        this.phone = phone;
        this.username = username;
    }
}

