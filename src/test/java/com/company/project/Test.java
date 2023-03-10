package com.company.project;

import com.company.project.common.utils.NumberUtils;

/**
 * @version V1.0
 * @author: YangHui
 * @date: 2021/2/10 19:07
 */
public class Test {
    public static void main(String[] args) {

        System.out.println(NumberUtils.toChinese("123"));

    }


    public static int climbStairs(int n) {
        if(n<=0)
            return 0;
        if(n==1){
            return 1;
        }
        if(n==2){
            return 2;
        }
        else
            return climbStairs(n-1)+climbStairs(n-2);
    }







}
