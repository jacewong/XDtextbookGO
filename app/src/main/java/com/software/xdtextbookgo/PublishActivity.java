package com.software.xdtextbookgo;

import android.app.Activity;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.media.Image;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.design.widget.TabLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TabHost;
import android.widget.TextView;
import android.app.TabActivity;

import com.avos.avoscloud.AVException;
import com.avos.avoscloud.AVFile;
import com.avos.avoscloud.AVObject;
import com.avos.avoscloud.AVUser;
import com.avos.avoscloud.SaveCallback;
import com.software.xdtextbookgo.popupwindow.SelectPicPopupWindow;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;

import com.software.xdtextbookgo.filter_opensource.ImageManager;
import com.software.xdtextbookgo.service.AVService;

/**
 * Created by huang zhen xi on 2016/4/24.
 */
public class PublishActivity extends XDtextbookGOActivity {
    private TextView title_text, et_dept, et_xinjiu, et_grade;
    private EditText et_bookname,et_author,et_publisher, et_preprice, et_price, et_count;
    private Button btn_back,btn_publish;
    private ImageView img_btn;
    private SelectPicPopupWindow menuWindow;
    private Context mContext;
    private ImageManager imageManager;
    private ProgressBar progressBar;
    private static final int PHOTO_REQUEST_TAKEPHOTO = 1;// 拍照
    private static final int PHOTO_REQUEST_GALLERY = 2;// 从相册中选择
    private static final int PHOTO_REQUEST_CUT = 3;// 结果
    File outputImage = new File(Environment.getExternalStorageDirectory(),"output_image.png");
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mContext = PublishActivity.this;
        setContentView(R.layout.publish_layout);
        img_btn = (ImageView) findViewById(R.id.addImageView);
        progressBar = createProgressBar(this,null);
        progressBar.setVisibility(View.INVISIBLE);
        et_bookname = (EditText) findViewById(R.id.et_bookname);
        et_author = (EditText) findViewById(R.id.et_author);
        et_publisher = (EditText) findViewById(R.id.et_publisher);
        et_preprice = (EditText) findViewById(R.id.et_preprice);
        et_price = (EditText) findViewById(R.id.et_price);
        et_count = (EditText) findViewById(R.id.et_count);

        try {
            if (outputImage.exists()) {
                outputImage.delete();
            }
            outputImage.createNewFile();
        } catch (IOException e) {
            e.printStackTrace();
        }
        init();

        inittoolbar();

