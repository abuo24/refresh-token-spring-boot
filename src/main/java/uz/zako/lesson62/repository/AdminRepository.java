package uz.zako.lesson62.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import uz.zako.lesson62.entity.Admin;

@Repository
public interface AdminRepository extends JpaRepository<Admin, Long> {

}
