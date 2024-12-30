package io.github.buzzxu.spuddy.controllers.system.requests;


import io.github.buzzxu.spuddy.security.objects.RoleType;
import lombok.Getter;
import lombok.Setter;

import java.util.Set;


@Getter
@Setter
public class SaveOrUpdateRoleRequest {
    private int id;
    private Integer roleId;
    private String name;
    private String code;
    private RoleType type;
    private String description;
    private Set<Integer> menuIds;
}
