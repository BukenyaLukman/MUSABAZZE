package com.mobilesoftware.musabazze.Company;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.mobilesoftware.musabazze.CompanyRegisterActivity;
import com.mobilesoftware.musabazze.CompanyTravelDetailsActivity;
import com.mobilesoftware.musabazze.R;
import com.mobilesoftware.musabazze.SetTravelRoutesActivity;

import java.util.HashMap;

public class CompanyLoginActivity extends AppCompatActivity {
    private EditText CompanyEmail,CompanyPassword;
    private Button LoginBtn;
    private TextView NeedNewAccount;
    private ProgressDialog loadingBar;
    private FirebaseAuth mAuth;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_company_login);

        mAuth = FirebaseAuth.getInstance();

        initializeFields();

        LoginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AllowUserToLogin();
            }
        });

        NeedNewAccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SendUserToRegisterActivity();
            }
        });
    }

    private void AllowUserToLogin() {
        String email = CompanyEmail.getText().toString();
        String password = CompanyPassword.getText().toString();

        if(TextUtils.isEmpty(email))
        {
            Toast.makeText(this, "Please enter your valid email...", Toast.LENGTH_SHORT).show();
        }
        if(TextUtils.isEmpty(password))
        {
            Toast.makeText(this, "Please enter your password...", Toast.LENGTH_SHORT).show();

        }else{
            loadingBar.setTitle("Signing in...");
            loadingBar.setMessage("Please wait...");
            loadingBar.setCanceledOnTouchOutside(false);
            loadingBar.show();


            mAuth.signInWithEmailAndPassword(email,password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if(task.isSuccessful()){
                        SendCompanyToSetTripActivity();
                        Toast.makeText(CompanyLoginActivity.this, "Logged in successfully", Toast.LENGTH_SHORT).show();
                        loadingBar.dismiss();


                    }else{
                        String message = task.getException().toString();
                        Toast.makeText(CompanyLoginActivity.this, "Error: " + message, Toast.LENGTH_SHORT).show();
                        loadingBar.dismiss();


                    }


                }
            });
        }
    }

    private void SendCompanyToSetTripActivity() {
        Intent homeIntent = new Intent(CompanyLoginActivity.this,CompanyTravelDetailsActivity.class);
        homeIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        homeIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(homeIntent);

    }


    private void SendUserToRegisterActivity() {
        Intent registerIntent = new Intent(CompanyLoginActivity.this, CompanyRegisterActivity.class);
        startActivity(registerIntent);

    }


    private void initializeFields() {
        CompanyEmail = findViewById(R.id.company_email);
        CompanyPassword = findViewById(R.id.company_password);
        LoginBtn = findViewById(R.id.login_btn);
        NeedNewAccount = findViewById(R.id.need_account);
        loadingBar = new ProgressDialog(this);
    }
}
