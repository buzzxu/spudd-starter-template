package io.github.buzzxu.spuddy.services;

import com.google.common.base.Objects;
import com.google.common.base.Strings;
import com.google.common.collect.Lists;
import io.github.buzzxu.spuddy.dal.jdbc.Jdbcer;
import io.github.buzzxu.spuddy.exceptions.ApplicationException;
import io.github.buzzxu.spuddy.jackson.Jackson;
import io.github.buzzxu.spuddy.objects.DataDictionaryItem;
import io.github.buzzxu.spuddy.objects.Pager;
import io.github.buzzxu.spuddy.objects.Pair;
import io.github.buzzxu.spuddy.objects.i18n.Langs;
import io.github.buzzxu.spuddy.redis.Redis;
import lombok.*;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

import static com.google.common.base.Preconditions.checkArgument;

/**
 * @author xux
 * @date 2024年12月31日 22:31:02
 */
@RequiredArgsConstructor
@Service
public class DataDictionaryService {
    private final Redis redis;

    private static final String SQL_COLUMN = "id,key,name,value,ext,ext1,langs,sorted";
    private static final String SQL_SELECT = "SELECT " + SQL_COLUMN + " FROM data_dictionary ";
    private final Jdbcer jdbcer;

    public void insert(String key,String name){
        insert(key,name,(Map<String,Object>)null);
    }
    public void insert(String key,String name,Map<String,Object> ext){
        insert(key,name,name,ext);
    }
    public void insert(String key,String name,String value){
        insert(key,name,value,null);
    }
    public void insert(String key,String name,String value,Map<String,Object> ext){
        insert(key,name,value,100,ext);
    }
    public void insert(String key,String name,String value,Map<String,Object> ext, Langs langs){
        insert(key,name,value,100,ext,langs);
    }
    public void insert(String key,String name,int sorted){
        insert(key,name,name,sorted,null);
    }
    public void insert(String key,String name,int sorted,Map<String,Object> ext){
        insert(key,name,name,sorted,ext);
    }

    public void insert(String key, String name, String value, int sorted, Map<String,Object> ext){
        insert(key,  name,value,sorted,ext,null);
    }
    public void insert(String key, String name, String value, int sorted, Map<String,Object> ext, Langs langs){
        checkArgument(!Strings.isNullOrEmpty(key),"请设置键值");
        checkArgument(!Strings.isNullOrEmpty(name),"请设置名称");
        if(Strings.isNullOrEmpty(value)){
            value = name;
        }
        //验证是否重复
        String sql = "SELECT COUNT(*) FROM data_dictionary WHERE ";
        List<Object> params = Lists.newArrayListWithCapacity(2);
        sql += extSql(key,ext,params);
        sql += " AND name = ? LIMIT 1";
        params.add(name);
        if(jdbcer.count(sql,params.toArray()) > 0){
            throw new IllegalArgumentException("名称`"+name+"`重复,请更换");
        }
        jdbcer.insert("INSERT INTO data_dictionary (key,name,value,ext,langs,sorted) VALUES (?,?,?,?,?,?)",key.strip(),name.strip(),value.strip(),ext != null ? Jackson.object2Json(ext) : null,langs != null ? Jackson.object2Json(langs) : null,sorted);
        clear("datadict:"+key(key,ext));
    }

    public void delete(String key,String name){
        checkArgument(!Strings.isNullOrEmpty(key),"请设置键值");
        checkArgument(!Strings.isNullOrEmpty(name),"请设置名称");
        Pair<String,String> val = findByλ(key,name);
        jdbcer.update("DELETE FROM data_dictionary WHERE key = ? AND name = ?",key,name);
        if(val != null){
            Object $key;
            if(!Strings.isNullOrEmpty(val.getValue())){
                $key = Objects.hashCode(val.getKey(), Jackson.json2Map(val.getValue()));
            }else{
                $key = val.getKey();
            }
            clear("datadict:"+$key);
        }
    }

    public void delete(Integer id){
        Pair<String,String> val = findByλ(id);
        jdbcer.update("DELETE FROM data_dictionary WHERE id = ?",id);
        if(val != null){
            Object $key;
            if(!Strings.isNullOrEmpty(val.getValue())){
                $key = Objects.hashCode(val.getKey(),Jackson.json2Map(val.getValue()));
            }else{
                $key = val.getKey();
            }
            clear("datadict:"+$key);
        }
    }

