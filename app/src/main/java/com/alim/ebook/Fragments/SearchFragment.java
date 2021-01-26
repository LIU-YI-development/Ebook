package com.alim.ebook.Fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;

import androidx.appcompat.widget.SearchView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.alim.ebook.Adapters.BookSearchAdapter;
import com.alim.ebook.Models.Book;
import com.alim.ebook.R;

import java.util.ArrayList;


public class SearchFragment extends Fragment {
    private View view;
    private BookSearchAdapter bookSearchAdapter;
    private final ArrayList<Book> books = new ArrayList<>();
    private SearchView searchText;
    public RecyclerView recyclerView;
    public RelativeLayout failedLayout;
   public ProgressBar progSearching;

    public SearchFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_search, container, false);
        initViews();
        searchText.setOnQueryTextListener(new SearchView.OnQueryTextListener() {

            @Override
            public boolean onQueryTextSubmit(String s) {
                bookSearchAdapter.getFilter().filter(s);
                progSearching.setVisibility(View.VISIBLE);
                recyclerView.setVisibility(View.VISIBLE);
                return true;
            }
            @Override
            public boolean onQueryTextChange(String s) {
                recyclerView.setVisibility(View.GONE);
                progSearching.setVisibility(View.GONE);
                failedLayout.setVisibility(View.GONE);
                return true;
            }
        });
        return view;
    }

    private void initViews() {
        searchText = view.findViewById(R.id.editText_search);
        recyclerView = view.findViewById(R.id.search_rv);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new GridLayoutManager(getActivity(), 3));
        recyclerView.setNestedScrollingEnabled(true);
        failedLayout = view.findViewById(R.id.search_failed_view);
        failedLayout.setVisibility(View.GONE);
        recyclerView.setVisibility(View.GONE);
        progSearching = view.findViewById(R.id.search_fragment_progress);
        bookSearchAdapter = new BookSearchAdapter(getActivity(), books, SearchFragment.this);
        recyclerView.setAdapter(bookSearchAdapter);
    }
}
