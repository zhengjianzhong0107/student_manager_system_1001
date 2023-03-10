package com.company.project.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.company.project.common.utils.DataResult;
import com.company.project.entity.DlClassTeacherEntity;
import com.company.project.service.DlClassTeacherService;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

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
public class DlClassTeacherController {
    @Autowired
    private DlClassTeacherService dlClassTeacherService;


    /**
    * 跳转到页面
    */
    @GetMapping("/index/dlClassTeacher")
    public String dlClassTeacher() {
        return "dlclassteacher/list";
        }

    @ApiOperation(value = "新增")
    @PostMapping("dlClassTeacher/add")
    @RequiresPermissions("dlClassTeacher:add")
    @ResponseBody
    public DataResult add(@RequestBody DlClassTeacherEntity dlClassTeacher){
        if(hasBean(dlClassTeacher)){
            return DataResult.fail("课程安排重复，请仔细检查");
        }
        dlClassTeacherService.save(dlClassTeacher);
        return DataResult.success();
    }

    @ApiOperation(value = "删除")
    @DeleteMapping("dlClassTeacher/delete")
    @RequiresPermissions("dlClassTeacher:delete")
    @ResponseBody
    public DataResult delete(@RequestBody @ApiParam(value = "id集合") List<String> ids){
        dlClassTeacherService.removeByIds(ids);
        return DataResult.success();
    }

    @ApiOperation(value = "更新")
    @PutMapping("dlClassTeacher/update")
    @RequiresPermissions("dlClassTeacher:update")
    @ResponseBody
    public DataResult update(@RequestBody DlClassTeacherEntity dlClassTeacher){
        if(hasBean(dlClassTeacher)){
            return DataResult.fail("课程安排重复，请仔细检查");
        }
        dlClassTeacherService.updateById(dlClassTeacher);
        return DataResult.success();
    }

    public boolean hasBean(DlClassTeacherEntity bean){
        LambdaQueryWrapper<DlClassTeacherEntity> queryWrapper = Wrappers.lambdaQuery();
        queryWrapper.eq(DlClassTeacherEntity::getClassId, bean.getClassId());
        queryWrapper.eq(DlClassTeacherEntity::getCourseId, bean.getCourseId());
        List<DlClassTeacherEntity> list = dlClassTeacherService.list(queryWrapper);
        if(list.size() > 1){
            return true;
        }else if(list.size() == 1){
            if(list.get(0).getId().equals(bean.getId())){//检查是否是本数据在修改，进行查重的时候，需要放行
                return false;
            }else{
                return true;
            }
        }


        return false;
    }

    @ApiOperation(value = "查询分页数据")
    @PostMapping("dlClassTeacher/listByPage")
    @RequiresPermissions("dlClassTeacher:list")
    @ResponseBody
    public DataResult findListByPage(@RequestBody DlClassTeacherEntity dlClassTeacher){
        Page page = new Page(dlClassTeacher.getPage(), dlClassTeacher.getLimit());
        LambdaQueryWrapper<DlClassTeacherEntity> queryWrapper = Wrappers.lambdaQuery();
        //查询条件示例
        //queryWrapper.eq(DlClassTeacherEntity::getId, dlClassTeacher.getId());
        if(!StringUtils.isEmpty(dlClassTeacher.getClassId())){
            queryWrapper.eq(DlClassTeacherEntity::getClassId, dlClassTeacher.getClassId());
        }
        if(!StringUtils.isEmpty(dlClassTeacher.getTermId())){
            queryWrapper.eq(DlClassTeacherEntity::getTermId, dlClassTeacher.getTermId() );
        }
        if(!StringUtils.isEmpty(dlClassTeacher.getTeacherId())){
            queryWrapper.eq(DlClassTeacherEntity::getTeacherId, dlClassTeacher.getTeacherId());
        }

        IPage<DlClassTeacherEntity> iPage = dlClassTeacherService.page(page, queryWrapper);
        return DataResult.success(iPage);
    }

}
