package com.example.bang.toeichelper.mydata;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by BANG on 2015-05-24.
 */
public class MyScore_Handler implements Serializable {

    private int MyScoreSize;
    private ArrayList<MYSCORE> arrMyScore;

    public MyScore_Handler() {
        MyScoreSize = 0;
        arrMyScore = new ArrayList<MYSCORE>();
    }

    public int getMyScoreSize(){ return this.MyScoreSize; }

    public MyScore_Handler(int MyScoreSize, ArrayList<MYSCORE> arrMyScore) {
        this.arrMyScore = arrMyScore;
        this.MyScoreSize = MyScoreSize;
    }

    public void addMyScore(MYSCORE myscore){
        arrMyScore.add(MyScoreSize, myscore);
        MyScoreSize++;
    }

    public MYSCORE getMyScorebyIndex(int index){
        return arrMyScore.get(index);
    }
}
