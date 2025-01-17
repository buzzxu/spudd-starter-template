package io.github.buzzxu.spuddy.controllers.system;

import com.google.common.collect.Sets;
import io.github.buzzxu.spuddy.R;
import io.github.buzzxu.spuddy.controllers.requests.IdsRequest;
import io.github.buzzxu.spuddy.objects.Pager;
import io.github.buzzxu.spuddy.security.MenuService;
import io.github.buzzxu.spuddy.security.objects.Menu;
import lombok.RequiredArgsConstructor;
import org.apache.shiro.authz.annotation.Logical;
import org.apache.shiro.authz.annotation.RequiresRoles;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Set;


@RequiredArgsConstructor
@RequiresRoles(value = {"superman"}, logical = Logical.OR)
@RestController()
@RequestMapping(value = "/sys/menu",produces = MediaType.APPLICATION_JSON_VALUE)
public class MenuController {
    private final MenuService menuService;

    @GetMapping("/list/{pageSize:\\d+}/{pageNumber:\\d+}")
    public R<Pager<Menu>> pager(@PathVariable("pageSize") int pageSize
            , @PathVariable("pageNumber") int pageNumber) {
        return R.of(menuService.paginate(pageNumber, pageSize, null));
    }
    /**
     * 获取全部菜单
     *
     * @return
     */
    @GetMapping("/all")
    public R<List<Menu>> all() {
        return R.of(menuService.getAll());
    }
    @GetMapping("/all/tree")
    public R<List<Menu>> tree(){
        return R.of(menuService.getAllTree());
    }

    /**
     * 创建菜单
     * @param menu
     * @return
     */
    @PostMapping("/saveOrUpdate")
    public R<Integer> saveOrUpdate(@RequestBody Menu menu) {
        if (menu.getId() != null && menu.getId() > 0) {
            return R.of(menuService.update(menu) ? menu.getId() : 0);
        }
        return R.of(menuService.create(menu.getParentId(), menu.getName(), menu.getCode(), menu.getTarget(), menu.getPath(), menu.getIcon(), menu.getRemark(), null));
    }


    @DeleteMapping("/{id}")
    public R<Boolean> delete(@PathVariable("id") int id){
        return R.of(menuService.delete(null,id));
    }


    @PutMapping("/role/{roleId}")
    public R<Boolean> menu2Role(@PathVariable("roleId")int roleId, @RequestParam("menuId")Set<Integer> menuIds){
        return R.of(menuService.menu2Role(roleId,menuIds,null));
    }

    /**
     * 根据角色ID获取菜单id
     *
     * @param roleId
     * @return
     */
    @GetMapping("/role/{roleId}/ids")
    public R<List<Integer>> findIdsByRole(@PathVariable("roleId") int roleId) {
        return R.of(menuService.findIdsByRoleIds(roleId));
    }

    @GetMapping("/role/ids/{roleId}")
    public R<Set<Integer>> idsByRole(@PathVariable("roleId") int roleId) {
        Set<Integer> ids = Sets.newHashSetWithExpectedSize(30);
        List<Menu> menus = menuService.getTreeByRoleId(roleId);
        for (Menu menu : menus) {
            ids.add(menu.getId());
            if (!menu.getChilds().isEmpty()) {
                ids.addAll(menu.getChilds().stream().map(Menu::getId).toList());
            }
        }
        return R.of(ids);
    }
}
