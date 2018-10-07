package com.example.bang.toeichelper.customdlg;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.Window;
import android.widget.TextView;

import com.example.bang.toeichelper.R;

/**
 * Created by BANG on 2015-02-26.
 */
public class ColorProgressDlg extends Dialog {

    private TextView txtvTitle;
    private String strTitle;

    public ColorProgressDlg(Context context, String strTitle) {
        super(context);

        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.dialog_colorprogress);

        this.strTitle = strTitle;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        txtvTitle = (TextView) findViewById(R.id.txtvColorDlgTitle);
        if(!strTitle.equals("")){
            txtvTitle.setText(strTitle);
        }

    }
}
