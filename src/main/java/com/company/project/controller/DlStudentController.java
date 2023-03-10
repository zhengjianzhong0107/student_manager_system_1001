package com.company.project.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.company.project.common.utils.DataResult;
import com.company.project.common.utils.DateUtils;
import com.company.project.common.utils.ExcelUtils;
import com.company.project.entity.DlClassEntity;
import com.company.project.entity.DlStudentEntity;
import com.company.project.entity.SysDictDetailEntity;
import com.company.project.service.DlClassService;
import com.company.project.service.DlStudentService;
import com.company.project.service.SysDictService;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;


/**
 * 
 *
 * @author wenbin
 * @email *****@mail.com
 * @date 2020-12-04 18:03:47
 */
@Controller
@RequestMapping("/")
public class DlStudentController {
    @Autowired
    private DlStudentService dlStudentService;
    @Autowired
    private DlClassService dlClassService;

    @Autowired
    private SysDictService dictService;


    /**
    * 跳转到页面
    */
    @GetMapping("/index/dlStudent")
    public String dlStudent() {
        return "dlstudent/list";
        }

    @ApiOperation(value = "新增")
    @PostMapping("dlStudent/add")
    @RequiresPermissions("dlStudent:add")
    @ResponseBody
    public DataResult add(@RequestBody DlStudentEntity dlStudent){
        DlClassEntity bean = dlClassService.getById(dlStudent.getClassId());
        if(!StringUtils.isEmpty(bean.getGradeId())){
            dlStudent.setGradeId(bean.getGradeId());
        }
        dlStudentService.save(dlStudent);
        return DataResult.success();
    }

    @ApiOperation(value = "删除")
    @DeleteMapping("dlStudent/delete")
    @RequiresPermissions("dlStudent:delete")
    @ResponseBody
    public DataResult delete(@RequestBody @ApiParam(value = "id集合") List<String> ids){
        dlStudentService.removeByIds(ids);
        return DataResult.success();
    }

    @ApiOperation(value = "更新")
    @PutMapping("dlStudent/update")
    @RequiresPermissions("dlStudent:update")
    @ResponseBody
    public DataResult update(@RequestBody DlStudentEntity dlStudent){
        dlStudentService.updateById(dlStudent);
        return DataResult.success();
    }

    @ApiOperation(value = "导入学生信息")
    @PostMapping("dlStudent/export")
    @RequiresPermissions("dlStudent:export")
    @ResponseBody
    public DataResult export(@RequestParam(value = "file") MultipartFile file){
        List<Map<Integer, String>> mapList = ExcelUtils.read(file);
        DlStudentEntity bean = null;
        List<DlStudentEntity> insertBeanList = new ArrayList<>();
        List<String> noInsertBeanList = new ArrayList<>();
        List<SysDictDetailEntity> joinModeList = dictService.getListByType("join_mode", true);
        List<SysDictDetailEntity> nativeTypeList = dictService.getListByType("native_type", true);
        mapList.remove(0);
        for(Map<Integer, String> map: mapList){
            bean = new DlStudentEntity();
            bean.setSName(map.get(0));
            bean.setSsNo(map.get(1));
            bean.setIdCard(map.get(2));
            bean.setGender(map.get(3).trim().equals("男") ? 1: 2);
            bean.setNativePlace(map.get(4));
            bean.setNation(map.get(5));
            bean.setHomeAddress(map.get(6));
            bean.setMatherPhone(map.get(7));
            bean.setBirthday(DateUtils.stringToDate(map.get(8).trim(), "yyyyMMdd"));
            bean.setHealthy(map.get(10));
            bean.setAddress(map.get(11));
            if(map.get(12).trim().length() == 6)
                bean.setJoinTime(DateUtils.stringToDate(map.get(12).trim(), "yyyyMM"));
            else
                bean.setJoinTime(DateUtils.stringToDate(map.get(12).trim(), "yyyyMMdd"));

            bean.setJoinMode(getDictValueByString(joinModeList, map.get(13), 0));
            bean.setStudyMethod(map.get(14));
            bean.setContactAddress(map.get(15));
            bean.setNativeType(getDictValueByString(nativeTypeList, map.get(16), 0));
            bean.setNowAddress(map.get(17));
            bean.setStatus(1);
            DlClassEntity clazz = dlClassService.getBeanByStr(map.get(9));
            if(clazz == null){
                noInsertBeanList.add(map.get(0) + "--" + map.get(9));
            }else{
                bean.setClassId(clazz.getId());
                insertBeanList.add(bean);
            }

        }

        if(noInsertBeanList.size() > 0){
            return DataResult.fail("插入错误，存在数据不对", noInsertBeanList);
        }else{
            for(DlStudentEntity s: insertBeanList){
                dlStudentService.save(s);
            }
        }
        return DataResult.success();
    }

    private Integer getDictValueByString(List<SysDictDetailEntity> list, String label, Integer defaultValue){
        if(!label.trim().isEmpty()){
            for(SysDictDetailEntity d: list){
                if(d.getLabel().equals(label.trim())){
                    return Integer.parseInt(d.getValue());
                }
            }
        }
        return defaultValue;
    }

    @ApiOperation(value = "查询在校生分页数据")
    @PostMapping("dlStudent/listByPage")
    @RequiresPermissions("dlStudent:list")
    @ResponseBody
    public DataResult findListByPage(@RequestBody DlStudentEntity dlStudent){
        Page page = new Page(dlStudent.getPage(), dlStudent.getLimit());
        LambdaQueryWrapper<DlStudentEntity> queryWrapper = Wrappers.lambdaQuery();
        //查询条件示例
        queryWrapper.eq(DlStudentEntity::getStatus, "1");//获取在校学生信息
        if(!StringUtils.isEmpty(dlStudent.getGradeId())){
            queryWrapper.eq(DlStudentEntity::getGradeId,dlStudent.getGradeId());
        }
        if(!StringUtils.isEmpty(dlStudent.getClassId())){
            queryWrapper.eq(DlStudentEntity::getClassId, dlStudent.getClassId());
        }

        if(!StringUtils.isEmpty(dlStudent.getSName())){
            queryWrapper.like(DlStudentEntity::getSName, dlStudent.getSName());
        }
        if(!StringUtils.isEmpty(dlStudent.getSNo())){
            queryWrapper.eq(DlStudentEntity::getSNo, dlStudent.getSNo());
        }
        if(!StringUtils.isEmpty(dlStudent.getSsNo())){
            queryWrapper.like(DlStudentEntity::getSsNo, dlStudent.getSsNo());
        }
        IPage<DlStudentEntity> iPage = dlStudentService.page(page, queryWrapper);
        return DataResult.success(iPage);
    }

    @ApiOperation(value = "查询非在校生分页数据")
    @PostMapping("dlStudent/notInSchoolListByPage")
    @RequiresPermissions("dlStudent:list")
    @ResponseBody
    public DataResult findNotInSchoolStudentListByPage(@RequestBody DlStudentEntity dlStudent){
        Page page = new Page(dlStudent.getPage(), dlStudent.getLimit());
        LambdaQueryWrapper<DlStudentEntity> queryWrapper = Wrappers.lambdaQuery();
        //查询条件示例
        queryWrapper.gt(DlStudentEntity::getStatus, "1");//获取在校学生信息

        IPage<DlStudentEntity> iPage = dlStudentService.page(page, queryWrapper);
        return DataResult.success(iPage);
    }

}
