package kz.yerakh.animaltrackerservice.config;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConfigurationProperties(prefix = "spring.liquibase")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class LiquibaseProperties {

    private String changeLog;
    private boolean enabled;
}
