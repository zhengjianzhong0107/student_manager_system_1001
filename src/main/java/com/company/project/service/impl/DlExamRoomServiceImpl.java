package com.company.project.service.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.company.project.entity.DlExamRoomEntity;
import com.company.project.mapper.DlClassMapper;
import com.company.project.mapper.DlExamClassMapper;
import com.company.project.mapper.DlExamRoomMapper;
import com.company.project.service.DlExamRoomService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.Map;


@Service("dlExamRoomService")
public class DlExamRoomServiceImpl extends ServiceImpl<DlExamRoomMapper, DlExamRoomEntity> implements DlExamRoomService {
    @Resource
    private DlExamRoomMapper mapper;
    @Resource
    private DlExamClassMapper dlExamClassMapper;
    @Resource
    private DlClassMapper dlClassMapper;

    /**
     * 检查考场是否能够满足学生入座
     * @param examId
     * @return
     */
    @Override
    public boolean checkRoomIsEnough(String examId) {
        Integer capacity = mapper.getRoomsCapacity(examId);
        Integer stu_num = dlExamClassMapper.getExamStudentCount(examId);

        System.out.println("教室容纳：" + capacity);
        System.out.println("学生人数：" + stu_num);
        if(capacity == null){
            capacity = 0;
        }
        if(stu_num == null){
            stu_num = 0;
        }
        if(capacity >= stu_num){
            return true;
        }
        return false;
    }

    @Override
    public DlExamRoomEntity getExamRoomBeanByStuId(String stuId, String planId) {
        Map<String, Object> map = new HashMap<>();
        map.put("stuId", stuId);
        map.put("planId", planId);
        return mapper.getExamRoomBeanByStuId(map);
    }


    public JSONArray getJSONList(String examId){
        LambdaQueryWrapper<DlExamRoomEntity> queryWrapper = Wrappers.lambdaQuery();
        queryWrapper.eq(DlExamRoomEntity::getExamId, examId);
        return JSONArray.parseArray(JSON.toJSONString(mapper.selectList(queryWrapper)));
    }


}