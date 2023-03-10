package com.company.project.service.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.company.project.entity.DlExamPlanEntity;
import com.company.project.entity.DlExamScoreEntity;
import com.company.project.mapper.DlExamPlanMapper;
import com.company.project.mapper.DlExamScoreMapper;
import com.company.project.service.DlExamScoreService;
import com.company.project.vo.resp.DlScoreVo;
import com.company.project.vo.resp.DlStudentScoreVo;
import com.company.project.vo.resp.EChartDataVo;
import com.company.project.vo.resp.ScoreRangeVO;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


@Service("dlExamScoreService")
public class DlExamScoreServiceImpl extends ServiceImpl<DlExamScoreMapper, DlExamScoreEntity> implements DlExamScoreService {

    @Resource
    private DlExamScoreMapper mapper;
    @Resource
    private DlExamPlanMapper dlExamPlanMapper;

    @Override
    public List<DlStudentScoreVo> getStuScoreListByStuId(String studentId) {

        return mapper.getStuScoreListByStuId(studentId);
    }

    @Override
    public Map<String, Object> getStudentDetailsByStuId(String studentId){
        List<DlStudentScoreVo> list = mapper.getStuScoreListByStuId(studentId);
        /*
        这里会出现查询数据，没有该学生的考试数据时，会出现list不为空，但是里面有且只有一个数据对象，而且这个数据对象为null，会导致后续程序出现问题，
        所以在这里判断并溢出该空对象，避免后面发生错误。
        具体这个情况是怎么产生的，还没有找到
         */
        if(list != null && list.size() == 1){
            if(list.get(0) == null){
                list.remove(0);
            }
        }

        Map<String, Object> map = new HashMap<>();

        List<EChartDataVo> dataList = new ArrayList<>();
        List<BigDecimal> ywData = new ArrayList<>();
        List<BigDecimal> sxData = new ArrayList<>();
        List<BigDecimal> yyData = new ArrayList<>();
        List<BigDecimal> wlData = new ArrayList<>();
        List<BigDecimal> hxData = new ArrayList<>();
        List<BigDecimal> swData = new ArrayList<>();
        List<BigDecimal> lsData = new ArrayList<>();
        List<BigDecimal> dlData = new ArrayList<>();
        List<BigDecimal> zzData = new ArrayList<>();

        List<String> examTitle = new ArrayList<>();
        int i = 0;//标记是第几条数据
        //只取前6条记录
        for(DlStudentScoreVo bean: list){
            if(bean == null){
                continue;
            }
            ywData.add(bean.getYw());
            sxData.add(bean.getSx());
            yyData.add(bean.getYy());
            wlData.add(bean.getWl());
            hxData.add(bean.getHx());
            swData.add(bean.getSw());
            lsData.add(bean.getLs());
            dlData.add(bean.getDl());
            zzData.add(bean.getZz());
            examTitle.add(bean.getExamTitle());
            i ++;
            if(i >= 6){
                break;
            }
        }
        List<String> legend = new ArrayList<>();
        setEchartVoBean(ywData, "语文", legend, dataList, "line");
        setEchartVoBean(sxData, "数学", legend, dataList, "line");
        setEchartVoBean(yyData, "英语", legend, dataList, "line");
        setEchartVoBean(wlData, "物理", legend, dataList, "line");
        setEchartVoBean(hxData, "化学", legend, dataList, "line");
        setEchartVoBean(swData, "生物", legend, dataList, "line");
        setEchartVoBean(lsData, "历史", legend, dataList, "line");
        setEchartVoBean(dlData, "地理", legend, dataList, "line");
        setEchartVoBean(zzData, "政治", legend, dataList, "line");
        List<EChartDataVo> barList = new ArrayList<>();
        EChartDataVo vo = null;
        for(EChartDataVo bean: dataList){
            vo = new EChartDataVo();
            vo.setName(bean.getName());
            vo.setType("bar");
            vo.setData(bean.getData());
            barList.add(vo);
        }

        map.put("list", list);
        map.put("xAxis", JSON.toJSONString(examTitle));
        map.put("series", JSON.toJSONString(dataList));
        map.put("barSeries", JSON.toJSONString(barList));
        map.put("legend", JSON.toJSONString(legend));



        return map;
    }

