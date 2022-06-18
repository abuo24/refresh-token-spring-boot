package uz.zako.lesson62.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import uz.zako.lesson62.entity.User;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    @Query(value = "select * from users where username=?1", nativeQuery = true)
    User findByUsername(String username);

    @Query(value = "select * from users where username=?1", nativeQuery = true)
    Optional<User> findByUsernames(String username);

}
