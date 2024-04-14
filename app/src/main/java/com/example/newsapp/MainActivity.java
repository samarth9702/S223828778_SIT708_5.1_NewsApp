package com.example.newsapp;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.SearchView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import com.google.android.material.progressindicator.LinearProgressIndicator;
import com.kwabenaberko.newsapilib.NewsApiClient;
import com.kwabenaberko.newsapilib.models.Article;
import com.kwabenaberko.newsapilib.models.request.TopHeadlinesRequest;
import com.kwabenaberko.newsapilib.models.response.ArticleResponse;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
//APIKey = "2a1d5ad38db3426fa7eabac3f4bf4327"

    Button btn1, btn2, btn3, btn4, btn5, btn6, btn7;
    RecyclerView recyclerView, recyclerView2;
    List<Article> articleList = new ArrayList<>();
    NewsRecyclerAdapter newsRecyclerAdapter;
    NewsAdapter newsAdapter;
    LinearProgressIndicator progressIndicator;
    SearchView searchView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);

        recyclerView = findViewById(R.id.recyclerView);
        recyclerView2 = findViewById(R.id.recyclerView2);
        progressIndicator = findViewById(R.id.progressBar);

        btn1 = findViewById(R.id.button1);
        btn2 = findViewById(R.id.button2);
        btn3 = findViewById(R.id.button3);
        btn4 = findViewById(R.id.button4);
        btn5 = findViewById(R.id.button5);
        btn6 = findViewById(R.id.button6);
        btn7 = findViewById(R.id.button7);

        searchView = findViewById(R.id.searchView);

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String s) {
                getNews("GENERAL", s);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String s) {
                return false;
            }
        });

        btn1.setOnClickListener(this);
        btn2.setOnClickListener(this);
        btn3.setOnClickListener(this);
        btn4.setOnClickListener(this);
        btn5.setOnClickListener(this);
        btn6.setOnClickListener(this);
        btn7.setOnClickListener(this);

        setUpRecyclerView2();
        setUpRecyclerView();
        getNews("GENERAL", null);
        getTopNews();
    }

    void setUpRecyclerView(){

//        This below code is for 2 columns news articles, for which I need to change size of news_recycler_row.xml

        recyclerView.setLayoutManager(new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL));
        newsRecyclerAdapter = new NewsRecyclerAdapter(articleList);
        recyclerView.setAdapter(newsRecyclerAdapter);
    }

    void setUpRecyclerView2(){
        recyclerView2.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
        newsAdapter = new NewsAdapter(articleList);
        recyclerView2.setAdapter(newsAdapter);
    }

    void changeInProgress(boolean show){
        if(show){
            progressIndicator.setVisibility(View.VISIBLE);
        } else{
            progressIndicator.setVisibility(View.INVISIBLE);
        }
    }

    public void getTopNews(){
        changeInProgress(true);
        NewsApiClient newsApiClient = new NewsApiClient("2a1d5ad38db3426fa7eabac3f4bf4327");
        newsApiClient.getTopHeadlines(
                new TopHeadlinesRequest.Builder()
                        .language("en")
                        .country("au")
                        .build(),
                new NewsApiClient.ArticlesResponseCallback() {
                    @Override
                    public void onSuccess(ArticleResponse response) {
                        runOnUiThread(() -> {
                            changeInProgress(false);
                            articleList = response.getArticles();
                            newsAdapter.updateData(articleList);
                            newsAdapter.notifyDataSetChanged();
                        });
                    }

                    @Override
                    public void onFailure(Throwable throwable) {
                        Log.i("Got Response", throwable.getMessage());
                    }
                }

        );
    }

    public void getNews(String category, String querry){
        changeInProgress(true);
        NewsApiClient newsApiClient = new NewsApiClient("2a1d5ad38db3426fa7eabac3f4bf4327");
        newsApiClient.getTopHeadlines(
                new TopHeadlinesRequest.Builder()
                        .language("en")
                        .category(category)
                        .q(querry)
                        .build(),
                new NewsApiClient.ArticlesResponseCallback() {
                    @Override
                    public void onSuccess(ArticleResponse response) {
                        runOnUiThread(() -> {
                            changeInProgress(false);
                            articleList = response.getArticles();
                            newsRecyclerAdapter.updateData(articleList);
//                            newsAdapter.updateData(articleList);
                            newsRecyclerAdapter.notifyDataSetChanged();
//                            newsAdapter.notifyDataSetChanged();
                        });
                    }

                    @Override
                    public void onFailure(Throwable throwable) {
                        Log.i("Got Response", throwable.getMessage());
                    }
                }

        );
    }

    @Override
    public void onClick(View view) {
        Button btn = (Button) view;
        String category = btn.getText().toString();
        getNews(category, null);
    }
}