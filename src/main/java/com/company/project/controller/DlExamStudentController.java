package com.company.project.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.company.project.common.utils.DataResult;
import com.company.project.common.utils.ExcelUtils;
import com.company.project.entity.*;
import com.company.project.service.*;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 * 
 *
 * @author hui
 * @email *****@mail.com
 * @date 2021-01-07 10:17:13
 */
@Controller
@RequestMapping("/")
public class DlExamStudentController {
    @Autowired
    private DlExamStudentService dlExamStudentService;
    @Autowired
    private DlExamPlanService dlExamPlanService;
    @Autowired
    private DlClassService dlClassService;
    @Autowired
    private DlExamRoomService dlExamRoomService;
    @Autowired
    private DlGradeService dlGradeService;

    /**
    * 跳转到页面
    */
    @GetMapping("/index/dlExamStudent")
    public String dlExamStudent() {
        return "dlexamstudent/list";
        }

    @ApiOperation(value = "新增")
    @PostMapping("dlExamStudent/add")
    @RequiresPermissions("dlExamStudent:add")
    @ResponseBody
    public DataResult add(@RequestBody DlExamStudentEntity dlExamStudent){
        dlExamStudentService.save(dlExamStudent);
        return DataResult.success();
    }

    @ApiOperation(value = "删除")
    @DeleteMapping("dlExamStudent/delete")
    @RequiresPermissions("dlExamStudent:delete")
    @ResponseBody
    public DataResult delete(@RequestBody @ApiParam(value = "id集合") List<String> ids){
        dlExamStudentService.removeByIds(ids);
        return DataResult.success();
    }

    @ApiOperation(value = "更新")
    @PutMapping("dlExamStudent/update")
    @RequiresPermissions("dlExamStudent:update")
    @ResponseBody
    public DataResult update(@RequestBody DlExamStudentEntity dlExamStudent){
        dlExamStudentService.updateById(dlExamStudent);
        return DataResult.success();
    }

    @ApiOperation(value = "查询分页数据")
    @PostMapping("dlExamStudent/listByPage")
    @RequiresPermissions("dlExamStudent:list")
    @ResponseBody
    public DataResult findListByPage(@RequestBody DlExamStudentEntity dlExamStudent){
        Page page = new Page(dlExamStudent.getPage(), dlExamStudent.getLimit());
        LambdaQueryWrapper<DlExamStudentEntity> queryWrapper = Wrappers.lambdaQuery();
        //查询条件示例
        //queryWrapper.eq(DlExamStudentEntity::getId, dlExamStudent.getId());
        IPage<DlExamStudentEntity> iPage = dlExamStudentService.page(page, queryWrapper);
        return DataResult.success(iPage);
    }

    @ApiOperation(value = "查询分页数据")
    @GetMapping("dlExamStudent/export")
    @RequiresPermissions("dlExamResult:list")
    public void export(@RequestParam String examId, HttpServletResponse response){
        LambdaQueryWrapper<DlExamStudentEntity> queryWrapper = Wrappers.lambdaQuery();
        queryWrapper.eq(DlExamStudentEntity::getExamPlanId, examId);
        queryWrapper.orderByAsc(true, DlExamStudentEntity::getClassId);
        List<DlExamStudentEntity> list = dlExamStudentService.list(queryWrapper);

        LambdaQueryWrapper<DlExamRoomEntity> roomWrapper = Wrappers.lambdaQuery();
        roomWrapper.eq(DlExamRoomEntity::getExamId, examId);
        List<DlExamRoomEntity> room_list = dlExamRoomService.list(roomWrapper);
        //创建工作薄对象
        HSSFWorkbook workbook=new HSSFWorkbook();
        //创建工作表对象
        HSSFSheet sheet = null;
        DlClassEntity classEntity = null;
        Map<String, List<DlExamStudentEntity>> map = new HashMap<>();
        Map<String, String> classNameMap = new HashMap<>();
        List<DlExamStudentEntity> stuList = null;
        for(DlExamStudentEntity bean: list){
            if(map.get(bean.getClassId()) == null){//如果在map中没有找到这样的classID，那么就证明是一个新的班级，就去查询该班级的信息，并保存在map中
                classEntity = dlClassService.getById(bean.getClassId());
                stuList = new ArrayList<>();
                stuList.add(bean);
                map.put(bean.getClassId(), stuList);
                classNameMap.put(bean.getClassId(), classEntity.getClassNo() + "班");
            }else{
                stuList = map.get(bean.getClassId());
                stuList.add(bean);
            }
        }

        Map<String, String> roomNameMap = new HashMap<>();
        for(DlExamRoomEntity room : room_list){
            roomNameMap.put(room.getId(), room.getRoomName() + "(" + room.getClassRoomName()+ ")");
        }

        int i = 0;//用于控制创建表格
        int count = 0;//用户控制数量
        HSSFRow row = null;
        String room_name = "";
        for(String key: map.keySet()){
            count = 0;
            sheet = workbook.createSheet();
            ExcelUtils.setColWidth(6, sheet, 1.8);

            workbook.setSheetName(i, classNameMap.get(key));//设置表格的名称
            row = sheet.createRow(0);
            row.createCell(0).setCellValue("序号");
            row.createCell(1).setCellValue("学生学号");
            row.createCell(2).setCellValue("学生姓名");
            row.createCell(3).setCellValue("考号");
            row.createCell(4).setCellValue("座位号");
            row.createCell(5).setCellValue("考场（教室）");

            stuList = map.get(key);
            for(DlExamStudentEntity bean: stuList){
                row = sheet.createRow(count + 1);
                row.createCell(0).setCellValue(count + 1);
                row.createCell(1).setCellValue(bean.getStuNum());
                row.createCell(2).setCellValue(bean.getStuName());
                row.createCell(3).setCellValue(bean.getStuExamNum());
                row.createCell(4).setCellValue(bean.getStuSeatNum());
                row.createCell(5).setCellValue(roomNameMap.get(bean.getExamRoomId()));
                count ++;
            }

            i++;
        }
        DlExamPlanEntity byId = dlExamPlanService.getById(examId);
        DlGradeEntity grade = dlGradeService.getById(byId.getGradeId());
        ExcelUtils.buildExcelDocument(grade.getGradeNameLabel() + "-学生考试安排信息表", workbook, response);
    }

}
