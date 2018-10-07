package com.example.bang.toeichelper.customdlg;

import android.app.Dialog;
import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.bang.toeichelper.R;
import com.example.bang.toeichelper.RSA;
import com.example.bang.toeichelper.secret.SHA_1;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.plus.Plus;
import com.google.android.gms.plus.model.people.Person;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Random;

/**
 * Created by BANG on 2015-02-14.
 */
public class GooglejoinDlg extends Dialog implements View.OnClickListener{

    private Context context;

    private TextView txtvEmail;
    private TextView txtvName;

    private String strEmail;
    private String strName;
    private String strPW;

    private EditText edtxPW;

    private Button btnJoinCancle;
    private Button btnJoinOK;

    private GoogleApiClient googleApiClient;

    private JoinMemberGoogle joinMemberGoogle;

    public GooglejoinDlg(Context context) {
        super(context);

        this.context = context;

        //타이틀 없애기
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.dialog_join_google);

        Init();
    }

    public void Init(){
        txtvEmail = (TextView) findViewById(R.id.txtvGoogleJoinEmail);
        txtvName = (TextView) findViewById(R.id.txtvGoogleJoinName);

        edtxPW = (EditText) findViewById(R.id.edtxGoogleJoinPW);

        btnJoinCancle = (Button) findViewById(R.id.btnGoogleJoinCancle);
        btnJoinOK = (Button) findViewById(R.id.btnGoogleJoinOK);
        btnJoinCancle.setOnClickListener(GooglejoinDlg.this);
        btnJoinOK.setOnClickListener(GooglejoinDlg.this);
    }

    public void setTextView(String strEmail, String strName){
        txtvEmail.setText(strEmail);
        txtvName.setText(strName);
    }

    public void setGoogleApiClient(GoogleApiClient googleApiClient){
        this.googleApiClient = googleApiClient;

        if(Plus.PeopleApi.getCurrentPerson(this.googleApiClient) != null){
            Person currentPerson = Plus.PeopleApi.getCurrentPerson(this.googleApiClient);

            strEmail = Plus.AccountApi.getAccountName(googleApiClient);
            strName = currentPerson.getDisplayName();

            this.setTextView(Plus.AccountApi.getAccountName(googleApiClient), currentPerson.getDisplayName());

            //strName = currentPerson.getDisplayName();
            //String personPhotoUrl = currentPerson.getImage().getUrl();
            //String personGooglePlusProfile = currentPerson.getUrl();
            //strEmail = Plus.AccountApi.getAccountName(mGoogleApiClient);
            //Log.w("Google", "Email : " + personEmail);
        }
    }

    @Override
    public void onClick(View v) {

        if(v.getId() == btnJoinCancle.getId()){
            this.dismiss();
        }
        else if(v.getId() == btnJoinOK.getId()){
            strPW = edtxPW.getText().toString();

            if(strPW.length() >= 8){
                //정규식으로 한글 검사
                if( strPW.matches(".*[ㄱ-ㅎㅏ-ㅣ가-힣]+.*") ){
                    Toast.makeText(context, "비밀번호에 한글을 포함할 수 없습니다.", Toast.LENGTH_SHORT).show();
                }
                else{
                    joinMemberGoogle = new JoinMemberGoogle();
                    joinMemberGoogle.execute("");
                }

            }
            else{
                Toast.makeText(context, "비밀번호가 최소 8자리 이상이여야 합니다.", Toast.LENGTH_SHORT).show();
            }
        }
    }

    @Override
    public void dismiss() {
        super.dismiss();

        if(googleApiClient != null){
            Plus.AccountApi.clearDefaultAccount(googleApiClient);
            googleApiClient.disconnect();
        }
    }

    public class JoinMemberGoogle extends AsyncTask <String, Integer, String>{

        private String strURL = "http://kebin1104.dothome.co.kr/ToeicHelper/JoinMember.php";

        private String ePW;

        public JoinMemberGoogle() {
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            SHA_1 sha_1 = new SHA_1();
            ePW = sha_1.encryptPassWord(strPW);
        }

        @Override
        protected String doInBackground(String... params) {
            String strResponse = "";

            try {
                //URL 설정
                URL url = new URL(strURL);

                //접속
                HttpURLConnection http = (HttpURLConnection) url.openConnection();

                if(http != null){	//연결이 제대로 되었다면

                    //전송 모드 설정
                    http.setDefaultUseCaches(false);
                    http.setDoInput(true);
                    http.setDoOutput(true);
                    http.setRequestMethod("POST");	//전송방식을 POST

                    //서버에게 웹에서 <Form>으로 값이 넘어온 것과 같은 방식으로 처리하라는 걸 알려준다
                    http.setRequestProperty("Accept", "application/xml");

                    //--------------------------
                    //  서버로 값 전송
                    //--------------------------

                    StringBuffer buffer = new StringBuffer();

                    //buffer.append("RSA_index=").append(rsaIndex + "&");
                    buffer.append("member_Email=").append(strEmail + "&");
                    buffer.append("member_Name=").append(strName + "&");
                    buffer.append("member_PW=").append(ePW);

                    OutputStreamWriter outStream = new OutputStreamWriter(http.getOutputStream(), "UTF-8");
                    PrintWriter writer = new PrintWriter(outStream);
                    writer.write(buffer.toString());
                    writer.flush();

                    //--------------------------
                    //  서버에서 값 전송 받기
                    //--------------------------

                    InputStreamReader tmp = new InputStreamReader(http.getInputStream(), "UTF-8");
                    BufferedReader br = new BufferedReader(tmp);

                    strResponse = br.readLine();

                    br.close();
                }

                http.disconnect();

            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }

            return strResponse;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);

            if(s.equals("overlap")){
                Toast.makeText(context, "Email 중복입니다.", Toast.LENGTH_SHORT).show();
            }
            else{
                Toast.makeText(context, "회원가입 완료!! 로그인 해주세요", Toast.LENGTH_SHORT).show();
                dismiss();
            }
        }
    }
}
