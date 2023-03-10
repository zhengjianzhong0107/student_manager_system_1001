package com.company.project;

import com.company.project.entity.DlClassTeacherEntity;
import com.company.project.entity.DlStudentEntity;
import com.company.project.mapper.DlClassTeacherMapper;
import com.company.project.service.DlExamPlanService;
import com.company.project.service.DlStudentService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RunWith(SpringRunner.class)
@SpringBootTest
public class CompanyFrameApplicationTests {
    @Resource
    private DlExamPlanService dlExamPlanService;
    @Resource
    private DlStudentService dlStudentService;
    @Resource
    private DlClassTeacherMapper dlClassTeacherMapper;


    @Test
    public void insertStudent(){
        DlStudentEntity bean = null;
        String class_id = "1347031319973756930";
        for(int i = 0; i < 50; i ++){
            bean = new DlStudentEntity();
            bean.setAge(i);
            bean.setClassId(class_id);
            bean.setGender(i%2);
            bean.setDeleted(1);
            bean.setSName("3学生" + i);
            bean.setSNo(i + 1 + "");
            dlStudentService.save(bean);
        }
    }
    @Test
    public void testInsert(){
        Map<String, String> map = new HashMap<>();
        map.put("arrId", "1360520275386908673");
        map.put("termId", "1346713330594394113");
        List<DlClassTeacherEntity> ctList = dlClassTeacherMapper.getArrCourseClassTeacherListByArrIdAndTermId(map);
        System.out.println("ctList.size():" + ctList.size());
    }










}
