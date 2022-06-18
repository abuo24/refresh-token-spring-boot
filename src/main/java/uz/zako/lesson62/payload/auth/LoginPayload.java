package uz.zako.lesson62.payload.auth;

import lombok.Data;

@Data
public class LoginPayload {
    private String username;
    private String password;
}
