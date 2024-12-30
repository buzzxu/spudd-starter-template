package io.github.buzzxu.spuddy.controllers.responses;

import lombok.Getter;
import lombok.Setter;

/**
 * @author xux
 * @date 2024年12月29日 19:01:54
 */
@Getter
@Setter
public class LoginResponse {
    private String userName;
    private String nickName;
    private String token;
    private String realName;
    private String avatar;
}
