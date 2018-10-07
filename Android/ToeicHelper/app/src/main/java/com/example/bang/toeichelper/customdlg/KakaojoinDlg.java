package com.example.bang.toeichelper.customdlg;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;

import com.example.bang.toeichelper.IMAGE_PROCESSING;
import com.example.bang.toeichelper.R;

/**
 * Created by BANG on 2015-02-14.
 */
public class KakaojoinDlg extends Dialog implements View.OnClickListener{

    private Context context;

    public KakaojoinDlg(Context context) {
        super(context);

        this.context = context;

        //타이틀 없애기
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.dialog_join_kakao);
    }

    @Override
    public void onClick(View v) {

    }
}