        //3个文本框弹出选项框，将选项框选择的内容传给文本
        et_dept = (TextView) this.findViewById(R.id.et_dept);
        et_grade = (TextView) this.findViewById(R.id.et_grade);
        et_xinjiu = (TextView) this.findViewById(R.id.et_xinjiu);
        et_dept.setOnClickListener(new View.OnClickListener() {

            public void onClick(View V) {
                new AlertDialog.Builder(PublishActivity.this)
                        .setTitle("院系")
                        .setItems(R.array.dept_items, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                final String[] dept_ary = getResources().getStringArray(R.array.dept_items); //getarray
                                et_dept.setText(dept_ary[which]);
                            }
                        })
                        .setNegativeButton("取消", new DialogInterface.OnClickListener() {

                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss(); //关闭alertDialog
                            }
                        })
                        .show();
            }
        });


        et_grade.setOnClickListener(new View.OnClickListener() {

            public void onClick(View V) {
                new AlertDialog.Builder(PublishActivity.this)
                        .setTitle("年级")
                        .setItems(R.array.grade_items, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                final String[] grade_ary = getResources().getStringArray(R.array.grade_items); //getarray
                                et_grade.setText(grade_ary[which]);
                            }
                        })
                        .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss(); //关闭alertDialog
                            }
                        })
                        .show();
            }
        });


        et_xinjiu.setOnClickListener(new View.OnClickListener() {

            public void onClick(View V) {
                new AlertDialog.Builder(PublishActivity.this)
                        .setTitle("书籍新旧")
                        .setItems(R.array.xinjiu_items, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                final String[] xinjiu_ary = getResources().getStringArray(R.array.xinjiu_items); //getarray
                                et_xinjiu.setText(xinjiu_ary[which]);
                            }
                        })
                        .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss(); //关闭alertDialog
                            }
                        })
                        .show();
            }
        });

        btn_publish = (Button) findViewById(R.id.bt_publish);
        btn_publish.setOnClickListener(publishListener);



    }



    View.OnClickListener publishListener = new View.OnClickListener(){
        public void onClick(View v){
            String bookName = et_bookname.getText().toString();
            String author = et_author.getText().toString();
            String publisher = et_publisher.getText().toString();
            String oriPrice = et_preprice.getText().toString();
            String dept = et_dept.getText().toString();
            String grade = et_grade.getText().toString();
            String price = et_price.getText().toString();
            String count = et_count.getText().toString();
            String xinjiu = et_xinjiu.getText().toString();
            if(bookName.isEmpty() || author.isEmpty() || publisher.isEmpty()||
                    oriPrice.isEmpty()||dept.isEmpty()||grade.isEmpty()||price.isEmpty()||count.isEmpty()||xinjiu.isEmpty()){
                showError(activity.getString(R.string.publish_message));
                return;
            }
         progressBar.setVisibility(View.VISIBLE);
            AVUser user = AVUser.getCurrentUser();
            try{
               final AVFile file = AVFile.withAbsoluteLocalPath("output_image.png", Environment.getExternalStorageDirectory() + "/output_image.png");
                file.saveInBackground(new SaveCallback() {
                    @Override
                    public void done(AVException e) {
                        if (e != null) {
                            progressBar.setVisibility(View.INVISIBLE);
                            showError(activity.getString(R.string.network_error));
                            // 存储成功
                        }
                        //      Log.d("tag", file.getUrl());//返回一个唯一的 Url 地址
                    }
                });
                SaveCallback saveCallback = new SaveCallback() {
                    @Override
                    public void done(AVException e) {
                        if (e == null) {
                            progressBar.setVisibility(View.INVISIBLE);
                            showPublishSuccess();
                            // 存储成功
                        } else {
                            progressBar.setVisibility(View.INVISIBLE);
                            showError(activity.getString(R.string.network_error));
                        }
                    }
                };
                AVService.upLoad(user, file, bookName, author, publisher, oriPrice, dept, grade, price, count, xinjiu, saveCallback);

            }catch (FileNotFoundException e){
                Log.e("tag","找不到文件");
            }


        }
    };

    private void inittoolbar()
    {
        TextView title = (TextView) findViewById(R.id.title_text);
        title.setText("出售书籍");
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
           // Intent intent = new Intent(PublishActivity.this,TabhostActivity.class);
           /// PublishActivity.this.startActivity(intent);
            //
            //TabhostActivity.Instance.getTabHost().setCurrentTab(0);
            //TabhostActivity.Instance.onTabChanged("Tab_Home");
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }


    /**
     * 5.2 zhao peng 增加拍照\相册获取图片，剪辑图片功能
     *
     * 5.7使用底部弹窗 zxhuang
     */

    public void backgroundAlpha(float bgAlpha)
    {
        WindowManager.LayoutParams lp = getWindow().getAttributes();
        lp.alpha = bgAlpha; //0.0-1.0
        getWindow().setAttributes(lp);
    }


    private void init() {
        //为ImageButton和Button添加监听事件
        img_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
  /*              backgroundAlpha(0.7f);
                menuWindow = new SelectPicPopupWindow(mContext, itemsOnClick);
                menuWindow.showAtLocation(findViewById(R.id.publishLayout),
                        Gravity.BOTTOM| Gravity.CENTER_HORIZONTAL, 0, 0);  */

                     showDialog();

            }
        });
    }
    //btn.setOnClickListener(this);

    //为弹出窗口实现监听类
  /*  private View.OnClickListener itemsOnClick = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            backgroundAlpha(1f);
            menuWindow.dismiss();
            switch (v.getId()) {
                // 拍照
                case R.id.takePhotoBtn:
                    // 调用系统的拍照功能
                    Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                    // 指定调用相机拍照后照片的储存路径
                    intent.putExtra(MediaStore.EXTRA_OUTPUT,Uri.fromFile(outputImage));
                    startActivityForResult(intent, PHOTO_REQUEST_TAKEPHOTO);

                // 相册选择图片
                case R.id.pickPhotoBtn:
                    Intent pickintent = new Intent(Intent.ACTION_PICK, null);
                    pickintent.setDataAndType(MediaStore.Images.Media.EXTERNAL_CONTENT_URI,"image/*");
                    startActivityForResult(pickintent, PHOTO_REQUEST_GALLERY);
                    break;
                default:
                    break;
            }
        }
    };  */





    //提示对话框方法
    private void showDialog() {
        new AlertDialog.Builder(this)
                .setTitle("图像设置")
                .setPositiveButton("拍照", new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // TODO Auto-generated method stub
                        dialog.dismiss();
                        // 调用系统的拍照功能
                        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                        // 指定调用相机拍照后照片的储存路径
                        intent.putExtra(MediaStore.EXTRA_OUTPUT,Uri.fromFile(outputImage));
                        startActivityForResult(intent, PHOTO_REQUEST_TAKEPHOTO);
                    }
                })
                .setNegativeButton("相册", new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // TODO Auto-generated method stub
                        dialog.dismiss();
                        Intent intent = new Intent(Intent.ACTION_PICK, null);
                        intent.setDataAndType(MediaStore.Images.Media.EXTERNAL_CONTENT_URI,"image/png");
                        startActivityForResult(intent, PHOTO_REQUEST_GALLERY);
                    }
                }).show();
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        // TODO Auto-generated method stub

        switch (requestCode) {
            case PHOTO_REQUEST_TAKEPHOTO:
                startPhotoZoom(Uri.fromFile(outputImage), 300);//更改数值来决定头像的大小
                break;

            case PHOTO_REQUEST_GALLERY:
                if (data != null)
                    startPhotoZoom(data.getData(), 300); //可以更改数值来决定头像的大小
                break;

            case PHOTO_REQUEST_CUT:
                if (data != null)
                    setPicToView(data);
                break;
        }
        super.onActivityResult(requestCode, resultCode, data);

    }

    private void startPhotoZoom(Uri uri, int size) {
        Intent intent = new Intent("com.android.camera.action.CROP");
        intent.setDataAndType(uri, "image/*");
        // crop为true是设置在开启的intent中设置显示的view可以剪裁
        intent.putExtra("crop", "true");

        // aspectX aspectY 是宽高的比例
        intent.putExtra("aspectX", 1);
        intent.putExtra("aspectY", 1);

        // outputX,outputY 是剪裁图片的宽高
        intent.putExtra("outputX", size);
        intent.putExtra("outputY", size);
        intent.putExtra("return-data", true);


        intent.putExtra(MediaStore.EXTRA_OUTPUT,Uri.fromFile(outputImage));
        startActivityForResult(intent, PHOTO_REQUEST_CUT);
    }

    //将进行剪裁后的图片显示到UI界面上
    private void setPicToView(Intent picdata) {
        Bundle bundle = picdata.getExtras();
        if (bundle != null) {
            Bitmap photo = bundle.getParcelable("data");
            Drawable drawable = new BitmapDrawable(photo);
            img_btn.setBackgroundDrawable(drawable);
        }
    }


    private void showPublishSuccess() {
        new AlertDialog.Builder(activity)
                .setTitle(
                        activity.getResources().getString(
                                R.string.dialog_message_title))
                .setMessage("发布成功！")
                .setNegativeButton(android.R.string.ok,
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog,
                                                int which) {
                                finish();
                                dialog.dismiss();
                            }
                        }).show();
    }


    /**
     * 在屏幕上添加一个转动条，默认为隐藏状态
     * 注意：务必保证此方法在setContentView()方法后调用，否则小菊花将会处于最底层，被屏幕其他View给覆盖
     *
     * @param activity                    需要添加菊花的Activity
     * @param customIndeterminateDrawable 自定义的菊花图片，可以为null，此时为系统默认菊花
     * @return {ProgressBar}    菊花对象
     */
    private ProgressBar createProgressBar(Activity activity, Drawable customIndeterminateDrawable) {
        // activity根部的ViewGroup，其实是一个FrameLayout
        FrameLayout rootContainer = (FrameLayout) activity.findViewById(android.R.id.content);
        // 给progressbar准备一个FrameLayout的LayoutParams
        FrameLayout.LayoutParams lp = new FrameLayout.LayoutParams(
                ViewGroup.LayoutParams.WRAP_CONTENT,
                ViewGroup.LayoutParams.WRAP_CONTENT);
        // 设置对其方式为：屏幕居中对其
        lp.gravity = Gravity.CENTER_VERTICAL | Gravity.CENTER_HORIZONTAL;

        ProgressBar progressBar = new ProgressBar(activity);
        progressBar.setVisibility(View.GONE);
        progressBar.setLayoutParams(lp);
        // 自定义小菊花
        if (customIndeterminateDrawable != null) {
            progressBar.setIndeterminateDrawable(customIndeterminateDrawable);
        }
        // 将菊花添加到FrameLayout中
        rootContainer.addView(progressBar);
        return progressBar;
    }
}

