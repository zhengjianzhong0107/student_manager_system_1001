package com.company.project.vo.resp;

import lombok.Data;

import java.math.BigDecimal;
import java.util.List;
@Data
public class EChartDataVo {

    private String name;
    private String type;
    private List<BigDecimal> data;
}
