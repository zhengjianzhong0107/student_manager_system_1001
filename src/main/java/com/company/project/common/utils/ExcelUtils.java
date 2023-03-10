package com.company.project.common.utils;

import com.company.project.common.aop.annotation.ExcelColumn;
import com.company.project.common.exception.BusinessException;
import com.company.project.common.exception.code.BaseResponseCode;
import com.company.project.entity.DlExamListEntity;
import com.company.project.entity.DlExamListTeacher;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.BooleanUtils;
import org.apache.commons.lang3.CharUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.math.NumberUtils;
import org.apache.poi.hssf.usermodel.*;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.math.BigDecimal;
import java.net.URLEncoder;
import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class ExcelUtils {



    private final static Logger log = LoggerFactory.getLogger(ExcelUtils.class);

    private final static String EXCEL2003 = "xls";
    private final static String EXCEL2007 = "xlsx";

    public static <T> List<T> readExcel(String path, Class<T> cls, MultipartFile file){

        String fileName = file.getOriginalFilename();
        if (!fileName.matches("^.+\\.(?i)(xls)$") && !fileName.matches("^.+\\.(?i)(xlsx)$")) {
            log.error("上传文件格式不正确");
        }
        List<T> dataList = new ArrayList<>();
        Workbook workbook = null;
        try {
            InputStream is = file.getInputStream();
            if (fileName.endsWith(EXCEL2007)) {
//                FileInputStream is = new FileInputStream(new File(path));
                workbook = new XSSFWorkbook(is);
            }
            if (fileName.endsWith(EXCEL2003)) {
//                FileInputStream is = new FileInputStream(new File(path));
                workbook = new HSSFWorkbook(is);
            }
            if (workbook != null) {
                //类映射  注解 value-->bean columns
                Map<String, List<Field>> classMap = new HashMap<>();
                List<Field> fields = Stream.of(cls.getDeclaredFields()).collect(Collectors.toList());
                fields.forEach(
                        field -> {
                            ExcelColumn annotation = field.getAnnotation(ExcelColumn.class);
                            if (annotation != null) {
                                String value = annotation.value();
                                if (StringUtils.isBlank(value)) {
                                    return;//return起到的作用和continue是相同的 语法
                                }
                                if (!classMap.containsKey(value)) {
                                    classMap.put(value, new ArrayList<>());
                                }
                                field.setAccessible(true);
                                classMap.get(value).add(field);
                            }
                        }
                );
                //索引-->columns
                Map<Integer, List<Field>> reflectionMap = new HashMap<>(16);
                //默认读取第一个sheet
                Sheet sheet = workbook.getSheetAt(0);

                boolean firstRow = true;
                for (int i = sheet.getFirstRowNum(); i <= sheet.getLastRowNum(); i++) {
                    Row row = sheet.getRow(i);
                    //首行  提取注解
                    if (firstRow) {
                        for (int j = row.getFirstCellNum(); j <= row.getLastCellNum(); j++) {
                            Cell cell = row.getCell(j);
                            String cellValue = getCellValue(cell);
                            if (classMap.containsKey(cellValue)) {
                                reflectionMap.put(j, classMap.get(cellValue));
                            }
                        }
                        firstRow = false;
                    } else {
                        //忽略空白行
                        if (row == null) {
                            continue;
                        }
                        try {
                            T t = cls.newInstance();
                            //判断是否为空白行
                            boolean allBlank = true;
                            for (int j = row.getFirstCellNum(); j <= row.getLastCellNum(); j++) {
                                if (reflectionMap.containsKey(j)) {
                                    Cell cell = row.getCell(j);
                                    String cellValue = getCellValue(cell);
                                    if (StringUtils.isNotBlank(cellValue)) {
                                        allBlank = false;
                                    }
                                    List<Field> fieldList = reflectionMap.get(j);
                                    fieldList.forEach(
                                            x -> {
                                                try {
                                                    handleField(t, cellValue, x);
                                                } catch (Exception e) {
                                                    log.error(String.format("reflect field:%s value:%s exception!", x.getName(), cellValue), e);
                                                }
                                            }
                                    );
                                }
                            }
                            if (!allBlank) {
                                dataList.add(t);
                            } else {
                                log.warn(String.format("row:%s is blank ignore!", i));
                            }
                        } catch (Exception e) {
                            log.error(String.format("parse row:%s exception!", i), e);
                        }
                    }
                }
            }
        } catch (Exception e) {
            log.error(String.format("parse excel exception!"), e);
        } finally {
            if (workbook != null) {
                try {
                    workbook.close();
                } catch (Exception e) {
                    log.error(String.format("parse excel exception!"), e);
                }
            }
        }
        return dataList;
    }

    private static <T> void handleField(T t, String value, Field field) throws Exception {
        Class<?> type = field.getType();
        if (type == null || type == void.class || StringUtils.isBlank(value)) {
            return;
        }
        if (type == Object.class) {
            field.set(t, value);
            //数字类型
        } else if (type.getSuperclass() == null || type.getSuperclass() == Number.class) {
            if (type == int.class || type == Integer.class) {
                field.set(t, NumberUtils.toInt(value));
            } else if (type == long.class || type == Long.class) {
                field.set(t, NumberUtils.toLong(value));
            } else if (type == byte.class || type == Byte.class) {
                field.set(t, NumberUtils.toByte(value));
            } else if (type == short.class || type == Short.class) {
                field.set(t, NumberUtils.toShort(value));
            } else if (type == double.class || type == Double.class) {
                field.set(t, NumberUtils.toDouble(value));
            } else if (type == float.class || type == Float.class) {
                field.set(t, NumberUtils.toFloat(value));
            } else if (type == char.class || type == Character.class) {
                field.set(t, CharUtils.toChar(value));
            } else if (type == boolean.class) {
                field.set(t, BooleanUtils.toBoolean(value));
            } else if (type == BigDecimal.class) {
                field.set(t, new BigDecimal(value));
            }
        } else if (type == Boolean.class) {
            field.set(t, BooleanUtils.toBoolean(value));
        } else if (type == Date.class) {
            //
            field.set(t, value);
        } else if (type == String.class) {
            field.set(t, value);
        } else {
            Constructor<?> constructor = type.getConstructor(String.class);
            field.set(t, constructor.newInstance(value));
        }
    }

    private static String getCellValue(Cell cell) {
        if (cell == null) {
            return "";
        }
        if (cell.getCellType() == Cell.CELL_TYPE_NUMERIC) {
            if (HSSFDateUtil.isCellDateFormatted(cell)) {
                return HSSFDateUtil.getJavaDate(cell.getNumericCellValue()).toString();
            } else {
                return new BigDecimal(cell.getNumericCellValue()).toString();
            }
        } else if (cell.getCellType() == Cell.CELL_TYPE_STRING) {
            return StringUtils.trimToEmpty(cell.getStringCellValue());
        } else if (cell.getCellType() == Cell.CELL_TYPE_FORMULA) {
            return StringUtils.trimToEmpty(cell.getCellFormula());
        } else if (cell.getCellType() == Cell.CELL_TYPE_BLANK) {
            return "";
        } else if (cell.getCellType() == Cell.CELL_TYPE_BOOLEAN) {
            return String.valueOf(cell.getBooleanCellValue());
        } else if (cell.getCellType() == Cell.CELL_TYPE_ERROR) {
            return "ERROR";
        } else {
            return cell.toString().trim();
        }

    }

    public static <T> void writeExcel(HttpServletResponse response, List<T> dataList, Class<T> cls, String excelFileName){
        Field[] fields = cls.getDeclaredFields();
        List<Field> fieldList = Arrays.stream(fields)
                .filter(field -> {
                    ExcelColumn annotation = field.getAnnotation(ExcelColumn.class);
                    if (annotation != null && annotation.col() > 0) {
                        field.setAccessible(true);
                        return true;
                    }
                    return false;
                }).sorted(Comparator.comparing(field -> {
                    int col = 0;
                    ExcelColumn annotation = field.getAnnotation(ExcelColumn.class);
                    if (annotation != null) {
                        col = annotation.col();
                    }
                    return col;
                })).collect(Collectors.toList());

        Workbook wb = new XSSFWorkbook();
        Sheet sheet = wb.createSheet("Sheet1");
        AtomicInteger ai = new AtomicInteger();
        {
            Row row = sheet.createRow(ai.getAndIncrement());
            AtomicInteger aj = new AtomicInteger();
            //写入头部
            fieldList.forEach(field -> {
                ExcelColumn annotation = field.getAnnotation(ExcelColumn.class);
                String columnName = "";
                if (annotation != null) {
                    columnName = annotation.value();
                }
                Cell cell = row.createCell(aj.getAndIncrement());

                CellStyle cellStyle = wb.createCellStyle();
                cellStyle.setFillForegroundColor(IndexedColors.WHITE.getIndex());
                cellStyle.setFillPattern(CellStyle.SOLID_FOREGROUND);
                cellStyle.setAlignment(CellStyle.ALIGN_CENTER);

                Font font = wb.createFont();
                font.setBoldweight(Font.BOLDWEIGHT_NORMAL);
                cellStyle.setFont(font);
                cell.setCellStyle(cellStyle);
                cell.setCellValue(columnName);
            });
        }
        if (CollectionUtils.isNotEmpty(dataList)) {
            dataList.forEach(t -> {
                Row row1 = sheet.createRow(ai.getAndIncrement());
                AtomicInteger aj = new AtomicInteger();
                fieldList.forEach(field -> {
                    Class<?> type = field.getType();
                    Object value = "";
                    try {
                        value = field.get(t);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    Cell cell = row1.createCell(aj.getAndIncrement());
                    if (value != null) {
                        if (type == Date.class) {
                            cell.setCellValue(value.toString());
                        } else {
                            cell.setCellValue(value.toString());
                        }
                        cell.setCellValue(value.toString());
                    }
                });
            });
        }
        //冻结窗格
        wb.getSheet("Sheet1").createFreezePane(0, 1, 0, 1);
        //浏览器下载excel
        buildExcelDocument(excelFileName, wb, response);
        //生成excel文件
//        buildExcelFile(".\\default.xlsx",wb);
    }

    /**
     * 浏览器下载excel
     * @param fileName
     * @param wb
     * @param response
     */

    public static void buildExcelDocument(String fileName, Workbook wb,HttpServletResponse response){
        try {
            response.setCharacterEncoding("utf-8");
            //response.setContentType(MediaType.APPLICATION_OCTET_STREAM_VALUE);
            response.setContentType("application/octet-stream;charset=UTF-8");

            response.addHeader("Content-Disposition", "attachment;filename="+ URLEncoder.encode(fileName + ".xlsx", "utf-8"));
            response.flushBuffer();
            wb.write(response.getOutputStream());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 生成excel文件
     * @param path 生成excel路径
     * @param wb
     */
    private static  void  buildExcelFile(String path, Workbook wb){

        File file = new File(path);
        if (file.exists()) {
            file.delete();
        }
        try {
            wb.write(new FileOutputStream(file));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    /**
     * 获取默认的单元格样式
     * @param workbook
     * @return
     */
    public static HSSFCellStyle getCellDefaultStyle(HSSFWorkbook workbook){
        HSSFCellStyle cellStyle = workbook.createCellStyle();
        cellStyle.setWrapText(true);
        cellStyle.setAlignment(HSSFCellStyle.ALIGN_CENTER);
        cellStyle.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);
        return cellStyle;
    }

    /**
     * 导入excel表格.返回的是List<Object> 的数据结构,使用反射去赋值.
     * @param file  导入的文件
     * @param clazz  存储数据的实体
     * @param <T>  表示这个方法是泛型方法.
     * @return
     */
    public static <T> List<T> readExcel(MultipartFile file, Class<T> clazz){
        try{
            //最终返回数据
            List<T> resultList = new ArrayList<T>();

            Workbook workbook = null;
            InputStream is = file.getInputStream();
            String name = file.getOriginalFilename().toLowerCase();
            // 创建excel操作对象
            if (name.contains(".xlsx") || name.contains(".xls")) {
                //使用工厂方法创建.
                workbook = WorkbookFactory.create(is);
            } else {
                throw new BusinessException(BaseResponseCode.EXCEL_READ_FILE_UN_KNOW);
            }
            //得到一个工作表
            Sheet sheet = workbook.getSheetAt(0);
            //获得数据的总行数
            int totalRowNum = sheet.getLastRowNum();
            //获得总列数
            int cellLength = sheet.getRow(0).getPhysicalNumberOfCells();
            //获取表头
            Row firstRow = sheet.getRow(0);
            //获取反射类的所有字段
            Field [] fields =  clazz.getDeclaredFields();
            //创建一个字段数组,用于和excel行顺序一致.
            Field [] newFields = new Field[cellLength];
            //获取静态方法.该方法是用于把excel的表头映射成实体字段名.
            Method m = clazz.getDeclaredMethod("convert", String.class);
            Object ob = clazz.newInstance();
            for(int i=0;i<cellLength;i++) {
                for (Field field:fields) {
                    Cell  cell = firstRow.getCell(i);
                    //按照excel中的存储存放数组,以便后面遍历excel表格,数据一一对应.
                    if(m.invoke(ob,getXCellVal(cell)).equals(field.getName())){
                        newFields[i] = field;
                        continue;
                    }
                }
            }

            //从第x行开始获取
            for(int x = 1 ; x <= totalRowNum ; x++){
                T object = clazz.newInstance();
                //获得第i行对象
                Row row = sheet.getRow(x);
                //如果一行里的所有单元格都为空则不放进list里面
                int a = 0;
                for(int y=0;y<cellLength;y++){
                    if (!(row==null)) {
                        Cell cell = row.getCell(y);
                        if (cell == null) {
                            a++;
                        } else {
                            Field field = newFields[y];
                            String value =  getXCellVal(cell);
                            if (value!=null && !value.equals("")){
                                //给字段设置值.
                                setValue(field,value,object);
                            }
                        }
                    }
                }//for
                if(a!=cellLength && row!=null){
                    resultList.add(object);
                }
            }
            return  resultList;
        }catch (Exception e){
            e.printStackTrace();
            throw new BusinessException(BaseResponseCode.EXCEL_READ_ERROR);
        }
    }
    /**
     * 给字段赋值,判断值的类型,然后转化成实体需要的类型值.
     * @param field  字段
     * @param value  值
     * @param object  对象
     */
    private static void setValue(Field field, String value, Object object) {
        try {
            field.setAccessible(true);
            DateFormat fmt = new SimpleDateFormat("yyyy-MM-dd");
            if (field.getGenericType().toString().contains("Integer")){
                field.set(object,Integer.valueOf(value));
            }else if(field.getGenericType().toString().contains("String")){
                field.set(object,value);
            }else
            if (field.getGenericType().toString().contains("Date")){
                field.set(object,fmt.parse(value));
            }
            field.setAccessible(false);
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    /**
     * @param cell
     * @return String
     * 获取单元格中的值
     */

    private static String getXCellVal(Cell cell) {
        DateFormat fmt = new SimpleDateFormat("yyyy-MM-dd");
        DecimalFormat df = new DecimalFormat("0.0000");
        String val = "";
        switch (cell.getCellType()) {
            case XSSFCell.CELL_TYPE_NUMERIC:
                if (DateUtil.isCellDateFormatted(cell)) {
                    val = fmt.format(cell.getDateCellValue()); //日期型
                } else {
                    val = df.format(cell.getNumericCellValue()); //数字型
                    // 去掉多余的0，如最后一位是.则去掉
                    val = val.replaceAll("0+?$", "").replaceAll("[.]$","");
                }
                break;
            case XSSFCell.CELL_TYPE_STRING: //文本类型
                val = cell.getStringCellValue();
                break;
            case XSSFCell.CELL_TYPE_BOOLEAN: //布尔型
                val = String.valueOf(cell.getBooleanCellValue());
                break;
            case XSSFCell.CELL_TYPE_BLANK: //空白
                val = cell.getStringCellValue();
                break;
            case XSSFCell.CELL_TYPE_ERROR: //错误
                val = "";
                break;
            case XSSFCell.CELL_TYPE_FORMULA: //公式
                try {
                    val = String.valueOf(cell.getStringCellValue());
                } catch (IllegalStateException e) {
                    val = String.valueOf(cell.getNumericCellValue());
                }
                break;
            default:
                val = cell.getRichStringCellValue() == null ? null : cell.getRichStringCellValue().toString();
        }
        return val;
    }


    /**  导入excel表格,返回的List<Map<Integer, String>>数据结构.
     * @param
     * @return 返回list集合
     * @throws Exception
     */
    public static List<Map<Integer, String>> read(MultipartFile file) {
        try{
            //最终返回数据
            List<Map<Integer, String>> list = new ArrayList<Map<Integer, String>>();

            Workbook workbook = null;

            InputStream is = new ByteArrayInputStream(file.getBytes());
            String name = file.getOriginalFilename().toLowerCase();
            // 创建excel操作对象
            if (name.contains(".xlsx") || name.contains(".xls")) {
                workbook = WorkbookFactory.create(is);
            } else {
                throw new BusinessException(BaseResponseCode.EXCEL_READ_FILE_UN_KNOW);
            }
            //得到一个工作表
            Sheet sheet = workbook.getSheetAt(0);
            //获得数据的总行数
            int totalRowNum = sheet.getLastRowNum();

            //获得总列数
            int cellLength = sheet.getRow(0).getPhysicalNumberOfCells();
            Map<Integer, String> map = null;
            //获得所有数据
            //从第x行开始获取
            for(int x = 0 ; x <= totalRowNum ; x++){
                map = new HashMap<Integer, String>();
                //获得第i行对象
                Row row = sheet.getRow(x);
                //如果一行里的所有单元格都为空则不放进list里面
                int a = 0;
                for(int y=0;y<cellLength;y++){
                    if (!(row==null)) {
                        Cell cell = row.getCell(y);
                        if (cell == null) {
                            map.put(y, "");
                        } else {
                            map.put(y,   getXCellVal(cell));
                        }
                        if (map.get(y) == null || "".equals(map.get(y))) {
                            a++;
                        }
                    }
                }//for
                if(a!=cellLength && row!=null){
                    list.add(map);
                }
            }
            return list;
        }catch (Exception e){
            e.printStackTrace();
            throw new BusinessException(BaseResponseCode.EXCEL_READ_ERROR);
        }
    }




    /**
     * 导出考试计划安排
     * @param list
     * @param title
     * @param response
     */
    public static void exportExamPlan(List<DlExamListEntity> list, String title, HttpServletResponse response){
        //创建工作薄对象
        HSSFWorkbook workbook=new HSSFWorkbook();
        //创建工作表对象
        HSSFSheet sheet = workbook.createSheet();
        sheet.addMergedRegion(new CellRangeAddress(0,0,0,4));

        //设置列宽
        setColWidth(5, sheet, 20*256);

        HSSFCellStyle cellStyle = workbook.createCellStyle();
        cellStyle.setWrapText(true);
        cellStyle.setAlignment(HSSFCellStyle.ALIGN_CENTER);
        cellStyle.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);
        //创建工作表的行
        HSSFRow row = sheet.createRow(0);
        HSSFCell cell = row.createCell(0);
        cell.setCellStyle(cellStyle);
        cell.setCellValue(title);
        row.setHeight((short) (30*20));

        row = sheet.createRow(1);
        row.setHeight((short) (20*20));
        row.createCell(0).setCellValue("考试日期");
        row.createCell(1).setCellValue("考试科目");
        row.createCell(2).setCellValue("考场");
        row.createCell(3).setCellValue("考试时间");
        row.createCell(4).setCellValue("监考教师");
        DlExamListEntity bean = null;
        for(int i = 0; i < list.size(); i ++){
            row = sheet.createRow(i + 2);//设置第一行，从零开始
            row.setHeight((short) (20*20));
            bean = list.get(i);
            row.createCell(0).setCellValue(DateUtils.format(bean.getExamTime(), "yyyy-MM-dd"));
            row.createCell(1).setCellValue(bean.getCourseId());
            row.createCell(2).setCellValue(bean.getExamRoomId());
            row.createCell(3).setCellValue(DateUtils.format(bean.getStartTime(), "HH:mm:ss") + " ~ " + DateUtils.format(bean.getEndTime(), "HH:mm:ss"));
            String tea_names = "";
            for(DlExamListTeacher te: bean.getTeachers()){
                tea_names += te.getTeacherName() + " ";
            }
            row.createCell(4).setCellValue(tea_names);
        }

        workbook.setSheetName(0, title);//设置sheet的Name
        buildExcelDocument(  "考务安排信息表", workbook, response);
//        buildExcelFile("G:\\default.xlsx", workbook);

    }

    /**
     * 给列添加固定宽度
     * @param colNums 共多少列
     * @param sheet
     */
    public static void setColWidth(Integer colNums, HSSFSheet sheet, Integer width){
        for(int i = 0; i < colNums; i++){
            sheet.setColumnWidth(i, width);
        }
    }

    /**
     * 给列添加自适应倍数宽度
     * @param colNums 列总数
     * @param sheet
     * @param widthMultiple 自适应的宽度倍数
     */
    public static void setColWidth(Integer colNums, HSSFSheet sheet, Double widthMultiple){
        for(int i = 0; i < colNums; i++){
            sheet.autoSizeColumn(i);
            int width = (int)Math.round(sheet.getColumnWidth(i) * widthMultiple);
            sheet.setColumnWidth(i, width);
        }
    }

}
