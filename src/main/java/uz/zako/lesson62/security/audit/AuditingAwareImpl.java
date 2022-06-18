package uz.zako.lesson62.security.audit;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.AuditorAware;
import uz.zako.lesson62.security.SecurityUtils;

import java.util.Optional;

@RequiredArgsConstructor
public class AuditingAwareImpl implements AuditorAware<String> {

    @Override
    public Optional<String> getCurrentAuditor() {
        return SecurityUtils.getCurrentUsername();
    }

}
