package uz.kibera.project.service;

import java.util.List;
import java.util.Map;

import static uz.kibera.project.common.FireBaseConst.FIREBASE_TOKEN_CONST;

import com.google.common.collect.Lists;
import com.google.firebase.messaging.MulticastMessage;
import com.google.firebase.messaging.Notification;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.ObjectUtils;
import org.springframework.stereotype.Service;
import uz.kibera.project.dto.NoticeRequest;
import uz.kibera.project.dto.NotificationDto;
import uz.kibera.project.dto.PushDto;
import uz.kibera.project.dto.PushRequest;
import uz.kibera.project.service.pushsender.MulticastPushSender;

@Service
@Slf4j
@RequiredArgsConstructor
public class FireBaseService {
    private final MulticastPushSender pushSender;

    public void processNotification(NotificationDto dto) {
        pushSender.send(createMulticastMessage(dto));
    }

    private MulticastMessage createMulticastMessage(NotificationDto dto) {

        Notification notification = Notification.builder()
                .setTitle(dto.getTitle())
                .setBody(dto.getContent())
                .setImage(dto.getImageUrl())
                .build();

        return MulticastMessage.builder()
                .addToken(FIREBASE_TOKEN_CONST)
                .setNotification(notification)
                .putAllData(Map.of("Hello", "World!"))
                .build();
    }
}
