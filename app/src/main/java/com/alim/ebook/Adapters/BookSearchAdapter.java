package com.alim.ebook.Adapters;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.alim.ebook.Activities.BookDetailActivity;
import com.alim.ebook.Fragments.SearchFragment;
import com.alim.ebook.Models.Book;
import com.alim.ebook.R;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;

public class BookSearchAdapter extends RecyclerView.Adapter<BookSearchAdapter.ViewHolder> implements Filterable {

    private final Context mContext;
    private final ArrayList<Book> list;
    private final SearchFragment searchFragment;

    public BookSearchAdapter(Context mContext, ArrayList<Book> list, SearchFragment searchFragment) {
        this.mContext = mContext;
        this.list = list;
        this.searchFragment = searchFragment;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.search_row_item, parent, false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, int i) {
        final Book book = list.get(i);
        holder.progressBar.setVisibility(View.VISIBLE);
        Glide.with(mContext).load(book.getBookThumbnail()).transition(DrawableTransitionOptions.withCrossFade()).listener(new RequestListener<Drawable>() {
            @Override
            public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
                holder.thumbnail.setImageResource(R.drawable.ic_photo);
                return false;
            }

            @Override
            public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
                holder.progressBar.setVisibility(View.GONE);
                return false;
            }
        })
                .into(holder.thumbnail);
        holder.bookTitle.setText(book.getBookTitle());
        holder.bookAuthor.setText(book.getBookAuthor());
        setOnItemClick(holder.row_parent, i);
        setOnCardClick(holder.cardView, i);

    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    @Override
    public Filter getFilter() {
        return filter;
    }

    private final Filter filter = new Filter() {
        @Override
        protected FilterResults performFiltering(CharSequence charSequence) {
            ArrayList<Book> matchedBooks = new ArrayList<>();
            CollectionReference ref = FirebaseFirestore.getInstance()
                    .collection("booksList");
            try {
                ref.whereArrayContains("tags", charSequence.toString().trim().toLowerCase())
                        .limit(6).get()
                        .addOnSuccessListener((value)->{
                            for (DocumentSnapshot snapshot : value.getDocuments()){
                                matchedBooks.add(snapshot.toObject(Book.class));
                            }
                            publish(matchedBooks, charSequence);
                        })
                        .addOnFailureListener(Throwable::printStackTrace);
            } catch (Exception e) {
                e.printStackTrace();
            }
            FilterResults results = new FilterResults();
            results.values = matchedBooks;
            return results;
        }

        @Override
        protected void publishResults(CharSequence charSequence, FilterResults filterResults) {

        }

        private void publish(ArrayList<Book> result, CharSequence charSequence){
            list.clear();
            list.addAll(result);
            if (list.size() == 0 && charSequence.length() != 0) {
                searchFragment.recyclerView.setVisibility(View.GONE);
                searchFragment.failedLayout.setVisibility(View.VISIBLE);
                searchFragment.progSearching.setVisibility(View.GONE);
            } else if (charSequence.length() == 0) {
                searchFragment.recyclerView.setVisibility(View.GONE);
                searchFragment.failedLayout.setVisibility(View.GONE);
                searchFragment.progSearching.setVisibility(View.GONE);

            } else {
                searchFragment.failedLayout.setVisibility(View.GONE);
                searchFragment.recyclerView.setVisibility(View.VISIBLE);
                searchFragment.progSearching.setVisibility(View.GONE);

            }
            notifyDataSetChanged();
        }
    };

    static class ViewHolder extends RecyclerView.ViewHolder {
        ImageView thumbnail;
        TextView bookTitle, bookAuthor;
        RelativeLayout row_parent;
        CardView cardView;
        ProgressBar progressBar;

        ViewHolder(@NonNull View itemView) {
            super(itemView);
            thumbnail = itemView.findViewById(R.id.search_bookThumbnail);
            bookTitle = itemView.findViewById(R.id.search_bookTitle);
            bookAuthor = itemView.findViewById(R.id.search_bookAuthor);
            row_parent = itemView.findViewById(R.id.search_parent_row_item);
            progressBar = itemView.findViewById(R.id.search_progress);
            cardView = itemView.findViewById(R.id.search_card);

        }

    }

    private void setOnItemClick(RelativeLayout layout, final int position) {
        layout.setOnClickListener(view -> {openDetail(position); });
    }
    private void setOnCardClick(CardView layout, final int position) {
        layout.setOnClickListener(view -> {openDetail(position); });
    }
    private void openDetail(int position) {
        Intent intent = new Intent(mContext, BookDetailActivity.class);
        intent.putExtra("bookId", list.get(position).getKey());
        mContext.startActivity(intent);
    }

}
