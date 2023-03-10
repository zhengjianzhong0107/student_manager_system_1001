package com.company.project.vo.resp;

import lombok.Data;
import lombok.ToString;

import java.util.List;

@Data
@ToString
public class EChartVo {
    private String title;
    private List<Object> legend;

    private List<Object> xAxis;
    private String xName;

    private List<Object> yAxis;
    private String yName;
    private List<Object> series;
}
