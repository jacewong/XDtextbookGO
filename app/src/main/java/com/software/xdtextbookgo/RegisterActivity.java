package com.software.xdtextbookgo;

import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.avos.avoscloud.AVException;
import com.avos.avoscloud.SignUpCallback;
import com.software.xdtextbookgo.service.AVService;
import com.software.xdtextbookgo.views.ProgressView;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by huang zhen xi on 2016/4/28.
 */
public class RegisterActivity extends XDtextbookGOActivity implements View.OnClickListener {
    private TextView title_text;
    private ProgressBar progressBar;
    private Button bt_register;
    private EditText et_name, et_pwd, et_email;
    private static final String EMAIL_PATTERN = "^[a-zA-Z0-9#_~!$&'()*+,;=:.\"(),:;<>@\\[\\]\\\\]+@[a-zA-Z0-9-]+(\\.[a-zA-Z0-9-]+)+$";
    private Pattern pattern = Pattern.compile(EMAIL_PATTERN);
    private Matcher matcher;
    private TextInputLayout nameWrapper, emailWrapper, pwdWrapper;
    private String name,email,password;
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.register_layout);
        bt_register = (Button) findViewById(R.id.bt_register);

        ProgressView progressView = new ProgressView(this);
        progressBar = progressView.createProgressBar(this, null);
        progressBar.setVisibility(View.INVISIBLE);

        inittoolbar();

        et_name = (EditText) findViewById(R.id.et_name);
        et_pwd = (EditText) findViewById(R.id.et_pwd);
        et_email = (EditText) findViewById(R.id.et_email);
        nameWrapper = (TextInputLayout) findViewById(R.id.nameWrapper);
        emailWrapper = (TextInputLayout) findViewById(R.id.emailWrapper);
        pwdWrapper = (TextInputLayout) findViewById(R.id.pwdWrapper);
      //  nameWrapper.setErrorEnabled(true);
      //  emailWrapper.setErrorEnabled(true);
     //   pwdWrapper.setErrorEnabled(true);

   //     final TextInputLayout phoneWrapper = (TextInputLayout) findViewById(R.id.phoneWrapper);
       bt_register.setOnClickListener(this);
        et_name.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                checkName(s.toString(),false);
            }
        });
        et_email.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                checkEmail(s.toString(), false);

            }
        });
        et_pwd.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                checkPwd(s.toString(),false);
            }
        });

    }
    @Override
    public void onClick(View v){
        if(v.getId() == R.id.bt_register){
            hideKeyboard();
            if(!checkName(et_name.getText(),true))
                return;
            if (!checkEmail(et_email.getText(),true))
                return;
            if (!validateEmail(emailWrapper.getEditText().getText().toString())){
                emailWrapper.setError("请输入有效Email地址");
                return;
            }
            if(!checkPwd(et_pwd.getText(),true))
               return;
            progressBar.setVisibility(View.VISIBLE);
            doLogin();
        }
    }
    private void doLogin()
    {
        SignUpCallback signUpCallback = new SignUpCallback() {
            @Override
            public void done(AVException e) {
                //Log.e("tag",e.getCode()+"");
                progressBar.setVisibility(View.INVISIBLE);
                if (e == null){
                    showRegisterSuccess();
                }
                else{
                    switch (e.getCode()){
                        case 202:
                            showError("用户名已存在");
                            break;
                        case 203:
                            showError("Email已被注册");
                            break;
                        default:
                            showError(activity.getString(R.string.network_error));
                            break;
                    }
                }
            }
        };
        String name = nameWrapper.getEditText().getText().toString();
        String email = emailWrapper.getEditText().getText().toString();
        String password = pwdWrapper.getEditText().getText().toString();
        AVService.signUp(name, email, password, signUpCallback);
    }


    private void inittoolbar()
    {
        TextView title = (TextView) findViewById(R.id.title_text);
        title.setText("注册");
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle("");
        setSupportActionBar(toolbar);
        getSupportActionBar().setHomeButtonEnabled(true); //设置返回键可用
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.back);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }
    public boolean onOptionsItemSelected(MenuItem item)
    {
        // TODO Auto-generated method stub
        if(item.getItemId() == android.R.id.home)
        {
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void hideKeyboard() {
        View view = getCurrentFocus();
        if (view != null) {
            ((InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE)).
                    hideSoftInputFromWindow(view.getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
        }
    }


    public boolean validateEmail(String email) {
        matcher = pattern.matcher(email);
        return matcher.matches();
    }

    /**
     *若用户名为空且用户是想登录，提示错误，返回false。不为空，错误设为null
     */
    private boolean checkName(CharSequence name,boolean isLogin) {
        if(TextUtils.isEmpty(name)) {
            if(isLogin) {
                nameWrapper.setError(getString(R.string.error_name_empty));
                return false;
            }
        }else{
            nameWrapper.setError(null);
        }
        return true;
    }

    private boolean checkEmail(CharSequence name,boolean isLogin) {
        if(TextUtils.isEmpty(name)) {
            if(isLogin) {
                emailWrapper.setError(getString(R.string.error_email_empty));
                return false;
            }
        }else{
            emailWrapper.setError(null);
        }
        return true;
    }

    private boolean checkPwd(CharSequence name,boolean isLogin) {
        if(TextUtils.isEmpty(name)) {
            if(isLogin) {
                pwdWrapper.setError(getString(R.string.error_pwd_empty));
                return false;
            }
        }else{
            pwdWrapper.setError(null);
        }
        return true;
    }
    private void showRegisterSuccess() {
        new AlertDialog.Builder(activity)
                .setTitle(
                        activity.getResources().getString(
                                R.string.dialog_message_title))
                .setMessage("注册成功！")
                .setNegativeButton(android.R.string.ok,
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog,
                                                int which) {
                                finish();
                                dialog.dismiss();
                            }
                        }).show();
    }
}
