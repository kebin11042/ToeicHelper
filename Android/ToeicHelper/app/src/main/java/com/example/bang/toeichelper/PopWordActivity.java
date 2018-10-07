package com.example.bang.toeichelper;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.example.bang.toeichelper.customdlg.ColorProgressDlg;
import com.example.bang.toeichelper.mydata.MYWORD;
import com.example.bang.toeichelper.mydata.POPWORD;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;

/**
 * Created by BANG on 2015-06-18.
 */
public class PopWordActivity extends ActionBarActivity{

    private ListView listvPopWord;
    private PopWordAdapter popwordAdapter;

    private ArrayList<POPWORD> arrPopWord;

    private ImportPopWord task_ImportPopWord;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_popword);

        setActionBar();

        Init();
    }

    public void Init(){
        listvPopWord = (ListView) findViewById(R.id.listvPopWord);

        task_ImportPopWord = new ImportPopWord();
        task_ImportPopWord.execute();
    }

    public void setActionBar(){
        ActionBar actionBar = getSupportActionBar();
        getSupportActionBar().setElevation(0);  // 요거 그림자 없애기
        //뒤로가기 버튼 만들기
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setBackgroundDrawable(getResources().getDrawable(R.drawable.actionbar_background_black));
        actionBar.setTitle("사용자 등록 단어 순위");
    }

    @Override
    public Intent getSupportParentActivityIntent() {
        finish();

        return super.getSupportParentActivityIntent();
    }

    public class PopWordAdapter extends BaseAdapter {

        private Context context;
        private int Count;

        public PopWordAdapter(Context context, int Count) {
            this.context = context;
            this.Count = Count;
        }

        @Override
        public int getCount() {
            return this.Count;
        }

        @Override
        public Object getItem(int position) {
            return null;
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {

            LayoutInflater inflater = (LayoutInflater)context.getSystemService(LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.listv_popword, parent, false);

            TextView txtvPos = (TextView) convertView.findViewById(R.id.txtvPopWordPosition);
            TextView txtvWord = (TextView) convertView.findViewById(R.id.txtvPopWordWord);
            TextView txtvCnt = (TextView) convertView.findViewById(R.id.txtvPopWordCnt);

            txtvPos.setText(position+1 + ".");
            txtvWord.setText(arrPopWord.get(position).getStrWord());
            txtvCnt.setText(arrPopWord.get(position).getnCnt() + " 명");

            return convertView;
        }
    }


    public class ImportPopWord extends AsyncTask<String, Integer, String> {

        //필요한 POST

        private String strURL = "http://kebin1104.dothome.co.kr/ToeicHelper/ImportPopWord.php";

        private ColorProgressDlg progressDlg;

        private int nWordSize;

        public ImportPopWord() {
            arrPopWord = new ArrayList<POPWORD>();
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            progressDlg = new ColorProgressDlg(PopWordActivity.this, "Loading...");
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

                    OutputStreamWriter outStream = new OutputStreamWriter(httpURLConnection.getOutputStream(), "UTF-8");
                    PrintWriter writer = new PrintWriter(outStream);
                    writer.write(buffer.toString());
                    writer.flush();


                    //수신
                    InputStreamReader inputStreamReader = new InputStreamReader(httpURLConnection.getInputStream(), "UTF-8");
                    BufferedReader bufferedReader = new BufferedReader(inputStreamReader);

                    nWordSize = Integer.parseInt(bufferedReader.readLine());
                    for(int i=0;i<nWordSize;i++){
                        String strWord = bufferedReader.readLine();
                        int reg = Integer.parseInt(bufferedReader.readLine());

                        POPWORD d = new POPWORD(strWord, reg);

                        arrPopWord.add(d);
                    }
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

            popwordAdapter = new PopWordAdapter(PopWordActivity.this, arrPopWord.size());
            listvPopWord.setAdapter(popwordAdapter);
        }
    }
}
