package uz.zako.lesson62.controller;

import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;
import springfox.documentation.annotations.ApiIgnore;
import uz.zako.lesson62.entity.RefreshToken;
import uz.zako.lesson62.entity.User;
import uz.zako.lesson62.entity.helper.ConfirmationCode;
import uz.zako.lesson62.exceptions.ResourceNotFoundException;
import uz.zako.lesson62.model.Result;
import uz.zako.lesson62.payload.auth.AuthPayload;
import uz.zako.lesson62.payload.auth.LoginPayload;
import uz.zako.lesson62.repository.ConfirmationCodeRepository;
import uz.zako.lesson62.repository.UserRepository;
import uz.zako.lesson62.security.AuthService;
import uz.zako.lesson62.security.JwtTokenProvider;
import uz.zako.lesson62.security.RefreshTokenUtil;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
@Slf4j
public class AuthController {

    private final UserRepository userRepository;
    private final JwtTokenProvider jwtTokenProvider;
    private final AuthenticationManager authenticationManager;
    private final ConfirmationCodeRepository confirmationCodeRepository;
    private final JavaMailSender javaMailSender;
    private final PasswordEncoder passwordEncoder;
    private final RefreshTokenUtil refreshTokenUtil;
    private final AuthService authService;
    @Value("${code.expiration.time}")
    private long codeExpirationTime;
    @Value("${spring.mail.username}")
    private String fromUser;

    @PostMapping("/login")
//    @ApiIgnore
    @ApiOperation(value = "Login uchun")
    public ResponseEntity<?> login(@RequestBody LoginPayload loginPayload) {

        try {
            return ResponseEntity.ok(authService.createToken(loginPayload));
        } catch (Exception e) {
            log.error("error in Login - {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Result.error(e.getMessage()));
        }

    }

    @PostMapping("/refresh-token")
    public ResponseEntity<?> refreshToken(@RequestBody AuthPayload authPayload) {
        try {
            return ResponseEntity.ok(authService.refreshToken(authPayload));
        } catch (Exception e) {
            log.error("error in Login - {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Result.error(e.getMessage()));
        }
    }

    //    forgot-pass
    @PostMapping("/forgot-password/{username}")
    public ResponseEntity<?> forgotPassword(@PathVariable String username) {
        try {

            User user = userRepository.findByUsernames(username).orElseThrow(() -> new ResourceNotFoundException("user not found"));

            ConfirmationCode confirmationCode = new ConfirmationCode();
            confirmationCode.setUser(user);
            confirmationCode.setCreatedAt(new Date());
            confirmationCode.setExpiredAt(new Date(new Date().getTime() + codeExpirationTime));
            confirmationCode.setCode((int) (Math.random() * 900000 + 100000)); // code - ****** - [1-9]{1}[0-9]{5} // 0.1*900000 // 100000 - 999999
            confirmationCodeRepository.save(confirmationCode);

            try {
                SimpleMailMessage simpleMailMessage = new SimpleMailMessage();
                simpleMailMessage.setFrom(fromUser);
//                simpleMailMessage.setSubject("TEST");
                simpleMailMessage.setTo("shohjahonrahimjonov730@gmail.com");
                simpleMailMessage.setText(String.valueOf(confirmationCode.getCode()));
                javaMailSender.send(simpleMailMessage);
            } catch (Exception e) {
                e.printStackTrace();
            }
            return ResponseEntity.ok(Result.ok(username));
        } catch (Exception e) {
            e.printStackTrace();
            log.error("error - {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Result.error(e.getMessage()));
        }
    }

    //    forgot-pass
    @PostMapping("/check-code/{username}")
    public ResponseEntity<?> checkCode(@PathVariable String username, @RequestParam int code) {
        try {
            User user = userRepository.findByUsernames(username).orElseThrow(() -> new ResourceNotFoundException("user not found"));
            ConfirmationCode confirmationCode = confirmationCodeRepository.findLastAndNotExpirationByUserId(user.getId()).orElseThrow(() -> new ResourceNotFoundException("code not found for this user"));
            if (confirmationCode.getCode() != code) {
                return ResponseEntity.ok(Result.error("code not equal"));
            }
            return ResponseEntity.ok(Result.ok(username));
        } catch (Exception e) {
            log.error("error - {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Result.error(e.getMessage()));
        }
    }

    //    forgot-pass
    @PostMapping("/reset-password/{username}")
    public ResponseEntity<?> resetPassword(@PathVariable String username, @RequestParam String newPassword) {
        try {
            User user = userRepository.findByUsernames(username).orElseThrow(() -> new ResourceNotFoundException("user not found"));
            user.setPassword(passwordEncoder.encode(newPassword));
            userRepository.save(user);
            return ResponseEntity.ok(Result.ok(username));
        } catch (Exception e) {
            log.error("error - {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Result.error(e.getMessage()));
        }
    }


}
