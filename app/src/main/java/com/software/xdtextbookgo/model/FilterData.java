package com.software.xdtextbookgo.model;

import com.software.xdtextbookgo.model.FilterEntity;

import java.io.Serializable;
import java.util.List;

/**
 * Created by sunfusheng on 16/4/23.
 */
public class FilterData implements Serializable {

    private List<FilterEntity> dept;
    private List<FilterEntity> grade;

    public List<FilterEntity> getDept() {
        return dept;
    }

    public void setDept(List<FilterEntity> dept) {
        this.dept = dept;
    }

    public List<FilterEntity> getGrade() {
        return grade;
    }

    public void setGrade(List<FilterEntity> grade) {
        this.grade = grade;
    }
}