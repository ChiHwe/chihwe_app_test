package com.example.chihwe_app_test.view;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.annotation.NonNull;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.example.chihwe_app_test.R;
import com.example.chihwe_app_test.database.DatabaseHelper;
import com.example.chihwe_app_test.database.modal.News;
import com.example.chihwe_app_test.utils.Shared_function;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    Context currentcontext;
    SwipeRefreshLayout swipeRefreshLayout;
    RecyclerView recyclerView;
    ProgressBar progressBar;
    int currentpage = 1;
    RecyclerView.LayoutManager vertical_LayoutManager;
    Recyleview_adapter adapter;
    Boolean check_updated = false;
    ImageButton ib_history;
    private DatabaseHelper db;

    ArrayList<String> arrayList_title = new ArrayList<>();
    ArrayList<String> arrayList_content = new ArrayList<>();

    ArrayList<String> arrayList_published_date = new ArrayList<>();
    private List<News> newsList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        currentcontext = this;

        swipeRefreshLayout = findViewById(R.id.swipe_refresh_layout);
        recyclerView = findViewById(R.id.recycleview);
        progressBar = findViewById(R.id.progress_loading);
        ib_history = findViewById(R.id.ib_history);

        db= new DatabaseHelper(currentcontext);

        newsList.addAll(db.getAllNews());

        //Log.d("testnews",newsList.get(0)+"");

        vertical_LayoutManager = new LinearLayoutManager(currentcontext);
        recyclerView.setLayoutManager(vertical_LayoutManager);

        if(Shared_function.checkNetworkConnection(currentcontext).equals("No")) {

            Toast.makeText(currentcontext,R.string.internet_not_found,Toast.LENGTH_LONG).show();
            progressBar.setVisibility(View.GONE);
        }else {

            new Load_news_list_task().executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);

        }

        ib_history.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(currentcontext, History_read_activity.class);
                startActivity(intent);
            }
        });

        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);

                if (!recyclerView.canScrollVertically(1) && newState==RecyclerView.SCROLL_STATE_IDLE) {

                    if(Shared_function.checkNetworkConnection(currentcontext).equals("No")) {
                        Toast.makeText(currentcontext,R.string.internet_not_found,Toast.LENGTH_LONG).show();
                    }else {

                        currentpage = currentpage + 1;
                        new Load_news_list_task().executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
                    }

                }

            }

            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);

            }
        });


        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {

                if(Shared_function.checkNetworkConnection(currentcontext).equals("No")) {

                    Toast.makeText(currentcontext,R.string.internet_not_found,Toast.LENGTH_LONG).show();
                    swipeRefreshLayout.setRefreshing(false);
                }else {

                    arrayList_title.clear();
                    arrayList_content.clear();
                    arrayList_published_date.clear();

                    currentpage =1;
                    check_updated = true;
                    new Load_news_list_task().executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
                }
            }
        });

    }

    //region load_sub_category
    @SuppressLint("StaticFieldLeak")
    private class Load_news_list_task extends AsyncTask<String,Integer,String> {

        Boolean bln_detect_error = false;

        @Override
        protected String doInBackground(String[] params) {

            String toDisplay  ="";

            toDisplay = Load_news_list();

            try {
                JSONObject json = new JSONObject(toDisplay);

                if (json.getString("status").equals("ok")){

                    JSONArray jsonArray = json.getJSONArray("articles");

                    for (int i =0; i < jsonArray.length(); i ++){

                        JSONObject jsonObject_item = jsonArray.getJSONObject(i);

                        arrayList_title.add(jsonObject_item.getString("title"));

                        arrayList_content.add(jsonObject_item.getString("content"));

                        arrayList_published_date.add(jsonObject_item.getString("publishedAt"));

                    }

                }else{
                    bln_detect_error = true;

                }



            } catch (JSONException e) {
                e.printStackTrace();
            }


            return toDisplay;
        }

        @Override
        protected void onPreExecute(){

        }

        @Override
        protected void onPostExecute(String result){

            if (bln_detect_error){

                Toast.makeText(currentcontext,R.string.maximumresult_reached,Toast.LENGTH_LONG).show();


            }else{

                if (currentpage ==1){
                    adapter = new Recyleview_adapter(currentcontext, arrayList_title,arrayList_content,arrayList_published_date,db);
                    recyclerView.setAdapter(adapter);
                }else{

                    adapter.notifyDataSetChanged();

                }

                if (check_updated){
                    swipeRefreshLayout.setRefreshing(false);
                    Toast.makeText(currentcontext,R.string.updated_news,Toast.LENGTH_SHORT).show();
                    check_updated = false;
                }
            }
            progressBar.setVisibility(View.GONE);
            super.onPostExecute(result);
        }
    }

    private String Load_news_list() {

        String temp, response = "";
        InputStream inputStream;

        String url = "https://newsapi.org/v2/top-headlines?country=sg&category=business&apiKey=104d7bd77d0b46f2802fef857710e84f&page="+ currentpage;

        HttpURLConnection urlConnection = null;
        try {

            urlConnection = (HttpURLConnection) Shared_function.makeGETRequest("GET", url);

            if (urlConnection.getResponseCode() < HttpURLConnection.HTTP_BAD_REQUEST) {
                inputStream = urlConnection.getInputStream();
            } else {
                inputStream = urlConnection.getErrorStream();
            }
            // parse stream
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));

            while ((temp = bufferedReader.readLine()) != null) {
                response += temp;
            }

            return response;
        } catch (Exception e) {
            e.printStackTrace();
            return e.toString();
        } finally {
            if (urlConnection != null) {
                urlConnection.disconnect();
            }
        }
    }

    //endregion



}
