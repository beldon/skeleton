package skeleton.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.Ordered;
import skeleton.repo.RememberMeTokenAutoRepo;
import skeleton.repo.UserAutoRepo;
import skeleton.web.security.SecurityFilter;
import skeleton.web.security.remember.PersistentRememberMeService;
import skeleton.web.security.remember.RememberMeFilter;
import skeleton.web.security.remember.RememberMeService;

/**
 * @author Beldon
 */
@Configuration
public class SecurityConfig {

    private final RememberMeTokenAutoRepo rememberMeTokenAutoRepo;
    private final UserAutoRepo userAutoRepo;

    @Autowired
    public SecurityConfig(RememberMeTokenAutoRepo rememberMeTokenAutoRepo, UserAutoRepo userAutoRepo) {
        this.rememberMeTokenAutoRepo = rememberMeTokenAutoRepo;
        this.userAutoRepo = userAutoRepo;
    }

    @Bean
    public FilterRegistrationBean securityFilter() {
        FilterRegistrationBean<SecurityFilter> registrationBean = new FilterRegistrationBean<>(new SecurityFilter());
        registrationBean.addUrlPatterns("/*");
        registrationBean.setOrder(Ordered.HIGHEST_PRECEDENCE);
        return registrationBean;
    }

    @Bean
    public FilterRegistrationBean rememberMeFilter() {
        RememberMeFilter rememberMeFilter = new RememberMeFilter(rememberMeService());
        FilterRegistrationBean<RememberMeFilter> registrationBean = new FilterRegistrationBean<>(rememberMeFilter);
        registrationBean.addUrlPatterns("/*");
        registrationBean.setOrder(Ordered.HIGHEST_PRECEDENCE + 1);
        return registrationBean;
    }

    @Bean
    public RememberMeService rememberMeService() {
        return new PersistentRememberMeService(rememberMeTokenAutoRepo, userAutoRepo);
    }

}