    @Override
    public Map<String, Object> getReportByExamIdAndStudentId(DlExamScoreEntity bean) {

        Map<String, Object> map = new HashMap<>();

        map.put("thisData", getStuThisExamReport(bean));
        //查询上一次考试信息
        DlExamPlanEntity planEntity = dlExamPlanMapper.selectById(bean.getExamId());
        planEntity.setId(bean.getStudentId());//设置学生ID，为下面的方法提供参数
        String examId = mapper.getUpExamId(planEntity);

        Map<String, Object> tempMap = (Map<String, Object>)map.get("thisData");//这次考试

        List<DlScoreVo> scoreList = (List<DlScoreVo>)tempMap.get("list");

        double allScore = 0.0;
        double score = 0.0;

        Map<String, Double> courseScoreRateMap = new HashMap<>();


        for(DlScoreVo vo: scoreList){
            allScore += vo.getTotalScore().doubleValue();
            score += vo.getScore().doubleValue();
            courseScoreRateMap.put(vo.getCourseName(), vo.getScore().doubleValue()/vo.getTotalScore().doubleValue());//存放科目的得分率
        }

        double scoreRate = score / allScore;


        String desc = scoreList.get(0).getStudentName() + "同学，你在《"
                + dlExamPlanMapper.selectById(bean.getExamId()).getName() + "》考试中参加了" + scoreList.size()
                + "个学科的成绩测试，从总分上看你的成绩" + getStrByScoreRate(scoreRate) + "。本次考试中";

        /**
         * 统计其在这次考试中的成绩做点评
         */
        boolean bool1 = false;//用于标识是否存在这样的科目信息
        boolean bool2 = false;
        String name1 = "";
        String name2 = "";
        //低于班级平均成绩的
        for(DlScoreVo vo: scoreList){
            if(vo.getScore().compareTo(vo.getClassScoreAvg()) == -1) {//判断分数小于班级平均分的科目
                name1 += vo.getCourseName() + "、";
                bool1 = true;
                if((vo.getScore().subtract(vo.getClassScoreAvg())).doubleValue() <= -10){
                    bool2 = true;
                    name2 = vo.getCourseName() + "、";
                }
            }
        }
        if(bool1){
            desc += "低于班级平均分的有" + name1.substring(0, name1.length() -1);
            if(bool2){
                desc += ", 其中" + name2.substring(0, name2.length() - 1) + "低于班级平均分10及以上，所以还需要继续努力；";
            }

        }
        boolean bool3 = false;
        boolean bool4 = false;
        String courseNames = "";//分数大于班级平均分的科目
        String courseNames2 = "";//分数大于班级平均分10分级以上的科目
        for(DlScoreVo vo: scoreList){
            if(vo.getScore().compareTo(vo.getClassScoreAvg()) == 1){//判断分数大于班级平均分的科目
                courseNames += vo.getCourseName() + "、";
                bool3 = true;
                if((vo.getScore().subtract(vo.getClassScoreAvg())).doubleValue() >= 10) {
                    courseNames2 += vo.getCourseName() + "、";
                    bool4 = true;
                }
            }

        }
        if(bool3){
            desc += "高于班级平均分的有" + courseNames.substring(0, courseNames.length()-1);
            if(bool4){
                desc += ", 其中" + courseNames2.substring(0, courseNames2.length() -1) + "比平均分高10分以上，需要继续保持";
            }
            desc += "。";
        }

        if(bool2 && bool4){//低于平均分10以上的存在，高于平均分10分及以上的，就是典型的偏科生
            desc += "你的" + name2.substring(0, name2.length() - 1) + "处于偏科，还需加强学习。";
        }

        Integer sumScoreClassOrderNum = (Integer) tempMap.get("sumScoreClassOrderNum");
        Integer sumScoreGradeOrderNum = (Integer) tempMap.get("sumScoreGradeOrderNum");
        /**
         * 对比上一次考试的成绩升降情况，班级和年级上的排名升降情况
         */
        if(!StringUtils.isEmpty(examId)){
            bean.setExamId(examId);
            map.put("upData", getStuThisExamReport(bean));

            Map<String, Object> upTempMap = (Map<String, Object>)map.get("upData");//上次考试
            List<DlScoreVo> upScoreList = (List<DlScoreVo>)upTempMap.get("list");
            /**
             * 统计每科成绩的上升还是下降
             */

            bool1 = false;
            bool2 = false;
            bool3 = false;
            bool4 = false;
            name1 = "";
            name2 = "";
            String name3 = "";
            String name4 = "";


            for(DlScoreVo n : scoreList){
                for(DlScoreVo o: upScoreList){
                    if(n.getClassOrderNum() - o.getClassOrderNum() > 0){
                        bool1 = true;
                        name1 = n.getCourseName() + "上升" + (n.getClassOrderNum() - o.getClassOrderNum()) + "位、";
                    }else if(n.getClassOrderNum() - o.getClassOrderNum() > 0){
                        bool2 = true;
                        name2 = n.getCourseName() + "下滑" + ( o.getClassOrderNum() - n.getClassOrderNum()) + "位、";
                    }

                    if(n.getGradeOrderNum() - o.getGradeOrderNum() > 0){
                        bool3 = true;
                        name3 = n.getCourseName() + "上升" + (n.getGradeOrderNum() - o.getGradeOrderNum()) + "位、";
                    }else if(n.getGradeOrderNum() - o.getGradeOrderNum() > 0){
                        bool4 = true;
                        name4 = n.getCourseName() + "下滑" + (n.getGradeOrderNum() - o.getGradeOrderNum()) + "位、";
                    }
                }
            }
            if(bool1){
                desc += "班级排名中上升的有" + name1.substring(0, name1.length() -1) + "；";
            }
            if(bool2){
                desc += "班级排名中下滑的有" + name2.substring(0, name1.length() -1) + "；";
            }
            if(bool3){
                desc += "年级排名中上升的有" + name3.substring(0, name1.length() -1) + "；";
            }
            if(bool4){
                desc += "年级排名中下滑的有" + name4.substring(0, name1.length() -1) + "；";
            }

            Integer sumScoreClassOrderNum1 = (Integer) upTempMap.get("sumScoreClassOrderNum");
            Integer sumScoreGradeOrderNum1 = (Integer) upTempMap.get("sumScoreGradeOrderNum");
            desc += "本次考试总分在班级中的排名为第" + sumScoreClassOrderNum + "名,";
            if(sumScoreClassOrderNum - sumScoreClassOrderNum1 > 0){
                desc += "相对于上一次上升了" + (sumScoreClassOrderNum - sumScoreClassOrderNum1) + "名；";
            }else if(sumScoreClassOrderNum - sumScoreClassOrderNum1 == 0){
                desc += "相对于上一次没有变化；";
            }else {
                desc += "相对于上一次下滑了" + (sumScoreClassOrderNum1 - sumScoreClassOrderNum) + "名；";
            }
            desc += "本次考试总分在年级中的排名为第" + sumScoreGradeOrderNum + "名,";
            if(sumScoreGradeOrderNum - sumScoreGradeOrderNum1 > 0){
                desc += "相对于上一次上升了" + (sumScoreGradeOrderNum - sumScoreGradeOrderNum1) + "名；";
            }else if(sumScoreGradeOrderNum - sumScoreGradeOrderNum1 == 0){
                desc += "相对于上一次没有变化；";
            }else {
                desc += "相对于上一次下滑了" + (sumScoreGradeOrderNum - sumScoreGradeOrderNum1) + "名；";
            }


        }else{
            desc += "本次考试总分在班级中的排名为第" + sumScoreClassOrderNum + "名；";
            desc += "本次考试总分在年级中的排名为第" + sumScoreGradeOrderNum + "名。";
            map.put("upData", null);
        }
        desc += "请继续加油！";
        map.put("desc", desc);
        /**
         * 添加柱状图数据，和圆形图数据
         */
        JSONObject barObject = new JSONObject();
        //柱状图，展示各科成绩与年级平均分，班级平均分的对比图
        List<String> legend = new ArrayList<>();
        List<String> pieLegend = new ArrayList<>();
        List<JSONObject> pieSeriesData = new ArrayList<>();
        legend.add("我的成绩");
        legend.add("班级平均分");
        legend.add("年级平均分");
        barObject.put("legend", legend);
        List<String> xAxis = new ArrayList<>();
        List<BigDecimal> barMyData = new ArrayList<>();
        List<BigDecimal> barClassData = new ArrayList<>();
        List<BigDecimal> barGradeData = new ArrayList<>();
        JSONObject pieData = null;
        for(DlScoreVo b: scoreList){
            pieLegend.add(b.getCourseName());
            pieData = new JSONObject();
            pieData.put("value", b.getScore());
            pieData.put("name", b.getCourseName());
            pieSeriesData.add(pieData);
            xAxis.add(b.getCourseName());
            barMyData.add(b.getScore());
            barClassData.add(b.getClassScoreAvg());
            barGradeData.add(b.getGradeScoreAvg());
        }
        barObject.put("xAxis", xAxis);
        JSONObject myObject = new JSONObject();
        JSONObject classObject = new JSONObject();
        JSONObject gradeObject = new JSONObject();
        myObject.put("name", "我的成绩");
        myObject.put("type", "bar");
        myObject.put("data", barMyData);
        classObject.put("name", "班级平均分");
        classObject.put("type", "bar");
        classObject.put("data", barClassData);
        gradeObject.put("name", "年级平均分");
        gradeObject.put("type", "bar");
        gradeObject.put("data", barGradeData);
        List<JSONObject> seriesList = new ArrayList<>();
        seriesList.add(myObject);
        seriesList.add(classObject);
        seriesList.add(gradeObject);
        barObject.put("series", seriesList);

        /**
         * 饼状图
         */

        JSONObject pieObject = new JSONObject();
        pieObject.put("legend", pieLegend);
        pieObject.put("series", pieSeriesData);

        map.put("barObject", barObject.toJSONString());
        map.put("pieObject", pieObject.toJSONString());
        return map;
    }

