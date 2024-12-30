package io.github.buzzxu.spuddy.configure.boss;

import io.github.buzzxu.spuddy.dal.SysUserDao;
import io.github.buzzxu.spuddy.security.MenuService;
import io.github.buzzxu.spuddy.security.RoleMenuService;
import io.github.buzzxu.spuddy.security.RoleService;
import io.github.buzzxu.spuddy.security.services.StandardMenu;
import io.github.buzzxu.spuddy.security.services.StandardRole;
import io.github.buzzxu.spuddy.security.services.StandardRoleMenuService;
import io.github.buzzxu.spuddy.util.Captcha;
import io.github.buzzxu.spuddy.util.Random;
import org.jdbi.v3.core.Jdbi;
import org.patchca.filter.predefined.WobbleRippleFilterFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Lazy;

/**
 * @author xux
 * @date 2024年12月30日 17:21:16
 */
@Configuration
public class BossConfigure {


    /**
     * 验证码
     * @return
     */
    @Bean
    public Captcha captcha(){
        return new Captcha.Builder().wordFactory(() -> Random.numeric(4)).filterFactory(new WobbleRippleFilterFactory()).color("#1529AD").build();
    }

    @Bean
    @Lazy
    public MenuService menuService(){
        return new StandardMenu();
    }

    @Bean
    @Lazy
    public RoleMenuService roleMenuService(RoleService roleService,MenuService menuService){
        return new StandardRoleMenuService(menuService, roleService);
    }
    @Bean
    @Lazy
    public RoleService roleService(){
        return new StandardRole();
    }

    /***************************  dal ************************************************/
    @Bean
    public SysUserDao sysUserDao(Jdbi jdbi){
        return jdbi.onDemand(SysUserDao.class);
    }
}
