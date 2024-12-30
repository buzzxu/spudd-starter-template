package io.github.buzzxu.spuddy.controllers.system.responses;

import lombok.Getter;
import lombok.Setter;

/**
 * @author xux
 * @date 2024年12月30日 17:44:28
 */
@Getter @Setter
public class SysUserInfoResponse {

    private Long id;
    private String userName;
    private String mobile;
    private String email;
    private int roleId;
    private String roleName;
    private String realName;
    private Integer status;




    public String getStatusText(){
        return  status != null && status == 0 ? "禁用" : "正常";
    }

}
