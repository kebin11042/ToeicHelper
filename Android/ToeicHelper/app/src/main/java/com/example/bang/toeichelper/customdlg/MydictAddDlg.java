package com.example.bang.toeichelper.customdlg;

import android.app.Dialog;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.bang.toeichelper.R;
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
public class MydictAddDlg extends Dialog implements View.OnClickListener{

    private Context context;

    private Button btnAdd, btnCancle;
    private EditText edtxDictName;

    private int member_pk;
    private String strDictName;

    private boolean CANCLE_FLAG;

    private AddMyDict task_AddMyDict;
    private MYDICT mydict;

    public MydictAddDlg(Context context, int member_pk) {
        super(context);

        this.context = context;
        this.member_pk = member_pk;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.dialog_mydict_adddict);

        Init();
    }

    public void Init(){
        CANCLE_FLAG = false;

        strDictName = "";

        btnAdd = (Button) findViewById(R.id.btnMydictAddDlgAdd);
        btnCancle = (Button) findViewById(R.id.btnMydictAddDlgCancle);

        edtxDictName = (EditText) findViewById(R.id.edtxMyDictAddDlgName);

        btnAdd.setOnClickListener(MydictAddDlg.this);
        btnCancle.setOnClickListener(MydictAddDlg.this);
    }

    public boolean getCANCLE_FLAG(){ return CANCLE_FLAG; }

    public MYDICT getMyDict(){ return mydict; }

    @Override
    public void onBackPressed() {
        super.onBackPressed();

        CANCLE_FLAG = true;
    }

    @Override
    public void onClick(View v) {
        if(v.getId() == btnCancle.getId()){
            CANCLE_FLAG = true;
            this.cancel();
        }
        else if(v.getId() == btnAdd.getId()){
            strDictName = edtxDictName.getText().toString();

            if(strDictName.equals("")){
                Toast.makeText(context, "단어장 이름을 설정해 주세요!", Toast.LENGTH_SHORT).show();
            }
            else{
                task_AddMyDict = new AddMyDict(this, member_pk+"", strDictName);
                task_AddMyDict.execute("");
            }
        }
    }

    public class AddMyDict extends AsyncTask<String, Integer, String> {

        private Dialog dialog;

        private String strURL = "http://kebin1104.dothome.co.kr/ToeicHelper/AddDict.php";

        private String member_pk;
        private String strDictName;
        private String strDate;
        private String strDictPk;

        private ColorProgressDlg progressDlg;

        public AddMyDict(Dialog dialog, String member_pk, String strDictName) {

            this.dialog = dialog;

            this.member_pk = member_pk;
            this.strDictName = strDictName;
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

                    buffer.append("member_pk=").append(member_pk + "&");
                    buffer.append("dict_name=").append(strDictName);

                    OutputStreamWriter outStream = new OutputStreamWriter(httpURLConnection.getOutputStream(), "UTF-8");
                    PrintWriter writer = new PrintWriter(outStream);
                    writer.write(buffer.toString());
                    writer.flush();


                    //수신
                    InputStreamReader inputStreamReader = new InputStreamReader(httpURLConnection.getInputStream(), "UTF-8");
                    BufferedReader bufferedReader = new BufferedReader(inputStreamReader);

                    strDate = bufferedReader.readLine();
                    strDictPk = bufferedReader.readLine();
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

            mydict = new MYDICT(Integer.parseInt(strDictPk), strDictName, strDate);

            ((MydictAddDlg)dialog).dismiss();

            Toast.makeText(context, "단어장을 만들었습니다.", Toast.LENGTH_SHORT).show();
        }
    }
}
