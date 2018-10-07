package com.example.bang.toeichelper.mydata;

import java.util.ArrayList;

/**
 * Created by BANG on 2015-06-18.
 */
public class POPWORD {

    private String strWord;
    private int nCnt;

    public POPWORD(String strWord, int nCnt) {
        this.strWord = strWord;
        this.nCnt = nCnt;
    }

    public String getStrWord() {
        return strWord;
    }

    public int getnCnt() {
        return nCnt;
    }
}
