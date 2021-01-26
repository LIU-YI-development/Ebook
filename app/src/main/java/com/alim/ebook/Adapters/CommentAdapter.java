package com.alim.ebook.Adapters;

import android.content.Context;
import android.text.format.DateFormat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RatingBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.alim.ebook.Models.Comment;
import com.alim.ebook.R;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Locale;

public class CommentAdapter extends RecyclerView.Adapter<CommentAdapter.ViewHolder> {

    private final Context mContext;
    private final ArrayList<Comment> ratesList;
    public CommentAdapter(Context mContext, ArrayList<Comment> ratesList) {
        this.mContext = mContext;
        this.ratesList = ratesList;
    }
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.comments_row_item, parent, false);
        return new ViewHolder(view);
    }
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Comment comment = ratesList.get(position);
        holder.userName.setText(comment.getUserName());
        holder.comment.setText(comment.getComment());
        holder.ratingBar.setRating(comment.getRating());
        holder.date.setText((timestampToString((long) comment.getTimeStamp())));

    }
    @Override
    public int getItemCount() {
        return ratesList.size();
    }
    static class ViewHolder extends RecyclerView.ViewHolder {
        TextView userName;
        TextView date;
        TextView comment;
        RatingBar ratingBar;

        ViewHolder(@NonNull View itemView) {
            super(itemView);
            userName = itemView.findViewById(R.id.userName);
            date = itemView.findViewById(R.id.tv_comment_date);
            comment = itemView.findViewById(R.id.tv_user_comment);
            ratingBar = itemView.findViewById(R.id.comment_ratingbar);
        }
    }
    private String timestampToString(long time) {
        Calendar calendar = Calendar.getInstance(Locale.ENGLISH);
        calendar.setTimeInMillis(time);
        return DateFormat.format("dd-MM-yyyy", calendar).toString();
    }


}
