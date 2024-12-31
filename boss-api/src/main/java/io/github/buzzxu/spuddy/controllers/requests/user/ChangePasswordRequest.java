package io.github.buzzxu.spuddy.controllers.requests.user;

import lombok.Getter;
import lombok.Setter;

/**
 * @author xux
 * @date 2024年12月31日 18:35:30
 */
@Getter @Setter
public class ChangePasswordRequest {
    private String oldPassword;
    private String newPassword;
}
