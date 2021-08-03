package uz.kibera.project.service;

import java.time.LocalDateTime;
import java.util.UUID;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import uz.kibera.project.dao.entity.Push;
import uz.kibera.project.dao.repository.PushRepository;
import uz.kibera.project.dto.PushDto;
import uz.kibera.project.dto.PushRequest;
import uz.kibera.project.mapper.NotificationMapper;

@Service
@Slf4j
@RequiredArgsConstructor
public class NotificationService {
    private final PushRepository pushRepository;
    private final NotificationMapper notificationMapper;

    private final FireBaseService fireBaseService;

    public void createPush(final PushRequest pushRequest) {
        Push push = notificationMapper.toNewPushEntity(pushRequest);
        fireBaseService.processNotification(pushRequest);
        log.info("Push notification sent!");
        pushRepository.save(push);
    }

    public Page<PushDto> fetchAllPushes(Pageable pageable) {
        Page<Push> allPushes = pushRepository.findAllByDeletedIsFalse(pageable);
        return allPushes.map(notificationMapper::toPushDto);
    }

    public void deletePush(UUID id) {
        Push deletingPush = fetchPush(id);
        deletingPush.setDeleted(true);
        deletingPush.setDeletedAt(LocalDateTime.now());
    }

    @Transactional(rollbackFor = Exception.class)
    public Push fetchPush(UUID id) {
        return pushRepository.findById(id).orElseThrow(() ->{
            //TODO create pushException
            throw new RuntimeException();
        });
    }
}
