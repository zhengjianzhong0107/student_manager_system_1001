package com.company.project.common.utils;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MapUtils {
    /**
     * 将bean中的ID作为Key，bean为value保存
     * @param list
     * @param <T> 需要是实体类又ID属性
     * @return
     */
    public static <T> Map<String, T> getMapByList(List<T> list)  {
        Map<String, T> map = new HashMap<>();
        for (T bean: list){
            //获取bean中的ID的get方法，并获取其ID值
            try{
                Field f = bean.getClass().getDeclaredField("id");
                if(f != null){
                    f.setAccessible(true);
                    String id = (String) f.get(bean);
                    map.put(id, bean);
                }
            }catch (Exception e ){
                e.printStackTrace();
            }
        }
        return map;
    }
}
