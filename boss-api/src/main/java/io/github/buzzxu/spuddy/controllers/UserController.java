package io.github.buzzxu.spuddy.controllers;

import io.github.buzzxu.spuddy.R;
import io.github.buzzxu.spuddy.controllers.requests.user.ChangePasswordRequest;
import io.github.buzzxu.spuddy.controllers.requests.user.EditUserRequest;
import io.github.buzzxu.spuddy.controllers.system.SysVoMapStructs;
import io.github.buzzxu.spuddy.controllers.system.responses.SysUserInfoResponse;
import io.github.buzzxu.spuddy.errors.LockedAccountException;
import io.github.buzzxu.spuddy.redis.Redis;
import io.github.buzzxu.spuddy.security.*;
import io.github.buzzxu.spuddy.security.boss.GetUser;
import io.github.buzzxu.spuddy.security.objects.BossUserInfo;
import io.github.buzzxu.spuddy.security.objects.Menu;
import lombok.RequiredArgsConstructor;
import org.apache.shiro.SecurityUtils;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @author xux
 * @date 2024年12月31日 18:32:26
 */
@RequiredArgsConstructor
@RestController
@RequestMapping( "/user")
public class UserController {

    private final UserService userService;
    private final MenuService menuService;
    private final AccountService accountService;
    private final UserInfoService<BossUserInfo> userInfoService;
    private final Redis redis;

    @GetMapping("/menus")
    public R<List<Menu>> menus() throws SecurityException {
        BossUserInfo user = GetUser.of();
        return R.of(menuService.getTreeByRoleId(user.getRoleId()));
    }


    @GetMapping("/info")
    public R<SysUserInfoResponse> info(){
        BossUserInfo user = GetUser.of();
        return R.of(SysVoMapStructs.INSTANCE.to(user));
    }
    /**
     * 密码修改
     * @param request
     * @return
     * @throws SecurityException
     * @throws LockedAccountException
     */
    @PostMapping("/password/change")
    public R<String> editPassword(@RequestBody ChangePasswordRequest request) throws SecurityException, LockedAccountException {
        BossUserInfo user = GetUser.of();
        return R.of(accountService.changePassword(user.getId(),request.getOldPassword(),request.getNewPassword(),userId->userInfoService.of(userId,BossUserInfo.class)).getKey());
    }

    /**
     * 修改信息
     * @param request
     * @return
     */
    @PostMapping("/edit")
    public R<Boolean> edit(@RequestBody EditUserRequest request){
        BossUserInfo user = GetUser.of();
        BossUserInfo upObj = new BossUserInfo();
        upObj.setId(user.getId());
        upObj.setMobile(request.getMobile());
        upObj.setRealName(request.getRealName());
        upObj.setEmail(request.getEmail());
        try {
            return R.of(userService.editUser(upObj,_user-> true));
        }finally {
            redis.execute(redis-> redis.del(KEY.USER_INFO.to(user.getId())));
        }
    }

}
