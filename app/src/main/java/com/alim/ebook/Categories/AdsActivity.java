package com.alim.ebook.Categories;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.alim.ebook.Adapters.AdsActivityAdapter;
import com.alim.ebook.Helper.NetworkConnectivity;
import com.alim.ebook.MainActivity;
import com.alim.ebook.Models.Ads;
import com.alim.ebook.R;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import java.util.ArrayList;

public class AdsActivity extends AppCompatActivity {

    ArrayList<Ads> adsList;
    AdsActivityAdapter adapter;
    RecyclerView recyclerView;
    ProgressBar catProgress;
    RelativeLayout nestedScrollView;
    RelativeLayout noNetworkLayout;
    Button btnRefresh, btnOpenMyBooks;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_category);
        TextView booksTitle =findViewById(R.id.books_title);
        booksTitle.setText(R.string.recommendation);
        initViews();
        if (!NetworkConnectivity.isNetworkConnected(this)) {
            catProgress.setVisibility(View.GONE);
            noNetworkLayout.setVisibility(View.VISIBLE);
            return;
        }
        initBooks();
        initAdapter();
    }
    private void initViews() {
        recyclerView = findViewById(R.id.books_rv);
        noNetworkLayout = findViewById(R.id.no_network_layout);
        adsList = new ArrayList<>();
        nestedScrollView = findViewById(R.id.nested_layout);
        catProgress = findViewById(R.id.catProgress);
        catProgress.setVisibility(View.VISIBLE);
        findViewById(R.id.img_back).setOnClickListener(view -> onBackPressed());
        btnRefresh = noNetworkLayout.findViewById(R.id.btn_retry);
        btnOpenMyBooks = noNetworkLayout.findViewById(R.id.btn_go_to_mybooks);

        btnRefresh.setOnClickListener(view -> {
            if (NetworkConnectivity.isNetworkConnected(getApplicationContext())){
                noNetworkLayout.setVisibility(View.GONE);
                catProgress.setVisibility(View.VISIBLE);
                initBooks();
                initAdapter();
            }
        });
        btnOpenMyBooks.setOnClickListener(view -> {
            Intent i = new Intent(getApplicationContext(), MainActivity.class);
            i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(i );
        });
    }
    private void initBooks() {
        FirebaseFirestore.getInstance()
                .collection("ads")
                .orderBy("timeStamp", Query.Direction.DESCENDING)
                .addSnapshotListener((value, error) -> {
                    adsList.clear();
                    for (QueryDocumentSnapshot snapshot : value) {
                        Ads ads = snapshot.toObject(Ads.class);
                        adsList.add(ads);
                    }
                    adapter.notifyDataSetChanged();
                    nestedScrollView.setVisibility(View.VISIBLE);
                    catProgress.setVisibility(View.GONE);
                });
    }

    private void initAdapter() {
        adapter = new AdsActivityAdapter(this, adsList);
        recyclerView.setLayoutManager(new GridLayoutManager(this, 1));
        recyclerView.setAdapter(adapter);
        recyclerView.setNestedScrollingEnabled(true);
    }
    public void BackBtn(View view) {
        onBackPressed();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }
}
