package com.example.bang.toeichelper.customdlg;

import android.app.Dialog;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.bang.toeichelper.R;
import com.example.bang.toeichelper.mydata.MEMBER_DATA;
import com.example.bang.toeichelper.mydata.MYDICT;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

/**
 * Created by BANG on 2015-05-26.
 */
public class MydictDeleteDlg extends Dialog implements View.OnClickListener{

    private Context context;

    private int dictPk;
    private String strDictName;
    private String strTextDictName;

    private TextView txtvDictName;

    private Button btnDelete, btnCancle;

    private boolean CANCLE_FLAG;

    private DeleteMyDict task_DeleteMyDict;

    public MydictDeleteDlg(Context context, int dictPk, String dictName) {
        super(context);

        this.context = context;

        this.dictPk = dictPk;
        this.strDictName = dictName;
        this.strTextDictName = dictName + "\n 단어장을 지우시겠습니까?";

        CANCLE_FLAG = false;
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();

        CANCLE_FLAG = true;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.dialog_mydict_deletedict);

        Init();
    }

    public void Init(){
        txtvDictName = (TextView) findViewById(R.id.txtvMydictDeleteDlgName);

        btnDelete = (Button) findViewById(R.id.btnMydictDeleteDlgDelete);
        btnCancle = (Button) findViewById(R.id.btnMydictDeleteDlgCancle);

        txtvDictName.setText(strTextDictName);

        btnDelete.setOnClickListener(MydictDeleteDlg.this);
        btnCancle.setOnClickListener(MydictDeleteDlg.this);
    }

    public boolean getCANCLE_FLAG(){ return CANCLE_FLAG; }

    @Override
    public void onClick(View v) {
        if(v.getId() == btnCancle.getId()){
            CANCLE_FLAG = true;
            this.cancel();
        }
        else if(v.getId() == btnDelete.getId()){

            task_DeleteMyDict = new DeleteMyDict(this, dictPk);
            task_DeleteMyDict.execute("");
        }
    }

    public class DeleteMyDict extends AsyncTask<String, Integer, String> {

        private Dialog dialog;

        private String strURL = "http://kebin1104.dothome.co.kr/ToeicHelper/DeleteDict.php";

        private int dictPk;

        private ColorProgressDlg progressDlg;

        public DeleteMyDict(Dialog dialog, int dictPk) {

            this.dialog = dialog;

            this.dictPk = dictPk;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            progressDlg = new ColorProgressDlg(context, "Loading...");
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

                    buffer.append("dict_pk=").append(dictPk+"");

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

            Toast.makeText(context, strDictName + " 단어장이 삭제되었습니다", Toast.LENGTH_SHORT).show();
            dialog.dismiss();
        }
    }
}
