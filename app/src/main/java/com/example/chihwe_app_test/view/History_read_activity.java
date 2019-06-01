package com.example.chihwe_app_test.view;

import android.content.Context;
import android.graphics.Rect;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageButton;

import com.example.chihwe_app_test.R;
import com.example.chihwe_app_test.database.DatabaseHelper;
import com.example.chihwe_app_test.database.modal.News;

import java.util.ArrayList;
import java.util.List;

public class History_read_activity extends AppCompatActivity {

    RecyclerView recyclerView;
    RecyclerView.LayoutManager vertical_LayoutManager;
    Context currentcontext;
    ImageButton backbutton;
    history_adapter adapter;
    DatabaseHelper db;
    private List<News> newsList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history_read_activity);

        currentcontext = this;

        backbutton = findViewById(R.id.backbutton);

        recyclerView = findViewById(R.id.recycleview);

        vertical_LayoutManager = new LinearLayoutManager(currentcontext);
        recyclerView.setLayoutManager(vertical_LayoutManager);

        db = new DatabaseHelper(currentcontext);

        newsList.addAll(db.getAllNews());

        adapter = new history_adapter(currentcontext,newsList);
        recyclerView.setAdapter(adapter);

        backbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });


    }
}
