package com.company.project.vo.resp;

import lombok.Data;

import java.util.List;

/**
 * 穿梭框实体类
 */
@Data
public class DlTransferVo {
    private List<?> beans;
    private List<String> values;
}
