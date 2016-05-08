package com.software.xdtextbookgo.structure;

/**
 * Created by huang zhen xi on 2016/4/29.
 * 主页显示书籍信息的实体类，作为home_listview的适配类型
 */
public class BookInfo {
    private int imageId;
    private  String book_name;
    private String author_name;
    private String publisher_name;
    private String dept_name;
    private String grade_name;
    private String price_name;
    private String xinjiu_name;
    public BookInfo(int imageId, String book_name, String author_name, String publisher_name,String dept_name, String grade_name, String price_name, String xinjiu_name)
    {
        this.imageId = imageId;
        this.book_name = book_name;
        this.author_name = author_name;
        this.publisher_name = publisher_name;
        this.dept_name = dept_name;
        this.grade_name = grade_name;
        this.price_name = price_name;
        this.xinjiu_name = xinjiu_name;
    }
    public int getImageId(){
        return imageId;
    }

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
}
