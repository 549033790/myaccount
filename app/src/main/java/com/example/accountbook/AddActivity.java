package com.example.accountbook;

import android.Manifest;
import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Build;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.FileProvider;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class AddActivity extends MyActivityManager implements View.OnClickListener {
    private TabLayout mTabLayout;
    private String[] mTitles={"Outcome","Income"};
    private MyFragmentPagerAdapter mAdapter;
    private ViewPager mViewPager;
    private MyAccount myAccount=new MyAccount(); //用来添加用的
    SimpleDateFormat format=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    PopupWindow popupWindow;
    View popupView;
    TranslateAnimation animation;

    private RadioButton radioButton_income;
    private RadioButton radioButton_outcome;
    private EditText editText_input_price_take_photo;
    private Button button_ok;
    private Button button_cancel;

    private static final int TAKE_PHOTO=999;

    private String outputImageName=null;
    private Uri imageUri;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add);

        mTabLayout=findViewById(R.id.tabLayout);
        mViewPager=findViewById(R.id.viewPager);

        addTabToTabLayout();
        setupWithViewPager();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.imageView_type_bonus:
                myAccount.setType("bonus");
                myAccount.setPriceType(0);
                setPopup();
                break;
            case R.id.imageView_type_investment:
                myAccount.setType("investment");
                myAccount.setPriceType(0);
                setPopup();
                break;
            case R.id.imageView_type_salary:
                myAccount.setType("salary");
                myAccount.setPriceType(0);
                setPopup();
                break;
            case R.id.imageView_type_food:
                myAccount.setType("food");
                myAccount.setPriceType(1);
                setPopup();
                break;
            case R.id.imageView_type_fruits:
                myAccount.setType("fruits");
                myAccount.setPriceType(1);
                setPopup();
                break;
            case R.id.imageView_type_communication:
                myAccount.setType("communication");
                myAccount.setPriceType(1);
                setPopup();
                break;
            case R.id.imageView_type_education:
                myAccount.setType("education");
                myAccount.setPriceType(1);
                setPopup();
                break;
            case R.id.imageView_type_trip:
                myAccount.setType("trip");
                myAccount.setPriceType(1);
                setPopup();
                break;
            case R.id.imageView_type_cloth:
                myAccount.setType("cloth");
                myAccount.setPriceType(1);
                setPopup();
                break;
            case R.id.imageView_type_medical:
                myAccount.setType("medical");
                myAccount.setPriceType(1);
                setPopup();
                break;
            case R.id.imageView_type_entertainment:
                myAccount.setType("entertainment");
                myAccount.setPriceType(1);
                setPopup();
                break;
            case R.id.imageView_type_takePhotos:
                //处理自己拍照记录的情况
                //运行时权限处理，这里需要相机权限和读写内部储存的权限
                SimpleDateFormat formatImageName=new SimpleDateFormat("yyyyMMdd_hhmmss");
                outputImageName=formatImageName.format(new Date())+".jpg";
                if(ContextCompat.checkSelfPermission(AddActivity.this, Manifest.permission.WRITE_EXTERNAL_STORAGE)!= PackageManager.PERMISSION_GRANTED){
                    ActivityCompat.requestPermissions(AddActivity.this,new String[]{
                            Manifest.permission.READ_EXTERNAL_STORAGE,
                            Manifest.permission.WRITE_EXTERNAL_STORAGE,
                            Manifest.permission.CAMERA
                    },1);
                }else{
                    //权限检查正常，在这里调用摄像头拍照储存
                    SetInfoDialog dialog=new SetInfoDialog(AddActivity.this);
                    if(dialog.showDialog()==0){
                        takePhoto();
                    }else{
                        Toast.makeText(this, "Canceled", Toast.LENGTH_SHORT).show();
                    }
                }
                break;
            case R.id.button_add_back:
                onBackPressed();
                break;
                default:
                    break;
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch(requestCode){
            case 1:
                if(grantResults.length>0 && grantResults[0]==PackageManager.PERMISSION_GRANTED && grantResults[1]==PackageManager.PERMISSION_GRANTED){
                    //照照片存入手机文件夹里
                    SetInfoDialog dialog=new SetInfoDialog(AddActivity.this);
                    if(dialog.showDialog()==0){
                        takePhoto();
                    }else{
                        Toast.makeText(this, "Canceled", Toast.LENGTH_SHORT).show();
                    }
                }else{
                    Toast.makeText(this, "Permission denied!", Toast.LENGTH_SHORT).show();
                }
                default:
                    break;
        }
    }

    private void takePhoto(){
        File outputImage=new File(getExternalFilesDir(null),outputImageName);
        Log.d("AddActivity", "takePhoto: "+getExternalFilesDir(null).toString()+outputImage);
        myAccount.setPhotoPath(outputImageName);
        try{
            if(outputImage.exists()){
                outputImage.delete();
            }
            outputImage.createNewFile();
        }catch(Exception e){
            e.printStackTrace();
        }

        if(Build.VERSION.SDK_INT>=24){
            imageUri=FileProvider.getUriForFile(AddActivity.this,"com.example.accountbook.fileprovider",outputImage);
        }else{
            imageUri=Uri.fromFile(outputImage);
        }

        //启动相机
        Intent intent=new Intent("android.media.action.IMAGE_CAPTURE");
        intent.putExtra(MediaStore.EXTRA_OUTPUT,imageUri);
        startActivityForResult(intent,TAKE_PHOTO);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        switch(requestCode){
            case TAKE_PHOTO:
                if(resultCode==RESULT_OK){
                    myAccount.save();
                    Toast.makeText(this, myAccount.getType()+" "+myAccount.getPrice()+" Saved!", Toast.LENGTH_SHORT).show();
                }else{
                    Toast.makeText(this, "Canceled!", Toast.LENGTH_SHORT).show();
                }

        }
    }

    private void addTabToTabLayout() {
        for (int i = 0; i < mTitles.length; i++) {
            mTabLayout.addTab(mTabLayout.newTab().setText(mTitles[i]));
        }
    }

    private void setupWithViewPager() {
        List<Fragment> mFragments = new ArrayList<>();
        mFragments.add(new FirstFragment());
        mFragments.add(new SecondFragment());
        mAdapter = new MyFragmentPagerAdapter(getSupportFragmentManager());
        mAdapter.addTitlesAndFragments(mTitles, mFragments);
        mViewPager.setAdapter(mAdapter); // 给ViewPager设置适配器
        mTabLayout.setupWithViewPager(mViewPager); //关联TabLayout和ViewPager
    }

    //显示结果的popup窗口
    private void setPopup() {


        popupView = View.inflate(this, R.layout.popup_add,null);
        final EditText inputPrice=popupView.findViewById(R.id.editText_input_price);
        final TextView inputType=popupView.findViewById(R.id.textView_add_type);
        inputType.setText(myAccount.getType());
        ImageView imageView=popupView.findViewById(R.id.imageView_add_submit);

        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                myAccount.setTime(format.format(new Date()));
                myAccount.setPrice(inputPrice.getText().toString());
                myAccount.save();
                popupWindow.dismiss();
                Toast.makeText(AddActivity.this, "Price "+inputPrice.getText().toString()+" has been recorded!", Toast.LENGTH_SHORT).show();
                popupView=null;
            }
        });

        // 参数2,3：指明popupwindow的宽度和高度
        popupWindow = new PopupWindow(popupView, 300, 300);
        WindowManager.LayoutParams lp = getWindow().getAttributes();
        lp.alpha = 0.4f;
        getWindow().setAttributes(lp);
        popupWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {
                WindowManager.LayoutParams lp = getWindow().getAttributes();
                lp.alpha = 1f;
                getWindow().setAttributes(lp);
            }
        });

        // 设置背景图片， 必须设置，不然动画没作用
        //popupWindow.setBackgroundDrawable(new BitmapDrawable());
        popupWindow.setFocusable(true);

        // 设置点击popupwindow外屏幕其它地方消失
        popupWindow.setOutsideTouchable(true);

        // 平移动画相对于手机屏幕的底部开始，X轴不变，Y轴从1变0
        animation = new TranslateAnimation(Animation.RELATIVE_TO_PARENT, 0, Animation.RELATIVE_TO_PARENT, 0,
                Animation.RELATIVE_TO_PARENT, 1, Animation.RELATIVE_TO_PARENT, 0);
        animation.setInterpolator(new AccelerateInterpolator());
        animation.setDuration(200);


        // 设置popupWindow的显示位置，此处是在手机屏幕底部且水平居中的位置
        popupWindow.showAtLocation(findViewById(R.id.imageView_type_takePhotos), Gravity.CENTER_VERTICAL | Gravity.CENTER_HORIZONTAL, 0, 0);
        popupView.startAnimation(animation);
    }

    //自己拍照的话就需要单独的dialog，设置完成以后调用拍照
    public class SetInfoDialog extends Dialog
    {
        int dialogResult;
        Handler mHandler ;

        public SetInfoDialog(Activity context)
        {
            super(context);
            setOwnerActivity(context);
            onCreate();
        }

        public int getDialogResult()
        {
            return dialogResult;
        }

        public void setDialogResult(int dialogResult)
        {
            this.dialogResult = dialogResult;
        }

        public void onCreate() {
            setContentView(R.layout.dialog_take_photo_info);
            radioButton_income=findViewById(R.id.radioButton_income);
            radioButton_outcome=findViewById(R.id.radioButton_outcome);
            editText_input_price_take_photo=findViewById(R.id.editText_input_price_take_photo);
            button_ok=findViewById(R.id.button_take_photo_popup_ok);
            button_cancel=findViewById(R.id.button_take_photo_popup_cancel);

            radioButton_outcome.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(radioButton_income.isChecked()){
                        radioButton_income.setChecked(false);
                    }
                    radioButton_outcome.setChecked(true);
                    myAccount.setType("outcome_take");
                    myAccount.setPriceType(1);
                }
            });

            radioButton_income.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(radioButton_outcome.isChecked()){
                        radioButton_outcome.setChecked(false);
                    }
                    radioButton_income.setChecked(true);
                    myAccount.setType("income_take");
                    myAccount.setPriceType(0);
                }
            });

            button_ok.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    myAccount.setPrice(editText_input_price_take_photo.getText().toString());
                    myAccount.setTime(format.format(new Date()));
                    endDialog(0);
                }
            });

            button_cancel.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    endDialog(1);
                }
            });
        }

        public void endDialog(int result) {
            dismiss();
            setDialogResult(result);
            Message m = mHandler.obtainMessage();
            mHandler.sendMessage(m);
        }

        public int showDialog() {
            mHandler = new Handler() {
                @Override
                public void handleMessage(Message mesg) {
                    // process incoming messages here
                    //super.handleMessage(msg);
                    throw new RuntimeException();
                }
            };
            super.show();
            try {
                Looper.getMainLooper().loop();
            }
            catch(RuntimeException e2) {
                e2.printStackTrace();
            }
            return dialogResult;
        }

    }

}


