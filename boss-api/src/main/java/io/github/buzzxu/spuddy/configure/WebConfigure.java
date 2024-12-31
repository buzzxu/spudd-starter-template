package io.github.buzzxu.spuddy.configure;

import io.github.buzzxu.spuddy.jackson.Jackson;
import io.github.buzzxu.spuddy.security.ShiroJwtHandler;
import io.github.buzzxu.spuddy.security.handler.SecurityUserHandler;
import io.github.buzzxu.spuddy.security.objects.BossUserInfo;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.StringHttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.servlet.config.annotation.ContentNegotiationConfigurer;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurationSupport;

import java.nio.charset.StandardCharsets;
import java.util.List;

/**
 * @author xux
 * @date 2024年12月30日 22:26:26
 */
@Configuration
public class WebConfigure extends WebMvcConfigurationSupport {

    @Bean
    public SecurityUserHandler<BossUserInfo> jwtHandler(){
        return new ShiroJwtHandler();
    }

    @Override
    protected void configureContentNegotiation(ContentNegotiationConfigurer configurer) {
        configurer.defaultContentType(MediaType.APPLICATION_JSON);
    }
    @Override
    protected void configureMessageConverters(List<HttpMessageConverter<?>> converters) {
        converters.add(new MappingJackson2HttpMessageConverter(Jackson.of()));
        converters.add(new StringHttpMessageConverter(StandardCharsets.UTF_8));
        super.configureMessageConverters(converters);
    }

    @Override
    protected void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler("/static/**").addResourceLocations("classpath:/static/");
    }


}
