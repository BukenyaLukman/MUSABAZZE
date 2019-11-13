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
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.mobilesoftware.musabazze.CompanyTravelDetailsActivity;
import com.mobilesoftware.musabazze.R;

import java.util.HashMap;

public class CompanyLoginActivity extends AppCompatActivity {
    private EditText CompanyName,CompManager,CompPhoneNumber;
    private String CompanyNameInput,CompanyManagerInput,CompanyNumberInput,CompanyKey;
    private Button ConfirmButton;
    private ProgressDialog loadingBar;
    private DatabaseReference InfoRef;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_company_login);

        InfoRef = FirebaseDatabase.getInstance().getReference().child("Companies");

        initializeFields();

        ConfirmButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                saveInfoToDatabase();
            }
        });
    }

    private void saveInfoToDatabase() {
       CompanyNameInput = CompanyName.getText().toString().toUpperCase();
       CompanyManagerInput = CompManager.getText().toString().toUpperCase();
       CompanyNumberInput = CompPhoneNumber.getText().toString();
       if (TextUtils.isEmpty(CompanyNameInput)){
           Toast.makeText(this, "Please Provide Company Name", Toast.LENGTH_SHORT).show();
       }else if(TextUtils.isEmpty(CompanyManagerInput)){
           Toast.makeText(this, "Please provide the Manager's Name", Toast.LENGTH_SHORT).show();
       }else if(TextUtils.isEmpty(CompanyNumberInput)){
           Toast.makeText(this, "Please provide the dedicated Phone Number", Toast.LENGTH_SHORT).show();
       }else{
            storeCompanyInfoToDatabase();
       }

    }

    private void storeCompanyInfoToDatabase() {
        loadingBar.setTitle("Saving Info...");
        loadingBar.setMessage("Please wait...");
        loadingBar.setCanceledOnTouchOutside(false);
        loadingBar.show();

        CompanyKey = CompanyNameInput + CompanyNumberInput;

        HashMap<String,Object> companyInfoMap = new HashMap<>();
        companyInfoMap.put("CompanyName",CompanyNameInput);
        companyInfoMap.put("CompanyManager",CompanyManagerInput);
        companyInfoMap.put("CompanyNumber",CompanyNumberInput);

        InfoRef.child(CompanyKey).updateChildren(companyInfoMap)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if(task.isSuccessful()){


                            Intent intent = new Intent(CompanyLoginActivity.this, CompanyTravelDetailsActivity.class);
                            //intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                            //intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                            startActivity(intent);

                            loadingBar.dismiss();
                            Toast.makeText(CompanyLoginActivity.this, CompanyNameInput +" registration successful", Toast.LENGTH_SHORT).show();
                        }else {
                            loadingBar.dismiss();
                            String message = task.getException().toString();
                            Toast.makeText(CompanyLoginActivity.this, "Error: " + message, Toast.LENGTH_SHORT).show();
                        }
                    }
                });

    }

    private void initializeFields() {
        CompanyName = findViewById(R.id.company_name);
        CompManager = findViewById(R.id.company_coordinator);
        CompPhoneNumber = findViewById(R.id.company_contact);
        ConfirmButton = findViewById(R.id.confirm_info_btn);
        loadingBar = new ProgressDialog(this);
    }
}
