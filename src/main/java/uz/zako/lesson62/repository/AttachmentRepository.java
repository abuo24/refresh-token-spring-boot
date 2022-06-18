package uz.zako.lesson62.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import uz.zako.lesson62.entity.Attachment;

import java.util.Optional;

@Repository
public interface AttachmentRepository extends JpaRepository<Attachment, Long> {

    //    1
    @Query(value = "select * from attachment a where a.hash_id=:hashId", nativeQuery = true)
    Optional<Attachment> findByHashId(@Param("hashId") String hashId);

    //    2
    //    Optional<Attachment> findByHashId(String hashId);

//    void deleteByHashId(String hashId);

    @Modifying
    @Transactional
    @Query(nativeQuery = true, value = "delete from attachment a where a.hash_id=?1")
    void deleteByHashId(String hashId);

}
