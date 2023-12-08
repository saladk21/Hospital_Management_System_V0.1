package com.example.hospital_management_system_v01;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class CustomAdapter extends RecyclerView.Adapter<CustomAdapter.MyViewHolder> {

    private Context context;
    private ArrayList book_id, book_title, book_author, book_pages;

    CustomAdapter(Context constext, ArrayList book_id, ArrayList book_title, ArrayList book_author, ArrayList book_pages) {
        this.book_id = book_id;
        this.book_title = book_title;
        this.book_author = book_author;
        this.book_pages = book_pages;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.my_row, parent, false);
                return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {

    }

    @Override
    public int getItemCount() {
        return 0;
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

        TextView book_id_txt, book_title_txt, book_author_txt, book_pages_txt;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            book_id_txt=itemView.findViewById(R.id.Column_id_txt);
            book_title_txt=itemView.findViewById(R.id.Column_id_txt);
            book_id_txt=itemView.findViewById(R.id.Column_id_txt);
            book_id_txt=itemView.findViewById(R.id.Column_id_txt);
        }
    }
}