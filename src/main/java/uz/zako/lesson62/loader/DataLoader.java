package uz.zako.lesson62.loader;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import uz.zako.lesson62.entity.Admin;
import uz.zako.lesson62.entity.Role;
import uz.zako.lesson62.entity.User;
import uz.zako.lesson62.repository.AdminRepository;
import uz.zako.lesson62.repository.RoleRepository;
import uz.zako.lesson62.repository.UserRepository;

import java.util.Arrays;
import java.util.List;

@Component
@RequiredArgsConstructor
@Slf4j
public class DataLoader implements CommandLineRunner {

    private final UserRepository userRepository;
    private final AdminRepository adminRepository;
    private final PasswordEncoder passwordEncoder;
    private final RoleRepository roleRepository;
    @Value("${spring.jpa.hibernate.ddl-auto}")
    private String initMode;

    @Override
    public void run(String... args) throws Exception {

        if (initMode.equalsIgnoreCase("create")) {
            try {

                saveAndGetRoles();

//                if (Optional.ofNullable(null).orElse(new String("dd")).equals("s")){}

                User user = new User();
                user.setFullName("Test Testov");
                user.setPassword(passwordEncoder.encode("root"));
                user.setUsername("root");
                user.setPhone("998977778866");
                user.setRoles(Arrays.asList(roleRepository.findByName("ROLE_USER").orElseThrow(() -> new RuntimeException("role not found"))));
                userRepository.save(user);

                Admin admin = new Admin();
                admin.setFullName("Test Testov");
                admin.setPhone("998977778877");
                admin.setPassword(passwordEncoder.encode("admin"));
                admin.setUsername("admin");
                admin.setBio("Bu dastur tomonidan qo'shildi");
                admin.setRoles(roleRepository.findAll());
                adminRepository.save(admin);
            } catch (Exception e) {
                log.error(e.getMessage());
            }
        }
    }

    private List<Role> saveAndGetRoles() {

        Role roleUser = new Role();
        roleUser.setName("ROLE_USER");
        roleUser.setId(1L);

        Role roleAdmin = new Role();
        roleAdmin.setName("ROLE_ADMIN");
        roleAdmin.setId(2L);

        return roleRepository.saveAll(Arrays.asList(roleUser, roleAdmin));

    }
}
