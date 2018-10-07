package com.example.bang.toeichelper.customdlg;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.bang.toeichelper.R;

/**
 * Created by BANG on 2015-05-21.
 */
public class GoalPartDlg extends Dialog implements View.OnClickListener{

    private Context context;

    private String strPartName;
    private String strFullScore;
    private int nFullScore;

    private TextView txtvPartName;
    private int nPart;
    private EditText edtxScore;
    private TextView txtvFullScore;

    private Button btnOk, btnCancle;

    private boolean CancleOK;

    public GoalPartDlg(Context context) {
        super(context);

        this.context = context;
    }

    public GoalPartDlg(Context context, int nPart, String strFullScore) {
        super(context);

        CancleOK = false;

        this.context = context;

        this.nPart = nPart;
        this.strPartName = "part" + (nPart + 1) ;
        this.strFullScore = strFullScore;

        nFullScore = Integer.parseInt(strFullScore);
        this.strFullScore = " / " + strFullScore;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.dialog_goalscore_part);

        Init();
    }

    public void Init(){
        txtvPartName = (TextView) findViewById(R.id.txtvGoalPartDlgPart);
        txtvFullScore = (TextView) findViewById(R.id.txtvGoalPartDlgFullscore);

        edtxScore = (EditText) findViewById(R.id.edtxGoalPartDlgScore);

        btnOk = (Button) findViewById(R.id.btnGoalPartDlgOk);
        btnCancle = (Button) findViewById(R.id.btnGoalPartDlgCancle);

        txtvPartName.setText(strPartName);
        txtvFullScore.setText(strFullScore);

        btnOk.setOnClickListener(GoalPartDlg.this);
        btnCancle.setOnClickListener(GoalPartDlg.this);
    }

    public String getScore(){
        return edtxScore.getText().toString();
    }

    public int getNpart(){
        return nPart;
    }

    public boolean getCancleOK(){
        return CancleOK;
    }

    @Override
    public void onClick(View v) {
        if(v.getId() == btnCancle.getId()){
            CancleOK = true;
            this.cancel();
        }
        else if(v.getId() == btnOk.getId()){
            //사용자가 아무것도 입력하지 않았을 것에 대해서 대비해야함 "" <- 요거
            String strScore = edtxScore.getText().toString();

            if(strScore.equals("")){
                Toast.makeText(context, "입력하신 점수를 확인해 주세요 ", Toast.LENGTH_SHORT).show();
            }
            else{
                int score = Integer.parseInt(strScore);

                if(score > nFullScore || score < 0){
                    Toast.makeText(context, "입력하신 점수를 확인해 주세요 ", Toast.LENGTH_SHORT).show();
                }
                else{
                    Toast.makeText(context, "점수를 입력하였습니다.", Toast.LENGTH_SHORT).show();
                    this.dismiss();
                }
            }
        }
    }
}
