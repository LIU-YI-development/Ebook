package com.alim.ebook.Fragments;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.Toast;

import androidx.core.widget.NestedScrollView;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.alim.ebook.Activities.ProfileActivity;
import com.alim.ebook.Adapters.GroupAdapter;
import com.alim.ebook.MainActivity;
import com.alim.ebook.Models.Ads;
import com.alim.ebook.Models.Book;
import com.alim.ebook.Models.Group;
import com.alim.ebook.R;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import java.util.ArrayList;
import java.util.Objects;

public class HomeFragment extends Fragment {

    private static final String TAG = "HomeFragment";
    private GroupAdapter groupAdapter;
    private ArrayList<Group> groups;
    private ArrayList<Ads> ads;
    private ArrayList<Book> latestBooks;
    private ArrayList<ArrayList<Book>> bookCategories;
    private RecyclerView homeRecyclerview;
    private View root;
    public ProgressBar progressBar;
    private NestedScrollView nestedlayout;

    private RelativeLayout failedLayout;
    Button btnRefresh, btnOpenMyBooks;

    public HomeFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        root = inflater.inflate(R.layout.fragment_home, container, false);


        initData();
        setAdapter();
        loadBooks();

        return root;
    }


    private void initData() {

        homeRecyclerview = root.findViewById(R.id.home_rv);
        failedLayout = root.findViewById(R.id.failedItem);
        failedLayout.setVisibility(View.GONE);
        btnOpenMyBooks = failedLayout.findViewById(R.id.btn_go_to_mybooks);
        btnOpenMyBooks.setOnClickListener(view -> {
            FragmentManager manager = getActivity().getSupportFragmentManager();
            manager.beginTransaction().remove(manager.findFragmentByTag(HomeFragment.class.getSimpleName())).commit();
            Fragment m = manager.findFragmentByTag(MyBooksFragment.class.getSimpleName());
            if (m != null) {
                manager.beginTransaction().show(manager.findFragmentByTag(MyBooksFragment.class.getSimpleName())).commit();
            } else {
                manager.beginTransaction().add(R.id.frame_container, new MyBooksFragment(), MyBooksFragment.class.getSimpleName()).commit();
            }
            MainActivity.bottomNavigation.getMenu().findItem(R.id.my_books).setChecked(true);


        });


        btnRefresh = failedLayout.findViewById(R.id.btn_retry);
        btnRefresh.setOnClickListener(view -> {
            Fragment fragment;
            fragment = Objects.requireNonNull(getActivity()).getSupportFragmentManager().findFragmentByTag("HomeFragment");
            final FragmentTransaction ft = getActivity().getSupportFragmentManager().beginTransaction();
            assert fragment != null;
            ft.detach(fragment);
            ft.attach(fragment);
            ft.commit();
        });

        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
        homeRecyclerview.setLayoutManager(layoutManager);
        nestedlayout = root.findViewById(R.id.fragment_home_parent);
        progressBar = root.findViewById(R.id.home_progress);
        progressBar.setVisibility(View.VISIBLE);
        LinearLayout layout = root.findViewById(R.id.rate_us);
        layout.findViewById(R.id.facebook_page).setOnClickListener(view -> makeToast("Facebook Page"));
        layout.findViewById(R.id.instagram_page).setOnClickListener(view -> makeToast("Instagram Page"));
        layout.findViewById(R.id.telegram_channel).setOnClickListener(view -> makeToast("Telegram Channel"));
        groups = new ArrayList<>();
        ads = new ArrayList<>();
        latestBooks = new ArrayList<>();

        bookCategories = new ArrayList<>();


        ImageView profile_setting = root.findViewById(R.id.profile_icon);
        profile_setting.setOnClickListener(view -> startActivity(new Intent(getActivity(), ProfileActivity.class)));

    }

    private void loadBooks() {
        if (isNetworkConnected(getActivity())) {
            readAds();
            readLatest();
            readBooks();

        } else {
            failedLayout.setVisibility(View.VISIBLE);
            progressBar.setVisibility(View.GONE);
        }


    }

    private void setAdapter() {
        String[] titles = {"Recommended",
               "Latest", "Literature","Psychology",
                "Philosophy", "English","Computer"
        };
        for (String title : titles) {
            groups.add(new Group()
                    .setGroupTitle(title)
                    .setGroupButtonTitle("View All »")
            );
        }
        groupAdapter = new GroupAdapter(getActivity(), groups, ads, latestBooks, bookCategories);
        homeRecyclerview.setAdapter(groupAdapter);

    }

    private void readAds() {
        FirebaseFirestore.getInstance()
                .collection("ads")
                .orderBy("timeStamp", Query.Direction.DESCENDING)
                .limit(3)
                .addSnapshotListener((value, error) -> {
                    assert value != null;
                    ads.clear();
                    for (QueryDocumentSnapshot snap : value) {
                        Ads ad = snap.toObject(Ads.class);
                        ads.add(ad);
                    }
                    groupAdapter.notifyDataSetChanged();
                    nestedlayout.setVisibility(View.VISIBLE);
                    progressBar.setVisibility(View.GONE);
                });
    }

    //  Latest
    private void readLatest() {
        FirebaseFirestore.getInstance()
                .collection("booksList")
                .orderBy("timeStamp", Query.Direction.DESCENDING)
                .limit(7)
                .addSnapshotListener((value, error) -> {
                    latestBooks.clear();
                    for (QueryDocumentSnapshot snapshot : value) {
                        Book latest = snapshot.toObject(Book.class);
                        latestBooks.add(latest);
                    }
                    groupAdapter.notifyDataSetChanged();
                    nestedlayout.setVisibility(View.VISIBLE);
                    progressBar.setVisibility(View.GONE);

                });
    }

    // assign books to sections
    private void readBooks() {
        String[] cats = {"literature", "psychology", "philosophy", "english", "computer"};

        try {
            bookCategories.clear();
            for (int i = 0; i < cats.length; i++) {
                bookCategories.add(new ArrayList<Book>());
            }
            int index = -1;
            for (String cat : cats) {
                final int tempIndex = ++index;
                FirebaseFirestore.getInstance()
                        .collection("booksList")
                        .whereEqualTo("categoryParent", cat)
                        .orderBy("timeStamp", Query.Direction.DESCENDING)
                        .limit(5)
                        .addSnapshotListener((value, error) -> {
                            if (error != null) {
                                Log.d(TAG, "onEvent: " + error);
                            } else {
                                for (QueryDocumentSnapshot doc : value) {
                                    Book book = doc.toObject(Book.class);
                                    bookCategories.get(tempIndex).add(book);
                                }
                            }
                            if (tempIndex == 10) {
                                groupAdapter.notifyDataSetChanged();
                            }
                        });
            }
        } catch (StringIndexOutOfBoundsException e) {
            e.printStackTrace();
        }


    }

    private void makeToast(String text) {
        Toast.makeText(getActivity(), text, Toast.LENGTH_SHORT).show();
    }


    private static boolean isNetworkConnected(Context context) {
        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        return cm.getActiveNetworkInfo() != null;
    }

}