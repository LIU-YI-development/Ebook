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

import com.alim.ebook.Categories.AdsActivity;
import com.alim.ebook.Categories.CategoryActivity;
import com.alim.ebook.Categories.Latest;
import com.alim.ebook.Models.Ads;
import com.alim.ebook.Models.Book;
import com.alim.ebook.Models.Group;
import com.alim.ebook.R;

import java.util.ArrayList;

public class GroupAdapter extends RecyclerView.Adapter<GroupAdapter.groupViewHolder> {
    private final Context mContext;
    private final ArrayList<Group> groups;
    private final ArrayList<Ads> ads;
    private final ArrayList<Book> latest;
    private final ArrayList<ArrayList<Book>> bookCategories;

    public GroupAdapter(Context mContext, ArrayList<Group> groups, ArrayList<Ads> ads, ArrayList<Book> latest, ArrayList<ArrayList<Book>> bookCategories) {
        this.mContext = mContext;
        this.groups = groups;
        this.ads = ads;
        this.latest = latest;
        this.bookCategories = bookCategories;
    }

    @NonNull
    @Override
    public groupViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.group_item, parent, false);
        return new groupViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull groupViewHolder holder, int position) {
        Group group = groups.get(position);
        setGroupTitle(holder.groupTitle, group.getGroupTitle());
        setGroupButtonTitle(holder.btnViewAll, group.getGroupButtonTitle());
        setOnClickViewAll(holder.group_parent, group.getGroupTitle());
        setLists(holder.group_recyclerView, position);

    }


    @Override
    public int getItemCount() {
        return groups.size();
    }


    static class groupViewHolder extends RecyclerView.ViewHolder {
        RelativeLayout group_parent;
        TextView groupTitle;
        TextView btnViewAll;
        RecyclerView group_recyclerView;

        groupViewHolder(@NonNull View itemView) {
            super(itemView);
            group_parent = itemView.findViewById(R.id.group_parent);
            groupTitle = itemView.findViewById(R.id.group_title);
            btnViewAll = itemView.findViewById(R.id.btn_group_all);
            group_recyclerView = itemView.findViewById(R.id.group_recyclerView);

        }
    }

    private void setGroupTitle(TextView textView, String groupTitle) {
        textView.setText(groupTitle);
    }

    private void setGroupButtonTitle(TextView btnViewAll, String btnText) {
        btnViewAll.setText(btnText);
    }

    private void setOnClickViewAll(RelativeLayout layout, final String groupTitle) {
        layout.setOnClickListener(view -> {
            if (groupTitle.equals("Recommended")) {
                openActivity(AdsActivity.class);
            } else if (groupTitle.equals("Latest")) {
                openActivity(Latest.class);
            } else {
                Intent i = new Intent(mContext, CategoryActivity.class);
                i.putExtra("title", groupTitle);
                mContext.startActivity(i);
            }
        });
    }

    private void setLists(RecyclerView recyclerView, int position) {
        if (position == 0) {
            setAdList(recyclerView, ads);
        } else if (position == 1) {
            setList(recyclerView, latest);
        } else {
            setList(recyclerView, bookCategories.get(position - 2));
        }
    }

    private void setList(RecyclerView recyclerView, ArrayList<Book> list) {
        HomeBookAdapter adapter = new HomeBookAdapter(mContext, list);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(mContext, LinearLayoutManager.HORIZONTAL, false));
        recyclerView.setAdapter(adapter);
        recyclerView.setNestedScrollingEnabled(true);

    }

    private void openActivity(Class view) {
        Intent intent = new Intent(mContext, view);
        mContext.startActivity(intent);

    }

    private void setAdList(RecyclerView recyclerView, ArrayList<Ads> list) {
        AdsAdapter adapter = new AdsAdapter(mContext, list);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(mContext, LinearLayoutManager.HORIZONTAL, false));
        recyclerView.setAdapter(adapter);
        recyclerView.setNestedScrollingEnabled(true);

    }
}