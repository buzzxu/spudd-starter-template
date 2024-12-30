package io.github.buzzxu.spuddy.controllers.system.requests;

import lombok.Getter;
import lombok.Setter;

/**
 * @author xux
 * @date 2024年09月05日 16:52:12
 */
@Getter
@Setter
public class SysConfigRequest {
    private String key;
    private String value;
}

