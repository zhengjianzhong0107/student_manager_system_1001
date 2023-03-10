package com.company.project.common.enums;

public enum CheckBoxStatusEnum {
    DISABLE(-1),//禁用
    CHECKED(1),//选中
    UNCHECKED(0);//未被选中

    public Integer value;

    CheckBoxStatusEnum(Integer value){
        this.value = value;
    }

    public Integer getValue(){
        return value;
    }
}
