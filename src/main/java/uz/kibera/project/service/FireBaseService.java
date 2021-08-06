package uz.kibera.project.service;

import com.google.firebase.messaging.MulticastMessage;
import com.google.firebase.messaging.Notification;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import uz.kibera.project.configuration.properties.FirebaseProperties;
import uz.kibera.project.dto.NotificationDto;
import uz.kibera.project.service.pushsender.MulticastPushSender;

@Service
@Slf4j
@RequiredArgsConstructor
public class FireBaseService {
    private final MulticastPushSender pushSender;
    private final FirebaseProperties firebaseProperties;

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
                .addToken(firebaseProperties.getClientToken())
                .setNotification(notification)
                .build();
    }
}
