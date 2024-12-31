package io.github.buzzxu.spuddy.controllers.system.requests;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class DataDictionaryItemRequest {
    private String key;
    private String name;
    private String value;
}

