package com.software.xdtextbookgo.structure;

/**
 * Created by huang zhen xi on 2016/4/25.
 */

import com.software.xdtextbookgo.HomeActivity;
import com.software.xdtextbookgo.PersonalActivity;
import com.software.xdtextbookgo.R;

/**
 * 出售
 * 发布
 * 我的
 */
public enum EnumTabInfo {


    Tab_Index(0, "Tab_Home", HomeActivity.class, R.drawable.ic_home),
    Tab_Contact(1, "Tab_Publish", HomeActivity.class, R.drawable.ic_add),
    Tab_My(2, "Tab_My", PersonalActivity.class, R.drawable.ic_mine);

    private int index;
    private String tag;
    private Class<?> clss;
    private int icon;
    private int iconSelected;

    private EnumTabInfo(int index, String tag, Class<?> clss, int icon) {
        this.index = index;
        this.tag = tag;
        this.clss = clss;
        this.icon = icon;
    }

    /**
     * 通过索引得到枚举
     *
     * @param index
     * @return
     */
    public static EnumTabInfo getTabInfoByIndex(int index) {
        EnumTabInfo[] values = values();
        for (int i = 0; i < values.length; i++) {
            if (index == values[i].index)
                return values[i];
        }

        return null;
    }

    /**
     * 通过tag得到枚举信息
     *
     * @param tag
     * @return
     */
    public static EnumTabInfo getTabInfoByTag(String tag) {

        EnumTabInfo[] values = EnumTabInfo.values();
        for (int i = 0; i < values.length; i++) {
            if (tag.equals(values[i].tag))
                return values[i];
        }
        return null;
    }

    public int getIndex() {
        return index;
    }

    public void setIndex(int index) {
        this.index = index;
    }

    public String getTag() {
        return tag;
    }

    public void setTag(String tag) {
        this.tag = tag;
    }

    public Class<?> getClss() {
        return clss;
    }

    public void setClss(Class<?> clss) {
        this.clss = clss;
    }

    public int getIcon() {
        return icon;
    }

    public void setIcon(int icon) {
        this.icon = icon;
    }

}
