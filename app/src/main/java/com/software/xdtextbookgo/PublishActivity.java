package com.software.xdtextbookgo;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.avos.avoscloud.AVException;
import com.avos.avoscloud.AVFile;
import com.avos.avoscloud.AVUser;
import com.avos.avoscloud.SaveCallback;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.HashMap;

import com.bumptech.glide.Glide;
import com.software.xdtextbookgo.model.BookInfo;
import com.software.xdtextbookgo.service.AVService;
import com.software.xdtextbookgo.views.ActionSheetDialog;
import com.software.xdtextbookgo.views.ProgressView;

/**
 * Created by huang zhen xi on 2016/4/24.
 */
public class PublishActivity extends XDtextbookGOActivity {
    private TextView et_dept, et_xinjiu, et_grade;
    private EditText et_bookname,et_author,et_publisher, et_preprice, et_price, et_count;
    private Button btn_publish;
    private ImageView img_btn;
    private Context mContext;
    private ProgressBar progressBar;
    private ActionSheetDialog mActionSheet;
    private BookInfo mbook;
    private String bookName, author, publisher, dept, grade, xinjiu, objectId;
    private int count;
    private double oriPrice, price;
    private AVUser user = AVUser.getCurrentUser();
    private static final int PHOTO_REQUEST_TAKEPHOTO = 1;// 拍照
    private static final int PHOTO_REQUEST_GALLERY = 2;// 从相册中选择
    private static final int PHOTO_REQUEST_CUT = 3;// 结果

    private boolean flag = false;
    File outputImage = new File(Environment.getExternalStorageDirectory(),"output_image.jpg");
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mContext = PublishActivity.this;
        setContentView(R.layout.publish_layout);
        img_btn = (ImageView) findViewById(R.id.addImageView);

        ProgressView progressView = new ProgressView(this);
        progressBar = progressView.createProgressBar(this, null);
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

        mbook = (BookInfo) getIntent().getSerializableExtra("book");
        if (mbook != null){
            initData();
        }

        btn_publish = (Button) findViewById(R.id.bt_publish);
        btn_publish.setOnClickListener(publishListener);

    }




    View.OnClickListener publishListener = new View.OnClickListener(){
        public void onClick(View v){
            bookName = et_bookname.getText().toString();
            author = et_author.getText().toString();
            publisher = et_publisher.getText().toString();
            String oriPri = et_preprice.getText().toString();
            dept = et_dept.getText().toString();
            grade = et_grade.getText().toString();
            String newPri = et_price.getText().toString();
            String cnt = et_count.getText().toString();
            xinjiu = et_xinjiu.getText().toString();
            if(bookName.isEmpty() || author.isEmpty() || publisher.isEmpty()||
                    oriPri.isEmpty()||dept.isEmpty()||grade.isEmpty()||newPri.isEmpty()||cnt.isEmpty()||xinjiu.isEmpty()){
                showError(activity.getString(R.string.publish_message));
                return;
            }
            oriPrice = Double.parseDouble(oriPri);
            count = Integer.parseInt(cnt);
            price = Double.parseDouble(newPri);

            progressBar.setVisibility(View.VISIBLE);
            if(mbook!=null)
                uploadChange();
            else uploadNew();

        }
    };

    private void initData(){
        objectId = mbook.getObjectId();
        et_bookname.setText(mbook.getBook_name());
        et_author.setText(mbook.getAuthor_name());
        et_publisher.setText(mbook.getPublisher_name());
        et_preprice.setText(mbook.getOri_price()+"");
        et_dept.setText(mbook.getDept_name());
        et_grade.setText(mbook.getGrade_name());
        et_price.setText(mbook.getPrice_name() + "");
        et_count.setText(mbook.getCount() + "");
        et_xinjiu.setText(mbook.getXinjiu_name());
        img_btn.setBackgroundDrawable(null);
        Glide.with(img_btn.getContext())
                .load(mbook.getImageId())
                .into(img_btn);



    }

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


    private void uploadNew(){
        try{
            final AVFile file = AVFile.withAbsoluteLocalPath("output_image.jpg", Environment.getExternalStorageDirectory() + "/output_image.jpg");
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
            AVService.upLoadOrChange(objectId, user, file, bookName, author, publisher, oriPrice, dept, grade, price, count, xinjiu, saveCallback);

        }catch (FileNotFoundException e){
            Log.e("tag","找不到文件");
        }
    }


    private void uploadChange(){
        if (flag == true){
            uploadNew();
        }
        else{
            final AVFile file = new AVFile("output_image.jpg", mbook.getImageId(), new HashMap<String, Object>());
            file.saveInBackground(new SaveCallback() {
                @Override
                public void done(AVException e) {
                    if (e != null) {
                        progressBar.setVisibility(View.INVISIBLE);
                        showError(activity.getString(R.string.network_error));
                        Log.e("error", e.getCode()+"");
                        // 存储成功
                    }
                    Log.e("success", file.getUrl());//返回一个唯一的 Url 地址
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
            AVService.upLoadOrChange(objectId, user, file, bookName, author, publisher, oriPrice, dept, grade, price, count, xinjiu, saveCallback);
        }
    }

    public boolean onOptionsItemSelected(MenuItem item)
    {
        // TODO Auto-generated method stub
        if(item.getItemId() == android.R.id.home)
        {
            showBackMsg();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }


    /**
     * 5.2 zhao peng 增加拍照\相册获取图片，剪辑图片功能
     *
     * 6.2使用底部弹窗 actionsheet zxhuang
     */

    private void init() {
        //为ImageButton和Button添加监听事件
        img_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if ( mActionSheet == null) {
                    mActionSheet = new ActionSheetDialog(PublishActivity.this);
                    mActionSheet.addMenuItem("拍照").addMenuItem("相册");
                    mActionSheet.setMenuListener(new ActionSheetDialog.MenuListener() {
                        @Override
                        public void onItemSelected(int position, String item) {
                            switch (position){
                                case 0:
                                    // 调用系统的拍照功能
                                    Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                                    // 指定调用相机拍照后照片的储存路径
                                    intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(outputImage));
                                    startActivityForResult(intent, PHOTO_REQUEST_TAKEPHOTO);
                                    break;
                                case 1:
                                    Intent picintent = new Intent(Intent.ACTION_PICK, null);
                                    picintent.setDataAndType(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, "image/*");
                                    startActivityForResult(picintent, PHOTO_REQUEST_GALLERY);
                                    break;
                                default:
                                    break;
                            }
                        }
                        @Override
                        public void onCancel() {
                        }
                    });
                }
                mActionSheet.show();

                    // showDialog();
            }
        });
    }

    //提示对话框方法
  /*  private void showDialog() {
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
                        intent.setDataAndType(MediaStore.Images.Media.EXTERNAL_CONTENT_URI,"image/*");
                        startActivityForResult(intent, PHOTO_REQUEST_GALLERY);
                    }
                }).show();
    }  */

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
        intent.putExtra("crop", "view");

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
        flag = true;
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

    private  void showBackMsg(){
        new AlertDialog.Builder(activity)
                .setTitle(
                        activity.getResources().getString(
                                R.string.dialog_message_title))
                .setMessage("确定返回?")
                .setNegativeButton(android.R.string.cancel,
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog,
                                                int which) {
                                dialog.dismiss();
                            }
                        })
                .setPositiveButton(android.R.string.ok,
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog,
                                                int which) {
                                finish();
                                dialog.dismiss();
                            }
                        })
                .show();
    }
}

