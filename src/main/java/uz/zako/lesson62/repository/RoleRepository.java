package uz.zako.lesson62.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import uz.zako.lesson62.entity.Role;

import java.util.Optional;

public interface RoleRepository extends JpaRepository<Role, Long> {

    @Query(nativeQuery = true, value = "select * from role r where r.name=?1")
    Optional<Role> findByName(String name);
}