    @Override
    public Map<String, Object> getGradeReportDataByExamId(String examId) {

        Map<String, Object> map = new HashMap<>();
        List<ScoreRangeVO> classList = mapper.getExamGradeClassRangeScoreByExamId(examId);
        List<ScoreRangeVO> list = mapper.getExamGradeRangeScoreByExamId(examId);
        map.put("list", list);
        map.put("classList", classList);
        JSONObject lineObject = new JSONObject();

        List<String> legend = new ArrayList<>();
        List<JSONObject> series = new ArrayList<>();
        JSONObject seriesBean = null;
        List<Integer> data = null;
        for(ScoreRangeVO bean: classList){
            legend.add(bean.getClassName() + "-" + bean.getCourseName());
            seriesBean = new JSONObject();
            seriesBean.put("name", bean.getClassName() + "-" + bean.getCourseName());
            seriesBean.put("type", "line");
            data = new ArrayList<>();
            data.add(bean.getZero());
            data.add(bean.getThirty());
            data.add(bean.getSixty());
            data.add(bean.getSeventy());
            data.add(bean.getEighty());
            data.add(bean.getNinety());
            seriesBean.put("data", data);
            series.add(seriesBean);
        }
        lineObject.put("legend",legend);
        lineObject.put("series", series);
        map.put("lineObject", lineObject.toJSONString());

        JSONObject barObject = new JSONObject();
        series = new ArrayList<>();
        legend = new ArrayList<>();
        for(ScoreRangeVO bean: list){
            legend.add(bean.getCourseName());
            seriesBean = new JSONObject();
            seriesBean.put("name", bean.getCourseName());
            seriesBean.put("type", "bar");
            data = new ArrayList<>();
            data.add(bean.getZero());
            data.add(bean.getThirty());
            data.add(bean.getSixty());
            data.add(bean.getSeventy());
            data.add(bean.getEighty());
            data.add(bean.getNinety());
            seriesBean.put("data", data);
            series.add(seriesBean);
        }

        barObject.put("legend",legend);
        barObject.put("series", series);
        map.put("barObject", barObject.toJSONString());





        return map;
    }

