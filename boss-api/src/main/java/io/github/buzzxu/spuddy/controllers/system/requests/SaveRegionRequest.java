package io.github.buzzxu.spuddy.controllers.system.requests;

import lombok.Getter;
import lombok.Setter;

/**
 * @author xux
 * @date 2024年09月05日 16:49:32
 */
@Getter
@Setter
public class SaveRegionRequest {
    /**
     * 父级
     */
    private String parent;
    /**
     * 编码
     */
    private String code;
    /**
     * 名称
     */
    private String name;
}
