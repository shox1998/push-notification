package uz.kibera.project.configuration.properties;

import javax.validation.Valid;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConfigurationProperties(prefix = "firebase")
@Valid
@Getter
@Setter
public class FirebaseProperties {
    private String clientToken;
}
