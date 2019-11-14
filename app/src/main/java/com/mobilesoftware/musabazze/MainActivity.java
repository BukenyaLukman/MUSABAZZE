package com.mobilesoftware.musabazze;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.mobilesoftware.musabazze.Company.CompanyLoginActivity;

public class MainActivity extends AppCompatActivity {
    private Button TravelCompanyBtn,PassengerBtn,PersonalHire;
    private Toolbar mToolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mToolbar = findViewById(R.id.page_toolbar);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setTitle("Company Registration");

        InitializeFields();
        TravelCompanyBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SendUSerToCompanyActivity();

            }
        });
    }

    private void SendUSerToCompanyActivity() {
        Intent CompanyIntent = new Intent(MainActivity.this, CompanyLoginActivity.class);
        startActivity(CompanyIntent);
        finish();
    }


    private void InitializeFields(){
        TravelCompanyBtn = findViewById(R.id.company_btn);
        PassengerBtn = findViewById(R.id.passenger_btn);
        PersonalHire = findViewById(R.id.personal_hire);
    }

}
