package com.alim.ebook.Models;

import androidx.annotation.Keep;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;

public class ExpandableList {
    public static LinkedHashMap<String, List<String>> getData() {
        LinkedHashMap<String, List<String>> expandableListDetail = new LinkedHashMap<>();

        List<String> literature = new ArrayList<String>();
        literature.add("Story");
        literature.add("Novel");
        literature.add("Biography");
        literature.add("Memoirs");


        List<String> psychology = new ArrayList<String>();
        psychology.add("Success");
        psychology.add("Family and Relations");
        psychology.add("Self-development");
        psychology.add("Kids");



        List<String> english =new ArrayList<>();
        english.add("Literature & Fiction");
        english.add("Entrepreneurship");
        english.add("Biographies");
        english.add("Sci-fi");
        english.add("Business");
        english.add("Comics");
        english.add("Other");

        List<String> computer = new ArrayList<String>();
        computer.add("Computer and Internet");
        computer.add("Programming");
        computer.add("E-commerce");
        computer.add("Graphic");
        computer.add("Network");

        List<String> philosophy = new ArrayList<String>();
        philosophy.add("Islam");
        philosophy.add("World");


        expandableListDetail.put("Literature", literature);
        expandableListDetail.put("Psychology", psychology);
        expandableListDetail.put("Philosophy", philosophy);
        expandableListDetail.put("English", english);
        expandableListDetail.put("Computer", computer);

        return expandableListDetail;
    }
}