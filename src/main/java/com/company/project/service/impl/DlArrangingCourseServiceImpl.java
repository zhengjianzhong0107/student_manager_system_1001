package com.company.project.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.company.project.common.utils.*;
import com.company.project.entity.*;
import com.company.project.mapper.DlArrBaseMapper;
import com.company.project.mapper.DlArrSetMapper;
import com.company.project.mapper.DlArrangingCourseMapper;
import com.company.project.mapper.DlCourseTableMapper;
import com.company.project.service.DlArrangingCourseService;
import lombok.Synchronized;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


@Service("dlArrangingCourseService")
public class DlArrangingCourseServiceImpl extends ServiceImpl<DlArrangingCourseMapper, DlArrangingCourseEntity> implements DlArrangingCourseService {
    @Resource
    private DlArrangingCourseMapper mapper;
    @Resource
    private DlArrBaseMapper dlArrBaseMapper;
    @Resource
    private DlArrSetMapper dlArrSetMapper;
    @Resource
    private DlCourseTableMapper dlCourseTableMapper;




    //-------------------------------排课需要的变量---------------------------
    private SnowflakeIdWorker idWorker = new SnowflakeIdWorker(1,1);

    private static final int DEPTH = 5;


    private List<DlArrSetEntity> teacherArrSetList;
    private List<DlArrSetEntity> courseArrSetList;
    private List<DlArrSetEntity> classArrSetList;
    private List<DlArrSetEntity> allArrSetList;
    private List<DlArrSetEntity> preArrSetList;
    private DlArrangingCourseEntity arrBean;


    private Map<String, DlArrBaseEntity> baseMap;
    private List<DlArrBaseEntity> baseEntityList;

    private Index<DlCourseTableEntity> teacherIndex;
    private Index<DlCourseTableEntity> classIndex;
    private Index<DlCourseTableEntity> courseIndex;
    private Index<DlCourseTableEntity> roomIndex;
    private Map<String, DlCourseTableEntity> courseTable;
    private Integer dayNum;//周上课天数
    private Integer amNum;//上午课时数
    private Integer pmNum;//下午课时数
    private Integer nightNum;//晚上课时数

    private List<DlArrBaseEntity> noInsertList;

//    private Map<String, >
    //-----------------------------排课需要的变量end---------------------------------

    private void init(String arrId){
        //先获取排课任务信息
        arrBean = mapper.selectById(arrId);
        DlArrSetEntity setEntity = new DlArrSetEntity();
        setEntity.setArrId(arrId);
        //获取整体不排课
        setEntity.setType(0);
        allArrSetList = dlArrSetMapper.list(setEntity);

        //获取教师不排课
        setEntity.setType(1);
        teacherArrSetList = dlArrSetMapper.list(setEntity);

        //获取班级不排课
        setEntity.setType(2);
        classArrSetList = dlArrSetMapper.list(setEntity);

        //获取课程不排课
        setEntity.setType(3);
        courseArrSetList = dlArrSetMapper.list(setEntity);

        //获取预先排课列表
        setEntity.setType(4);
        preArrSetList = dlArrSetMapper.list(setEntity);

        //获取需要排课班级课程列表信息
        LambdaQueryWrapper<DlArrBaseEntity> queryWrapper = Wrappers.lambdaQuery();
        queryWrapper.eq(DlArrBaseEntity::getArrId, arrId);
        queryWrapper.orderByAsc(DlArrBaseEntity::getCourseType);
        baseEntityList = dlArrBaseMapper.selectList(queryWrapper);
        baseMap = MapUtils.getMapByList(baseEntityList);


        /**
         * 排课规则
         * 1.课程尽量分散，不要一门课程在一天就排完了
         * 2.不排课的优先级为1 预排课的优先级为2
         * 3.主课尽量在上午，副课尽量排在下午，普通科尽量排下午，晚上智能排主课和副课 优先级为3
         * 最终不能满足的情况保存到消息中去
         */
        //教师索引-- 以教师ID为key
        teacherIndex = new Index<>();
        //班级索引-- 以班级ID为key
        classIndex = new Index<>();
        //课程索引---》班级课程 --以DlArrBase的ID为key
        courseIndex = new Index<>();
        //教室索引-- 以教室ID为key
        roomIndex = new Index<>();

        //所有排课信息----》key为一个唯一标识的UUID
        courseTable = new HashMap<>();

        amNum = arrBean.getAmNum();
        pmNum = arrBean.getPmNum();
        nightNum = arrBean.getNightNum();
        dayNum = arrBean.getWeekDayNum();
        noInsertList = new ArrayList<>();
    }

