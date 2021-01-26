package com.alim.ebook.Models;

public class Report {
    private String uid;
    private String userName;
    private String userEmail;
    private String reportTitle;
    private String reportText;
    private String reportImage;

    public Report() {
    }

    public String getUid() {
        return uid;
    }

    public Report setUid(String uid) {
        this.uid = uid;
        return this;
    }

    public String getUserEmail() {
        return userEmail;
    }

    public Report setUserEmail(String userEmail) {
        this.userEmail = userEmail;
        return this;
    }

    public String getReportTitle() {
        return reportTitle;
    }

    public Report setReportTitle(String reportTitle) {
        this.reportTitle = reportTitle;
        return this;
    }

    public String getReportText() {
        return reportText;
    }

    public Report setReportText(String reportText) {
        this.reportText = reportText;
        return this;
    }

    public String getReportImage() {
        return reportImage;
    }

    public Report setReportImage(String reportImage) {
        this.reportImage = reportImage;
        return this;
    }

    public String getUserName() {
        return userName;
    }

    public Report setUserName(String userName) {
        this.userName = userName;
        return this;
    }
}
