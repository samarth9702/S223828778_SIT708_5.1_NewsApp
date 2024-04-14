package com.example.newsapp;

import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.kwabenaberko.newsapilib.models.Article;
import com.squareup.picasso.Picasso;

import java.util.List;

public class NewsAdapter extends RecyclerView.Adapter<NewsAdapter.NewsViewHolder2> {

    List<Article> atricleList;

    NewsAdapter(List<Article> articleList){
        this.atricleList = articleList;
    }

    @NonNull
    @Override
    public NewsAdapter.NewsViewHolder2 onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.news_horizontal, parent, false);
        return new NewsAdapter.NewsViewHolder2(view);
    }

    @Override
    public void onBindViewHolder(@NonNull NewsAdapter.NewsViewHolder2 holder, int position) {
        Article article = atricleList.get(position);
        holder.titleTextView.setText(article.getTitle());
        holder.sourceView.setText(article.getSource().getName());
        Picasso.get().load(article.getUrlToImage())
                .error(R.drawable.no_image_icon)
                .placeholder(R.drawable.no_image_icon)
                .into(holder.imageView);

        holder.itemView.setOnClickListener((v) -> {
            Intent intent = new Intent(v.getContext(), NewsFullActivity.class);
            intent.putExtra("url", article.getUrl());
            v.getContext().startActivity(intent);
        });
    }

    void updateData(List<Article> data){
        atricleList.clear();
        atricleList.addAll(data);
    }

    @Override
    public int getItemCount() {
        return atricleList.size();
    }

    class NewsViewHolder2 extends RecyclerView.ViewHolder{

        TextView titleTextView, sourceView;
        ImageView imageView;

        public NewsViewHolder2(@NonNull View itemView) {
            super(itemView);
            titleTextView = itemView.findViewById(R.id.article_title2);
            sourceView = itemView.findViewById(R.id.article_source2);
            imageView = itemView.findViewById(R.id.article_image_view2);
        }
    }
}
