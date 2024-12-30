package io.github.buzzxu.spuddy.configure;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.github.buzzxu.spuddy.jackson.Jackson;
import io.github.buzzxu.spuddy.security.OrganizationService;
import io.github.buzzxu.spuddy.security.UserService;
import io.github.buzzxu.spuddy.security.jwt.JWTs;
import io.github.buzzxu.spuddy.security.services.StandardOrganization;
import io.github.buzzxu.spuddy.security.services.StandardUser;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Lazy;
import org.springframework.context.annotation.Primary;

/**
 * @author xux
 * @date 2024年12月30日 17:18:21
 */
@Configuration
public class GlobalConfigure {




    @Bean
    @Lazy
    public UserService userService(){
        return new StandardUser();
    }

    @Bean
    @Lazy
    public OrganizationService organizationService(){
        return new StandardOrganization();
    }


}
