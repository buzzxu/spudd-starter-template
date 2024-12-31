package io.github.buzzxu.spuddy.controllers.requests;

import lombok.Getter;
import lombok.Setter;

import java.util.Set;

/**
 * @author xux
 * @date 2024年12月31日 14:48:57
 */
@Getter @Setter
public class IdsRequest {
    private Set<Integer> ids;
}
