package uz.kibera.project.service;

import java.util.List;
import java.util.Map;

import static uz.kibera.project.common.FireBaseConst.FIREBASE_TOKEN_CONST;

import com.google.common.collect.Lists;
import com.google.firebase.messaging.MulticastMessage;
import com.google.firebase.messaging.Notification;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import uz.kibera.project.dto.PushDto;
import uz.kibera.project.dto.PushRequest;
import uz.kibera.project.service.pushsender.MulticastPushSender;

@Service
@Slf4j
@RequiredArgsConstructor
public class FireBaseService {
    private final MulticastPushSender pushSender;

    public void processNotification(PushRequest dto) {
        pushSender.send(createMulticastMessage(dto));
    }

    private MulticastMessage createMulticastMessage(PushRequest dto) {
        return MulticastMessage.builder()
                .addToken(FIREBASE_TOKEN_CONST)
                .setNotification(Notification.builder()
                        .setTitle(dto.getTitle())
                        .setBody(dto.getContent())
                        .build())
                .putAllData(Map.of("Hello", "World!"))
                .build();
    }

}
