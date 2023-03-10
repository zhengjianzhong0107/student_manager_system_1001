package com.company.project.vo.req;

import com.company.project.entity.DlCourseTableEntity;
import lombok.Data;
import lombok.ToString;

/**
 * @version V1.0
 * @author: YangHui
 * @date: 2021/2/15 10:16
 */
@Data
@ToString
public class CourseExchangeVo {
    private DlCourseTableEntity oldBean;
    private DlCourseTableEntity newBean;
}
