package com.example.bang.toeichelper.mydata;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by BANG on 2015-05-25.
 */
public class MyDict_Handler implements Serializable {

    private ArrayList<MYDICT> arrMyDict;
    private int DictSize;

    public MyDict_Handler() {
        arrMyDict = new ArrayList<MYDICT>();
        DictSize = 0;
    }

    public MyDict_Handler(int DictSize, ArrayList<MYDICT> arrMyDict) {
        this.arrMyDict = arrMyDict;
        this.DictSize = DictSize;
    }

    //단어장 추가
    public void addMydict(MYDICT myDict){
        arrMyDict.add(DictSize, myDict);
        DictSize++;
    }

    //단어장 제거
    public void deleteMydict(int pk){

        for(int i=0;i<arrMyDict.size();i++){
            if(pk == arrMyDict.get(i).getPk()){
                arrMyDict.remove(i);
                break;
            }
        }

        this.DictSize--;
    }

    public MYDICT getMyDictbyIndex(int index){
        return arrMyDict.get(index);
    }

    public int getDictSize(){
        return this.DictSize;
    }
}
