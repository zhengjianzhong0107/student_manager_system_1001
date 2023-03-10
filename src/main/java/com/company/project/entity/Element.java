package com.company.project.entity;

import lombok.Data;
import lombok.ToString;

/**
 * @version V1.0
 * @author: YangHui
 * @date: 2021/2/9 18:36
 */
@Data
@ToString
public class Element {
    private Integer col;
    private Integer row;

    private Integer status;
    private String desc;
    private DlCourseTableEntity bean;

    public Element(){

    }

    public Element(Integer col, Integer row){
        this.col = col;
        this.row = row;
    }

    public Element(Integer col, Integer row, Integer status){
        this.col = col;
        this.row = row;
        this.status = status;
    }

    public Element(Integer col, Integer row, Integer status, String desc){
        this.col = col;
        this.row = row;
        this.status = status;
        this.desc = desc;
    }
}
