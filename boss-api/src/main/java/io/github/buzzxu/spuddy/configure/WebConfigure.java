package io.github.buzzxu.spuddy.configure;

import io.github.buzzxu.spuddy.security.ShiroJwtHandler;
import io.github.buzzxu.spuddy.security.handler.SecurityUserHandler;
import io.github.buzzxu.spuddy.security.objects.BossUserInfo;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author xux
 * @date 2024年12月30日 22:26:26
 */
@Configuration
public class WebConfigure {
    @Bean
    public SecurityUserHandler<BossUserInfo> jwtHandler(){
        return new ShiroJwtHandler();
    }
}