    @Override
    public Map<String, Object> getClassReportDataByExamIdAndClassId(String examId, String classId) {
        Map<String, Object> map = new HashMap<>();
        DlExamScoreEntity bean = new DlExamScoreEntity();
        bean.setExamId(examId);
        bean.setClassId(classId);
        List<DlStudentScoreVo> list = mapper.getStuScoreByConditions(bean);

        List<ScoreRangeVO> scoreRangeVOS = mapper.getExamClassRangeScoreByExamIdAndClassId(bean);

        JSONObject lineObject = new JSONObject();

        List<String> legend = new ArrayList<>();
        List<JSONObject> series = new ArrayList<>();
        JSONObject seriesBean = null;
        List<Integer> data = null;
        for(ScoreRangeVO vo: scoreRangeVOS){
            legend.add(vo.getCourseName());
            seriesBean = new JSONObject();
            seriesBean.put("name", vo.getCourseName());
            seriesBean.put("type", "line");
            data = new ArrayList<>();
            data.add(vo.getZero());
            data.add(vo.getThirty());
            data.add(vo.getSixty());
            data.add(vo.getSeventy());
            data.add(vo.getEighty());
            data.add(vo.getNinety());
            seriesBean.put("data", data);
            series.add(seriesBean);
        }
        lineObject.put("legend",legend);
        lineObject.put("series", series);
        map.put("lineObject", lineObject.toJSONString());

        DlExamPlanEntity dlExamPlanEntity = dlExamPlanMapper.selectById(examId);
        if(dlExamPlanEntity != null){
            map.put("classType", dlExamPlanEntity.getClassType());
        }else{
            map.put("classType", 2);
        }
        map.put("list", list);

        return map;
    }






