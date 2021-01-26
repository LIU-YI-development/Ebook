package com.alim.ebook.Fragments;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ExpandableListAdapter;
import android.widget.ExpandableListView;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import com.alim.ebook.Adapters.ExpandableAdapter;
import com.alim.ebook.Categories.BooksActivity;
import com.alim.ebook.MainActivity;
import com.alim.ebook.Models.ExpandableList;
import com.alim.ebook.R;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;


public class CategoryFragment extends Fragment {
    private View view;
    private ExpandableListAdapter expandableListAdapter;

   public static boolean goToMyBooks = false;
    public CategoryFragment() {
        // Required empty public constructor
    }
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_category, container, false);
        initViews();
        return view;
    }

    private void initViews() {
        ExpandableListView expandableListView = (ExpandableListView) view.findViewById(R.id.expandableListView);
        HashMap<String, List<String>> expandableListDetail = ExpandableList.getData();
        List<String> expandableListTitle = new ArrayList<String>(expandableListDetail.keySet());
        expandableListAdapter = new ExpandableAdapter(getActivity().getApplicationContext(), expandableListTitle, expandableListDetail);
        expandableListView.setAdapter(expandableListAdapter);

        expandableListView.setOnChildClickListener((parent, v, groupPosition, childPosition, id) -> {
            final String selected = (String) expandableListAdapter.getChild(groupPosition, childPosition);
            switch (selected) {
//                    Literature starts
                case "Story":
                    openActivity("Story");
                    break;
                case "Novel":
                    openActivity("Novel");
                    break;
                case "Biography":
                    openActivity("Biography");
                    break;
                case "Memoirs":
                    openActivity("Memoirs");
                    break;

//          Literature Ends

//                    psychology starts
                case "Success":
                    openActivity("Success");
                    break;
                case "Family and Relations":
                    openActivity("Family and Relations");
                    break;
                case "Self-development":
                    openActivity("Self-development");
                    break;
                case "Kids":
                    openActivity("Kids");
                    break;

//                        English Books start
                case "Literature & Fiction":
                    openActivity("Literature & Fiction");
                    break;
                case "Entrepreneurship":
                    openActivity("Entrepreneurship");
                    break;
                case "Biographies":
                    openActivity("Biographies");
                    break;
                case "Sci-fi":
                    openActivity("Sci-fi");
                    break;
                case "Business":
                    openActivity("Business");
                    break;
                case "Comics":
                    openActivity("Comics");
                    break;
                case "Other":
                    openActivity("Other");
                    break;
//                        computer starts
                case "Computer and Internet":
                    openActivity("Computer and Internet");
                    break;
                case "Programming":
                    openActivity("Programming");
                    break;
                case "E-commerce":
                    openActivity("E-commerce");
                    break;
                case "Graphic":
                    openActivity("Graphic");
                    break;
                case "Network":
                    openActivity("Network");
                    break;
//              philosophy starts
                case "Islam":
                    openActivity("Islam");
                    break;
                case "World":
                    openActivity("World");
                    break;
            }
            return false;
        });

    }

    private void openActivity(String title) {
        Intent intent = new Intent(getActivity(), BooksActivity.class);
        intent.putExtra("title", title);
        startActivity(intent);
    }

    @Override
    public void onResume() {
        if(goToMyBooks){
            goToMyBooks = false;
            FragmentManager manager = getActivity().getSupportFragmentManager();
            manager.beginTransaction().remove(manager.findFragmentByTag(CategoryFragment.class.getSimpleName())).commit();
            Fragment m = manager.findFragmentByTag(MyBooksFragment.class.getSimpleName());
            if(m!=null) {
                manager.beginTransaction().show(manager.findFragmentByTag(MyBooksFragment.class.getSimpleName())).commit();
            } else {
                manager.beginTransaction().add(R.id.frame_container,new MyBooksFragment(),MyBooksFragment.class.getSimpleName()).commit();
            }
            MainActivity.bottomNavigation.getMenu().findItem(R.id.my_books).setChecked(true);
        }
        super.onResume();
    }
}

