package com.software.xdtextbookgo.service;

import com.avos.avoscloud.AVUser;
import com.avos.avoscloud.SignUpCallback;

/**
 * Created by huang zhen xi on 2016/5/23.
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

}
