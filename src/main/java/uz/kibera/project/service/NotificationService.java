package uz.kibera.project.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.lang3.ObjectUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import uz.kibera.project.dao.entity.Notice;
import uz.kibera.project.dao.entity.Push;
import uz.kibera.project.dao.repository.NoticeRepository;
import uz.kibera.project.dao.repository.PushRepository;
import uz.kibera.project.dto.*;
import uz.kibera.project.exception.EmptyFileException;
import uz.kibera.project.exception.NoticeNotFoundException;
import uz.kibera.project.exception.PushNotFoundException;
import uz.kibera.project.mapper.NotificationMapper;

import java.io.File;
import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.Objects;
import java.util.UUID;

import static uz.kibera.project.common.FireBaseConst.STATIC_IMAGE_URL;

@Service
@Slf4j
@RequiredArgsConstructor
public class NotificationService {
    private final PushRepository pushRepository;
    private final NoticeRepository noticeRepository;

    private final NotificationMapper notificationMapper;

    private final FireBaseService fireBaseService;

    @Transactional(rollbackFor = Exception.class)
    public void createPush(final PushRequest pushRequest) {
        Push push = notificationMapper.toNewPushEntity(pushRequest);
        NotificationDto notificationDto = NotificationDto.builder()
                .title(pushRequest.getTitle())
                .content(pushRequest.getContent())
                .build();
        fireBaseService.processNotification(notificationDto);
        log.info("Push notification sent! And saved.");
        pushRepository.save(push);
    }

    @Transactional(rollbackFor = Exception.class)
    public Page<PushDto> fetchAllPushes(Pageable pageable) {
        Page<Push> allPushes = pushRepository.findAllByDeletedIsFalse(pageable);
        log.info("All Pushes retrieved.");
        return allPushes.map(notificationMapper::toPushDto);
    }

    @Transactional(rollbackFor = Exception.class)
    public void deletePush(UUID id) {
        Push deletingPush = fetchPush(id);
        deletingPush.setDeleted(true);
        deletingPush.setDeletedAt(LocalDateTime.now());
        log.info("Deleted Push with {} id at {}", id, deletingPush.getDeletedAt());
        pushRepository.save(deletingPush);
    }

    @Transactional(rollbackFor = Exception.class)
    public Push fetchPush(UUID id) {
        return pushRepository.findByIdAndDeletedIsFalse(id).orElseThrow(() -> {
            throw new PushNotFoundException();
        });
    }

    @Transactional(rollbackFor = Exception.class)
    public PushDto updatePush(PushRequest pushRequest, UUID pushId) {
        Push updatablePush = fetchPush(pushId);
        notificationMapper.updatePush(updatablePush, pushRequest);
        log.info("Updated Push with {} id", pushId);
        NotificationDto notificationDto = NotificationDto.builder()
                .title(pushRequest.getTitle())
                .content(pushRequest.getContent())
                .build();
        fireBaseService.processNotification(notificationDto);
        log.info("Push notification sent! And saved.");
        return notificationMapper.toPushDto(pushRepository.save(updatablePush));
    }

//    public String uploadFile(MultipartFile multipartFile) {
//        checkMultipartFile(multipartFile);
//
//        String fileName = fileResource.concat(fileName(multipartFile));
//
//        File file = new File(fileName);
//
//        try {
//            FileUtils.writeByteArrayToFile(file, multipartFile.getBytes());
//        } catch (IOException ex) {
//            log.error("Failed to store file into filesystem.", ex);
//            throw new RuntimeException();
//        }
//
//        log.info("File saved to System storage in {}", new Date());
//
//        return fileName;
//    }

//    private String fileName(MultipartFile multipartFile) {
//        var fileExtension = ".".concat(Objects.requireNonNull(FilenameUtils.getExtension(multipartFile.getOriginalFilename())));
//        return UUID.randomUUID().toString().concat(fileExtension);
//    }
//
//    void checkMultipartFile(MultipartFile multipartFile) {
//        if (ObjectUtils.isEmpty(multipartFile)) {
//            log.error("File must not be empty!");
//            throw new EmptyFileException();
//        }
//    }

    @Transactional(rollbackFor = Exception.class)
    public void createNotice(NoticeRequest noticeRequest) {
        Notice notice = notificationMapper.toNewNoticeEntity(noticeRequest);
//        final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
//        LocalDate toDate = LocalDate.from(LocalDate.parse(noticeRequest.getToDate(), formatter));
//        LocalDate fromDate = LocalDate.from(LocalDate.parse(noticeRequest.getToDate(), formatter));
//        notice.setToDate(toDate);
//        notice.setFromDate(fromDate);
        NotificationDto notificationDto = NotificationDto.builder()
                .title(noticeRequest.getTitle())
                .content(noticeRequest.getContent())
                .build();
        fireBaseService.processNotification(notificationDto);
        log.info("Notice notification sent! And saved.");
        noticeRepository.save(notice);
    }

    @Transactional(rollbackFor = Exception.class)
    public Page<NoticeDto> fetchAllNotices(Pageable pageable) {
        Page<Notice> allNotices = noticeRepository.findAllByDeletedIsFalse(pageable);
        return allNotices.map(notificationMapper::toNoticeDto);
    }

    @Transactional(rollbackFor = Exception.class)
    public void deleteNotice(UUID id) {
        Notice deletingNotice = fetchNotice(id);
        deletingNotice.setDeleted(true);
        deletingNotice.setDeletedAt(LocalDateTime.now());
        log.info("Deleted Notice with {} id at {}", id, deletingNotice.getDeletedAt());
        noticeRepository.save(deletingNotice);
    }

    @Transactional(rollbackFor = Exception.class)
    public Notice fetchNotice(UUID id) {
        return noticeRepository.findByIdAndDeletedIsFalse(id).orElseThrow(() -> {

            throw new NoticeNotFoundException();
        });
    }
    @Transactional(rollbackFor = Exception.class)
    public NoticeDto updateNotice(NoticeRequest noticeRequest, UUID noticeId) {
        Notice updatableNotice = fetchNotice(noticeId);
        notificationMapper.updateNotice(updatableNotice, noticeRequest);
        log.info("Updated Notice with {} id", noticeId);
//        if (!ObjectUtils.isEmpty(noticeRequest.getFileName())) {
//            noticeRequest.setFileName(STATIC_IMAGE_URL);
//        }
//        final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
//        LocalDate toDate = LocalDate.from(LocalDate.parse(noticeRequest.getToDate(), formatter));
//        LocalDate fromDate = LocalDate.from(LocalDate.parse(noticeRequest.getToDate(), formatter));
//        updatableNotice.setFromDate(fromDate);
//        updatableNotice.setToDate(toDate);
        NotificationDto notificationDto = NotificationDto.builder()
                .title(noticeRequest.getTitle())
                .content(noticeRequest.getContent())
                .build();
        fireBaseService.processNotification(notificationDto);
        log.info("Notice notification sent! And saved.");
        return notificationMapper.toNoticeDto(noticeRepository.save(updatableNotice));
    }
}