    /**
     * 智能排课
     * 5666222  5516666
     * 该排课算法是模拟手工排课
     * @param arrId
     * @return
     */
    @Override
    @Synchronized
    public DataResult autoArrayCourse(String arrId) {
        //删除之前的排课信息
        dlCourseTableMapper.deleteByArrId(arrId);


        init(arrId);//初始化数据模型
        Map<String, List<String>> map = findOutOfCourseNumList(arrId);//查询超课现象
        if(map.get("teacher").size() > 0 || map.get("class").size() > 0){
            return DataResult.fail("排课失败，存在超课现象", map);
        }

        for(DlArrBaseEntity bean: baseEntityList) {
            bean.setSupCourseNum(bean.getCourseNum());
        }



        DlCourseTableEntity tableBean = null;
        DlArrBaseEntity baseBean = null;
        //-------------------------------先将预排课排进系统------------------------------
        for(DlArrSetEntity bean: preArrSetList){
            //预排课中的typeId是保存的DlArrBaseEntity数据中的ID值
            baseBean = baseMap.get(bean.getTypeId());
            if(baseBean != null){
                insertCourseTableBean(baseBean,bean.getCol(),bean.getRow(),2);
            }
            if(baseBean.getSupCourseNum() > 0){
                baseBean.setStatus(2);
            }else{
                baseBean.setStatus(3);
            }
            dlArrBaseMapper.updateById(baseBean);
        }
        //--------------------------------预排课end-------------------------------


        //--------------------------------规则排课--------------------------------
        for(DlArrBaseEntity bean: baseEntityList){

            System.out.println(baseBean.getTeacherName() + "--" + baseBean.getClassName() + "--" + baseBean.getCourseName() + "--baseBean.getSupCourseNum():" + baseBean.getSupCourseNum());

            //检查总课时数，好进行均匀分配到每天去
            int totalNum = bean.getCourseNum();
            int maxNum = totalNum / dayNum + 1;//每天最多上课数

            int num = bean.getSupCourseNum();
            //检查该课程信息的类型

            for(int i = 0; i < num; i ++){//将该课程的所有课全部排了
                int row = NumberUtils.random(1, amNum);
                int col = NumberUtils.random(1, dayNum);//随机一个坐标
                int tempCol = col;
                int tempRow = row;

                //先在上午找位置
                if(arrCourseInTable(col, row, amNum, 0, maxNum, 3, bean)){
                    continue;
                }

                //再去下午找位置
                col = tempCol;
                row = NumberUtils.random(amNum + 1, amNum + pmNum);
                if(arrCourseInTable(col, row, pmNum, amNum, maxNum, 4, bean)){
                    continue;
                }

                //再去晚上找位置
                if(nightNum != 0 && bean.getCourseType() != 3){//判断晚上是否排课，并检测是否是普通课程，普通课程将不进行晚上排课
                    col = tempCol;
                    row = amNum + pmNum + 1;
                    if(arrCourseInTable(col, row, nightNum, amNum + pmNum, maxNum, 3, bean)){
                        continue;
                    }
                }
                //还是没有找到，就找位置换----遍历该班级中所有的教师的课表，进行调整课表
                Element element = findAndExchangeArrPosition(bean.getId(), bean.getClassId(), 1, 2, 3, maxNum, DEPTH);
                if(element != null){//找到排课位置了，那就排课
                    insertCourseTableBean(bean, element.getCol(), element.getRow(), 3);
                }else{
                    //如果也没有找到能换的，就将该信息保存到异常信息中，该课程不能再进行排课了
                    noInsertList.add(bean);

                }

            }

            if(bean.getSupCourseNum() > 0){
                bean.setStatus(2);
            }else{
                bean.setStatus(3);
            }
            dlArrBaseMapper.updateById(bean);
        }
        //--------------------------------规则排课end-----------------------------

        System.out.println("courseTable.keySet().size():" + courseTable.keySet().size());
        System.out.println("noInsertList:" + noInsertList);

        //将课程信息保存到数据库中
        for (String key: courseTable.keySet()){
            tableBean = courseTable.get(key);
            tableBean.setId(null);
            dlCourseTableMapper.insert(tableBean);
        }
        if(noInsertList.size() > 0){
            return DataResult.success(1);//存在未能排课数量
        }
        return DataResult.success(0);
    }


