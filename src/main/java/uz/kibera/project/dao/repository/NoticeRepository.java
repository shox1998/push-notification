package uz.kibera.project.dao.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import uz.kibera.project.dao.entity.Notice;

import java.util.UUID;
@Repository
public interface NoticeRepository  extends JpaRepository<Notice, UUID> {
    Page<Notice> findAllByDeletedIsFalse(Pageable pageable);
}
