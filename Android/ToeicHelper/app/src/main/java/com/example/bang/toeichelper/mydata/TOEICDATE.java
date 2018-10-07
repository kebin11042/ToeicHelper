package com.example.bang.toeichelper.mydata;

import android.util.Log;

import java.io.Serializable;

/**
 * Created by BANG on 2015-01-29.
 */
public class TOEICDATE implements Serializable{

    private int key;        //순서

    private String strTH;               //회차
    private String strDATE;             //시험일
    private String strAnnouncement;     //성적발표일
    private String strApplicationPeriod;    //접수 기간

    private int nExamYear, nExamMonth, nExamDay;   //시험 날짜

    public TOEICDATE() {
    }

    public TOEICDATE(int key, String strTH, String strDATE, String strAnnouncement, String strApplicationPeriod) {
        this.key = key;

        this.strTH = strTH;
        this.strDATE = strDATE;
        this.strAnnouncement = strAnnouncement;
        this.strApplicationPeriod = strApplicationPeriod;

        nExamYear = 2000 + Integer.parseInt(strDATE.substring(0,2));
        nExamMonth = Integer.parseInt(strDATE.substring(3,5));
        nExamDay = Integer.parseInt(strDATE.substring(6,8));

        //Log.w("DATE", "date = " + String.format("%d / %d / %d", nExamYear, nExamMonth, nExamDay));
    }

    public int getKey() {
        return key;
    }

    public String getStrTH() {
        return strTH;
    }

    public String getStrDATE() {
        return strDATE;
    }

    public String getStrAnnouncement() {
        return strAnnouncement;
    }

    public String getStrApplicationPeriod() {
        return strApplicationPeriod;
    }

    public int getnExamYear() {
        return nExamYear;
    }

    public int getnExamMonth() {
        return nExamMonth;
    }

    public int getnExamDay() {
        return nExamDay;
    }

    public void setStrTH(String strTH) {
        this.strTH = strTH;
    }

    public void setStrDATE(String strDATE) {
        this.strDATE = strDATE;
    }

    public void setStrAnnouncement(String strAnnouncement) {
        this.strAnnouncement = strAnnouncement;
    }

    public void setStrApplicationPeriod(String strApplicationPeriod) {
        this.strApplicationPeriod = strApplicationPeriod;
    }
}
