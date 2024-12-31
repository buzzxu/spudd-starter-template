package io.github.buzzxu.spuddy.controllers.system;

import com.google.common.base.Strings;
import io.github.buzzxu.spuddy.R;
import io.github.buzzxu.spuddy.controllers.system.requests.SaveOrUpdateRoleRequest;
import io.github.buzzxu.spuddy.objects.Pager;
import io.github.buzzxu.spuddy.objects.Pair;
import io.github.buzzxu.spuddy.security.RoleMenuService;
import io.github.buzzxu.spuddy.security.RoleService;
import io.github.buzzxu.spuddy.security.boss.GetUser;
import io.github.buzzxu.spuddy.security.objects.BossUserInfo;
import io.github.buzzxu.spuddy.security.objects.Role;
import io.github.buzzxu.spuddy.security.objects.UserInfo;
import org.apache.shiro.authz.annotation.Logical;
import org.apache.shiro.authz.annotation.RequiresRoles;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;
import java.util.List;

import static com.google.common.base.Preconditions.checkArgument;

/**
 * @auther xux
 * @date 2018/6/6 上午9:08
 */
@RestController
@RequestMapping(value = "/sys/role",produces = MediaType.APPLICATION_JSON_VALUE)
@RequiresRoles(value = {"superman"}, logical = Logical.OR)
public class RoleController {

    @Autowired
    RoleService roleService;
    @Autowired
    private RoleMenuService roleMenuService;


    @GetMapping("/options")
    public R<List<Pair<Integer,String>>> rolesOptions(){
        return R.of(Collections.emptyList());
    }

    @GetMapping("/list/{pageSize:\\d+}/{pageNumber:\\d+}")
    public R<Pager<Role>> pager(@PathVariable("pageSize") int pageSize
            , @PathVariable("pageNumber") int pageNumber) {
        return R.of(roleService.paginate(pageNumber, pageSize, null));
    }
    @PostMapping("/saveOrUpdate")
    public R<Boolean> saveOrUpdate(@RequestBody SaveOrUpdateRoleRequest request) {
        checkArgument(!Strings.isNullOrEmpty(request.getName()), "请设置角色名称");
        checkArgument(!Strings.isNullOrEmpty(request.getCode()), "请设置角色编码");
        BossUserInfo user = GetUser.of();
        return R.of(roleMenuService.create(Role.builder().id(request.getId()).name(request.getName()).code(request.getCode()).type(request.getType()).description(request.getDescription()).build(), request.getMenuIds(), null, user.of()) > 0);
    }


    @DeleteMapping("/{roleId}")
    public R<Boolean> delete(@PathVariable("roleId") int roleId){
        roleService.delete(roleId,null);
        return R.of(true);
    }

    @GetMapping("/selector")
    public R<List<Pair<Integer,String>>> selections(){
        return R.of(roleService.getOptions());
    }
}
