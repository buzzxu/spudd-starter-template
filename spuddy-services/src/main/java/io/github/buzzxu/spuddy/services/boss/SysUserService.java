package io.github.buzzxu.spuddy.services.boss;

import com.google.common.base.Strings;
import io.github.buzzxu.spuddy.dal.SysUserDao;
import io.github.buzzxu.spuddy.objects.Pager;
import io.github.buzzxu.spuddy.redis.Redis;
import io.github.buzzxu.spuddy.security.KEY;
import io.github.buzzxu.spuddy.security.RoleService;
import io.github.buzzxu.spuddy.security.UserService;
import io.github.buzzxu.spuddy.security.objects.BossUserInfo;
import io.github.buzzxu.spuddy.security.objects.Operator;
import io.github.buzzxu.spuddy.security.objects.UserInfo;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;

import static com.google.common.base.Preconditions.checkArgument;

/**
 * @author xux
 * @date 2024年12月29日 17:11:34
 */
@RequiredArgsConstructor
@Slf4j
@Service
public class SysUserService {
    private final UserService userService;
    private final RoleService roleService;
    private final SysUserDao sysUserDao;
    private final Redis redis;
    private static final String pwd= "111111";



    public Pager<BossUserInfo> list(int pageNumber, int pageSize, Map<String,Object> params){
        return sysUserDao.list(pageNumber,pageSize,params);
    }

    @Transactional(rollbackFor = {IllegalArgumentException.class, Exception.class})
    public boolean saveOrderUpdate(BossUserInfo userInfo, Operator operator){
        checkArgument(!Strings.isNullOrEmpty(userInfo.getUserName()),"缺少参数: 用户名");
        try {
            if(userInfo.getId() != null && userInfo.getId() > 0){
                //修改

                if(userService.editUser(userInfo)){
                    List<Integer> roleIds = roleService.roleIdByUserId(userInfo.getId());
                    if((userInfo.getRoleId() > 0) && (roleIds.isEmpty() || !roleIds.contains(userInfo.getRoleId()))){
                        roleService.user2Role(userInfo.getId(),userInfo.getRoleId());
                    }
                    return true;
                }
                return false;
            }else{
                userInfo.setStatus(1); //默认正常
                userInfo.setPassword(pwd); //默认密码
                userService.create(userInfo,user->{
                    //设置角色
                    roleService.user2Role(user.getId(),userInfo.getRoleId());
                });
                return true;
            }
        }finally {
            redis.execute(redis-> redis.del(KEY.USER_INFO.to(userInfo.getId())));
        }
    }
    @Transactional(rollbackFor = {IllegalArgumentException.class, Exception.class})
    public boolean reopen(Long userId, Operator operator){
        checkArgument(userId != null && userId > 0, "缺少参数:用户ID");
        checkArgument(Objects.equals(userId,operator.getId()),"无法操作自己的账户");
        log.info("删除用户,{},操作人: {}",userId,operator.name());
        try {
            return userService.isDisable(userId) ? userService.normal(userId,id->true) : userService.disable(userId,id->true);
        }finally {
            redis.execute(redis-> redis.del(KEY.USER_INFO.to(userId)));
        }
    }
    /**
     * 删除用户
     * @param userId
     * @param operator
     * @return
     */
    @Transactional(rollbackFor = {IllegalArgumentException.class, Exception.class})
    public boolean delete(Long userId, Operator operator){
        checkArgument(userId != null && userId > 0, "缺少参数:用户ID");
        log.info("删除用户,{},操作人: {}",userId,operator.name());
        return userService.delete(userId);
    }

    /**
     * 重置密码
     * @param userId
     * @param operator
     * @return
     */
    @Transactional(rollbackFor = {IllegalArgumentException.class, Exception.class})
    public boolean resetPassword(Long userId,Operator operator){
        checkArgument(userId != null && userId > 0, "缺少参数:用户ID");
        log.info("重置账户密码,{},操作人: {}",userId,operator.name());
        return userService.resetPassword(userId,()->pwd);
    }

}
