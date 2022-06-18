package uz.zako.lesson62.security;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import uz.zako.lesson62.entity.RefreshToken;
import uz.zako.lesson62.entity.User;
import uz.zako.lesson62.payload.auth.AuthPayload;
import uz.zako.lesson62.repository.RefreshTokenRepository;

import java.sql.Ref;
import java.util.Date;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
public class RefreshTokenUtil {

    private final RefreshTokenRepository refreshTokenRepository;
    @Value("${refresh-token.expired.time}")
    private Long refreshExpiredTimeMilly;

    public RefreshToken createRefreshToken(User user) {

        RefreshToken refreshToken = new RefreshToken();
        refreshToken.setRefreshToken(UUID.randomUUID().toString());
        refreshToken.setUser(user);
        refreshToken.setExpiredTime(new Date(new Date().getTime() + refreshExpiredTimeMilly));
        refreshTokenRepository.save(refreshToken);

        return refreshToken;
    }

    public boolean validateRefreshToken(RefreshToken refreshToken) {
        return refreshToken.getExpiredTime().after(new Date());
    }



}

