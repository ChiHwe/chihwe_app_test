package com.example.chihwe_app_test.view;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.webkit.WebView;
import android.widget.ImageButton;
import android.widget.TextView;

import com.example.chihwe_app_test.R;

import org.w3c.dom.Text;

import java.util.Date;

public class News_details extends AppCompatActivity {


    TextView tv_news_title;
    WebView webView_content;
    ImageButton backbutton;
    String mime = "text/html";
    String encoding = "utf-8";


    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_details);

        backbutton = findViewById(R.id.backbutton);
        tv_news_title = findViewById(R.id.tv_news_title);
        webView_content = findViewById(R.id.webview);

        tv_news_title.setText(getIntent().getStringExtra("news_title"));

        String s="<head><meta name='viewport' content='target-densityDpi=device-dpi'/></head>";

        webView_content.loadDataWithBaseURL(null, s+"<style>img{display: inline;height: auto;max-width: 100%;}</style>" + getIntent().getStringExtra("news_content"), mime, encoding, null);

        backbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });


    }
}
