package com.example.chihwe_app_test;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.AsyncTask;
import android.support.annotation.NonNull;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    Context currentcontext;
    SwipeRefreshLayout swipeRefreshLayout;
    RecyclerView recyclerView;
    int currentpage;
    RecyclerView.LayoutManager vertical_LayoutManager;
    Recyleview_adapter adapter;
    Boolean check_updated = false;

    ArrayList<String> arrayList_title = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        currentcontext = this;

        swipeRefreshLayout = findViewById(R.id.swipe_refresh_layout);
        recyclerView = findViewById(R.id.recycleview);

        currentpage = 1;

        vertical_LayoutManager = new LinearLayoutManager(currentcontext);
        recyclerView.setLayoutManager(vertical_LayoutManager);


        new Load_news_list_task().executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);

        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);

                if (!recyclerView.canScrollVertically(1) && newState==RecyclerView.SCROLL_STATE_IDLE) {
                    currentpage = currentpage +1;

                    new Load_news_list_task().executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);


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

                arrayList_title.clear();

                if(Shared_function.checkNetworkConnection(currentcontext).equals("No")) {

                    Toast.makeText(currentcontext,"Not Internet Connection",Toast.LENGTH_LONG).show();

                }else {
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

                    }

                }else{

                    Toast.makeText(currentcontext,"Some problem to retrieve news,please try again later.",Toast.LENGTH_LONG).show();

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


            if (currentpage ==1){
                adapter = new Recyleview_adapter(currentcontext, arrayList_title);
                recyclerView.setAdapter(adapter);
            }else{
                adapter.notifyDataSetChanged();
                //bln_check_finish_load = true;
            }


            if (check_updated){
                swipeRefreshLayout.setRefreshing(false);
                Toast.makeText(currentcontext,"Updated News",Toast.LENGTH_SHORT).show();
                check_updated = false;
            }

            super.onPostExecute(result);
        }
    }

    private String Load_news_list() {

        String temp, response = "";
        InputStream inputStream;
        String url ="";

        url = "https://newsapi.org/v2/top-headlines?country=sg&category=business&apiKey=104d7bd77d0b46f2802fef857710e84f&page="+ currentpage;

        //String requestBody;
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

            final int chunkSize = 2048;
            for (int i = 0; i <response.length(); i += chunkSize) {
                Log.d("response_1", response.substring(i, Math.min(response.length(), i + chunkSize)));

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
