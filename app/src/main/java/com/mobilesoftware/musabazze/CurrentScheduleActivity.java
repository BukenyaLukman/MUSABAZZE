package com.mobilesoftware.musabazze;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;

public class CurrentScheduleActivity extends AppCompatActivity {

    private RecyclerView  recyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_current_schedule);

        recyclerView = findViewById(R.id.recycler_view);
    }
}
