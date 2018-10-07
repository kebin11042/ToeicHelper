package com.example.bang.toeichelper.myfragment;

import android.content.Context;
import android.content.IntentSender;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.Toast;

import com.example.bang.toeichelper.MainActivity;
import com.example.bang.toeichelper.R;
import com.example.bang.toeichelper.customdlg.GooglejoinDlg;
import com.example.bang.toeichelper.customdlg.KakaojoinDlg;
import com.example.bang.toeichelper.secret.SHA_1;
import com.google.android.gms.common.ConnectionResult;
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
import java.util.regex.Pattern;

/**
 * Created by BANG on 2015-01-10.
 */
public class JoinFragment extends Fragment implements View.OnClickListener, GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener{

    //private Button btnBack;
    private FrameLayout fBtnBack;

    //일반회원
    private EditText edtxEamil;
    private EditText edtxName;
    private EditText edtxPW;
    private String strEmail, strName, strPW;

    private Button btnJoin;
    private FrameLayout fbtnKakaoJoin;
    private FrameLayout fbtnGoogleJoin;

    //구글
    private static final int RC_SIGN_IN = 0;
    private GoogleApiClient mGoogleApiClient;
    public boolean mIntentInProgress;
    //private String strGoogleEmail;
    //private String strGoogleName;


    public static JoinFragment create(){
        JoinFragment fragment = new JoinFragment();

        return fragment;
    }

    public GoogleApiClient getmGoogleApiClient(){
        return this.mGoogleApiClient;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mGoogleApiClient = null;
        //mIntentInProgress = true;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        ViewGroup rootView = (ViewGroup) inflater.inflate(R.layout.fragment_join, container, false);

        edtxEamil = (EditText) rootView.findViewById(R.id.edtxJoinEmail);
        edtxName = (EditText) rootView.findViewById(R.id.edtxJoinName);
        edtxPW = (EditText) rootView.findViewById(R.id.edtxJoinPW);

        fBtnBack = (FrameLayout) rootView.findViewById(R.id.btnJoinLogin);
        btnJoin = (Button) rootView.findViewById(R.id.btnJoinJoin);
        fbtnKakaoJoin = (FrameLayout) rootView.findViewById(R.id.btnJoinKakao);
        fbtnGoogleJoin = (FrameLayout) rootView.findViewById(R.id.btnJoinGoogle);

        fBtnBack.setOnClickListener(JoinFragment.this);
        btnJoin.setOnClickListener(JoinFragment.this);
        fbtnKakaoJoin.setOnClickListener(JoinFragment.this);
        fbtnGoogleJoin.setOnClickListener(JoinFragment.this);

        return rootView;
    }

