package io.github.buzzxu.spuddy.controllers;

import io.github.buzzxu.spuddy.controllers.responses.LoginResponse;
import io.github.buzzxu.spuddy.objects.Pair;
import io.github.buzzxu.spuddy.security.boss.controllers.BossLoginController;
import io.github.buzzxu.spuddy.security.objects.BossUserInfo;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author xux
 * @date 2024年12月29日 19:00:10
 */
@RestController("bossLoginController")
public class LoginController extends BossLoginController<BossUserInfo> {
    public LoginController() {
        super(BossUserInfo.class);
    }

    @Override
    protected Object loginInfo(Pair<String, BossUserInfo> user) {
        BossUserInfo $user = user.getValue();
        LoginResponse val = new LoginResponse();
        val.setUserName($user.getUserName());
        val.setNickName($user.getNickName());
        val.setToken(user.getKey());
        val.setRealName($user.getRealName());
        val.setAvatar($user.getAvatar());
        return val;
    }
}
