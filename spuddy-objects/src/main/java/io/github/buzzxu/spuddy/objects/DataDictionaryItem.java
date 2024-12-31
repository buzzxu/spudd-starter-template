package io.github.buzzxu.spuddy.objects;

import io.github.buzzxu.spuddy.objects.i18n.Langs;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Map;

/**
 * @author xux
 * @date 2024年12月31日 23:25:42
 */
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class DataDictionaryItem {
    private Integer id;
    private String key;
    private String name;
    private String value;
    private Map<String,Object> ext;
    private String ext1;
    private Langs langs;
    private int sorted;
}
