package com.example.emergencyapp.model;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.emergencyapp.R;

import java.util.ArrayList;

public class CommentAdapter extends RecyclerView.Adapter<CommentAdapter.MyViewHolder> {
    private ArrayList<com.example.emergencyapp.model.Comment> mList;

    public void setmList(ArrayList<com.example.emergencyapp.model.Comment> mList) {
        this.mList = mList;
    }
    private OnItemClickListener mListener;

    public interface OnItemClickListener {
        void onItemClick(int position);
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        mListener = listener;
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView name;
        public TextView date;
        public TextView comment;

        public MyViewHolder(@NonNull View itemView, final OnItemClickListener
                listener) {
            super(itemView);
            name = itemView.findViewById(R.id.comment_user);
            date = itemView.findViewById(R.id.comment_date);
            comment = itemView.findViewById(R.id.comment_comment);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (listener != null) {
                        int position = getAdapterPosition();
                        if (position != RecyclerView.NO_POSITION) {
                            listener.onItemClick(position);
                        }
                    }
                }
            });
        }
    }

    public CommentAdapter(ArrayList<com.example.emergencyapp.model.Comment> aList) {
        mList = aList;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.comment_view, viewGroup, false);
        MyViewHolder evh = new MyViewHolder(v, mListener);
        return evh;
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder viewHolder, int i) {
        com.example.emergencyapp.model.Comment temp = mList.get(i);
        viewHolder.name.setText(temp.getUsername());
        viewHolder.date.setText(temp.getTime());
        viewHolder.comment.setText(temp.getContent());
    }

    @Override
    public int getItemCount() {
        if (mList == null)
            return 0;
        else
            return mList.size();
    }
}
