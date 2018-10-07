package com.example.bang.toeichelper;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import com.example.bang.toeichelper.customdlg.ColorProgressDlg;
import com.example.bang.toeichelper.customdlg.GoalPartDlg;
import com.example.bang.toeichelper.mydata.MEMBER_DATA;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

/**
 * Created by BANG on 2015-05-19.
 */
public class GoalscoreActivity extends ActionBarActivity implements View.OnClickListener, DialogInterface.OnDismissListener {

    private static final int RESULT_CODE_CHANGE_OK = 5;
    private static final int RESULT_CODE_CHANGE_NO = 6;

    private MEMBER_DATA member_data;

    private TextView[] txtvPart;
    private int partFullScore[];

    private TextView txtvLCtotal, txtvRCtotal;

    private TextView txtvLC, txtvRC, txtvTotal;

    private ImageButton ibtnPart[];


    private UpdateGoal task_updateGoal;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Intent intent = getIntent();
        member_data = (MEMBER_DATA) intent.getExtras().getSerializable("member_data");

        this.setResult(RESULT_CODE_CHANGE_NO);

        setContentView(R.layout.activity_goalscore);

        setActionBar();

        Init();
    }

    public void setActionBar(){
        ActionBar actionBar = getSupportActionBar();
        //뒤로가기 버튼 만들기
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setBackgroundDrawable(getResources().getDrawable(R.drawable.actionbar_background_black));
        actionBar.setTitle("목표 점수");
    }

    //part별 만점 설정
    public void setFullScore(){
        partFullScore = new int[7];

        partFullScore[0] = 10;
        partFullScore[1] = 30;
        partFullScore[2] = 30;
        partFullScore[3] = 30;
        partFullScore[4] = 40;
        partFullScore[5] = 12;
        partFullScore[6] = 48;
    }

    public void setTxtvPart(){
        //파트별 점수 txtv 설정
        txtvPart = new TextView[7];

        txtvPart[0] = (TextView) findViewById(R.id.txtvGoalPart1);
        txtvPart[1] = (TextView) findViewById(R.id.txtvGoalPart2);
        txtvPart[2] = (TextView) findViewById(R.id.txtvGoalPart3);
        txtvPart[3] = (TextView) findViewById(R.id.txtvGoalPart4);
        txtvPart[4] = (TextView) findViewById(R.id.txtvGoalPart5);
        txtvPart[5] = (TextView) findViewById(R.id.txtvGoalPart6);
        txtvPart[6] = (TextView) findViewById(R.id.txtvGoalPart7);

        for(int i=0;i<txtvPart.length;i++){
            String strScore = member_data.getGoal_data().getPartbyIndex(i) + " / " + partFullScore[i];
            txtvPart[i].setText(strScore);
        }
    }

    public void setLCRCTotal(){
        //LCtotal, RCtotal
        txtvLCtotal = (TextView) findViewById(R.id.txtvGoalLCtotal);
        txtvRCtotal = (TextView) findViewById(R.id.txtvGoalRCtotal);

        txtvLCtotal.setText(member_data.getGoal_data().getstrLCtotal());
        txtvRCtotal.setText(member_data.getGoal_data().getstrRCtotal());

        //LC, RC, Total
        txtvLC = (TextView) findViewById(R.id.txtvGoalLCscore);
        txtvRC = (TextView) findViewById(R.id.txtvGoalRCscore);
        txtvTotal = (TextView) findViewById(R.id.txtvGoalTotalscore);

        txtvLC.setText(member_data.getGoal_data().getstrLCScore());
        txtvRC.setText(member_data.getGoal_data().getstrRCScore());
        txtvTotal.setText(member_data.getGoal_data().getstrTotalScore() + " 점");
    }

    public void setIbtnPart(){
        ibtnPart = new ImageButton[7];

        ibtnPart[0] = (ImageButton) findViewById(R.id.ibtnGoalPart1);
        ibtnPart[1] = (ImageButton) findViewById(R.id.ibtnGoalPart2);
        ibtnPart[2] = (ImageButton) findViewById(R.id.ibtnGoalPart3);
        ibtnPart[3] = (ImageButton) findViewById(R.id.ibtnGoalPart4);
        ibtnPart[4] = (ImageButton) findViewById(R.id.ibtnGoalPart5);
        ibtnPart[5] = (ImageButton) findViewById(R.id.ibtnGoalPart6);
        ibtnPart[6] = (ImageButton) findViewById(R.id.ibtnGoalPart7);

        for(int i=0;i<ibtnPart.length;i++){
            ibtnPart[i].setOnClickListener(GoalscoreActivity.this);
        }
    }

    public void Init(){

        setFullScore();

        setTxtvPart();

        setLCRCTotal();

        setIbtnPart();
    }

    //액션바에 있는 뒤로가기 버튼을 눌렀을 경우 작동
    @Override
    public Intent getSupportParentActivityIntent() {
        this.finish();

        return super.getSupportParentActivityIntent();
    }


    //클릭 리스너

    @Override
    public void onClick(View v) {
        for(int i=0;i<ibtnPart.length;i++){
            if(v.getId() == ibtnPart[i].getId()){
                String strFullScore = partFullScore[i] + "";

                GoalPartDlg dialog = new GoalPartDlg(GoalscoreActivity.this, i, strFullScore);
                dialog.setOnDismissListener(GoalscoreActivity.this);

                dialog.show();
            }
        }
    }


    //dismiss 리스너
    @Override
    public void onDismiss(DialogInterface dialog) {

        GoalPartDlg partDialog = (GoalPartDlg) dialog;

        if(partDialog.getCancleOK()){

        }
        else{
            //점수 변화가 있을경우 LoginedActivity에 바뀐 점수값을 보내줘야 하기 때문에

            String strScore = partDialog.getScore();
            int nPart = partDialog.getNpart();
            int nScore = Integer.parseInt(strScore);

            txtvPart[nPart].setText(strScore + " / " + partFullScore[nPart]);
            member_data.getGoal_data().setScorebyIndex(nPart, nScore);

            setTxtvPart();
            setLCRCTotal();

            task_updateGoal = new UpdateGoal(member_data.getPk(), nPart, nScore);
            task_updateGoal.execute("");

            Log.w("task_update", "member_pk = " + member_data.getPk() + ", nPart = " + nPart + ", nScore = " + nScore);

            Intent intent = new Intent();
            intent.putExtra("member_data", member_data);
            this.setResult(RESULT_CODE_CHANGE_OK, intent);
        }
    }

    public class UpdateGoal extends AsyncTask<String, Integer, String> {

        //필요한 POST
        //member_pk, part_no, part_score

        private String strURL = "http://kebin1104.dothome.co.kr/ToeicHelper/UpdateGoal.php";

        private String member_pk;
        private int part_no, part_score;

        private ColorProgressDlg progressDlg;

        public UpdateGoal(String member_pk, int part_no, int part_score) {

            this.member_pk = member_pk;
            this.part_no = part_no + 1;
            this.part_score = part_score;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            progressDlg = new ColorProgressDlg(GoalscoreActivity.this, "Loading...");
            progressDlg.setCancelable(false);
            progressDlg.show();
        }

        @Override
        protected String doInBackground(String... params) {
            String s = "";

            try {
                URL url = new URL(strURL);

                HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();

                if(httpURLConnection != null){
                    httpURLConnection.setDefaultUseCaches(false);
                    httpURLConnection.setDoInput(true);
                    httpURLConnection.setDoOutput(true);
                    httpURLConnection.setRequestMethod("POST");

                    //xml파일로 요청
                    httpURLConnection.setRequestProperty("Accept", "application/xml");


                    //--------------------------
                    //  서버로 값 전송
                    //--------------------------

                    StringBuffer buffer = new StringBuffer();

                    buffer.append("member_pk=").append(member_pk + "&");
                    buffer.append("goal_part_no=").append(part_no + "&");
                    buffer.append("goal_part_score=").append(part_score);

                    OutputStreamWriter outStream = new OutputStreamWriter(httpURLConnection.getOutputStream(), "UTF-8");
                    PrintWriter writer = new PrintWriter(outStream);
                    writer.write(buffer.toString());
                    writer.flush();


                    //수신
                    InputStreamReader inputStreamReader = new InputStreamReader(httpURLConnection.getInputStream(), "UTF-8");
                    BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
                }
                else{
                    s = "Not connected";
                }

            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }

            return s;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);

            progressDlg.dismiss();

            Log.w("s", s);
        }
    }
}
