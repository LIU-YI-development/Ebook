package com.alim.ebook.Adapters;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.net.ConnectivityManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
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

import java.util.ArrayList;

public class BookAdapter extends RecyclerView.Adapter<BookAdapter.mViewHolder> {

    private static final String TAG = "BookAdapter";
    private final Context mContext;
    private final ArrayList<Book> booksList;

    public BookAdapter(Context mContext, ArrayList<Book> booksList) {
        this.mContext = mContext;
        this.booksList = booksList;
    }

    @NonNull
    @Override
    public mViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(mContext).inflate(R.layout.row_item, parent, false);
        return new mViewHolder(view);

    }


    @Override
    public void onBindViewHolder(@NonNull final mViewHolder holder, int position) {
        Book book = booksList.get(position);

        setTitle(holder.title, book.getBookTitle());
        setAuthor(holder.author, book.getBookAuthor());
        holder.progressBar.setVisibility(View.VISIBLE);
        Glide.with(mContext).load(book.getBookThumbnail()).listener(new RequestListener<Drawable>() {
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
    }

    @Override
    public int getItemCount() {
        return booksList.size();
    }

    static class mViewHolder extends RecyclerView.ViewHolder {
        RelativeLayout row_parent;
        CardView cardView;
        ImageView thumbnail;
        TextView title, author;
        ProgressBar progressBar;

        mViewHolder(@NonNull View itemView) {
            super(itemView);

            thumbnail = itemView.findViewById(R.id.bookThumbnail);
            title = itemView.findViewById(R.id.bookTitle);
            author = itemView.findViewById(R.id.bookAuthor);
            row_parent = itemView.findViewById(R.id.parent_row_item);
            progressBar = itemView.findViewById(R.id.progressBar);
            cardView = itemView.findViewById(R.id.card);
        }
    }


    private void setTitle(TextView textView, String title) {
        textView.setText(title);
    }

    private void setAuthor(TextView textView, String title) {
        textView.setText(title);
    }

    private void setOnItemClick(RelativeLayout layout, final int position) {
        layout.setOnClickListener(view -> {
            openDetail(position);
        });
    }

    private void setOnCardClick(CardView layout, final int position) {
        layout.setOnClickListener(view -> {
            openDetail(position);
        });
    }

    private void openDetail(int position) {
        Intent intent = new Intent(mContext, BookDetailActivity.class);
        intent.putExtra("bookId", booksList.get(position).getKey());
        Log.d(TAG, "openDetail: "+booksList.get(position).getKey());

        mContext.startActivity(intent);
    }
}