    /**
     * 检验班级课程和教师课程是否已经超课----------------？？？？？？？？？？？？？？？？？？
     * 上面查教师不排课时间，也需要统计一个学期的所有排课信息
     * @return
     */
    private Map<String, List<String>> findOutOfCourseNumList(String arrId){
        //先将不排课的教师的不排课数量确定，放入Map中
        Map<String, Integer> teacherNoArrCourseNum = new HashMap<>();
        for(DlArrSetEntity bean: teacherArrSetList){
            Integer num = teacherNoArrCourseNum.get(bean.getTypeId());
            if(num == null){
                teacherNoArrCourseNum.put(bean.getTypeId(), 1);
            }else{
                teacherNoArrCourseNum.put(bean.getTypeId(), num + 1);
            }
        }
        for(DlArrSetEntity bean: courseArrSetList){
            DlArrBaseEntity base = baseMap.get(bean.getTypeId());
            if(base != null){
                Integer num = teacherNoArrCourseNum.get(base.getTeacherId());
                if(num == null){
                    teacherNoArrCourseNum.put(base.getTeacherId(), 1);
                }else{
                    teacherNoArrCourseNum.put(base.getTeacherId(), num + 1);
                }
            }
        }


        //应该加上已经排课的
        List<DlCourseTableEntity> courseTableEntities = dlCourseTableMapper.getTermCountGroupByTeacherId(arrBean.getTermId());
        for (DlCourseTableEntity c: courseTableEntities){
            Integer num = teacherNoArrCourseNum.get(c.getTeacherId());
            if(num != null){
                teacherNoArrCourseNum.put(c.getTeacherId(), num + c.getWeek());
            }else{
                teacherNoArrCourseNum.put(c.getTeacherId(), c.getWeek());
            }
        }


        List<DlArrBaseEntity> list = new ArrayList<>();
        //查找班级超课的列表
        //查出了所有参排教师的超课-----------------------后续这个位置可能需要修改，查询的时候通过学期来查
        // （如果出现一个学期多次排课，同时也存在一个教师在着多次排课中都有课程安排，就会出现问题）
        List<DlArrBaseEntity> teacherList = dlArrBaseMapper.getTeacherCourseNumList(arrId);
        Integer maxNum = (amNum + pmNum + nightNum) * dayNum;
        List<String> teacherOutOfNumNameList = new ArrayList<>();
        for(DlArrBaseEntity bean: teacherList){
            Integer num = teacherNoArrCourseNum.get(bean.getTeacherId());
            if(num ==  null){
                num = 0;
            }
            if (num + bean.getCourseNum() > maxNum){
                //到这里的就是表示已经超课了
                teacherOutOfNumNameList.add(bean.getTeacherName() + "(课程数：" + bean.getCourseNum() + ", 不排课数：" + num + ")");
            }
        }


        //获取班级不排课
        Map<String, Integer> classNoArrCourseNum = new HashMap<>();
        for(DlArrSetEntity bean: classArrSetList){
            if(classNoArrCourseNum.get(bean.getTypeId()) == null){
                classNoArrCourseNum.put(bean.getTypeId(), 1);
            }else
                classNoArrCourseNum.put(bean.getTypeId(), classNoArrCourseNum.get(bean.getTypeId()) + 1);
        }

        //查找教师超课列表
        List<DlArrBaseEntity> classList = dlArrBaseMapper.getClassCourseNumList(arrId);
        List<String> classOutOfNumNameList = new ArrayList<>();
        for (DlArrBaseEntity bean: classList){
            Integer num = classNoArrCourseNum.get(bean.getClassId());
            if(num == null)
                num = 0;
            if(bean.getCourseNum() + num > maxNum){
                //这里也是需要记录那些班级是超课时了
                classOutOfNumNameList.add(bean.getClassName() + "(课程数：" + bean.getCourseNum() + ", 不排课数：" + num + ")");

            }
        }
        Map<String, List<String>> map = new HashMap<>();
        map.put("class", classOutOfNumNameList);
        map.put("teacher", teacherOutOfNumNameList);


        return map;
    }




