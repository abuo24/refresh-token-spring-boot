package uz.zako.lesson62.security;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import uz.zako.lesson62.entity.RefreshToken;
import uz.zako.lesson62.entity.User;
import uz.zako.lesson62.payload.auth.AuthPayload;
import uz.zako.lesson62.payload.auth.LoginPayload;
import uz.zako.lesson62.repository.RefreshTokenRepository;
import uz.zako.lesson62.repository.UserRepository;

@Service
@RequiredArgsConstructor
@Slf4j
public class AuthService {

    private final UserRepository userRepository;
    private final AuthenticationManager authenticationManager;
    private final JwtTokenProvider jwtTokenProvider;
    private final RefreshTokenUtil refreshTokenUtil;
    private final RefreshTokenRepository refreshTokenRepository;


    public AuthPayload createToken(LoginPayload loginPayload) {
        User user = userRepository.findByUsername(loginPayload.getUsername());
        if (user == null) {
            throw new RuntimeException("Bu usernameli user mavjud emas!");
        }

        authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(loginPayload.getUsername(), loginPayload.getPassword()));
        // create token  -  userga vaqtinchalik token biriktiramiz
        String token = jwtTokenProvider.createToken(user.getUsername(), user.getRoles());
        if (token == null || token.isEmpty() || !StringUtils.hasText(token)) {
            throw new RuntimeException("Iltimos qayta urining biron nima xato ketdi!");
        }
//            refresh tokenni yaratamiz
        RefreshToken refreshToken = refreshTokenUtil.createRefreshToken(user);
//            ---------

        AuthPayload authPayload = new AuthPayload();
        authPayload.setAccess_token(token);
        authPayload.setRefresh_token(refreshToken.getRefreshToken());
        authPayload.setUsername(user.getUsername());
        authPayload.setSuccess(true);
        return authPayload;
    }


    public AuthPayload createTokenByUsername(String username) {
        User user = userRepository.findByUsername(username);
        if (user == null) {
            throw new RuntimeException("Bu usernameli user mavjud emas!");
        }

        // create token  -  userga vaqtinchalik token biriktiramiz
        String token = jwtTokenProvider.createToken(user.getUsername(), user.getRoles());
        if (token == null || token.isEmpty() || !StringUtils.hasText(token)) {
            throw new RuntimeException("Iltimos qayta urining biron nima xato ketdi!");
        }
//            refresh tokenni yaratamiz
        RefreshToken refreshToken = refreshTokenUtil.createRefreshToken(user);
//            ---------

        AuthPayload authPayload = new AuthPayload();
        authPayload.setAccess_token(token);
        authPayload.setRefresh_token(refreshToken.getRefreshToken());
        authPayload.setUsername(user.getUsername());
        authPayload.setSuccess(true);
        return authPayload;
    }


    public AuthPayload refreshToken(AuthPayload authPayload) {
        RefreshToken refreshToken = refreshTokenRepository.findFirstByRefreshTokenOrderByCreatedAtDesc(authPayload.getRefresh_token()).orElseThrow(() -> new RuntimeException("refresh not found"));
        if (!refreshTokenUtil.validateRefreshToken(refreshToken)) {
            throw new RuntimeException("refresh_token is expired");
        }
        return createTokenByUsername(refreshToken.getUser().getUsername());
    }

}
