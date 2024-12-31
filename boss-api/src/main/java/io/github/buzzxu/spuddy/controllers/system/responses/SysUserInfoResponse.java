package io.github.buzzxu.spuddy.controllers.system.responses;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.google.common.base.Strings;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

/**
 * @author xux
 * @date 2024年12月30日 17:44:28
 */
@Getter @Setter
public class SysUserInfoResponse {

    private Long id;
    private String avatar;
    private String userName;
    private String mobile;
    private String email;
    private int roleId;
    private String roleName;
    private String realName;
    private Integer status;
    @JsonFormat(
            pattern = "yyyy-MM-dd HH:mm"
    )
    protected LocalDateTime createdAt;
    @JsonFormat(
            pattern = "yyyy-MM-dd HH:mm"
    )
    protected LocalDateTime updatedAt;

    public String getUserName() {
        return Strings.isNullOrEmpty(userName) ? mobile : userName;
    }


    public String getStatusText(){
        return  status != null && status == 0 ? "禁用" : "正常";
    }

}
