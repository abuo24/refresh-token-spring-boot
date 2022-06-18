package uz.zako.lesson62.payload.auth;

import lombok.Data;

@Data
public class AuthPayload {

    private boolean success;
    private String access_token;
    private String refresh_token;
    private String username;
    private String token_type;

}
