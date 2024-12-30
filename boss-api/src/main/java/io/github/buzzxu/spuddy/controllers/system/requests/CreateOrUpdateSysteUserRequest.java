package io.github.buzzxu.spuddy.controllers.system.requests;

import lombok.Getter;
import lombok.Setter;



@Getter
@Setter
public class CreateOrUpdateSysteUserRequest {
    private Long id;
    private String userName;
    private String mobile;
    private String email;
    private int roleId;
    private String realName;
}
