package uz.kibera.project.dao.repository;

import java.util.Optional;
import java.util.UUID;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import uz.kibera.project.dao.entity.Notice;
import uz.kibera.project.dao.entity.Push;

@Repository
public interface PushRepository extends JpaRepository<Push, UUID> {
    Page<Push> findAllByDeletedIsFalse(Pageable pageable);
    Optional<Push> findByIdAndDeletedIsFalse(UUID id);
}
