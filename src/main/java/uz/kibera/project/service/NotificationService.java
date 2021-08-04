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
import uz.kibera.project.mapper.NotificationMapper;

import java.io.File;
import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.Objects;
import java.util.UUID;

@Service
@Slf4j
@RequiredArgsConstructor
public class NotificationService {
    @Value("${files.path}")
    private String fileResource;

    @Value("${files.base-url}")
    private String fileBaseUrl;

    private final PushRepository pushRepository;
    private final NoticeRepository noticeRepository;

    private final NotificationMapper notificationMapper;

    private final FireBaseService fireBaseService;

    public void createPush(final PushRequest pushRequest) {
        Push push = notificationMapper.toNewPushEntity(pushRequest);
        NotificationDto notificationDto = NotificationDto.builder()
                .title(pushRequest.getTitle())
                .content(pushRequest.getContent())
                .imageUrl("")
                .build();
        fireBaseService.processNotification(notificationDto);
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
        pushRepository.save(deletingPush);
    }

    @Transactional(rollbackFor = Exception.class)
    public Push fetchPush(UUID id) {
        return pushRepository.findById(id).orElseThrow(() -> {
            //TODO create pushException
            throw new RuntimeException();
        });
    }

    public String uploadFile(MultipartFile multipartFile) {
        checkMultipartFile(multipartFile);

        String fileName = fileResource.concat(fileName(multipartFile));

        File file = new File(fileName);

        try {
            FileUtils.writeByteArrayToFile(file, multipartFile.getBytes());
        } catch (IOException ex) {
            log.error("Failed to store file into filesystem.", ex);
            throw new RuntimeException();
        }

        log.info("File saved to System storage in {}", new Date());

        return fileName;
    }

    private String fileName(MultipartFile multipartFile) {
        var fileExtension = ".".concat(Objects.requireNonNull(FilenameUtils.getExtension(multipartFile.getOriginalFilename())));
        return UUID.randomUUID().toString().concat(fileExtension);
    }

    void checkMultipartFile(MultipartFile multipartFile) {
        if (ObjectUtils.isEmpty(multipartFile)) {
            log.error("File must not be empty!");
            throw new EmptyFileException();
        }
    }

    @Transactional(rollbackFor = Exception.class)
    public void createNotice(NoticeRequest noticeRequest) {
        if (!ObjectUtils.isEmpty(noticeRequest.getFileName())) {
            noticeRequest.setFileName(fileBaseUrl.concat(noticeRequest.getFileName()));
        }
        Notice notice = notificationMapper.toNewNoticeEntity(noticeRequest);
        final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        LocalDate toDate = LocalDate.from(LocalDate.parse(noticeRequest.getToDate(), formatter));
        LocalDate fromDate = LocalDate.from(LocalDate.parse(noticeRequest.getToDate(), formatter));
        notice.setToDate(toDate);
        notice.setFromDate(fromDate);
        NotificationDto notificationDto = NotificationDto.builder()
                .title(noticeRequest.getTitle())
                .content(noticeRequest.getContent())
                .imageUrl(noticeRequest.getFileName())
                .build();
        fireBaseService.processNotification(notificationDto);
        log.info("Push notification sent!");
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
        noticeRepository.save(deletingNotice);
    }

    @Transactional(rollbackFor = Exception.class)
    public Notice fetchNotice(UUID id) {
        return noticeRepository.findById(id).orElseThrow(() -> {
            //TODO create noticeException
            throw new RuntimeException();
        });
    }
}