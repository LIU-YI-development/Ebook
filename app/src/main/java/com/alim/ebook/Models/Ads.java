package com.alim.ebook.Models;

public class Ads {
    private String adTitle;
    private String adThumbnail;
    private String adLink;
    private int clickCount;
    private String key;

    public Ads() {
    }

    public Ads(String adTitle, String adThumbnail, String adLink, int clickCount) {
        this.adTitle = adTitle;
        this.adThumbnail = adThumbnail;
        this.adLink = adLink;
        this.clickCount = clickCount;
    }

    public String getAdTitle() {
        return adTitle;
    }

    public void setAdTitle(String adTitle) {
        this.adTitle = adTitle;
    }

    public String getAdThumbnail() {
        return adThumbnail;
    }

    public void setAdThumbnail(String adThumbnail) {
        this.adThumbnail = adThumbnail;
    }

    public String getAdLink() {
        return adLink;
    }

    public void setAdLink(String adLink) {
        this.adLink = adLink;
    }

    public int getClickCount() {
        return clickCount;
    }

    public void setClickCount(int clickCount) {
        this.clickCount = clickCount;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }
}
