package uz.zako.lesson62.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import uz.zako.lesson62.entity.User;
import uz.zako.lesson62.exceptions.ResourceNotFoundException;
import uz.zako.lesson62.payload.UserPayload;
import uz.zako.lesson62.repository.UserRepository;
import uz.zako.lesson62.security.SecurityUtils;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/user")
public class UserController {
    private final UserRepository userRepository;

    @GetMapping("/me")
    public User getCurrentUser() {
//        System.out.println(SecurityUtils.getCurrentUsername());
//        if (userRepository.findByUsername("root2") == null) {
//            throw new ResourceNotFoundException("user not found");
//        };
        return userRepository.findByUsername(SecurityUtils.getCurrentUsername().orElseThrow(() -> new ResourceNotFoundException("user not found")));
    }

    @PutMapping("/")
    public User editCurrentUser(
            @RequestBody UserPayload userPayload
    ) {
        User user = userRepository.findByUsername(SecurityUtils.getCurrentUsername().orElseThrow(() -> new ResourceNotFoundException("user not found")));
        user.setPhone(userPayload.getPhone());
        return userRepository.save(user);
    }

}

