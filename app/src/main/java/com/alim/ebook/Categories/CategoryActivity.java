package com.alim.ebook.Categories;


import android.os.Bundle;
import android.view.View;
import android.widget.AbsListView;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.alim.ebook.Adapters.BookAdapter;
import com.alim.ebook.Fragments.CategoryFragment;
import com.alim.ebook.Helper.NetworkConnectivity;
import com.alim.ebook.Models.Book;
import com.alim.ebook.R;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;

import java.util.ArrayList;
import java.util.Arrays;

public class CategoryActivity extends AppCompatActivity {

    BookAdapter bookAdapter;
    RecyclerView recyclerView;
    ArrayList<Book> bookList;
    ProgressBar catProgress;
    RelativeLayout nestedScrollView;
    RelativeLayout noNetworkLayout;
    Button btnRefresh, btnOpenMyBooks;
    private String cat;
    private DocumentSnapshot lastVisible;
    private boolean isScrolling = false;
    private boolean isLastItemReached = false;
    private final int limit = 12;

    String[] cats = {"literature", "psychology", "philosophy", "english", "computer"};

    String[] titles = {"Literature","Psychology",
            "Philosophy", "English","Computer"

    };

    CollectionReference bookRef = FirebaseFirestore.getInstance().collection("booksList");
    Query query;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_category);
        String title = getIntent().getStringExtra("title");
        cat = cats[Arrays.asList(titles).indexOf(title)];

        TextView booksTitle = findViewById(R.id.books_title);
        booksTitle.setText(title);

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
        bookList = new ArrayList<>();
        nestedScrollView = findViewById(R.id.nested_layout);
        catProgress = findViewById(R.id.catProgress);
        catProgress.setVisibility(View.VISIBLE);
        findViewById(R.id.img_back).setOnClickListener(view -> onBackPressed());
        btnRefresh = noNetworkLayout.findViewById(R.id.btn_retry);
        btnOpenMyBooks = noNetworkLayout.findViewById(R.id.btn_go_to_mybooks);

        btnRefresh.setOnClickListener(view -> {
            if (NetworkConnectivity.isNetworkConnected(getApplicationContext())) {
                noNetworkLayout.setVisibility(View.GONE);
                catProgress.setVisibility(View.VISIBLE);
                initBooks();
                initAdapter();
            }
        });
        btnOpenMyBooks.setOnClickListener(view -> {
            CategoryFragment.goToMyBooks = true;
            finish();
        });
    }


    private void initBooks() {
        bookList.clear();
        query = bookRef.whereEqualTo("categoryParent", cat)
                .orderBy("timeStamp", Query.Direction.DESCENDING)
                .limit(limit);
        query.get().addOnCompleteListener(c -> {
            try {
                if (c.isSuccessful()) {
                    for (DocumentSnapshot snapshot : c.getResult()) {
                        Book b = snapshot.toObject(Book.class);
                        bookList.add(b);
                    }
                    lastVisible = c.getResult().getDocuments().get(c.getResult().size() - 1);
                    bookAdapter.notifyDataSetChanged();
                    if (bookList.size() == 0) {
                        catProgress.setVisibility(View.GONE);
                        findViewById(R.id.yet_uploading).setVisibility(View.VISIBLE);
                    } else {
                        nestedScrollView.setVisibility(View.VISIBLE);
                        catProgress.setVisibility(View.GONE);
                    }
                    recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
                        @Override
                        public void onScrollStateChanged(@NonNull RecyclerView rec, int newState) {
                            super.onScrollStateChanged(rec, newState);
                            if (newState == AbsListView.OnScrollListener.SCROLL_STATE_TOUCH_SCROLL) {
                                isScrolling = true;
                            }

                        }

                        @Override
                        public void onScrolled(@NonNull RecyclerView rec, int dx, int dy) {
                            super.onScrolled(rec, dx, dy);
                            LinearLayoutManager linearLayoutManager = ((LinearLayoutManager) recyclerView.getLayoutManager());
                            int firstVisibleItemPosition = linearLayoutManager.findFirstVisibleItemPosition();
                            int visibleItemCount = linearLayoutManager.getChildCount();
                            int totalItemCount = linearLayoutManager.getItemCount();

                            if (isScrolling && (firstVisibleItemPosition + visibleItemCount == totalItemCount) && !isLastItemReached) {
                                isScrolling = false;
                                Query nextQuery = bookRef.whereEqualTo("categoryParent", cat)
                                        .orderBy("timeStamp", Query.Direction.DESCENDING)
                                        .startAfter(lastVisible).limit(limit);
                                nextQuery.get().addOnCompleteListener(cm -> {
                                    try {
                                        if (cm.isSuccessful()) {
                                            for (DocumentSnapshot d : cm.getResult()) {
                                                Book productModel = d.toObject(Book.class);
                                                bookList.add(productModel);
                                            }
                                            lastVisible = cm.getResult().getDocuments().get(cm.getResult().size() - 1);
                                            if (cm.getResult().size() < limit) {
                                                isLastItemReached = true;
                                            }
                                            bookAdapter.notifyDataSetChanged();
                                        }
                                    } catch (StringIndexOutOfBoundsException e) {
                                        e.printStackTrace();
                                    }

                                });
                            }

                        }
                    });

                }

            } catch (StringIndexOutOfBoundsException e){
                e.printStackTrace();
            }
        });
    }

    private void initAdapter() {
        bookAdapter = new BookAdapter(this, bookList);
        recyclerView.setLayoutManager(new GridLayoutManager(this, 3));
        recyclerView.setAdapter(bookAdapter);
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
