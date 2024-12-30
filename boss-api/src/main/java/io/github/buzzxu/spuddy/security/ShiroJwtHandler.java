package io.github.buzzxu.spuddy.security;

import io.github.buzzxu.spuddy.dal.SysUserDao;
import io.github.buzzxu.spuddy.errors.SecurityException;
import io.github.buzzxu.spuddy.security.boss.BossJwtHandler;
import io.github.buzzxu.spuddy.security.exceptions.TokenAuthException;
import io.github.buzzxu.spuddy.security.objects.BossUserInfo;
import lombok.RequiredArgsConstructor;

import java.util.Optional;

/**
 * @author xux
 * @date 2024年12月30日 21:49:51
 */
@RequiredArgsConstructor
public class ShiroJwtHandler extends BossJwtHandler<BossUserInfo> {

    @Override
    protected boolean tokenIdentity(String token) throws TokenAuthException {
        return true;
    }

    @Override
    public Optional<BossUserInfo> getUser(long userId,int type) throws SecurityException {
        return Optional.of(userInfoService.of(userId,type,BossUserInfo.class));
    }
}
