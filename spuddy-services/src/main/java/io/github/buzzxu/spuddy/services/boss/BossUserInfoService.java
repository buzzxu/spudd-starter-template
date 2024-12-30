package io.github.buzzxu.spuddy.services.boss;

import io.github.buzzxu.spuddy.dal.SysUserDao;
import io.github.buzzxu.spuddy.errors.SecurityException;
import io.github.buzzxu.spuddy.redis.Redis;
import io.github.buzzxu.spuddy.security.objects.BossUserInfo;
import io.github.buzzxu.spuddy.security.objects.UserInfo;
import io.github.buzzxu.spuddy.security.services.AbstractUserInfoService;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;

/**
 * @author xux
 * @date 2024年12月30日 21:55:04
 */
@Service
public class BossUserInfoService extends AbstractUserInfoService<BossUserInfo> {

    @Resource
    private SysUserDao sysUserDao;

    public BossUserInfoService() {
        super(BossUserInfo.class);
    }



    @Override
    protected BossUserInfo load(long id, int type) throws SecurityException {
        return sysUserDao.of(id,type).orElse(null);
    }
}
