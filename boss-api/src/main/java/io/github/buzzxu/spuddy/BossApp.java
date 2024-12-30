package io.github.buzzxu.spuddy;



import io.github.buzzxu.spuddy.spring.boot.SpringBootApp;
import io.github.buzzxu.spuddy.spring.boot.SpuddySpringBoot;
import org.springframework.boot.web.servlet.ServletComponentScan;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

/**
 * @author xux
 * @date 2024年12月29日 16:00:50
 */
@EnableWebMvc
@ServletComponentScan
@SpuddySpringBoot
public class BossApp {

    public static void main(String[] args) {
        new SpringBootApp().run(BossApp.class,args);
    }
}
