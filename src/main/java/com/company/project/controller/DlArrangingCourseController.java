package com.company.project.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.company.project.common.utils.DataResult;
import com.company.project.entity.DlArrGradeEntity;
import com.company.project.entity.DlArrangingCourseEntity;
import com.company.project.entity.DlGradeEntity;
import com.company.project.service.DlArrGradeService;
import com.company.project.service.DlArrangingCourseService;
import com.company.project.service.DlGradeService;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.Iterator;
import java.util.List;



/**
 * 
 *
 * @author hui
 * @email *****@mail.com
 * @date 2021-01-19 15:22:38
 */
@Controller
@RequestMapping("/")
public class DlArrangingCourseController {
    @Autowired
    private DlArrangingCourseService dlArrangingCourseService;

    @Autowired
    private DlArrGradeService dlArrGradeService;
    @Autowired
    private DlGradeService dlGradeService;

    /**
    * 跳转到页面
    */
    @GetMapping("/index/dlArrangingCourse")
    public String dlArrangingCourse() {
        return "dlarrangingcourse/list";
        }

    @ApiOperation(value = "新增")
    @PostMapping("dlArrangingCourse/add")
    @RequiresPermissions("dlArrangingCourse:add")
    @ResponseBody
    public DataResult add(@RequestBody DlArrangingCourseEntity dlArrangingCourse){
        String[] gradeIds = dlArrangingCourse.getGradeIds();
        dlArrangingCourseService.save(dlArrangingCourse);
        DlArrGradeEntity bean = null;
        for (String id: gradeIds){
            bean = new DlArrGradeEntity();
            bean.setGradeId(id);
            DlGradeEntity gradeEntity = dlGradeService.getById(id);
            if(gradeEntity != null)
                bean.setGradeName(gradeEntity.getGradeNameLabel());
            bean.setArrId(dlArrangingCourse.getId());
            bean.setTermId(dlArrangingCourse.getTermId());
            dlArrGradeService.save(bean);
        }


        return DataResult.success(dlArrangingCourse);
    }

    @ApiOperation(value = "删除")
    @DeleteMapping("dlArrangingCourse/delete")
    @RequiresPermissions("dlArrangingCourse:delete")
    @ResponseBody
    public DataResult delete(@RequestBody @ApiParam(value = "id集合") List<String> ids){
        dlArrangingCourseService.removeByIds(ids);
        return DataResult.success();
    }

    @ApiOperation(value = "更新")
    @PutMapping("dlArrangingCourse/update")
    @RequiresPermissions("dlArrangingCourse:update")
    @ResponseBody
    public DataResult update(@RequestBody DlArrangingCourseEntity dlArrangingCourse){
        LambdaQueryWrapper<DlArrGradeEntity> wrapper = Wrappers.lambdaQuery();
        wrapper.eq(DlArrGradeEntity::getArrId, dlArrangingCourse.getId());
        List<DlArrGradeEntity> list = dlArrGradeService.list(wrapper);
        boolean bool = false;//是否在gradeIds中存在该ID

        for (String id: dlArrangingCourse.getGradeIds()){
            bool = false;
            Iterator<DlArrGradeEntity> iterator = list.iterator();
            while (iterator.hasNext()){
                DlArrGradeEntity g = iterator.next();
                if(id != null && id.equals(g.getGradeId())){
                    bool = true;
                    //从list中移除该元素
                    iterator.remove();
                    break;
                }
            }
            if(!bool){//未找到就新增
                DlArrGradeEntity bean = new DlArrGradeEntity();
                bean.setGradeId(id);
                DlGradeEntity gradeEntity = dlGradeService.getById(id);
                if(gradeEntity != null)
                    bean.setGradeName(gradeEntity.getGradeNameLabel());
                bean.setArrId(dlArrangingCourse.getId());
                bean.setTermId(dlArrangingCourse.getTermId());
                dlArrGradeService.save(bean);
            }

        }
        list = dlArrGradeService.list(wrapper);
        for(DlArrGradeEntity bean: list){
            bool = false;
            for (String id: dlArrangingCourse.getGradeIds()){
                if(id.equals(bean.getGradeId())){
                    bool = true;
                    break;
                }
            }
            if(!bool){
                dlArrGradeService.removeById(bean.getId());
            }
        }


        dlArrangingCourseService.updateById(dlArrangingCourse);
        return DataResult.success();
    }

    @ApiOperation(value = "查询分页数据")
    @PostMapping("dlArrangingCourse/listByPage")
    @RequiresPermissions("dlArrangingCourse:list")
    @ResponseBody
    public DataResult findListByPage(@RequestBody DlArrangingCourseEntity dlArrangingCourse){
        Page page = new Page(dlArrangingCourse.getPage(), dlArrangingCourse.getLimit());
        LambdaQueryWrapper<DlArrangingCourseEntity> queryWrapper = Wrappers.lambdaQuery();
        //查询条件示例
        //queryWrapper.eq(DlArrangingCourseEntity::getId, dlArrangingCourse.getId());
        IPage<DlArrangingCourseEntity> iPage = dlArrangingCourseService.page(page, queryWrapper);
        return DataResult.success(iPage);
    }

    @ApiOperation(value = "智能排课")
    @PostMapping("dlArrangingCourse/arrCourse")
    @RequiresPermissions("dlArrangingCourse:arrCourse")
    @ResponseBody
    public DataResult arrCourse(@RequestBody DlArrangingCourseEntity dlArrangingCourse){
        return dlArrangingCourseService.autoArrayCourse(dlArrangingCourse.getId());
    }




}
