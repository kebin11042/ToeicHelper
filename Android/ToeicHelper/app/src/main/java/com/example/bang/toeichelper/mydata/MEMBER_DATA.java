package com.example.bang.toeichelper.mydata;

import java.io.Serializable;

/**
 * Created by BANG on 2015-02-11.
 */
public class MEMBER_DATA implements Serializable{

    private String pk;
    private String strEmail;
    private String strName;
    private String strPW;
    private GOAL_DATA goal_data;

    public MyScore_Handler myScore_handler;
    public MyDict_Handler myDict_handler;


    public MEMBER_DATA() {}

    public MEMBER_DATA(String pk, String strEmail, String strName, String strPW){
        this.pk = pk;
        this.strEmail = strEmail;
        this.strName = strName;
        this.strPW = strPW;

        myScore_handler = new MyScore_Handler();
        myDict_handler = new MyDict_Handler();
    }

    public MEMBER_DATA(String pk, String strEmail, String strName, String strPW, GOAL_DATA goal_data){
        this.pk = pk;
        this.strEmail = strEmail;
        this.strName = strName;
        this.strPW = strPW;

        this.goal_data = goal_data;

        myScore_handler = new MyScore_Handler();
        myDict_handler = new MyDict_Handler();
    }

    public String getPk() {
        return pk;
    }

    public String getStrEmail() {
        return strEmail;
    }

    public String getStrName() {
        return strName;
    }

    public String getStrPW() {
        return strPW;
    }

    public GOAL_DATA getGoal_data(){ return goal_data; }

    public void setPk(String pk) {
        this.pk = pk;
    }

    public void setStrEmail(String strEmail) {
        this.strEmail = strEmail;
    }

    public void setStrName(String strName) {
        this.strName = strName;
    }

    public void setStrPW(String strPW) {
        this.strPW = strPW;
    }

    public void setGoal_data(GOAL_DATA goal_data) { this.goal_data = goal_data; }
}
