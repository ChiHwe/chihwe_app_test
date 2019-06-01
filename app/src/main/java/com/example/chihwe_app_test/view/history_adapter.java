package com.example.chihwe_app_test.view;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.chihwe_app_test.R;
import com.example.chihwe_app_test.database.DatabaseHelper;
import com.example.chihwe_app_test.database.modal.News;
import com.example.chihwe_app_test.utils.ItemClickListener;

import java.util.ArrayList;
import java.util.List;

public class history_adapter extends RecyclerView.Adapter<history_adapter.ViewHolder> {

        private Context context;
        List<News> newsList = new ArrayList<>();
        private ArrayList<String> arrayList_content;
        private ArrayList<String> arrayList_published_date;


        private DatabaseHelper db;
        private LayoutInflater inflater;

        public history_adapter(Context context, List<News> newsList) {


            this.newsList = newsList;
            this.context = context;

            inflater = (LayoutInflater)context.
                    getSystemService(Context.LAYOUT_INFLATER_SERVICE);


        }
        @Override
        public int getItemCount() {
            return newsList.size() ;
        }


        public Object getItem(int position) {

            return newsList.get(position);
        }

        @Override
        public long getItemId(int position) {

            return (long) newsList.get(position).hashCode();

        }

        @Override
        public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {

            final View view=inflater.inflate(R.layout.custom_recycle_item,viewGroup,false);

            return  new ViewHolder(view);
        }

        @Override
        public void onBindViewHolder(ViewHolder holder, final int position) {

            News news = newsList.get(position);

            holder.tv_title.setText(news.getNews());


            holder.setClickListener(new ItemClickListener() {
                @Override
                public void onClick(View view, int position, boolean isLongClick) {
                    if (!isLongClick) {

                    }
                }
            });

        }


        public static class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener, View.OnLongClickListener {

            TextView tv_title;

            private ItemClickListener clickListener;

            public ViewHolder(View itemView) {
                super(itemView);

                tv_title = itemView.findViewById(R.id.tv_title);

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




    }

