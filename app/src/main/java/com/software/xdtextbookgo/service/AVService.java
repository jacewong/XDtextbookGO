package com.software.xdtextbookgo.service;

import com.avos.avoscloud.AVFile;
import com.avos.avoscloud.AVObject;
import com.avos.avoscloud.AVUser;
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

    public static void upLoad(AVUser user,AVFile file, String bookName, String author, String publisher, String oriPrice, String dept, String grade,
                                    String price, String count, String xinjiu, SaveCallback saveCallback){
        AVObject object = new AVObject("SaleInfo");
        object.put("userName", user.getUsername());
        object.put("picture", file);
        object.put("bookName", bookName);
        object.put("author", author);
        object.put("publisher",publisher);
        object.put("oriPrice","￥"+ oriPrice + ".00");
        object.put("dept", dept);
        object.put("grade", grade);
        object.put("price", "￥"+ price + ".00");
        object.put("count", count +"本");
        object.put("xinjiu", xinjiu);
        object.saveInBackground(saveCallback);

    }
}
