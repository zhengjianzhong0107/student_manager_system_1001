package com.company.project.vo.req;

import com.company.project.entity.DlTestItemEntity;
import lombok.Data;

import java.util.List;

@Data
public class TestVO {
    private String id;
    private String code;
    private String testName;
    /**
     * 注意事项
     */
    private String notes;
    /**
     * 考试时长
     */
    private Double duration;
    private Double score;
    private Integer self;
    private String subjectId;
    private Integer gradeType;

    List<DlTestItemEntity> items;
}
