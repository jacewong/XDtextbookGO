package com.software.xdtextbookgo.service;

import java.text.DecimalFormat;

/**
 * Created by huang on 2016/6/3.
 */
public class DoubleFormat {
    public String changeFormat(double num){
        DecimalFormat df = new DecimalFormat("######0.00");
        String result = df.format(num);
        return result;
    }
}
