package com.company.project.common.utils;

import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class NumberUtils {
    private static final Random r = new Random();

    public static boolean isInteger(String value){
        try{
            Integer.parseInt(value);
            return true;
        }catch (NumberFormatException e){
            return false;
        }
    }

    public static String getNumCode(String prefix){
        Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        // 获取当前月
        int month = calendar.get(Calendar.MONTH) + 1;
        // 获取当前日
        int day = calendar.get(Calendar.DATE);
        return prefix + year + month + day + NumberUtils.getRandomNumber(4);
    }

    public static String getRandomNumber(int bit){
        String number = "";
        for(int i = 0; i < bit; i ++){
            number += r.nextInt(10);
        }
        return number;
    }

    /**
     * 获取随机范围数
     * @param min 最小值
     * @param max 最大值
     * @return [min, max] 返回的值包括max和min
     */
    public static Integer random(Integer min, Integer max){

        if(min > max){//如果最小值大于最大值，就交换位置
            int tem = min;
            min = max;
            max = tem;
        }else if(min == max){//如果两个数相等，那么就直接返回该值
            return min;
        }

        return r.nextInt(max - min + 1) + min;
    }


    /**
     * 提取字符串中的数值，并将其存在list中返回
     * @param str
     * @return
     */
    public static List<Integer> getNumbersFromStr(String str){
        List<Integer> list = new ArrayList<>();
        String regex = "\\d*";    //提取字符串末尾的数字：封妖塔守卫71 == >> 71
        Pattern p2 = Pattern.compile(regex);
        Matcher m = p2.matcher(str);
        while (m.find()) {
            if (!"".equals(m.group()))
                list.add(Integer.parseInt(m.group()));
        }
        return list;
    }




    public static String getUUID(){
        return UUID.randomUUID().toString().replace("-", "");
    }


    /**
     * 中文數字转阿拉伯数组【十万九千零六十  --> 109060】
     * @author 雪见烟寒
     * @param chineseNumber
     * @return
     */
    @SuppressWarnings("unused")
    private static int chineseNumber2Int(String chineseNumber){
        int result = 0;
        int temp = 1;//存放一个单位的数字如：十万
        int count = 0;//判断是否有chArr
        char[] cnArr = new char[]{'一','二','三','四','五','六','七','八','九'};
        char[] chArr = new char[]{'十','百','千','万','亿'};
        for (int i = 0; i < chineseNumber.length(); i++) {
            boolean b = true;//判断是否是chArr
            char c = chineseNumber.charAt(i);
            for (int j = 0; j < cnArr.length; j++) {//非单位，即数字
                if (c == cnArr[j]) {
                    if(0 != count){//添加下一个单位之前，先把上一个单位值添加到结果中
                        result += temp;
                        temp = 1;
                        count = 0;
                    }
                    // 下标+1，就是对应的值
                    temp = j + 1;
                    b = false;
                    break;
                }
            }
            if(b){//单位{'十','百','千','万','亿'}
                for (int j = 0; j < chArr.length; j++) {
                    if (c == chArr[j]) {
                        switch (j) {
                            case 0:
                                temp *= 10;
                                break;
                            case 1:
                                temp *= 100;
                                break;
                            case 2:
                                temp *= 1000;
                                break;
                            case 3:
                                temp *= 10000;
                                break;
                            case 4:
                                temp *= 100000000;
                                break;
                            default:
                                break;
                        }
                        count++;
                    }
                }
            }
            if (i == chineseNumber.length() - 1) {//遍历到最后一个字符
                result += temp;
            }
        }
        return result;
    }



    public static String replaceChineseNumberToInt(String str){

        str.replace("一", "1");
        str.replace("二", "2");
        str.replace("三", "3");
        str.replace("四", "4");
        str.replace("五", "5");
        str.replace("六", "6");
        str.replace("七", "7");
        str.replace("八", "8");
        str.replace("九", "9");
        str.replace("零", "0");
        char[] chArr = new char[]{'十','百','千','万','亿'};
        for(char c: chArr){
            str.replace(c + "", "");
        }
        return str;
    }


    public static String toChinese(String str) {
        String[] s1 = { "零", "一", "二", "三", "四", "五", "六", "七", "八", "九" };
        String[] s2 = { "十", "百", "千", "万", "十", "百", "千", "亿", "十", "百", "千" };
        String result = "";
        int n = str.length();
        for (int i = 0; i < n; i++) {
            int num = str.charAt(i) - '0';
            if (i != n - 1 && num != 0) {
                result += s1[num] + s2[n - 2 - i];
            } else {
                result += s1[num];
            }
        }
        return result;
    }





}
