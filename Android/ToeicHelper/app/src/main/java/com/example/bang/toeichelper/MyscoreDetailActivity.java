package com.example.bang.toeichelper;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.bang.toeichelper.customdlg.ColorProgressDlg;
import com.example.bang.toeichelper.mydata.MYSCORE;

import org.w3c.dom.Text;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Calendar;
import java.util.GregorianCalendar;

/**
 * Created by BANG on 2015-05-30.
 */
public class MyscoreDetailActivity extends ActionBarActivity implements View.OnClickListener, TextWatcher{

    private EditText[] edtxPart;
    private TextView txtvLCTotal, txtvRCTotal;
    private TextView txtvDate;

    private int[] nPartFullScore;
    private int Year, Month, Day;

    private int position;
    private MYSCORE myscore;
    private int[] part;
    private int nLCTotal, nRCTotal;
    private String strDate;

    private UpdateMyScore task_UpdateMyscore;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_myscore_detail);

        setActionBar();

        Intent intent = getIntent();
        position = intent.getExtras().getInt("position");
        myscore = (MYSCORE) intent.getExtras().getSerializable("myscore");
        strDate = myscore.getStrDate();

        String[] str = strDate.split("-");
        Year = Integer.parseInt(str[0]);
        Month = Integer.parseInt(str[1]);
        Day = Integer.parseInt(str[2]);

        Init();
    }

    public void setActionBar(){
        ActionBar actionBar = getSupportActionBar();
        //뒤로가기 버튼 만들기
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setBackgroundDrawable(getResources().getDrawable(R.drawable.actionbar_background_black));
        actionBar.setTitle("성적 자세히 보기");
    }

    //뒤로가기 버튼 만들기
    @Override
    public Intent getSupportParentActivityIntent() {
        this.finish();

        return super.getSupportParentActivityIntent();
    }

    public void Init(){

        txtvDate = (TextView) findViewById(R.id.txtvMyScoreDetailDate);
        txtvDate.setOnClickListener(MyscoreDetailActivity.this);

        txtvLCTotal = (TextView) findViewById(R.id.txtvMyScoreDetailLCtotal);
        txtvRCTotal = (TextView) findViewById(R.id.txtvMyScoreDetailRCtotal);

        txtvDate.setText(String.format("%d 년 %d 월 %d 일", Year, Month, Day));

        nLCTotal = myscore.getTotalLC();
        nRCTotal = myscore.getTotalRC();
        txtvLCTotal.setText(nLCTotal + " / 100");
        txtvRCTotal.setText(nRCTotal + " / 100");

        setEdtxPart();
    }

    public void setEdtxPart(){

        nPartFullScore = new int[7];
        nPartFullScore[0] = 10;
        nPartFullScore[1] = 30;
        nPartFullScore[2] = 30;
        nPartFullScore[3] = 30;
        nPartFullScore[4] = 40;
        nPartFullScore[5] = 12;
        nPartFullScore[6] = 48;

        edtxPart = new EditText[7];

        edtxPart[0] = (EditText) findViewById(R.id.edtxMyscoreDetailPart1);
        edtxPart[1] = (EditText) findViewById(R.id.edtxMyscoreDetailPart2);
        edtxPart[2] = (EditText) findViewById(R.id.edtxMyscoreDetailPart3);
        edtxPart[3] = (EditText) findViewById(R.id.edtxMyscoreDetailPart4);
        edtxPart[4] = (EditText) findViewById(R.id.edtxMyscoreDetailPart5);
        edtxPart[5] = (EditText) findViewById(R.id.edtxMyscoreDetailPart6);
        edtxPart[6] = (EditText) findViewById(R.id.edtxMyscoreDetailPart7);

        part = new int[edtxPart.length];
        part = myscore.getPart();

        for(int i=0;i<edtxPart.length;i++){
            edtxPart[i].setText(part[i] + "");
        }

        for(int i=0;i<edtxPart.length;i++){
            edtxPart[i].addTextChangedListener(MyscoreDetailActivity.this);
        }
    }

    @Override
    public void onClick(View v) {
        if(v.getId() == txtvDate.getId()){

            DatePickerDialog datePickerDialog = new DatePickerDialog(MyscoreDetailActivity.this, new DatePickerDialog.OnDateSetListener() {
                @Override
                public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                    strDate = String.format("%d-%02d-%02d", year, monthOfYear+1, dayOfMonth);
                    String strTxtDate = year + " 년 " + ( monthOfYear+1 ) + " 월 " + dayOfMonth + " 일";
                    txtvDate.setText(strTxtDate);

                    Year = year;
                    Month = monthOfYear + 1;
                    Day = dayOfMonth;
                }
            },
            Year, Month-1, Day);

            datePickerDialog.show();
        }
    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {
        nLCTotal = 0;
        nRCTotal = 0;

        for(int i=0;i<edtxPart.length;i++){
            if(edtxPart[i].getText().toString().equals("")){

            }
            else{
                int nScore = Integer.parseInt(edtxPart[i].getText().toString());
                //Log.w("edtxPart", "pos = " + i + " score = " + nScore);

                if(nScore < 0 || nScore > nPartFullScore[i]){
                    Toast.makeText(MyscoreDetailActivity.this, "점수를 범위에 맞게 입력해 주세요", Toast.LENGTH_SHORT).show();
                    edtxPart[i].setText("");
                    nScore = 0;
                }

                if(i< 4){
                    nLCTotal += nScore;
                }
                else{
                    nRCTotal += nScore;
                }
            }
        }

        txtvLCTotal.setText(nLCTotal + " / 100");
        txtvRCTotal.setText(nRCTotal + " / 100");
    }

    @Override
    public void afterTextChanged(Editable s) {

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_myscore_detail, menu);

        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        if(item.getItemId() == R.id.action_save){

            boolean ERR_FLAG = false;

            for(int i=0;i<edtxPart.length;i++){
                if(edtxPart[i].getText().toString().equals("")){
                    edtxPart[i].requestFocus();
                    ERR_FLAG = true;
                    break;
                }
            }

            if(ERR_FLAG){
                Toast.makeText(MyscoreDetailActivity.this, "점수 입력란을 확인해 주세요", Toast.LENGTH_SHORT).show();
            }
            else{
                int nPart[] = new int[7];
                for(int i=0;i<nPart.length;i++){
                    nPart[i] = Integer.parseInt(edtxPart[i].getText().toString());
                }

                task_UpdateMyscore = new UpdateMyScore(myscore.getPk(), nPart);
                task_UpdateMyscore.execute("");
            }
        }

        return super.onOptionsItemSelected(item);
    }



    public class UpdateMyScore extends AsyncTask<String, Integer, String> {

        //필요한 POST

        private String strURL = "http://kebin1104.dothome.co.kr/ToeicHelper/UpdateMyscore.php";

        private int MyScore_pk;
        private int[] part;

        private ColorProgressDlg progressDlg;

        public UpdateMyScore(int MyScore_pk, int[] part) {
            this.MyScore_pk = MyScore_pk;
            this.part = part;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            //Log.w("strDate", strDate);
            //Log.w("myscore_pk", myscore.getPk() + "");
            progressDlg = new ColorProgressDlg(MyscoreDetailActivity.this, "Loading...");
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

                    buffer.append("myscore_pk=").append(MyScore_pk + "&");
                    buffer.append("part1=").append(part[0] + "&");
                    buffer.append("part2=").append(part[1] + "&");
                    buffer.append("part3=").append(part[2] + "&");
                    buffer.append("part4=").append(part[3] + "&");
                    buffer.append("part5=").append(part[4] + "&");
                    buffer.append("part6=").append(part[5] + "&");
                    buffer.append("part7=").append(part[6] + "&");
                    buffer.append("date=").append(strDate);

                    OutputStreamWriter outStream = new OutputStreamWriter(httpURLConnection.getOutputStream(), "UTF-8");
                    PrintWriter writer = new PrintWriter(outStream);
                    writer.write(buffer.toString());
                    writer.flush();


                    //수신
                    InputStreamReader inputStreamReader = new InputStreamReader(httpURLConnection.getInputStream(), "UTF-8");
                    BufferedReader bufferedReader = new BufferedReader(inputStreamReader);

                    String str = bufferedReader.readLine();
                    //Log.w("bufferedReader", str);
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

            Toast.makeText(MyscoreDetailActivity.this, "저장 되었습니다.", Toast.LENGTH_SHORT).show();
        }
    }
}
