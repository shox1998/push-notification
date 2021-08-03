package uz.kibera.project.configuration;

import java.io.IOException;
import java.io.InputStream;

import com.google.auth.oauth2.GoogleCredentials;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import com.google.firebase.messaging.FirebaseMessaging;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;

@Configuration
public class FireBaseConfiguration {
        @Value("${davrpay.fcm.credentials}")
        private String credentialsPath;

        @Bean
        public FirebaseMessaging firebaseMessaging() {

            try (InputStream serviceAccount = new ClassPathResource(credentialsPath).getInputStream()) {
                FirebaseOptions options = FirebaseOptions.builder()
                        .setCredentials(GoogleCredentials.fromStream(serviceAccount))
                        .build();
                if (FirebaseApp.getApps().isEmpty()) {
                    FirebaseApp.initializeApp(options);
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
            return FirebaseMessaging.getInstance();

        }
}
