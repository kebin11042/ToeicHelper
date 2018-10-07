package com.example.bang.toeichelper.mydata;

import java.io.Serializable;

/**
 * Created by BANG on 2015-05-25.
 */
public class MYDICT implements Serializable {

    private int pk;
    private String Name;
    private String Date;

    public MyWord_Handler myWord_handler;

    public MYDICT(int pk, String Name, String Date){
        this.pk = pk;
        this.Name = Name;
        this.Date = Date;

        myWord_handler = null;
    }

    //getter
    public int getPk() {
        return pk;
    }

    public String getName() {
        return Name;
    }

    public String getDate() {
        return Date;
    }

    //setter
    public void setPk(int pk) {
        this.pk = pk;
    }

    public void setName(String name) {
        Name = name;
    }

    public void setDate(String date) {
        Date = date;
    }
}
