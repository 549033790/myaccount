package com.example.accountbook;

import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.TextView;

import org.litepal.LitePal;
import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends MyActivityManager {
    private ImageView imageViewAddButton;
    private List<MyAccount> myAccountList=new ArrayList<>();
    private ProgressDialog progressDialog=new ProgressDialog();
    private RecyclerView recyclerView;
    private LinearLayoutManager layoutManager=new LinearLayoutManager(this);
    private float income_total=0;
    private float outcome_total=0;
    private TextView textView_outcome_total;
    private TextView textView_income_total;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        recyclerView=findViewById(R.id.recyclerView_list);

        textView_income_total=findViewById(R.id.textView_income_total);

        textView_outcome_total=findViewById(R.id.textView_outcome_total);

        imageViewAddButton=findViewById(R.id.imageView_add_button);
        imageViewAddButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //启动添加的Activity
                Intent intent=new Intent(MainActivity.this,AddActivity.class);
                startActivity(intent);
            }
        });
    }

    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);
        refreshTask task=new refreshTask();
        task.execute();
    }

    class refreshTask extends AsyncTask<Void,Void,Void>{
        @Override
        protected void onPreExecute() {
            progressDialog.show();
            outcome_total=0;
            income_total=0;
            myAccountList.clear();
        }

        @Override
        protected Void doInBackground(Void... voids) {
            myAccountList=LitePal.findAll(MyAccount.class);
            for(MyAccount myAccount:myAccountList){
                if(myAccount.getPriceType()==1){
                    outcome_total=outcome_total+Float.parseFloat(myAccount.getPrice());
                }else{
                    income_total=income_total+ Float.parseFloat(myAccount.getPrice());
                }
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            layoutManager.setStackFromEnd(true);
            layoutManager.setReverseLayout(true);
            recyclerView.setLayoutManager(layoutManager);
            MyAccountListAdapter adapter=new MyAccountListAdapter(MainActivity.this,myAccountList);
            recyclerView.setAdapter(adapter);
            textView_income_total.setText(String.valueOf(income_total));
            textView_outcome_total.setText(String.valueOf(outcome_total));
            progressDialog.dismiss();
        }
    }

    //显示进度的弹窗
    class ProgressDialog {
        private PopupWindow popupWindow;
        private View popupView;
        private TranslateAnimation animation;

        private void show() {
            this.popupView = View.inflate(MainActivity.this, R.layout.layout_waiting, null);

            // 参数2,3：指明popupwindow的宽度和高度
            popupWindow = new PopupWindow(popupView, 500, 500);
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

            // 设置点击popupwindow外屏幕其它地方消失
            popupWindow.setFocusable(false);
            popupWindow.setOutsideTouchable(false);
            // 平移动画相对于手机屏幕的底部开始，X轴不变，Y轴从1变0
            animation = new TranslateAnimation(Animation.RELATIVE_TO_PARENT, 0, Animation.RELATIVE_TO_PARENT, 0,
                    Animation.RELATIVE_TO_PARENT, 1, Animation.RELATIVE_TO_PARENT, 0);
            animation.setInterpolator(new AccelerateInterpolator());
            animation.setDuration(200);
            // 设置popupWindow的显示位置，此处是在手机屏幕底部且水平居中的位置
            popupWindow.showAtLocation(MainActivity.this.findViewById(R.id.recyclerView_list), Gravity.CENTER_VERTICAL | Gravity.CENTER_HORIZONTAL, 0, 0);
            popupView.startAnimation(animation);
            getWindow().setFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE, WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
        }

        private void dismiss() {
            popupWindow.dismiss();
            popupView = null;
            getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
        }
    }


}
