package com.alim.ebook.Fragments;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.core.widget.NestedScrollView;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.alim.ebook.Activities.ProfileActivity;
import com.alim.ebook.Adapters.MyBooksAdapter;
import com.alim.ebook.Models.FavBook;
import com.alim.ebook.Models.FavBooks;
import com.alim.ebook.R;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.MobileAds;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import java.util.ArrayList;
import java.util.Objects;

public class MyBooksFragment extends Fragment {

    private static final String TAG = "MyBooksFragment";
    private View view;
    private RecyclerView rv;
    private ArrayList<FavBook> favList;
    private final FirebaseFirestore db = FirebaseFirestore.getInstance();
    private final CollectionReference ref = db.collection("favBooks");
    ProgressBar progressBar;
    NestedScrollView nestedScrollView;
    TextView noBooksAvailable;
    public MyBooksFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.my_book_fragment, container, false);
        getActivity().setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);


        initViews();
        loadBooks();
        return view;
    }

    private void initViews() {
        nestedScrollView = view.findViewById(R.id.nestedScroll);
        progressBar = view.findViewById(R.id.mybook_progressbar);
        progressBar.setVisibility(View.VISIBLE);
        nestedScrollView.setVisibility(View.GONE);
        noBooksAvailable = view.findViewById(R.id.no_book_available);
        rv = view.findViewById(R.id.my_books_rv);
        ImageView imgProfile = view.findViewById(R.id.img_profile);
        ImageView actionRef = view.findViewById(R.id.action_refresh);
        imgProfile.setOnClickListener(view -> {
            Intent intent = new Intent(getActivity(), ProfileActivity.class);
            getActivity().startActivity(intent);
        });

        actionRef.setOnClickListener(view -> {
            try {
                Fragment fragment;
                fragment = Objects.requireNonNull(getActivity()).getSupportFragmentManager().findFragmentByTag("MyBooksFragment");
                final FragmentTransaction ft = getActivity().getSupportFragmentManager().beginTransaction();
                assert fragment != null;
                ft.detach(fragment);
                ft.attach(fragment);
                ft.commit();
            } catch (Exception e ){
                Log.d(TAG, "onClick: "+e);
            }

        });
    }

    private void loadBooks() {
        favList = new ArrayList<>();
        try {
            String uid = FirebaseAuth.getInstance().getCurrentUser().getUid();
            ref.document(uid).addSnapshotListener((documentSnapshot, e) -> {
                if (documentSnapshot != null && e == null) {
                    FavBooks books = documentSnapshot.toObject(FavBooks.class);
                    if (books != null) {
                        favList.clear();
                        favList.addAll(books.getBooks());
                        setAdapter();
                    }
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void setAdapter() {
        if (favList.size() != 0) {
            MyBooksAdapter myBooksAdapter = new MyBooksAdapter(getContext(), favList);
            LinearLayoutManager layoutManager = new GridLayoutManager(getActivity(), 3);
            rv.setLayoutManager(layoutManager);
            rv.setAdapter(myBooksAdapter);
            rv.setNestedScrollingEnabled(true);
            progressBar.setVisibility(View.GONE);
            nestedScrollView.setVisibility(View.VISIBLE);
        } else {
            progressBar.setVisibility(View.GONE);
            noBooksAvailable.setVisibility(View.VISIBLE);
        }
    }
}
