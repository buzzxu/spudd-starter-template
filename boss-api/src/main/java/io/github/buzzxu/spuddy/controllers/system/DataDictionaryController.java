package io.github.buzzxu.spuddy.controllers.system;

import com.google.common.base.Strings;
import com.google.common.collect.Maps;
import io.github.buzzxu.spuddy.R;
import io.github.buzzxu.spuddy.controllers.system.requests.DataDictionaryItemRequest;
import io.github.buzzxu.spuddy.objects.DataDictionaryItem;
import io.github.buzzxu.spuddy.objects.Pager;
import io.github.buzzxu.spuddy.objects.Pair;
import io.github.buzzxu.spuddy.services.DataDictionaryService;
import lombok.RequiredArgsConstructor;
import org.apache.shiro.authz.annotation.Logical;
import org.apache.shiro.authz.annotation.RequiresRoles;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Optional;

/**
 * @author xux
 * @date 2024年12月31日 23:30:37
 */
@RequiredArgsConstructor
@RequestMapping( "/datadict")
@RestController
public class DataDictionaryController {
    private final DataDictionaryService dataDictionary;

    @GetMapping("/list/{pageSize:[1-4]\\d{1}+}/{pageNumber:\\d+}")
    public R<Pager<DataDictionaryItem>> list(@PathVariable("pageSize") int pageSize
            , @PathVariable("pageNumber") int pageNumber
            , @RequestParam("key") Optional<String> key
            , @RequestParam("name") Optional<String> name){
        Map<String,Object> params = Maps.newHashMapWithExpectedSize(1);
        key.ifPresent(v->{
            if(!Strings.isNullOrEmpty(v)){
                params.put("key", v.strip());
            }
        });
        name.ifPresent(v->{
            if(!Strings.isNullOrEmpty(v)){
                params.put("name", v.strip());
            }
        });
        return R.of(dataDictionary.paginate(pageNumber,pageSize, Collections.emptyMap()));
    }

    @RequiresRoles(value = {"superman"}, logical = Logical.OR)
    @PostMapping("/create")
    public R<Boolean> create(@RequestBody DataDictionaryItemRequest request){
        dataDictionary.insert(request.getKey(),request.getName(),request.getValue());
        return R.of(true);
    }

    @RequiresRoles(value = {"superman"}, logical = Logical.OR)
    @DeleteMapping("/{id}")
    public R<Boolean> delete(@PathVariable("id")Integer id){
        dataDictionary.delete(id);
        return R.of(true);
    }



    @GetMapping("/list/{key}")
    public R<List<Pair<String,String>>> find(@PathVariable("key")String key){
        return R.of(dataDictionary.selection(key));
    }

    @GetMapping("/{id}")
    public R<DataDictionaryItem> get(@PathVariable("id")Integer id){
        return R.of(dataDictionary.of(id));
    }
}
