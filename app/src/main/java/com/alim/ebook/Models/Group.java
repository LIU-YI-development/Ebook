package com.alim.ebook.Models;


public class Group {
    private String groupTitle;
    private String groupButtonTitle;

    public Group() {
    }


    public String getGroupTitle() {
        return groupTitle;
    }

    public String getGroupButtonTitle() {
        return groupButtonTitle;
    }

    public Group setGroupTitle(String groupTitle){
        this.groupTitle = groupTitle;
        return this;
    }

    public Group setGroupButtonTitle(String groupButtonTitle){
        this.groupButtonTitle = groupButtonTitle;
        return this;
    }
}
