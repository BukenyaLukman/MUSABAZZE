package com.mobilesoftware.musabazze;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;


public class MainActivity extends AppCompatActivity {
    private EditText CompanyEmailLogin,CompanyPassword;
    private Button LoginBtn;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initializeFields();

        LoginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SendUserToHomeActivity();
            }
        });

    }

    private void SendUserToHomeActivity() {
        Intent loginIntent = new Intent(MainActivity.this,HomeActivity.class);
        //loginIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        //loginIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(loginIntent);
        //finish();
    }

    private void initializeFields() {
        CompanyEmailLogin = findViewById(R.id.user_email);
        CompanyPassword = findViewById(R.id.user_password);
        LoginBtn =findViewById(R.id.login_btn);
    }

}
