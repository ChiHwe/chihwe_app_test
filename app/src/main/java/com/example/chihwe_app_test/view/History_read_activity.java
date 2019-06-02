package com.example.chihwe_app_test.view;


import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.view.View;
import android.widget.ImageButton;

import com.example.chihwe_app_test.R;
import com.example.chihwe_app_test.database.DatabaseHelper;
import com.example.chihwe_app_test.database.modal.News;
import com.example.chihwe_app_test.utils.RecyclerItemTouchHelper;

import java.util.ArrayList;
import java.util.List;

public class History_read_activity extends AppCompatActivity implements RecyclerItemTouchHelper.RecyclerItemTouchHelperListener {

    RecyclerView recyclerView;
    RecyclerView.LayoutManager vertical_LayoutManager;

    ImageButton backbutton;
    History_list_adapter adapter;
    DatabaseHelper db;
    private List<News> newsList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history_read_activity);

        backbutton = findViewById(R.id.backbutton);

        recyclerView = findViewById(R.id.recycleview);

        vertical_LayoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerView.setLayoutManager(vertical_LayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.VERTICAL));
        db = new DatabaseHelper(this);

        newsList.addAll(db.getAllNews());

        adapter = new History_list_adapter(this,newsList);
        recyclerView.setAdapter(adapter);

        ItemTouchHelper.SimpleCallback itemTouchHelperCallback = new RecyclerItemTouchHelper(0, ItemTouchHelper.LEFT, this);

        new ItemTouchHelper(itemTouchHelperCallback).attachToRecyclerView(recyclerView);

        backbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

    }

    @Override
    public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction, int position) {


        if (viewHolder instanceof History_list_adapter.MyViewHolder) {

            db.deleteNews(newsList.get(viewHolder.getAdapterPosition()));

            adapter.removeItem(viewHolder.getAdapterPosition());

        }
    }

}
