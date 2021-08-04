package uz.kibera.project.service.pushsender;

import com.google.firebase.FirebaseException;
import com.google.firebase.messaging.BatchResponse;
import com.google.firebase.messaging.FirebaseMessaging;
import com.google.firebase.messaging.FirebaseMessagingException;
import com.google.firebase.messaging.MulticastMessage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import uz.kibera.project.exception.CustomFirebaseException;

@Slf4j
@Service
@RequiredArgsConstructor
public class MulticastPushSender {
    private final FirebaseMessaging firebaseMessaging;

    public void send(MulticastMessage message) {
        try {
            BatchResponse response = firebaseMessaging.sendMulticast(message);
            response.getResponses().forEach(res -> {
                String logMsg;
                if (res.isSuccessful()) {
                    logMsg = "Successfully sent push notification. Message id: " + res.getMessageId();
                    log.info(logMsg);
                } else {
                    logMsg = "Failed to send push notification. Reason: " + res.getException().getLocalizedMessage();
                    log.warn(logMsg);
                }
            });
        } catch (FirebaseMessagingException e) {
            log.error("Failed to send push notification. Reason: " + e.getLocalizedMessage());
            throw new CustomFirebaseException(e.getLocalizedMessage());
        }
    }
}
