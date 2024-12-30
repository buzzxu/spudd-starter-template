package io.github.buzzxu.spuddy.services;

import com.auth0.jwt.JWTCreator;
import com.google.common.base.Strings;
import io.github.buzzxu.spuddy.security.objects.UserInfo;
import io.github.buzzxu.spuddy.security.services.AbstractAccountService;
import io.github.buzzxu.spuddy.util.Replaces;
import org.springframework.stereotype.Service;

import java.util.function.Supplier;

/**
 * @author xux
 * @date 2024年12月30日 22:51:44
 */
@Service
public class AccountService extends AbstractAccountService {
    @Override
    protected <U extends UserInfo> void writeInfoToToken(JWTCreator.Builder builder, U u) {
        builder.withClaim("mobile", Strings.isNullOrEmpty(u.getMobile()) ? "" : Replaces.mobile(u.getMobile()));
    }

    @Override
    protected <U extends UserInfo> Supplier<String> jwtSecretSupplier(U userInfo) {
        if (!Strings.isNullOrEmpty(userInfo.getPassword())){
            return userInfo::getPassword;
        }
        return ()-> !Strings.isNullOrEmpty(userInfo.getOAuthUser().getOAuthId()) ? userInfo.getOAuthUser().getOAuthId() : "111111";
    }
}
