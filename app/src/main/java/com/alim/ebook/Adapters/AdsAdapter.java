package com.alim.ebook.Adapters;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.alim.ebook.Models.Ads;
import com.alim.ebook.R;
import com.bumptech.glide.GenericTransitionOptions;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;

public class AdsAdapter extends RecyclerView.Adapter<AdsAdapter.mViewHolder> {

    private final Context mContext;
    private final ArrayList<Ads> adsList;
    Ads ad;
    public AdsAdapter(Context mContext, ArrayList<Ads> adsList) {
        this.mContext = mContext;
        this.adsList = adsList;
    }

    @NonNull
    @Override
    public mViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.ads_item, parent, false);
        return new mViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final mViewHolder holder, int position) {
         ad = adsList.get(position);
        setTitle(holder.title, ad.getAdTitle());
        holder.progressBar.setVisibility(View.VISIBLE);
        Glide.with(mContext).load(ad.getAdThumbnail()).listener(new RequestListener<Drawable>() {
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
        }).transition(GenericTransitionOptions.with(R.anim.fade_in)).into(holder.thumbnail);
        setOnItemClick(holder.row_parent);
    }

    @Override
    public int getItemCount() {
        return adsList.size();
    }

    static class mViewHolder extends RecyclerView.ViewHolder {
        CardView row_parent;
        ImageView thumbnail;
        TextView title;
        ProgressBar progressBar;

        mViewHolder(@NonNull View itemView) {
            super(itemView);
            thumbnail = itemView.findViewById(R.id.adThumbnail);
            title = itemView.findViewById(R.id.adTitle);
            row_parent = itemView.findViewById(R.id.card_parent_row_item);
            progressBar = itemView.findViewById(R.id.progressBar);
        }
    }


    private void setTitle(TextView textView, String title) {
        textView.setText(title);
    }

    private void setOnItemClick(CardView layout) {
        layout.setOnClickListener(view -> {
            final String adKey = ad.getAdLink();
            openAds(adKey);
        });
    }

    private void openAds(String key) {
        Log.d("TAG", "openAds: "+key);
        try {
            Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(key));
            mContext.startActivity(intent);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
