package io.github.buzzxu.spuddy.controllers.system;

import com.google.common.base.Strings;
import com.google.common.collect.Maps;
import io.github.buzzxu.spuddy.R;
import io.github.buzzxu.spuddy.controllers.system.requests.CreateOrUpdateSysteUserRequest;
import io.github.buzzxu.spuddy.controllers.system.responses.SysUserInfoResponse;
import io.github.buzzxu.spuddy.objects.Pager;
import io.github.buzzxu.spuddy.security.boss.GetUser;
import io.github.buzzxu.spuddy.security.objects.BossUserInfo;
import io.github.buzzxu.spuddy.services.boss.SysUserService;
import lombok.RequiredArgsConstructor;
import org.apache.shiro.authz.annotation.Logical;
import org.apache.shiro.authz.annotation.RequiresRoles;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.util.Map;
import java.util.Optional;

import static com.google.common.base.Preconditions.checkArgument;

/**
 * @author xux
 * @date 2024年09月05日 14:28:02
 */
@RequiredArgsConstructor
@RestController
@RequestMapping(value = "/system/users",produces = MediaType.APPLICATION_JSON_VALUE)
@RequiresRoles(value = {"superman"}, logical = Logical.OR)
public class SystemUserController {

    private final SysUserService systemUserService;

    @GetMapping("/list/{pageSize:[1-4]\\d{1}+}/{pageNumber:\\d+}")
    public R<Pager<SysUserInfoResponse>> searchSys(@PathVariable("pageNumber")int pageNumber
            , @PathVariable("pageSize") int pageSize
            , @RequestParam("keywords") Optional<String> keywords
            , @RequestParam("status") Optional<Integer> status
            , @RequestParam("roleId")  Optional<Integer> roleId){
        Map<String,Object> params = Maps.newHashMapWithExpectedSize(3);
        keywords.ifPresent(v->{
            if(!Strings.isNullOrEmpty(v)){
                params.put("keywords", v.strip());
            }
        });
        status.ifPresent(v->{
            if(v>0){
                params.put("status", v);
            }
        });
        roleId.ifPresent(v->{
            if(v>0){
                params.put("roleId", v);
            }
        });
        return R.of(systemUserService.list(pageNumber,pageSize,params).convert(VoMapStructs.INSTANCE::to));
    }

    @PostMapping("/create")
    public R<Boolean> createSystemUser(@RequestBody CreateOrUpdateSysteUserRequest request){
        checkArgument(!Strings.isNullOrEmpty(request.getRealName()),"请填写真实姓名");
        checkArgument(request.getRoleId() !=0,"请设置角色");
        BossUserInfo user = GetUser.of();
        BossUserInfo userInfo = VoMapStructs.INSTANCE.to(request);
        return R.of(systemUserService.saveOrderUpdate(userInfo,user.of()));
    }



    @DeleteMapping("/{userId:\\d+}")
    public R<Boolean> deleteSysUser(@PathVariable("userId") long userId){
        checkArgument(userId>0,"用户ID不能为空");
        BossUserInfo user = GetUser.of();
        return R.of(systemUserService.delete(userId,user.of()));
    }






    @PutMapping("/password/reset/{userId:\\d+}")
    public R<Boolean> passwordReset(@PathVariable("userId") Long userId){
        BossUserInfo user = GetUser.of();
        return R.of(systemUserService.resetPassword(userId,user.of()));
    }
}
