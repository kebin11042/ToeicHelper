package com.example.bang.toeichelper;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.bang.toeichelper.customdlg.ColorProgressDlg;
import com.example.bang.toeichelper.mydata.MYSCORE;

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
public class MyscoreAddActivity extends ActionBarActivity implements TextWatcher, View.OnClickListener{

    private String member_pk;

    private EditText[] edtxPart;
    private int[] nPartFullScore;

    private TextView txtvLCTotal, txtvRCTotal;
    private int nLCTotal, nRCTotal;

    private TextView txtvDate;
    private String strDate;

    private InsertMyScore task_InsertMyScore;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_myscore_add);

        Intent intent = getIntent();
        member_pk = intent.getExtras().getString("member_pk");

        setActionBar();

        Init();
    }

    public void Init(){
        setEdtxPart();

        txtvLCTotal = (TextView) findViewById(R.id.txtvMyScoreAddLCtotal);
        txtvRCTotal = (TextView) findViewById(R.id.txtvMyScoreAddRCtotal);

        txtvDate = (TextView) findViewById(R.id.txtvMyScoreAddDate);
        txtvDate.setOnClickListener(MyscoreAddActivity.this);

        GregorianCalendar calendar = new GregorianCalendar();
        int Year = calendar.get(Calendar.YEAR);
        int Month = calendar.get(Calendar.MONTH);
        int Day = calendar.get(Calendar.DAY_OF_MONTH);
        strDate = String.format("%d-%02d-%02d", Year, Month + 1, Day);

        txtvDate.setText(Year + " 년 " + (Month+1) + " 월 " + Day + " 일");
        //Log.w("strDate", strDate);
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
        edtxPart[0] = (EditText) findViewById(R.id.edtxMyscoreAddPart1);
        edtxPart[1] = (EditText) findViewById(R.id.edtxMyscoreAddPart2);
        edtxPart[2] = (EditText) findViewById(R.id.edtxMyscoreAddPart3);
        edtxPart[3] = (EditText) findViewById(R.id.edtxMyscoreAddPart4);
        edtxPart[4] = (EditText) findViewById(R.id.edtxMyscoreAddPart5);
        edtxPart[5] = (EditText) findViewById(R.id.edtxMyscoreAddPart6);
        edtxPart[6] = (EditText) findViewById(R.id.edtxMyscoreAddPart7);

        for(int i=0;i<edtxPart.length;i++){
            edtxPart[i].addTextChangedListener(MyscoreAddActivity.this);
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
                    Toast.makeText(MyscoreAddActivity.this, "점수를 범위에 맞게 입력해 주세요", Toast.LENGTH_SHORT).show();
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
    public void onClick(View v) {
        if(v.getId() == txtvDate.getId()){
            GregorianCalendar calendar = new GregorianCalendar();
            int Year = calendar.get(Calendar.YEAR);
            int Month = calendar.get(Calendar.MONTH);
            int Day = calendar.get(Calendar.DAY_OF_MONTH);

            DatePickerDialog datePickerDialog = new DatePickerDialog(MyscoreAddActivity.this, new DatePickerDialog.OnDateSetListener() {
                @Override
                public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                    strDate = String.format("%d-%02d-%02d", year, monthOfYear+1, dayOfMonth);
                    String strTxtDate = year + " 년 " + (monthOfYear+1) + " 월 " + dayOfMonth + " 일";
                    txtvDate.setText(strTxtDate);
                }
            },
            Year, Month, Day);

            datePickerDialog.show();
        }
    }



    public void setActionBar(){
        ActionBar actionBar = getSupportActionBar();
        //뒤로가기 버튼 만들기
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setBackgroundDrawable(getResources().getDrawable(R.drawable.actionbar_background_black));
        actionBar.setTitle("성적 추가");
    }

    //뒤로가기 버튼 만들기
    @Override
    public Intent getSupportParentActivityIntent() {
        this.finish();

        return super.getSupportParentActivityIntent();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_myscore_add, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        if(item.getItemId() == R.id.action_ok){
            boolean ERR_FLAG = false;

            for(int i=0;i<edtxPart.length;i++){
                if(edtxPart[i].getText().toString().equals("")){
                    edtxPart[i].requestFocus();
                    ERR_FLAG = true;
                    break;
                }
            }

            if(ERR_FLAG){
                Toast.makeText(MyscoreAddActivity.this, "점수 입력란을 확인해 주세요", Toast.LENGTH_SHORT).show();
            }
            else{
                int nPart[] = new int[7];
                for(int i=0;i<nPart.length;i++){
                    nPart[i] = Integer.parseInt(edtxPart[i].getText().toString());
                }

                task_InsertMyScore = new InsertMyScore(nPart);
                task_InsertMyScore.execute("");
            }
        }

        return super.onOptionsItemSelected(item);
    }


    public class InsertMyScore extends AsyncTask<String, Integer, String> {

        //필요한 POST

        private String strURL = "http://kebin1104.dothome.co.kr/ToeicHelper/InsertMyscore.php";

        private int MyScore_pk;
        private int[] part;

        private ColorProgressDlg progressDlg;

        public InsertMyScore(int[] part) {
            this.part = part;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            progressDlg = new ColorProgressDlg(MyscoreAddActivity.this, "Loading...");
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

                    MyScore_pk = Integer.parseInt(bufferedReader.readLine());
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

            MYSCORE myscore = new MYSCORE(MyScore_pk, part, strDate);
            Intent intent = new Intent();
            intent.putExtra("myscore", myscore);
            MyscoreAddActivity.this.setResult(1, intent);

            finish();
        }
    }
}