    public String getStrByScoreRate(double scoreRate){
        int rate = (int)Math.ceil(scoreRate * 100);
        String str = "";
        if(rate >= 90){
            str = "非常优秀";
        }else if(rate >= 85){
            str = "优秀";
        }else if(rate >= 80){
            str = "较为优秀";
        }else if(rate >= 70){
            str = "良好";
        }else if(rate >= 60){
            str = "一般";
        }else if(rate >= 40){
            str = "不太好";
        }else{
            str = "非常不好";
        }

        return str;
    }


    public boolean allIsZero(List<BigDecimal> list){
        for(BigDecimal value: list){
            if(value == null){
                continue;
            }
            if(!(value.compareTo(BigDecimal.ZERO) == 0)){
                return false;
            }
        }
        return true;
    }

    public void setEchartVoBean(List<BigDecimal> datas, String name, List<String> legend, List<EChartDataVo> dataList, String type){
        EChartDataVo echart = null;
        if(!allIsZero(datas)){
            echart = new EChartDataVo();
            echart.setData(datas);
            echart.setName(name);
            echart.setType(type);
            dataList.add(echart);
            legend.add(name);
        }
    }

    /**
     * 1查询该学生本次考试的所有成绩
     * 2.查询本次考试，班级的所有科目的平均成绩
     * 3.查询本次考试，全年级所有科目的平均成绩
     * 4.查询学生在班级科目中的排名
     * 5.查询学生在年级中的科目排名
     * 6.查询学生在总分在班级和年级中的排名
     * 7.查询学生上一次考试的成绩
     * 学生姓名  科目  成绩  班级平均分   年级平均分  班级排名   年级排名
     *
     * @param bean
     */
    public Map<String, Object> getStuThisExamReport(DlExamScoreEntity bean){
        Map<String, Object> map = new HashMap<>();
        //该list包含了学生信息，科目成绩 班级平均分   年级平均分  班级排名   年级排名
        List<DlScoreVo> scoreList = mapper.getStuExamScore(bean);
        for(DlScoreVo vo: scoreList){
            bean.setCourseId(vo.getCourseId());
            bean.setClassId(null);
            List<DlExamScoreEntity> gradeScoreList = mapper.getOrderList(bean);
            vo.setGradeOrderNum(getOrderNum(gradeScoreList, vo.getScore()));


            bean.setClassId(vo.getClassId());
            List<DlExamScoreEntity> classScoreList = mapper.getOrderList(bean);
            vo.setClassOrderNum(getOrderNum(classScoreList, vo.getScore()));

        }
        List<DlExamScoreEntity> classAvgList = mapper.getAvgScoreByConditions(bean);
        List<BigDecimal> classSumOrderList = mapper.getSumScoreOrderList(bean);
        bean.setClassId(null);
        List<DlExamScoreEntity> gradeAvgList = mapper.getAvgScoreByConditions(bean);
        List<BigDecimal> gradeSumOrderList = mapper.getSumScoreOrderList(bean);
        BigDecimal totalScore = new BigDecimal(0);//总分
        for(DlScoreVo vo: scoreList){
            for(DlExamScoreEntity avg: classAvgList){
                if(vo.getCourseId().equals(avg.getCourseId())){
                    vo.setClassScoreAvg(avg.getScore());
                    break;
                }
            }
            for(DlExamScoreEntity avg: gradeAvgList){
                if(vo.getCourseId().equals(avg.getCourseId())){
                    vo.setGradeScoreAvg(avg.getScore());
                    break;
                }
            }
            totalScore = totalScore.add(vo.getScore());//计算总分
        }
        //获取总分，班级排名与年级排名



        map.put("sumScoreClassOrderNum", getSumOrderNum(classSumOrderList, totalScore));
        map.put("sumScoreGradeOrderNum", getSumOrderNum(gradeSumOrderList, totalScore));
        map.put("totalScore", totalScore);
        map.put("list", scoreList);
        return map;
    }





    /**
     * 获取排名
     * @param list
     * @param score
     * @return
     */
    public int getOrderNum(List<DlExamScoreEntity> list, BigDecimal score){
        for(int i = 0; i < list.size(); i ++){
            if(list.get(i).getScore().compareTo(score) == 0){
                return i + 1;
            }
        }
        return 0;
    }

    public int getSumOrderNum(List<BigDecimal> list , BigDecimal score){
        int index = 0;
        for(int i = 0; i < list.size(); i++){
            if(list.get(i).compareTo(score) == 0){
                index = i + 1;
                break;
            }
        }
        return index;
    }









}