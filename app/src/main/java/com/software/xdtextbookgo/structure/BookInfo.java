package com.software.xdtextbookgo.structure;

import java.io.Serializable;
import java.net.URL;

/**
 * Created by huang zhen xi on 2016/4/29.
 * 主页显示书籍信息的实体类，作为home_listview的适配类型
 */
public class BookInfo implements Serializable {
    private String imageId;
    private String book_name;
    private String author_name;
    private String publisher_name;
    private String dept_name;
    private String grade_name;
    private String ori_price;
    private String price_name;
    private String xinjiu_name;
    private String count;
    private String publish_user;

    public String getUser(){return publish_user;}
    public String getCount(){return count;}

    public String getImageId(){
        return imageId;
    }

    public String getOri_price(){return ori_price;}

    public String getAuthor_name() {
        return author_name;
    }

    public String getBook_name() {
        return book_name;
    }

    public String getDept_name() {
        return dept_name;
    }

    public String getGrade_name() {
        return grade_name;
    }

    public String getPrice_name() {
        return price_name;
    }

    public String getPublisher_name() {
        return publisher_name;
    }

    public String getXinjiu_name() {
        return xinjiu_name;
    }

    public void setCount(String count){this.count = count;}

    public void setImageId(String imageId){
        this.imageId = imageId;
    }

    public void setOri_price(String ori_price){this.ori_price = ori_price;}

    public void setAuthor_name(String author_name) {
        this.author_name=author_name;
    }

    public void setBook_name(String book_name) {
        this.book_name = book_name;
    }

    public void setDept_name(String dept_name) {
        this.dept_name = dept_name;
    }

    public void setGrade_name(String grade_name) {
        this.grade_name = grade_name;
    }

    public void setPrice_name(String price_name) {
        this.price_name = price_name;
    }

    public void setPublisher_name(String publisher_name) {this.publisher_name = publisher_name;
    }
    public void setPublish_user(String publish_user){this.publish_user = publish_user;}

    public void setXinjiu_name(String xinjiu_name) {
        this.xinjiu_name = xinjiu_name;
    }
}