    public void delete(String key,Integer id,Map<String,Object> ext){
        checkArgument(!Strings.isNullOrEmpty(key),"请设置键值");
        checkArgument(id != null && id > 0,"请设置ID");
        String sql = "DELETE FROM data_dictionary WHERE id = ? AND ";
        List<Object> params = Lists.newArrayListWithCapacity(2);
        params.add(id);
        sql += extSql(key,ext,params);
        jdbcer.update(sql,params.toArray());
        clear("datadict:"+key(key,ext));
    }

    public void delete(String key,String name,Map<String,Object> ext){
        checkArgument(!Strings.isNullOrEmpty(key),"请设置键值");
        checkArgument(!Strings.isNullOrEmpty(name),"请设置名称");
        String sql = "DELETE FROM data_dictionary WHERE ";
        List<Object> params = Lists.newArrayListWithCapacity(2);
        sql += extSql(key,ext,params);
        jdbcer.update(sql,params.toArray());
        clear("datadict:"+key(key,ext));
    }

    /***
     * 排序上移
     * @param id
     * @param key
     * @param ext
     * @return
     */
    public boolean sortedUp(Integer id,String key,Map<String,Object> ext){
        List<DataDictionaryItem> all = findBy(key,ext);
        int current = 0;
        for(int i = 0; i< all.size();i++){
            if(Objects.equal(all.get(i).getId(),id)){
                current = i;
                break;
            }
        }
        if(current > 0){
            DataDictionaryItem pre = all.get(current -1);
            if(jdbcer.update("UPDATE data_dictionary SET sorted = ? WHERE id = ?",pre.getSorted() + 1,id) > 0){
                clear("datadict:"+key(key,ext));
                return true;
            }
        }
        return false;
    }

    protected Object key(String key,Map<String,Object> ext){
        if(ext != null && !ext.isEmpty()){
            return Objects.hashCode(key,ext);
        }else{
            return key;
        }
    }
    protected Pair<String,String> findByλ(Integer id){
        return jdbcer.findFirst("SELECT key,ext FROM data_dictionary WHERE id = ? LIMIT 1", rs->{
            if(rs.next()){
                return Pair.of(rs.getString("key"),rs.getString("ext"));
            }
            return null;
        }, id);
    }
    protected Pair<String,String> findByλ(String key,String name){
        return jdbcer.findFirst("SELECT key,ext FROM data_dictionary WHERE key = ? AND name = ? LIMIT 1", rs->{
            if(rs.next()){
                return Pair.of(rs.getString("key"),rs.getString("ext"));
            }
            return null;
        }, key,name);
    }



    public List<DataDictionaryItem> findBy(String key){
        //30d
        return redis.gets("datadict:"+ key,2592000,()->{
            return jdbcer.find(SQL_SELECT + " WHERE key = ? ORDER BY sorted DESC" , rs->{
                List<DataDictionaryItem> data = Lists.newArrayListWithCapacity(5);
                while (rs.next()){
                    String $langs = rs.getString("langs");
                    Langs langs = Strings.isNullOrEmpty($langs) ? null : Jackson.json2Object($langs, Langs.class);
                    String $ext = rs.getString("ext");
                    Map<String,Object> $$ext = Strings.isNullOrEmpty($langs) ? null : Jackson.json2Map($ext);
                    data.add(new DataDictionaryItem(rs.getInt("id"),rs.getString("key"),rs.getString("name"),rs.getString("value"),$$ext,rs.getString("ext1"),langs,rs.getInt("sorted")));
                }
                return data;
            },key);
        },DataDictionaryItem.class);
    }


    public List<DataDictionaryItem> findBy(String key, Map<String,Object> ext){
        checkArgument(ext != null && !ext.isEmpty(),"扩展参数为null");
        String $key = "datadict:"+ java.util.Objects.hash(key,ext);
        return redis.gets($key,2592000,()->{
            String sql = SQL_SELECT + " WHERE ";
            List<Object> params = Lists.newArrayListWithCapacity(2);
            sql += extSql(key,ext,params);
            sql += " ORDER BY sorted DESC";
            return jdbcer.find(sql , rs -> {
                List<DataDictionaryItem> data = Lists.newArrayListWithCapacity(5);
                while (rs.next()){
                    String $langs = rs.getString("langs");
                    Langs langs = Strings.isNullOrEmpty($langs) ? null : Jackson.json2Object($langs, Langs.class);
                    String $ext = rs.getString("ext");
                    Map<String,Object> $$ext = Strings.isNullOrEmpty($langs) ? null : Jackson.json2Map($ext);
                    data.add(new DataDictionaryItem(rs.getInt("id"),rs.getString("key"),rs.getString("name"),rs.getString("value"),$$ext,rs.getString("ext1"), langs, rs.getInt("sorted")));
                }
                return data;
            },params.toArray());
        },DataDictionaryItem.class);
    }