    /**
     *交换位置，进行排课，这个方法需要一层一层的进行筛查，直到找到或者找的深度结束为止
     * @param baseId
     * @param classId
     * @param first
     * @param second
     * @param third
     * @param depth 遍历的深度
     * @return 返回null表示未找到位置，找到了就返回上一层元素的坐标
     */
    public Element findAndExchangeArrPosition(String baseId, String classId, Integer first, Integer second, Integer third, Integer maxNum, Integer depth){
        //找到班级中的空位
        List<Element> emptyElement = getClassTableEmptyPositions(classId, first, second, third);

        //找到该教师在班级中可以排课的位置
        List<Element> allowElement = findAllowArrPositions(baseId, first, second, third, maxNum);//能安排的位置
        System.out.println("depth:" + depth);
        Map<String, String> conditionsMap = new HashMap<>();
        depth --;
        if(depth < 0){
            return null;
        }
        for (Element a : allowElement){
            conditionsMap.put("col", a.getCol().toString());
            conditionsMap.put("row", a.getRow().toString());
            //获取到该位置的排课信息，
            DlCourseTableEntity bean = classIndex.getOne(classId, conditionsMap);//这里待确认是否存在获取为空的情况
            //处理空的情况
            if(bean == null){
                continue;
            }
            System.out.println("findAndExchangeArrPosition:" + bean);
            //
            for (Element e : emptyElement){
                if(isOk(bean.getBaseId(), e.getCol(), e.getRow(), maxNum)){
                    Element element = new Element();
                    element.setCol(bean.getCol());//保存原有位置的坐标信息
                    element.setRow(bean.getRow());
                    bean.setCol(e.getCol());//更新坐标信息
                    bean.setRow(e.getRow());
                    return element;//返回找到了的可排课位置的的坐标
                }
            }

            Element element = findAndExchangeArrPosition(bean.getBaseId(), bean.getClassId(), first, second, third, maxNum, depth);
            if(element != null){
                Element e = new Element();
                e.setCol(bean.getCol());//保存原有位置的坐标信息
                e.setRow(bean.getRow());
                bean.setCol(element.getCol());//更新坐标信息
                bean.setRow(element.getRow());
                return e;
            }
        }
        return null;
    }





    /**
     * 查找能允许排课的位置
     * @param baseId
     * @return
     */
    private List<Element> findAllowArrPositions(String baseId, Integer first, Integer second, Integer third, Integer maxNum){
        ElementMap table = new ElementMap();
        for (int i = 1; i <= dayNum; i ++){
            for (int j = 1; j <= (amNum + pmNum + nightNum); j ++){
                if(isOk(baseId, i, j, maxNum)){
                    if(amNum >= j){
                        table.add(1, i, j);
                    }else if((amNum + pmNum) >= j){
                        table.add(2, i, j);
                    }else{
                        table.add(3, i, j);
                    }
                }
            }
        }
        List<Element> list = table.get(first);
        List<Element> tempList = null;
        if(list == null){
            list = new ArrayList<>();
        }
        if(second != null){
            tempList = table.get(second);
            if(tempList != null)
                list.addAll(tempList);
        }

        if(third != null){
            tempList = table.get(third);
            if(tempList != null)
                list.addAll(tempList);
        }

        return list;
    }






