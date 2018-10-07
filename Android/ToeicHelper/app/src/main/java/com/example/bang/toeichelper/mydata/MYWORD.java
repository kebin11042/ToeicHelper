package com.example.bang.toeichelper.mydata;

import java.io.Serializable;

/**
 * Created by BANG on 2015-06-17.
 */
public class MYWORD implements Serializable{

    private int nPublicDictPk;
    private String strWord;
    private String strSub;

    public MYWORD(int nPublicDictPk, String strWord, String strSub) {
        this.nPublicDictPk = nPublicDictPk;
        this.strWord = strWord;
        this.strSub = strSub;
    }

    public int getnPublicDictPk() {
        return nPublicDictPk;
    }

    public void setnPublicDictPk(int nPublicDictPk) {
        this.nPublicDictPk = nPublicDictPk;
    }

    public String getStrWord() {
        return strWord;
    }

    public void setStrWord(String strWord) {
        this.strWord = strWord;
    }

    public String getStrSub() {
        return strSub;
    }

    public void setStrSub(String strSub) {
        this.strSub = strSub;
    }
}
