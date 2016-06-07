package com.software.xdtextbookgo.model;

import java.util.ArrayList;
import java.util.List;

/**
 *
 * Created by huang zhen xi on 16/6/2.
 *过滤器显示过滤的数据
 */
public class ModelUtil {
    // 院系数据
    public static List<FilterEntity> getDeptData() {
        List<FilterEntity> list = new ArrayList<>();
        list.add(new FilterEntity("全部", "1"));
        list.add(new FilterEntity("通信工程学院", "2"));
        list.add(new FilterEntity("电子信息工程学院", "3"));
        list.add(new FilterEntity("计算机学院", "4"));
        list.add(new FilterEntity("机电工程学院", "5"));
        list.add(new FilterEntity("物理与光电工程学院", "6"));
        list.add(new FilterEntity("经济与管理学院", "7"));
        list.add(new FilterEntity("软件学院", "8"));
        return list;
    }

    // 年级数据
    public static List<FilterEntity> getGradeData() {
        List<FilterEntity> list = new ArrayList<>();
        list.add(new FilterEntity("全部", "1"));
        list.add(new FilterEntity("大一", "2"));
        list.add(new FilterEntity("大二", "3"));
        list.add(new FilterEntity("大三", "4"));
        list.add(new FilterEntity("大四", "5"));
        return list;
    }

}