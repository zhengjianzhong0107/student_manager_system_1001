package com.company.project.entity;

import lombok.Data;
import lombok.ToString;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 用于课表使用
 *
 * @version V1.0
 * @author: YangHui
 * @date: 2021/2/9 18:33
 */
@Data
@ToString
public class ElementMap {

    private Map<Integer, List<Element>> map = new HashMap<>();


    public void add(Integer type, Integer col, Integer row){
        if(map.get(type) == null){
            map.put(type, new ArrayList<>());
        }
        map.get(type).add(new Element(col, row));
    }

    public List<Element> get(Integer type){
        return map.get(type);
    }

    public Map<Integer, List<Element>> get(){
        return map;
    }
}
