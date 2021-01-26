package com.alim.ebook.Adapters;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RatingBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.alim.ebook.Activities.BookDetailActivity;
import com.alim.ebook.Models.Book;
import com.alim.ebook.R;
import com.bumptech.glide.GenericTransitionOptions;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import java.util.ArrayList;

public class RelatedBookAdapter extends RecyclerView.Adapter<RelatedBookAdapter.mViewHolder> {

    private final Context mContext;
    private final ArrayList<Book> postsList;
    public RelatedBookAdapter(Context mContext, ArrayList<Book> postsList) {
        this.mContext = mContext;
        this.postsList = postsList;
    }

    @NonNull
    @Override
    public mViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.row_related_books, parent, false);
        return new mViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final mViewHolder holder, int position) {
        Book post = postsList.get(position);
        setTitle(holder.title, post.getBookTitle());
        setAuthor(holder.author, post.getBookAuthor());
        holder.ratingBar.setRating(post.getRating());
        holder.progressBar.setVisibility(View.VISIBLE);
        Glide.with(mContext).load(post.getBookThumbnail()).listener(new RequestListener<Drawable>() {
            @Override
            public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
                holder.thumbnail.setImageResource(R.drawable.ic_photo);
                return false;
            }

            @Override
            public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
                //set progressbar visibility gone
                holder.progressBar.setVisibility(View.GONE);
                return false;
            }
        })
                .transition(GenericTransitionOptions.with(R.anim.fade_in))
                .into(holder.thumbnail);

        setOnItemClick(holder.row_parent, position);
        setOnCardClick(holder.cardView, position);
        setAverageRating(post.getKey(), holder.ratingText, holder.ratingBar);
    }

    @Override
    public int getItemCount() {
        return postsList.size();
    }

    static class mViewHolder extends RecyclerView.ViewHolder {
        RelativeLayout row_parent;
        CardView cardView;
        ImageView thumbnail;
        TextView title, author;
        ProgressBar progressBar;
        RatingBar ratingBar;
        TextView ratingText;

        mViewHolder(@NonNull View itemView) {
            super(itemView);

            thumbnail = itemView.findViewById(R.id.related_book_thumbnail);
            title = itemView.findViewById(R.id.related_book_Title);
            author = itemView.findViewById(R.id.related_book_author);
            row_parent = itemView.findViewById(R.id.related_parent_row_item);
            progressBar = itemView.findViewById(R.id.related_progressBar);
            cardView = itemView.findViewById(R.id.related_book_card);
            ratingBar = itemView.findViewById(R.id.related_book_ratingbar);
            ratingText = itemView.findViewById(R.id.related_book_votes);
        }
    }

    private void setTitle(TextView textView, String title) {
        textView.setText(title);
    }
    private void setAuthor(TextView textView, String title) {
        textView.setText(title);
    }
    private void setOnItemClick(RelativeLayout layout, final int position) {
        layout.setOnClickListener(view -> {openDetail(position); });
    }
    private void setOnCardClick(CardView layout, final int position) {
        layout.setOnClickListener(view -> {openDetail(position);});
    }

    private void setAverageRating(String bookId, final TextView numOfVotes, final RatingBar rating) {
        FirebaseFirestore.getInstance()
                .collection("comments")
                .document(bookId)
                .collection("comments")
                .get()
                .addOnSuccessListener(queryDocumentSnapshots -> {
                   long numberOfRates = queryDocumentSnapshots.size();
                    float sumOfAll;
                    long  total = 0;
                    try {
                        numOfVotes.setText((String.valueOf(numberOfRates)) + " votes");
                        for (DocumentSnapshot snapshot : queryDocumentSnapshots.getDocuments()) {
                            long rateVal = snapshot.getLong("rating");
                            total += rateVal;
                        }
                        sumOfAll = (total / numberOfRates);
                        rating.setRating(sumOfAll);
                    } catch (Exception e){
                        e.printStackTrace();
                    }
                });
    }


    private void openDetail(int position) {
        Intent intent = new Intent(mContext, BookDetailActivity.class);
        intent.putExtra("bookId", postsList.get(position).getKey());
        mContext.startActivity(intent);
    }
}
