package com.company.project.controller;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.company.project.common.utils.DataResult;
import com.company.project.entity.DlClassEntity;
import com.company.project.entity.DlCourseEntity;
import com.company.project.entity.DlSubjectEntity;
import com.company.project.service.DlClassService;
import com.company.project.service.DlCourseService;
import com.company.project.service.DlSubjectService;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;



/**
 * 
 *
 * @author wenbin
 * @email *****@mail.com
 * @date 2020-12-04 18:03:47
 */
@Controller
@RequestMapping("/")
public class DlCourseController {
    @Autowired
    private DlCourseService dlCourseService;
    @Resource
    private DlClassService dlClassService;
    @Resource
    private DlSubjectService dlSubjectService;


    /**
    * 跳转到页面
    */
    @GetMapping("/index/dlCourse")
    public String dlCourse() {
        return "dlcourse/list";
        }

    @ApiOperation(value = "新增")
    @PostMapping("dlCourse/add")
    @RequiresPermissions("dlCourse:add")
    @ResponseBody
    public DataResult add(@RequestBody DlCourseEntity dlCourse){
        dlCourse = setCourseName(dlCourse);
        dlCourseService.save(dlCourse);
        return DataResult.success();
    }

    @ApiOperation(value = "删除")
    @DeleteMapping("dlCourse/delete")
    @RequiresPermissions("dlCourse:delete")
    @ResponseBody
    public DataResult delete(@RequestBody @ApiParam(value = "id集合") List<String> ids){
        dlCourseService.removeByIds(ids);
        return DataResult.success();
    }

    public DlCourseEntity setCourseName(DlCourseEntity bean){
        DlSubjectEntity byId = dlSubjectService.getById(bean.getSubjectId());
        if(byId != null){
            bean.setName(byId.getCourseName());
        }
        return bean;
    }

    @ApiOperation(value = "更新")
    @PutMapping("dlCourse/update")
    @RequiresPermissions("dlCourse:update")
    @ResponseBody
    public DataResult update(@RequestBody DlCourseEntity dlCourse){
        dlCourse = setCourseName(dlCourse);
        dlCourseService.updateById(dlCourse);
        return DataResult.success();
    }

    @ApiOperation(value = "查询分页数据")
    @PostMapping("dlCourse/listByPage")
    @RequiresPermissions("dlCourse:list")
    @ResponseBody
    public DataResult findListByPage(@RequestBody DlCourseEntity dlCourse){
        Page page = new Page(dlCourse.getPage(), dlCourse.getLimit());
        LambdaQueryWrapper<DlCourseEntity> queryWrapper = Wrappers.lambdaQuery();
        //查询条件示例
        //queryWrapper.eq(DlCourseEntity::getId, dlCourse.getId());
        if(!StringUtils.isEmpty(dlCourse.getGrade())){
            queryWrapper.likeRight(DlCourseEntity::getGrade, dlCourse.getGrade());
        }
        if(!StringUtils.isEmpty(dlCourse.getCourseType())){
            queryWrapper.eq(DlCourseEntity::getCourseType, dlCourse.getCourseType());
        }
        IPage<DlCourseEntity> iPage = dlCourseService.page(page, queryWrapper);
        return DataResult.success(iPage);
    }

    /**
     * 通过班级ID来查询该班级应该有的一些课程列表
     * @param classId
     * @return
     */
    @ApiOperation(value = "班级ID查询课程列表")
    @PostMapping("dlCourse/getCourseListByClassId")
    @RequiresPermissions("dlCourse:getCourseListByClassId")
    @ResponseBody
    public DataResult getCourseListByClassId(@RequestBody String classId){
        DlClassEntity classEntity = dlClassService.getById(JSONObject.parseObject(classId).get("classId").toString());
        if(classEntity == null){
            return DataResult.fail("未找到对应的班级信息");
        }
        if(classEntity.getGradeNum() == null){
            return DataResult.fail("未找到对应的年级信息");
        }
        return DataResult.success(dlCourseService.getListByGrade(classEntity.getGradeNum().toString()));
    }

    @ApiOperation(value = "班级ID查询课程列表")
    @PostMapping("dlCourse/getListByClassType")
    @RequiresPermissions("dlCourse:list")
    @ResponseBody
    public DataResult getListByClassType(@RequestBody DlCourseEntity bean){
        List<DlCourseEntity> list = dlCourseService.getListByClassType(bean.getGrade());
        if(list == null){
            return DataResult.fail("年级不能为空，请重新选择");
        }
        return DataResult.success(list);
    }


}