    @Override
    public void onClick(View v) {
        if(v.getId() == fBtnBack.getId()){
            ViewPager viewPager = (ViewPager) getActivity().findViewById(R.id.vpagerMain);
            viewPager.setCurrentItem(0);
        }
        else if(v.getId() == btnJoin.getId()){
            strEmail = edtxEamil.getText().toString();
            strName = edtxName.getText().toString();
            strPW = edtxPW.getText().toString();

            //공백 띄어쓰기 검사
            if(IsEmpty(strEmail) || IsEmpty(strName) || IsEmpty(strPW)
                    || strSpaceCheck(strEmail) || strSpaceCheck(strName) || strSpaceCheck(strPW)){
                Toast.makeText(getActivity(), "입력란을 비우거나 공백을 포함할 수 없습니다.", Toast.LENGTH_SHORT).show();
            }
            else{

                //이메일 유효성 검사
                if(isEmail(strEmail)){
                    //비밀번호 검사
                    if(strPW.length() >= 8){
                        //정규식으로 한글 검사
                        if( strPW.matches(".*[ㄱ-ㅎㅏ-ㅣ가-힣]+.*") ){
                            Toast.makeText(getActivity(), "비밀번호에 한글을 포함할 수 없습니다.", Toast.LENGTH_SHORT).show();
                        }
                        else{
                            JoinMember joinMember = new JoinMember();
                            joinMember.execute("");
                        }
                    }
                    else{
                        Toast.makeText(getActivity(), "비밀번호가 최소 8자리 이상이여야 합니다.", Toast.LENGTH_SHORT).show();
                    }
                }
                else{
                    Toast.makeText(getActivity(), "이메일이 유효하지 않습니다.", Toast.LENGTH_SHORT).show();
                }


            }
        }
        else if(v.getId() == fbtnKakaoJoin.getId()){
            KakaojoinDlg kakaojoinDlg = new KakaojoinDlg(getActivity());
            kakaojoinDlg.show();
        }
        else if(v.getId() == fbtnGoogleJoin.getId()){

            //구글 설정
            mGoogleApiClient = new GoogleApiClient.Builder(getActivity())
                    .addConnectionCallbacks(JoinFragment.this)
                    .addOnConnectionFailedListener(JoinFragment.this)
                    .addApi(Plus.API)
                    .addScope(Plus.SCOPE_PLUS_LOGIN)
                    .build();
            //연결 시도
            mGoogleApiClient.connect();

//            GooglejoinDlg googlejoinDlg = new GooglejoinDlg(getActivity());
//            googlejoinDlg.show();
        }
        //Log.w("Join onClick", "ok = " + v.getId());
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

    //이메일 검사
    public static boolean isEmail(String email) {
        if (email==null) return false;
        boolean b = Pattern.matches("[\\w\\~\\-\\.]+@[\\w\\~\\-]+(\\.[\\w\\~\\-]+)+", email.trim());
        return b;
    }

    @Override
    public void onConnected(Bundle bundle) {
        //Log.w("onConnected", "onConnected");
        // We've resolved any connection errors.  mGoogleApiClient can be used to
        // access Google APIs on behalf of the user.
        getProfileInformation();

        GooglejoinDlg dlg = new GooglejoinDlg(getActivity());
        //dlg.setTextView(strGoogleEmail, strGoogleName);
        dlg.setGoogleApiClient(mGoogleApiClient);
        dlg.show();
    }

    @Override
    public void onConnectionSuspended(int i) {
        //Log.w("onConnectionSuspended", "onConnectionSuspended");
        mGoogleApiClient.connect();
    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {
        Log.w("onConnectionFailed", "onConnectionFailed");
        if (!mIntentInProgress && connectionResult.hasResolution()) {
            try {
                //Log.w("onConnectionFailed", "!mIntentInProgress && connectionResult.hasResolution()");
                mIntentInProgress = true;
                getActivity().startIntentSenderForResult(connectionResult.getResolution().getIntentSender(),
                        RC_SIGN_IN, null, 0, 0, 0);
            } catch (IntentSender.SendIntentException e) {
                // The intent was canceled before it was sent.  Return to the default
                // state and attempt to connect to get an updated ConnectionResult.
                //Log.w("onConnectionFailed", "IntentSender.SendIntentException e");
                mIntentInProgress = false;
                mGoogleApiClient.connect();
            }
        }
    }

    //구글 프로파일 받기
    public void getProfileInformation(){
        //Log.w("Google", "getProfileInformation()");
        if(Plus.PeopleApi.getCurrentPerson(mGoogleApiClient) != null){
            Person currentPerson = Plus.PeopleApi.getCurrentPerson(mGoogleApiClient);

            //strGoogleName = currentPerson.getDisplayName();
            //String personPhotoUrl = currentPerson.getImage().getUrl();
            //String personGooglePlusProfile = currentPerson.getUrl();
            //strGoogleEmail = Plus.AccountApi.getAccountName(mGoogleApiClient);
            //Log.w("Google", "Email : " + personEmail);
        }
    }



    public class JoinMember extends AsyncTask<String, Integer, String> {

        private Context context;

        private String strURL = "http://kebin1104.dothome.co.kr/ToeicHelper/JoinMember.php";

        private String ePW;

        public JoinMember() {
            context = getActivity();
            //rsa = new RSA();

            //Random random = new Random();
            //rsaIndex = random.nextInt(4) + 1;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            //eEmail = rsa.encryptMSG(strEmail, rsaIndex);
            //eName = rsa.encryptMSG(strName, rsaIndex);
            //ePW = rsa.encryptMSG(strPW, rsaIndex);

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
                edtxEamil.setText("");
                edtxName.setText("");
                edtxPW.setText("");
                Toast.makeText(context, "회원가입 완료!! 로그인 해주세요", Toast.LENGTH_SHORT).show();

                //키보드 닫기
                InputMethodManager manager = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
                manager.hideSoftInputFromWindow(edtxPW.getWindowToken(), 0);

                ViewPager viewPager = (ViewPager) getActivity().findViewById(R.id.vpagerMain);
                viewPager.setCurrentItem(0);

                ((LoginFragment)((MainActivity)getActivity()).getPagerAdapter().getItem(0)).getEdtxID().requestFocus();
            }
        }
    }
}
