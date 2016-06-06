package com.software.xdtextbookgo.service;

import com.avos.avoscloud.AVException;
import com.avos.avoscloud.AVFile;
import com.avos.avoscloud.AVObject;
import com.avos.avoscloud.AVQuery;
import com.avos.avoscloud.AVUser;
import com.avos.avoscloud.FindCallback;
import com.avos.avoscloud.GetCallback;
import com.avos.avoscloud.SaveCallback;
import com.avos.avoscloud.SignUpCallback;

/**
 * Created by huang zhen xi on 2016/5/23.
 * leancloud云服务的服务接口
 */
public class AVService {

    public static void signUp(String username, String email,String password, SignUpCallback signUpCallback) {
        AVUser user = new AVUser();
        user.setUsername(username);
        user.setPassword(password);
        user.setEmail(email);
        user.signUpInBackground(signUpCallback);
    }


    public static void logout() {
        AVUser.logOut();
    }

    //上传/修改书籍信息
    public static void upLoadOrChange(String objectId,AVUser user,AVFile file, String bookName, String author, String publisher, double oriPrice, String dept, String grade,
                                    double price, int count, String xinjiu, SaveCallback saveCallback){
        AVObject object = new AVObject("SaleInfo");
        if (objectId != null) //修改书籍  获取其id号
            object.setObjectId(objectId);
        object.put("userName", user.getUsername());
        object.put("picture", file);
        object.put("bookName", bookName);
        object.put("author", author);
        object.put("publisher", publisher);
        object.put("oriPrice", oriPrice);
        object.put("dept", dept);
        object.put("grade", grade);
        object.put("price", price );
        object.put("count", count );
        object.put("xinjiu", xinjiu);
        object.saveInBackground(saveCallback);
    }

    public static void refreshQuery(int count_query,FindCallback findCallback){
        AVQuery<AVObject> query = new AVQuery<AVObject>("SaleInfo");
        query.orderByDescending("updatedAt");//按照时间降序
        query.setLimit(count_query);//最大6个
        query.findInBackground(findCallback);
    }

    public static void loadQuery(FindCallback findCallback){
        AVQuery<AVObject> query = new AVQuery<AVObject>("SaleInfo");
        query.orderByDescending("updatedAt");//按照时间降序
        //query.setLimit(6);//最大6个
        //query.skip(count);
        query.findInBackground(findCallback);
    }

    public static void userSaleQuery(String user, FindCallback findCallback){
        AVQuery<AVObject> query = new AVQuery<AVObject>("SaleInfo");
        query.whereEqualTo("userName", user);
        query.orderByDescending("updateAt");
        query.findInBackground(findCallback);
    }



}
