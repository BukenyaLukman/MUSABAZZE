package com.mobilesoftware.musabazze;

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
import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.mobilesoftware.musabazze.Company.CompanyLoginActivity;

public class CompanyRegisterActivity extends AppCompatActivity {

    private EditText CompanyEmail, CompanyPassword;
    private TextView AlreadyHaveAccount;
    private Button CreateAccount;
    private FirebaseAuth mAuth;
    private ProgressDialog loadingBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        FirebaseApp.initializeApp(this);
        setContentView(R.layout.activity_company_register);

        mAuth = FirebaseAuth.getInstance();


        initializeFields();

        CreateAccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                createCompanyAccount();
            }
        });

        AlreadyHaveAccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SendUserTOCompanyLogin();
            }
        });
    }


    private void SendUserTOCompanyLogin(){
        Intent loginIntent = new Intent(CompanyRegisterActivity.this, CompanyLoginActivity.class);
        startActivity(loginIntent);
    }

    private void createCompanyAccount() {
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

            loadingBar.setTitle("Creating New Account");
            loadingBar.setMessage("Please wait...");
            loadingBar.setCanceledOnTouchOutside(false);
            loadingBar.show();

            mAuth.createUserWithEmailAndPassword(email,password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if(task.isSuccessful()){

                        Toast.makeText(CompanyRegisterActivity.this, "Account Created successfully", Toast.LENGTH_SHORT).show();
                        loadingBar.dismiss();

                    }else{
                        String message = task.getException().toString();
                        Toast.makeText(CompanyRegisterActivity.this, "Error: "+ message, Toast.LENGTH_SHORT).show();
                        loadingBar.dismiss();

                    }
                }
            });
        }
    }

    private void initializeFields() {
        CompanyEmail = findViewById(R.id.company_email_reg);
        CompanyPassword = findViewById(R.id.company_password_reg);
        AlreadyHaveAccount = findViewById(R.id.already_have_account);
        CreateAccount = findViewById(R.id.create_account_btn);

        loadingBar = new ProgressDialog(this);
    }
}
