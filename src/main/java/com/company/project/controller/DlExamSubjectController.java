package com.company.project.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.company.project.common.utils.DataResult;
import com.company.project.entity.DlExamSubjectEntity;
import com.company.project.service.DlExamSubjectService;
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
 * @date 2020-12-04 18:03:46
 */
@Controller
@RequestMapping("/")
public class DlExamSubjectController {
    @Autowired
    private DlExamSubjectService dlExamSubjectService;


    /**
    * 跳转到页面
    */
    @GetMapping("/index/dlExamSubject")
    public String dlExamSubject() {
        return "dlexamsubject/list";
        }

    @ApiOperation(value = "新增")
    @PostMapping("dlExamSubject/add")
    @RequiresPermissions("dlExamSubject:add")
    @ResponseBody
    public DataResult add(@RequestBody DlExamSubjectEntity dlExamSubject){
        dlExamSubjectService.save(dlExamSubject);
        return DataResult.success();
    }

    @ApiOperation(value = "删除")
    @DeleteMapping("dlExamSubject/delete")
    @RequiresPermissions("dlExamSubject:delete")
    @ResponseBody
    public DataResult delete(@RequestBody @ApiParam(value = "id集合") List<String> ids){
        dlExamSubjectService.removeByIds(ids);
        return DataResult.success();
    }

    @ApiOperation(value = "删除")
    @DeleteMapping("dlExamSubject/deleteByCourseId")
    @RequiresPermissions("dlExamSubject:delete")
    @ResponseBody
    public DataResult deleteByCourseId(@RequestBody DlExamSubjectEntity bean){
        LambdaQueryWrapper<DlExamSubjectEntity> queryWrapper = Wrappers.lambdaQuery();
        queryWrapper.eq(DlExamSubjectEntity::getCourseId, bean.getCourseId());
        queryWrapper.eq(DlExamSubjectEntity::getExamId, bean.getExamId());
        bean = dlExamSubjectService.getOne(queryWrapper);
        if(bean == null){
            return DataResult.fail("未找到相关信息");
        }
        dlExamSubjectService.removeById(bean.getId());
        return DataResult.success();
    }

    @ApiOperation(value = "更新")
    @PutMapping("dlExamSubject/update")
    @RequiresPermissions("dlExamSubject:update")
    @ResponseBody
    public DataResult update(@RequestBody DlExamSubjectEntity dlExamSubject){
        System.out.println(dlExamSubject);

        //检查时间段是否存在重复
        if(StringUtils.isEmpty(dlExamSubject.getExamId())){
            return DataResult.fail("考试计划主键缺失");
        }
        if(dlExamSubject.getExamDate() == null || dlExamSubject.getStartTime() == null || dlExamSubject.getEndTime() == null){
            return DataResult.fail("信息填写不完整,请填写完整");
        }
        if(dlExamSubjectService.timeIsOverlap(dlExamSubject)){
            return DataResult.fail("考试时间是上存在冲突，请确认后再试");
        }

        dlExamSubjectService.updateById(dlExamSubject);
        return DataResult.success();
    }

    /**
     * 检查是否已经设置完毕--考试时间设置
     * @param dlExamSubject
     * @return
     */
    @PostMapping("dlExamSubject/isComplete")
    @RequiresPermissions("dlExamSubject:list")
    @ResponseBody
    public DataResult checkSetIsComplete(@RequestBody DlExamSubjectEntity dlExamSubject){
        return DataResult.success(dlExamSubjectService.checkSetIsComplete(dlExamSubject.getExamId()));
    }

    @ApiOperation(value = "查询分页数据")
    @PostMapping("dlExamSubject/listByPage")
    @RequiresPermissions("dlExamSubject:list")
    @ResponseBody
    public DataResult findListByPage(@RequestBody DlExamSubjectEntity dlExamSubject){
        Page page = new Page(dlExamSubject.getPage(), dlExamSubject.getLimit());
        LambdaQueryWrapper<DlExamSubjectEntity> queryWrapper = Wrappers.lambdaQuery();
        if(StringUtils.isEmpty(dlExamSubject.getExamId())){
            return DataResult.success();
        }
        //查询条件示例
        queryWrapper.eq(DlExamSubjectEntity::getExamId, dlExamSubject.getExamId());
        IPage<DlExamSubjectEntity> iPage = dlExamSubjectService.page(page, queryWrapper);
        return DataResult.success(iPage);
    }

}
