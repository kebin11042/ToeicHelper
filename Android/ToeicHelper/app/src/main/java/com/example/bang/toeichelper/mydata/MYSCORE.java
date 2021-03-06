package com.example.bang.toeichelper.mydata;

import java.io.Serializable;

/**
 * Created by BANG on 2015-05-24.
 */
public class MYSCORE implements Serializable {

    private int pk;
    private int[] part;
    private String strDate;

    private int sumLC, sumRC;
    private int LC_max, LC_min;
    private int RC_max, RC_min;
    private int Total_min, Total_max;

    public MYSCORE() {

    }

    public MYSCORE(int pk, int[] part, String strDate) {
        this.pk = pk;
        this.part = part;
        this.strDate = strDate;

        this.setScore();
    }

    public MYSCORE(int pk, int part1, int part2, int part3, int part4, int part5, int part6, int part7, String strDate){
        this.pk = pk;

        this.part = new int[7];

        part[0] = part1;
        part[1] = part2;
        part[2] = part3;
        part[3] = part4;
        part[4] = part5;
        part[5] = part6;
        part[6] = part7;

        this.strDate = strDate;

        this.setScore();
    }

    public void setScore(){
        sumLC = 0;
        sumRC = 0;

        //LC 합산 개수
        for(int i=0;i<4;i++){
            sumLC += part[i];
        }
        //RC 합산 개수
        for(int i=4;i<7;i++){
            sumRC += part[i];
        }

        //LC점수 결정
        if(sumLC > 95){
            LC_max = 495;
            LC_min = 480;
        }
        else if(sumLC > 90){
            LC_max = 490;
            LC_min = 435;
        }
        else if(sumLC > 85){
            LC_max = 450;
            LC_min = 395;
        }
        else if(sumLC > 80){
            LC_max = 415;
            LC_min = 355;
        }
        else if(sumLC > 75){
            LC_max = 475;
            LC_min = 325;
        }
        else if(sumLC > 70){
            LC_max = 340;
            LC_min = 295;
        }
        else if(sumLC > 65){
            LC_max = 315;
            LC_min = 265;
        }
        else if(sumLC > 60){
            LC_max = 285;
            LC_min = 240;
        }
        else if(sumLC > 55){
            LC_max = 260;
            LC_min = 215;
        }
        else if(sumLC > 50){
            LC_max = 235;
            LC_min = 190;
        }
        else if(sumLC > 45){
            LC_max = 210;
            LC_min = 160;
        }
        else if(sumLC > 40){
            LC_max = 180;
            LC_min = 135;
        }
        else if(sumLC > 35){
            LC_max = 155;
            LC_min = 110;
        }
        else if(sumLC > 30){
            LC_max = 130;
            LC_min = 85;
        }
        else if(sumLC > 25){
            LC_max = 105;
            LC_min = 70;
        }
        else if(sumLC > 20){
            LC_max = 90;
            LC_min = 50;
        }
        else if(sumLC > 15){
            LC_max = 70;
            LC_min = 35;
        }
        else if(sumLC > 10){
            LC_max = 55;
            LC_min = 20;
        }
        else if(sumLC > 5){
            LC_max = 40;
            LC_min = 15;
        }
        else if(sumLC > 0){
            LC_max = 20;
            LC_min = 5;
        }
        else{
            LC_min = 5;
            LC_max = 5;
        }



        //RC점수 결정
        if(sumRC > 95){
            RC_max = 495;
            RC_min = 460;
        }
        else if(sumRC > 90){
            RC_max = 475;
            RC_min = 410;
        }
        else if(sumRC > 85){
            RC_max = 430;
            RC_min = 380;
        }
        else if(sumRC > 80){
            RC_max = 400;
            RC_min = 355;
        }
        else if(sumRC > 75){
            RC_max = 375;
            RC_min = 325;
        }
        else if(sumRC > 70){
            RC_max = 345;
            RC_min = 295;
        }
        else if(sumRC > 65){
            RC_max = 315;
            RC_min = 265;
        }
        else if(sumRC > 60){
            RC_max = 285;
            RC_min = 235;
        }
        else if(sumRC > 55){
            RC_max = 255;
            RC_min = 205;
        }
        else if(sumRC > 50){
            RC_max = 225;
            RC_min = 175;
        }
        else if(sumRC > 45){
            RC_max = 195;
            RC_min = 150;
        }
        else if(sumRC > 40){
            RC_max = 170;
            RC_min = 120;
        }
        else if(sumRC > 35){
            RC_max = 140;
            RC_min = 100;
        }
        else if(sumRC > 30){
            RC_max = 120;
            RC_min = 75;
        }
        else if(sumRC > 25){
            RC_max = 100;
            RC_min = 55;
        }
        else if(sumRC > 20){
            RC_max = 80;
            RC_min = 40;
        }
        else if(sumRC > 15){
            RC_max = 65;
            RC_min = 30;
        }
        else if(sumRC > 10){
            RC_max = 50;
            RC_min = 20;
        }
        else if(sumRC > 5){
            RC_max = 35;
            RC_min = 15;
        }
        else if(sumRC > 0){
            RC_max = 20;
            RC_min = 5;
        }
        else{
            RC_min = 5;
            RC_max = 5;
        }

        Total_max = LC_max + RC_max;
        Total_min = LC_min + RC_min;
    }

    public int getTotalLC(){
        int totalLC = 0;

        for(int i=0;i<5;i++){
            totalLC += part[i];
        }

        return totalLC;
    }

    public int getTotalRC(){
        int totalRC = 0;

        for(int i=5;i<part.length;i++){
            totalRC += part[i];
        }

        return totalRC;
    }

    public int getTotal(){
        int Total = 0;

        for(int i=0;i<part.length;i++){
            Total += part[i];
        }

        return Total;
    }

    public int getPk(){ return this.pk; }

    public int[] getPart() {
        return part;
    }

    public String getStrDate() {
        return strDate;
    }

    public void setPartbyIndex(int partScore, int index){
        this.part[index] = partScore;
    }

    public void setStrDate(String strDate) {
        this.strDate = strDate;
    }
}
