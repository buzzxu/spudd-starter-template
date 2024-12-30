package io.github.buzzxu.spuddy.controllers.system.requests;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class DataDictionaryItemRequest {
    private Integer id;
    private String code;
    private String name;
    private String value;

    private String ext1;
}

