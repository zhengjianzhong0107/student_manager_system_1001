package com.company.project.vo.resp;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

@Data
public class MenuTreeVo {
    private String id;
    private String title;

    private List<?> children;
    private Object bean;
    @ApiModelProperty(value = "是否展开 默认不展开(false)")
    private boolean spread = true;
    private boolean checked = false;
    private boolean disabled = false;

}
