package uz.zako.lesson62.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import uz.zako.lesson62.entity.helper.ConfirmationCode;

import java.util.Optional;

@Repository
public interface ConfirmationCodeRepository extends JpaRepository<ConfirmationCode, Long> {

    @Query(nativeQuery = true,
            value = "select * from confirmation_code where user_id=? and expired_at>now() order by created_at desc limit 1")
    Optional<ConfirmationCode> findLastAndNotExpirationByUserId(Long userId);

}
