package uz.zako.lesson62.security.audit;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.data.domain.AuditorAware;
import org.springframework.stereotype.Component;

@RequiredArgsConstructor
@Component
public class AuditingConfig {

    @Bean
    public AuditorAware<String> auditorAware() {
        return new AuditingAwareImpl();
    }

}