    public DataDictionaryItem of(Integer id){
        return  jdbcer.findFirst(DataDictionaryItem.class,SQL_SELECT + " WHERE id = ? LIMIT 1",id);
    }


    public Pager<DataDictionaryItem> paginate(int pageNumber, int pageSize, Map<String,Object> params){
        List<Object> args = Lists.newArrayListWithCapacity(2);
        Pager<DataDictionaryItem> pager = new Pager<>(pageNumber,pageSize);
        jdbcer.paginate(SQL_SELECT + extSql(params,args),pager, DataDictionaryItem.class,rs->{
            List<DataDictionaryItem> items = Lists.newArrayListWithCapacity(3);
            while (rs.next()){
                DataDictionaryItem item = new DataDictionaryItem();
                item.setId(rs.getInt("id"));
                item.setKey(rs.getString("key"));
                item.setName(rs.getString("name"));
                item.setValue(rs.getString("value"));
                item.setExt1(rs.getString("ext1"));
                String ext = rs.getString("ext");
                if(!Strings.isNullOrEmpty(ext)){
                    item.setExt(Jackson.json2Map(ext));
                }
                String langs = rs.getString("langs");
                if(!Strings.isNullOrEmpty(langs)){
                    item.setLangs(Jackson.json2Object(langs,Langs.class));
                }
                items.add(item);
            }
            return items;
        },args.toArray());
        return pager;
    }

    public List<Pair<String,String>> selection(String key){
        return findBy(key).stream().map(v-> Pair.of(v.getName(),v.getValue())).toList();
    }

    public List<Pair<String,String>> selection(String key,Map<String,Object> ext){
        return findBy(key, ext).stream().map(v-> Pair.of(v.getName(),v.getValue())).toList();
    }
    protected String extSql(String key,Map<String,Object> ext,List<Object> params){
        String sql = " key = ? ";
        params.add(key);
        if(ext != null){
            if(ext.containsKey("json")){
                sql += " AND JSON_OVERLAPS(ext,?) ";
                params.add(Jackson.object2Json(ext.get("json")));
            }else{
                sql += " AND (";
                for(Map.Entry<?,?> entry : ext.entrySet()){
                    Object value = entry.getValue();
                    if(value instanceof String val){
                        sql += "  ext->'$."+entry.getKey().toString()+"' = '"+ val+"' AND ";
                    }else {
                        sql += "  ext->'$."+entry.getKey().toString()+"' = "+ value.toString() + " AND ";
                    }
                }
                sql += " 1 = 1 ) ";
            }
        }
        return sql;
    }

    protected String extSql(Map<String,Object> params,List<Object> args){
        String sql = "";
        if(params != null && !params.isEmpty()){
            sql += " WHERE ";
            if(params.containsKey("key")){
                sql += " key = ? AND ";
                args.add(params.get("key"));
            }
            if(params.containsKey("name")){
                sql += " name = ? AND ";
                args.add(params.get("name"));
            }
            if(params.containsKey("value")){
                sql += " value = ? AND ";
                args.add(params.get("value"));
            }
            if(params.containsKey("ext")){
                if(params.get("ext") instanceof Map<?,?> map){
                    if(map.containsKey("json")){
                        sql += " JSON_OVERLAPS(ext,'"+ Jackson.object2Json(map.get("json")) +"') AND ";
                    }else{
                        for(Map.Entry<?,?> entry : map.entrySet()){
                            Object value = entry.getValue();
                            if(value instanceof String val){
                                sql += " ext->'$."+entry.getKey().toString()+"' = '"+ val+"' AND ";
                            }else {
                                sql += " ext->'$."+entry.getKey().toString()+"' = "+ value.toString() + " AND ";
                            }
                        }
                    }
                }else {
                    throw ApplicationException.raise("ext参数，值类型需要是map");
                }
            }
            sql += " 1=1 ";
        }
        sql += " ORDER BY sorted DESC";
        return sql;
    }

    protected void clear(String key){
        redis.execute(redis-> redis.del(key));
    }

}
