package com.company.project.common.utils;

import java.io.Serializable;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 索引工具类
 *
 * @author: YangHui
 * @version V1.0
 * @date: 2021/2/5 19:49
 */
public class Index<V> implements Serializable {
    private final Map<String, List<V>> index = new HashMap<>();
    public List<V> get(String key) {
        return index.get(key);
    }

    /**
     * 通过map的Key和对应的V类型的属性名，来查询map中是否存在
     * 一个bean的属性名与对应的值都满足fieldMap中的值，如果都满足就返回该对象，
     * 遍历到最后都未找到这样的bean就直接返回一个null
     * @param key
     * @param fieldMap 属性名与属性值的Map
     * @return
     */
    public V getOne(String key, Map<String, String> fieldMap) {
        List<V> list = get(key);
        if (list == null){
            return null;
        }

        for(V v: list){
            boolean bool = true;
            for (String fieldName : fieldMap.keySet()){
                String value = getValueByFieldName(v, fieldName);
                //只要检查到了有一个属性不相等，就修改bool=false
                //并直接跳出循环
                if(! fieldMap.get(fieldName).equals(value)){
                    bool = false;
                    break;
                }
            }
            if(bool){
                return v;
            }
        }
        return null;
    }



    /**
     * 获取满足条件的数据条数
     * @param key
     * @param fieldMap
     * @return
     */
    public Integer getCount(String key,  Map<String, String> fieldMap){
        List<V> list = get(key);
        if (list == null){
            return 0;
        }
        int count = 0;
        for(V v: list){
            boolean bool = true;
            for (String fieldName: fieldMap.keySet()){
                String value = getValueByFieldName(v, fieldName);
                if(!fieldMap.get(fieldName).equals(value)){
                    bool = false;
                    break;
                }
            }
            if(bool){
                count ++;
            }
        }
        return count;
    }





    private String getValueByFieldName(V v, String fieldName){
        String value = null;
        try{
            Field f = v.getClass().getDeclaredField(fieldName);
            if(f != null){
                f.setAccessible(true);
                value = f.get(v).toString();
            }
        }catch (Exception e){
            e.printStackTrace();
        }
        return value;
    }

    public void put(String key, V value) {
        //检查里面是否有
        if(!index.containsKey(key)){
            index.put(key, new ArrayList<V>());
        }
        index.get(key).add(value);
    }



    /**
     * 通过map的Key和对应的V类型的属性名，来查询map中是否存在
     * 一个bean的属性名与对应的值都满足fieldMap中的值,
     * 如果满足就移除该对象
     * 如果存在多条数据，就移除多条数据，并返回移除的条数
     * @param key
     * @param fieldMap 属性名与属性值的Map
     * @return 移除的数据条数
     */
    public int remove(String key, Map<String, String> fieldMap){
        List<V> list = get(key);
        if (list == null){
            return 0;
        }
        int count = 0;
        for(V v: list){
            boolean bool = true;
            for (String fieldName: fieldMap.keySet()){
                String value = getValueByFieldName(v, fieldName);
                if(!fieldMap.get(fieldName).equals(value)){
                    bool = false;
                    break;
                }
            }
            if(bool){
                list.remove(v);
                count ++;
            }
        }
        return count;
    }



}
