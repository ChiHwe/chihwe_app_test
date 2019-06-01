package com.example.chihwe_app_test.view;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
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

public class Recyleview_adapter extends RecyclerView.Adapter<Recyleview_adapter.ViewHolder> {

        private Context context;
        private ArrayList<String> arrayList_title ;
        private ArrayList<String> arrayList_content;
        private ArrayList<String> arrayList_published_date;


        private DatabaseHelper db;
        private LayoutInflater inflater;

        public Recyleview_adapter(Context context, ArrayList<String> arrayList_title, ArrayList<String> arrayList_content, ArrayList<String> arrayList_published_date, DatabaseHelper db) {


            this.arrayList_title = arrayList_title;
            this.arrayList_content = arrayList_content;
            this.arrayList_published_date = arrayList_published_date;
            this.db = db;
            this.context = context;

            inflater = (LayoutInflater)context.
                    getSystemService(Context.LAYOUT_INFLATER_SERVICE);


        }
        @Override
        public int getItemCount() {
            return arrayList_title.size() ;
        }


        public Object getItem(int position) {

            return arrayList_title.get(position);
        }

        @Override
        public long getItemId(int position) {

            return (long) arrayList_title.get(position).hashCode();

        }

        @Override
        public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {

            final View view=inflater.inflate(R.layout.custom_recycle_item,viewGroup,false);

            return  new ViewHolder(view);
        }

        @Override
        public void onBindViewHolder(ViewHolder holder, final int position) {


            holder.tv_title.setText(arrayList_title.get(position));


            holder.setClickListener(new ItemClickListener() {
                @Override
                public void onClick(View view, int position, boolean isLongClick) {
                    if (!isLongClick) {

                        int count_new = db.getNewscount();

                        if (count_new > 0) {

                            String news = db.getnews(arrayList_title.get(position));

                            if (news == null) {
                                db.insertNews(arrayList_title.get(position));
                            }
                        }else{
                            db.insertNews(arrayList_title.get(position));
                        }

                        Intent intent = new Intent(context, News_details.class);
                        intent.putExtra("news_title",arrayList_title.get(position));
                        intent.putExtra("news_date",arrayList_published_date.get(position));
                        intent.putExtra("news_content",arrayList_content.get(position));
                        context.startActivity(intent);

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