    /**
     * 获取班级课表中的空位
     * 返回一个二维数组，数组元素值为1的表示空位
     * 坐标1  +1表示col
     * 坐标2  +1表示row
     * @param classId
     * @return
     */
    private List<Element> getClassTableEmptyPositions(String classId, Integer first, Integer second, Integer third){
        return getArr(classIndex.get(classId),first, second, third);

    }

    /**
     * 获取教师课表中的空位
     * 返回一个二维数组，数组元素值为1的表示空位
     * 坐标1  +1表示col
     * 坐标2  +1表示row
     * @param teacherId
     * @return
     */
    private List<Element> getTeacherTableEmptyPositions(String teacherId, Integer first, Integer second, Integer third){
        return getArr(teacherIndex.get(teacherId), first, second, third);
    }


    private List<Element> getArr(List<DlCourseTableEntity> list, Integer first, Integer second, Integer third){
        int col = dayNum;
        int row = amNum + pmNum + nightNum;
        int arr[][] = new int[col][row];
        for (DlCourseTableEntity bean: list){
            arr[bean.getCol() - 1][bean.getRow() - 1] = 1;
        }
        ElementMap table = new ElementMap();//空位
        for (int i = 0; i < arr.length; i++){
            for (int j = 0; j < arr[i].length; j++){
                if(arr[i][j] == 1){
                    if(amNum >= (j + 1)){
                        table.add(1, i + 1, j + 1);
                    }else if((amNum + pmNum) >= (j + 1)){
                        table.add(2, i + 1, j + 1);
                    }else{
                        table.add(3, i + 1, j + 1);
                    }
                }
            }
        }
        List<Element> eList = table.get(first);
        List<Element> tempList = null;
        if(eList == null){
            eList = new ArrayList<>();
        }
        if(second != null){
            tempList = table.get(second);
            if(tempList != null)
                eList.addAll(tempList);
        }

        if(third != null){
            tempList = table.get(third);
            if(tempList != null)
                eList.addAll(tempList);
        }


        return eList;
    }





    /**
     * 插入课程信息
     * @param col
     * @param row
     * @param rowNum 该阶段的课时数，上午是4节课，就传4，如果是下午排课，就传下午的课时数
     * @param maxNum
     * @param level
     * @param baseBean
     * @return true：插入成功  false：插入失败
     */
    private boolean arrCourseInTable(Integer col, Integer row,
                                     Integer rowNum, Integer rowStartNum,
                                     Integer maxNum, Integer level,
                                     DlArrBaseEntity baseBean){
        Integer tempCol = col;
        Integer tempRow = row;
        do{
            do{
                if(isOk(baseBean,col,row,maxNum)){
                    insertCourseTableBean(baseBean,col,row,3);
                    return true;
                }
                col ++;
                if(col % dayNum != 0){
                    col = col % dayNum;
                }
                if(col == tempCol)
                    break;;
            }while (true);

            row ++;
            if(row > rowStartNum + rowNum){
                row -= rowNum;
            }
            if(row == tempRow){
                return false;
            }
        }while (true);
    }


