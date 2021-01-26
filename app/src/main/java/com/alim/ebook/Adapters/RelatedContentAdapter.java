package com.alim.ebook.Adapters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.alim.ebook.Categories.RelatedBooks;
import com.alim.ebook.Models.Book;
import com.alim.ebook.Models.Group;
import com.alim.ebook.R;

import java.util.ArrayList;

public class RelatedContentAdapter extends RecyclerView.Adapter<RelatedContentAdapter.ContentViewHolder> {

    private final Context mContext;
    private final ArrayList<Group> title;
    private final ArrayList<Book> relatedBooks;

    public RelatedContentAdapter(Context mContext, ArrayList<Group> title, ArrayList<Book> relatedBooks) {
        this.mContext = mContext;
        this.title = title;
        this.relatedBooks = relatedBooks;
    }

    @NonNull
    @Override
    public ContentViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.group_item, parent,false);
        return new ContentViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ContentViewHolder holder, int position) {

        setGroupTitle(holder.groupTitle, title.get(position).getGroupTitle());
        setGroupButtonTitle(holder.btnViewAll, title.get(position).getGroupButtonTitle());
        setOnClickViewAll(holder.group_parent, position);
        setList(holder.group_recyclerView);
    }

    @Override
    public int getItemCount() {
        return title.size();
    }
    static class ContentViewHolder extends RecyclerView.ViewHolder {
        RelativeLayout group_parent;
        TextView groupTitle;
        TextView btnViewAll;
        RecyclerView group_recyclerView;

        ContentViewHolder(@NonNull View itemView) {
            super(itemView);
            group_parent = itemView.findViewById(R.id.group_parent);
            groupTitle = itemView.findViewById(R.id.group_title);
            btnViewAll = itemView.findViewById(R.id.btn_group_all);
            group_recyclerView = itemView.findViewById(R.id.group_recyclerView);
        }
    }

    private void setGroupTitle(TextView textView, String groupTitle){
        textView.setText(groupTitle);
    }
    private void setGroupButtonTitle(TextView btnViewAll, String btnText){
        btnViewAll.setText(btnText);
    }
    private void setOnClickViewAll(RelativeLayout layout, final int position){
        layout.setOnClickListener(view -> {
            try {
                Intent intent = new Intent(mContext, RelatedBooks.class);
                intent.putExtra("category", relatedBooks.get(position).getCategoryChild());
                intent.putExtra("bookId", relatedBooks.get(position).getKey());
                mContext.startActivity(intent);
            } catch (Exception e) {
                e.printStackTrace();
            }
        });

    }
    private void setList(RecyclerView recyclerView){
        BookAdapter adapter = new BookAdapter(mContext, relatedBooks);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(mContext, LinearLayoutManager.HORIZONTAL, false));
        recyclerView.setAdapter(adapter);
        recyclerView.setNestedScrollingEnabled(true);

    }
}
