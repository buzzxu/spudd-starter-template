package io.github.buzzxu.spuddy.controllers.requests.user;

import lombok.Getter;
import lombok.Setter;

/**
 * @author xux
 * @date 2024年12月31日 21:15:38
 */
@Getter @Setter
public class EditUserRequest {
    private String mobile;
    private String realName;
    private String email;;
}