    private boolean isOk(String baseId,
                         Integer col, Integer row, Integer maxNum){
        DlArrBaseEntity baseBean = baseMap.get(baseId);
        String classId = baseBean.getClassId();
        String teacherId = baseBean.getTeacherId();
        String roomId = baseBean.getRoomId();


        Map<String, String> classMap = new HashMap<>();
        classMap.put("row", row.toString());
        classMap.put("col", col.toString());
        //检查该位置是不是不排课位置
        //整体不排课
        for(DlArrSetEntity bean: allArrSetList){
            if(bean.getCol() == col && bean.getRow() == row){
                return false;
            }
        }

        //班级不排课检查
        for(DlArrSetEntity bean: classArrSetList){
            if(bean.getTypeId().equals(classId) && bean.getCol() == col && bean.getRow() == row){
                return false;
            }
        }

        //教师不排课检查
        for(DlArrSetEntity bean: teacherArrSetList){
            if(bean.getTypeId().equals(teacherId) && bean.getCol() == col && bean.getRow() == row){
                return false;
            }
        }
        //课程不排课检查
        for(DlArrSetEntity bean: courseArrSetList){
            if(bean.getTypeId().equals(baseId) && bean.getCol() == col && bean.getRow() == row){
                return false;
            }
        }

        //检查教师当前位置是否已经排课了
        int count = teacherIndex.getCount(baseBean.getTeacherId(), classMap);
        if(count > 0){//如果当前教师在该时间段已经排课了，就不能安排课程
            return false;
        }

        //检查该教师在本学期的其他排课任务中是否已经排课了
        DlCourseTableEntity courseTableEntity = new DlCourseTableEntity();
        courseTableEntity.setTermId(arrBean.getTermId());
        courseTableEntity.setTeacherId(teacherId);
        courseTableEntity.setCol(col);
        courseTableEntity.setRow(row);
        Integer arredNum = dlCourseTableMapper.getCountByConditions(courseTableEntity);
        if(arredNum != null && arredNum > 0){
            return false;
        }


        //检查当天前已经安排了多少节课了
        Map<String, String> courseMap = new HashMap<>();
        courseMap.put("col", col.toString());
        courseMap.put("courseId", baseBean.getCourseId());
        count = classIndex.getCount(baseBean.getClassId(), courseMap);//获取当天该课程的排课数量
        if(maxNum <= count){
            return false;
        }

        return true;
    }


    /**
     * 检查该位置是否可以进行排课
     * @param baseBean
     * @param col
     * @param row
     * @param maxNum
     * @return
     */
    private boolean isOk(DlArrBaseEntity baseBean,
                         Integer col,
                         Integer row,
                         Integer maxNum){

        Map<String, String> classMap = new HashMap<>();
        classMap.put("row", row.toString());
        classMap.put("col", col.toString());
        //检查该位置是不是不排课位置


        if(!isOk(baseBean.getId(),col, row, maxNum)){
            return false;
        }

        //检查班级当前位置是否已经排课了
        int count = classIndex.getCount(baseBean.getClassId(), classMap);
        if(count > 0){//表示当前位置已经排课了
            return false;
        }

        //几个条件都满足了，就可以允许排课
        return true;
    }

    private void insertCourseTableBean(DlArrBaseEntity baseBean,
                                       Integer col,
                                       Integer row,
                                       Integer level){

        DlCourseTableEntity tableBean = new DlCourseTableEntity();
        tableBean.setArrId(arrBean.getId());
        tableBean.setBaseId(baseBean.getId());
        tableBean.setClassId(baseBean.getClassId());
        tableBean.setClassName(baseBean.getClassName());
        tableBean.setTeacherId(baseBean.getTeacherId());
        tableBean.setTeacherName(baseBean.getTeacherName());
        tableBean.setCourseId(baseBean.getCourseId());
        tableBean.setCourseName(baseBean.getCourseName());
        tableBean.setId(idWorker.nextId() + "");
        tableBean.setRoomId(baseBean.getRoomId());
        tableBean.setRoomName(baseBean.getRoomName());
        tableBean.setTermId(arrBean.getTermId());
        tableBean.setCol(col);
        tableBean.setRow(row);
        tableBean.setLevel(level);//优先级

        //保存到所有排课信息map中
        courseTable.put(tableBean.getId(), tableBean);
        //减少原始排课信息中的课程数
        baseBean.setSupCourseNum(baseBean.getSupCourseNum() - 1);

        courseIndex.put(tableBean.getCourseId(), tableBean);
        classIndex.put(tableBean.getClassId(), tableBean);
        teacherIndex.put(tableBean.getTeacherId(),tableBean);
        roomIndex.put(tableBean.getRoomId(), tableBean);
    }





}