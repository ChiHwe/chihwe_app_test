package com.example.chihwe_app_test.view;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.chihwe_app_test.R;
import com.example.chihwe_app_test.database.modal.News;
import com.example.chihwe_app_test.utils.ItemClickListener;

import java.util.ArrayList;
import java.util.List;

public class History_list_adapter extends RecyclerView.Adapter<History_list_adapter.MyViewHolder> {
    private Context context;
    List<News> newsList = new ArrayList<>();

    public class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener, View.OnLongClickListener  {
        public TextView title;
        public RelativeLayout viewBackground, viewForeground;
        private ItemClickListener clickListener;

        public MyViewHolder(View view) {
            super(view);
            title = view.findViewById(R.id.tv_title);
            viewBackground = view.findViewById(R.id.view_background);
            viewForeground = view.findViewById(R.id.view_foreground);

            itemView.setOnClickListener(this);
            itemView.setOnLongClickListener(this);
        }

        public void setClickListener(ItemClickListener itemClickListener) {
            this.clickListener = itemClickListener;
        }

        @Override
        public void onClick(View view) {
            clickListener.onClick(view, getAdapterPosition(), false);
        }

        @Override
        public boolean onLongClick(View view) {
            clickListener.onClick(view, getAdapterPosition(), true);
            return true;
        }
    }


    public History_list_adapter(Context context, List<News> newsList) {
        this.context = context;
        this.newsList = newsList;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.custom_history_item, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, final int position) {
        News news = newsList.get(position);
        holder.title.setText(news.getNews());

        holder.setClickListener(new ItemClickListener() {
            @Override
            public void onClick(View view, int position, boolean isLongClick) {
                News news = newsList.get(position);
                if (!isLongClick) {

                    Intent intent = new Intent(context, News_details_activity.class);
                    intent.putExtra("news_title",news.getNews());
                    intent.putExtra("news_content",news.getNews_content());
                    context.startActivity(intent);

                }
            }
        });

    }

    @Override
    public int getItemCount() {
        return newsList.size();
    }

    public void removeItem(int position) {
        newsList.remove(position);
        // notify the item removed by position
        // to perform recycler view delete animations
        // NOTE: don't call notifyDataSetChanged()
        notifyItemRemoved(position);
    }

}
