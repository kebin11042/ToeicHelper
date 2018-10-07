package com.example.bang.toeichelper.myfragment;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.bang.toeichelper.LoginedActivity;
import com.example.bang.toeichelper.R;
import com.example.bang.toeichelper.customdlg.ColorProgressDlg;
import com.example.bang.toeichelper.mydata.GOAL_DATA;
import com.example.bang.toeichelper.mydata.MEMBER_DATA;
import com.example.bang.toeichelper.mydata.MYDICT;
import com.example.bang.toeichelper.mydata.MYSCORE;
import com.example.bang.toeichelper.mydata.MyDict_Handler;
import com.example.bang.toeichelper.mydata.MyScore_Handler;
import com.example.bang.toeichelper.secret.SHA_1;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

/**
 * Created by BANG on 2015-01-10.
 */
public class LoginFragment extends Fragment implements View.OnClickListener{

    private EditText edtxID;
    private EditText edtxPW;

    private Button btnJoin;
    private Button btnLogin;

    private String strEmail;
    private String strPW;

    private static SharedPreferences preferences;
    private static SharedPreferences.Editor editor;

    //목표점수
    private int part1, part2, part3, part4, part5, part6, part7;


    public static LoginFragment create(){
        LoginFragment fragment = new LoginFragment();

        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        //자동 로그인 검사
        preferences = getActivity().getSharedPreferences("pref", getActivity().MODE_PRIVATE);
        editor = preferences.edit();
        String preID = preferences.getString("ID", "NOT");
        String prePW = preferences.getString("PW", "NOT");

        Log.w("preID", " = " + preID);
        if(!preID.equals("NOT")){
//            Intent intent = new Intent(MainActivity.this, LoginedActivity.class);
//            startActivity(intent);
//            Log.w("Auto Logined", "OK");
            strEmail = preID;
            strPW = prePW;
            RequestLogin requestLogin = new RequestLogin();
            requestLogin.execute("");
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        ViewGroup rootView = (ViewGroup) inflater.inflate(R.layout.fragment_login, container, false);

        edtxID = (EditText) rootView.findViewById(R.id.edtxLoginID);
        edtxPW = (EditText) rootView.findViewById(R.id.edtxLoginPW);

        btnJoin = (Button) rootView.findViewById(R.id.btnLoginJoin);
        btnLogin = (Button) rootView.findViewById(R.id.btnLoginLogin);

        btnJoin.setOnClickListener(LoginFragment.this);
        btnLogin.setOnClickListener(LoginFragment.this);

        return rootView;
    }

    @Override
    public void onClick(View v) {
        if(v.getId() == btnJoin.getId()){
            ViewPager viewPager = (ViewPager) getActivity().findViewById(R.id.vpagerMain);
            viewPager.setCurrentItem(1);
        }
        else if(v.getId() == btnLogin.getId()){
            strEmail = edtxID.getText().toString();
            strPW = edtxPW.getText().toString();

            //빈칸 검사 && 띄어쓰기 검사
            if( IsEmpty(strEmail) || strSpaceCheck(strEmail)
                    || IsEmpty(strPW) || strSpaceCheck(strPW) ){
                Toast.makeText(getActivity(), "입력란을 비우거나 공백을 포함할 수 없습니다.", Toast.LENGTH_SHORT).show();

            }
            else{
                //LoginCheck
                RequestLogin requestLogin = new RequestLogin();
                requestLogin.execute("");
            }
        }
    }

    public boolean IsEmpty(String str){
        return str.equals("") || ( str.length() == 0 ) ;
    }

    public boolean strSpaceCheck(String str){
        for(int i=0;i<str.length();i++){
            if(str.charAt(i) == ' '){
                return true;
            }
        }
        return false;
    }

    public EditText getEdtxID() {
        return edtxID;
    }

    public EditText getEdtxPW() {
        return edtxPW;
    }

    public class RequestLogin extends AsyncTask<String, Integer, String> {

        private String strURL = "http://kebin1104.dothome.co.kr/ToeicHelper/LoginMember.php";

        private String ePW;

        private String strPk;
        private String strName;

        //목표점수 데이터
        private GOAL_DATA goal_data;

        //myscore 데잍어
        private int MyScoreSize = 0;
        private ArrayList<MYSCORE> arrMyScore;

        //사전 데이터
        private int DictSize = 0;
        private MyDict_Handler myDict_handler;
        private ArrayList<MYDICT> arrMyDict;

        private ColorProgressDlg dlg;

        public RequestLogin() {

        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            dlg = new ColorProgressDlg(getActivity(), "로그인 중...");
            dlg.setCancelable(false);
            dlg.show();

            SHA_1 sha_1 = new SHA_1();

            ePW = sha_1.encryptPassWord(strPW);
            goal_data = null;
            //초기화
            arrMyScore = new ArrayList<MYSCORE>();
            arrMyDict = new ArrayList<MYDICT>();
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

                    buffer.append("member_Email=").append(strEmail + "&");
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
                    if(strResponse.equals("success")){
                        strPk = br.readLine();
                        strName = br.readLine();

                        part1 = Integer.parseInt(br.readLine());
                        part2 = Integer.parseInt(br.readLine());
                        part3 = Integer.parseInt(br.readLine());
                        part4 = Integer.parseInt(br.readLine());
                        part5 = Integer.parseInt(br.readLine());
                        part6 = Integer.parseInt(br.readLine());
                        part7 = Integer.parseInt(br.readLine());

                        goal_data = new GOAL_DATA(part1, part2, part3, part4, part5, part6, part7);

                        MyScoreSize = Integer.parseInt(br.readLine());
                        //Log.w("MySocreSize", "MyScoreSize = " + MyScoreSize);
                        for(int i=0;i<MyScoreSize;i++){
                            int MyScorePk = Integer.parseInt(br.readLine());
                            int[] MyScorePart = new int[7];
                            for(int j=0;j<MyScorePart.length;j++){
                                MyScorePart[j] = Integer.parseInt(br.readLine());
                                //Log.w("MyScorePart", "MyScorePart"+j+" = " + MyScorePart[j]);
                            }
                            String MyScoreDate = br.readLine();

                            MYSCORE myscore = new MYSCORE(MyScorePk, MyScorePart, MyScoreDate);
                            arrMyScore.add(i, myscore);
                        }

                        DictSize = Integer.parseInt(br.readLine());
                        for(int i=0;i<DictSize;i++){
                            int DictPk = Integer.parseInt(br.readLine());
                            String DictName = br.readLine();
                            String DictDate = br.readLine();
                            //Log.w("DictInfo", "pk = " + DictPk + ", Name = " + DictName + ", Date = " + DictDate);
                            MYDICT mydict = new MYDICT(DictPk, DictName, DictDate);
                            arrMyDict.add(i, mydict);
                        }
                    }

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

            if(s.equals("email fail")){
                Toast.makeText(getActivity(), "이메일 주소를 다시 확인해 주세요!!", Toast.LENGTH_SHORT).show();
            }
            else if(s.equals("pw fail")){
                Toast.makeText(getActivity(), "비밀번호가 일치하지 않습니다!!", Toast.LENGTH_SHORT).show();
            }
            else{

                Toast.makeText(getActivity(), strName + " 님 반갑습니다.", Toast.LENGTH_SHORT).show();
                //Toast.makeText(getActivity(), "part1 = " + part1 + ", part2 = " + part2 + ", part3 = " + part3, Toast.LENGTH_SHORT).show();

                //자동 로그인 등록
                editor.putString("ID", strEmail);
                editor.putString("PW", strPW);
                editor.apply();
                //Log.w("editor", strEmail + strPW);

                //로그인에 성공한 member_data임
                MEMBER_DATA member_data = new MEMBER_DATA(strPk, strEmail, strName, ePW, goal_data);

                //myScore 데이터 멤버데이터에 추가
                if(MyScoreSize != 0){
                    member_data.myScore_handler = new MyScore_Handler(MyScoreSize, arrMyScore);
                }
                //myScore 데이터가 없다면
                else{
                    member_data.myScore_handler = new MyScore_Handler();
                }


                //단어장 데이터 멤버데이터에 추가
                if(DictSize != 0){
                    member_data.myDict_handler = new MyDict_Handler(DictSize, arrMyDict);
                }
                //단어장이 없다면
                else{
                    member_data.myDict_handler = new MyDict_Handler();
                }

                Intent intent = new Intent(getActivity(), LoginedActivity.class);

                intent.putExtra("member_data", member_data);

                startActivity(intent);

                getActivity().finish();
            }

            dlg.dismiss();
        }
    }
}
