package com.example.bang.toeichelper.mydata;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by BANG on 2015-06-17.
 */
public class MyWord_Handler implements Serializable {

    private int MyWordsSize;
    private ArrayList<MYWORD> arrMyWord;

    public MyWord_Handler() {
        MyWordsSize = 0;
        arrMyWord = new ArrayList<MYWORD>();
    }

    public int getMyWordsSize() {
        return MyWordsSize;
    }

    public void setArrMyWord(ArrayList<MYWORD> arrMyWord) {
        this.arrMyWord = arrMyWord;
    }

    public void AddMyWord(MYWORD myWord){
        arrMyWord.add(MyWordsSize, myWord);
        MyWordsSize++;
    }

    public MYWORD getMyWordbyIndex(int index){
        return arrMyWord.get(index);
    }

}
