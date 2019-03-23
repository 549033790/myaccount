package com.example.accountbook;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import org.litepal.LitePal;

public class LoginActivity extends MyActivityManager {
    private EditText editTextInputUsername;
    private EditText editTextInputPassword;
    private Button buttonLogin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        LitePal.getDatabase();

        editTextInputUsername=findViewById(R.id.editText_input_account);
        editTextInputPassword=findViewById(R.id.editText_input_password);
        buttonLogin=findViewById(R.id.button_login);

        buttonLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //验证账号密码进行登陆
                String username=editTextInputUsername.getText().toString();
                String password=editTextInputPassword.getText().toString();
                if(username.equals("") || password.equals("")){
                    Toast.makeText(LoginActivity.this, "Username or Password can't be vacant!", Toast.LENGTH_SHORT).show();
                }else{
                    if(username.equals("admin") && password.equals("admin")){
                        Intent intent=new Intent(LoginActivity.this, MainActivity.class).setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_NEW_TASK);
                        startActivity(intent);
                    }else{
                        Toast.makeText(LoginActivity.this, "Invalid Username or Password! ", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });
    }
}
