package com.example.bang.toeichelper;

import android.animation.Animator;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.OvershootInterpolator;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.bang.toeichelper.customprogress.CircularProgress;
import com.example.bang.toeichelper.mydata.MEMBER_DATA;
import com.example.bang.toeichelper.mydata.TOEICDATE_Handler;
import com.kakao.UserProfile;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;

/**
 * Created by BANG on 2015-01-11.
 */
public class LoginedActivity extends ActionBarActivity implements View.OnClickListener {

    //Color Dict API 보면 이렇게 나와있음
    public static final String SEARCH_ACTION   = "colordict.intent.action.SEARCH";
    public static final String PICK_RESULT_ACTION  = "colordict.intent.action.PICK_RESULT";
    public static final String EXTRA_QUERY    = "EXTRA_QUERY";
    public static final String EXTRA_FULLSCREEN = "EXTRA_FULLSCREEN";
    public static final String EXTRA_HEIGHT   = "EXTRA_HEIGHT";
    public static final String EXTRA_WIDTH    = "EXTRA_WIDTH";
    public static final String EXTRA_GRAVITY   = "EXTRA_GRAVITY";
    public static final String EXTRA_MARGIN_LEFT  = "EXTRA_MARGIN_LEFT";
    public static final String EXTRA_MARGIN_TOP   = "EXTRA_MARGIN_TOP";
    public static final String EXTRA_MARGIN_BOTTOM  = "EXTRA_MARGIN_BOTTOM";
    public static final String EXTRA_MARGIN_RIGHT  = "EXTRA_MARGIN_RIGHT";

    //디스플레이 설정 변수
    private int DP;
    private DisplayMetrics displayMetrics;
    private int windowW, windowH;

    //글씨체
    private Typeface typeface;

    //액션바
    private Toolbar toolbar;
    private DrawerLayout drawerLayout;
    private ActionBarDrawerToggle actionBarDrawerToggle;
    private boolean dictOK;

    //자동 로그인 정보
    private static SharedPreferences preferences;
    private static SharedPreferences.Editor editor;

    //사용자 정보
    static private MEMBER_DATA member_data;

    private FrameLayout layContainer;
    private RecyclerView recyclerView;
    private RecyvAdapter recyvAdapter;

    //menu
    private LinearLayout layMenu;
    private TextView txtvName;
    private TextView txtvEmail;
    private TextView txtvLogout;

    // 0
    private TextView txtvTest;
    private CircularProgress cprogressToeicDate;

    //카드뷰 레이아웃 ID
    private int[] nCardvId;

    private ImportToeicDate taskImportToeicDate;

    private TOEICDATE_Handler toeicdate_handler;

    private static UserProfile userProfile;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        overridePendingTransition(R.anim.abc_slide_in_bottom, R.anim.abc_slide_out_bottom);

        //View decorView = getWindow().getDecorView();
        // Hide the status bar.
//        int uiOptions = View.SYSTEM_UI_FLAG_FULLSCREEN;
//        decorView.setSystemUiVisibility(uiOptions);
        // Remember that you should never show the action bar if the
        // status bar is hidden, so hide that too if necessary.
        //ActionBar actionBar = getActionBar();
        //actionBar.hide();

        setContentView(R.layout.activity_logined);
        //userProfile = UserProfile.loadFromCache();
        //Log.w("ID", userProfile.toString());

        displayMetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        DP = displayMetrics.densityDpi; //DP 가져오기
        windowW = getWindowManager().getDefaultDisplay().getWidth();
        windowH = getWindowManager().getDefaultDisplay().getHeight();

        typeface = Typeface.createFromAsset(getAssets(), "fonts/aritta_bold.ttf");

        //로그인 성공한 멤버 데이터 받기
        Intent intent = getIntent();
        member_data = (MEMBER_DATA) intent.getExtras().getSerializable("member_data");

        setnCardvId();

        Init();
    }

    @Override
    public void finish() {
        super.finish();

//        UserManagement.requestLogout(new LogoutResponseCallback() {
//            @Override
//            protected void onSuccess(long l) {
//
//            }
//
//            @Override
//            protected void onFailure(APIErrorResult apiErrorResult) {
//
//            }
//        });
    }

    public void Init(){

        //툴바로 드뤄월레이아웃 액션바 만듦
        toolbar = (Toolbar) findViewById(R.id.toolbarLogined);
        toolbar.setBackground(getResources().getDrawable(R.drawable.actionbar_background_black));

        layContainer = (FrameLayout) findViewById(R.id.containerLogined);
        drawerLayout = (DrawerLayout) findViewById(R.id.drawlayLogined);
        recyclerView = (RecyclerView) findViewById(R.id.recyvLogined);

        layMenu = (LinearLayout) findViewById(R.id.laydrawerLogined);
        txtvName = (TextView) findViewById(R.id.txtvLoginedMenuName);
        txtvEmail = (TextView) findViewById(R.id.txtvLoginedMenuEmail);
        txtvLogout = (TextView) findViewById(R.id.txtvLoginedMenuLogout);

        txtvName.setText(member_data.getStrName() + " 님");
        txtvEmail.setText(member_data.getStrEmail());

        layMenu.setOnClickListener(LoginedActivity.this);
        txtvLogout.setOnClickListener(LoginedActivity.this);

        //사전 사용여부
        dictOK = true;

        setSupportActionBar(toolbar);
        getSupportActionBar().setElevation(0);

        actionBarDrawerToggle = new ActionBarDrawerToggle(LoginedActivity.this, drawerLayout, R.string.hello_world, R.string.hello_world){
            @Override
            public void onDrawerOpened(View drawerView) {
                super.onDrawerOpened(drawerView);

                Log.w("actionBar", "onDrawerOpened");
            }

            @Override
            public void onDrawerClosed(View drawerView) {
                super.onDrawerClosed(drawerView);

                Log.w("actionBar", "onDrawerClosed");
            }
        };
        drawerLayout.setDrawerListener(actionBarDrawerToggle);

        recyclerView.setHasFixedSize(true);
        LinearLayoutManager manager = new LinearLayoutManager(LoginedActivity.this);
        manager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(manager);

        //리싸이클러뷰 어댑터 등록
        recyvAdapter = new RecyvAdapter(LoginedActivity.this, nCardvId.length, nCardvId);
        recyclerView.setAdapter(recyvAdapter);
    }


    public MEMBER_DATA getMember_data() {
        return member_data;
    }

    public TOEICDATE_Handler getToeicdate_handler() {
        return toeicdate_handler;
    }

    public void executeTaskImportToeicDate(TextView txtvTest, CircularProgress progress){
        this.txtvTest = txtvTest;
        this.cprogressToeicDate = progress;
        taskImportToeicDate = new ImportToeicDate();
        taskImportToeicDate.execute("");
    }

    public void setnCardvId() {
        //리싸이클러뷰로 들어갈 순서대로 카드뷰 layout ID 배열
        nCardvId = new int[5];

        nCardvId[0] = R.layout.cardview_logined_toeicdate;
        nCardvId[1] = R.layout.cardview_logined_graph;
        nCardvId[2] = R.layout.cardview_logined_mydict;
        nCardvId[3] = R.layout.cardview_logined_popdict;
        nCardvId[4] = R.layout.cardview_logined_goalscore;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public void onBackPressed() {
        if(dictOK){
            super.onBackPressed();
        }
        else{

        }
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);

        actionBarDrawerToggle.syncState();
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        actionBarDrawerToggle.onConfigurationChanged(newConfig);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(actionBarDrawerToggle.onOptionsItemSelected(item)){
            return true;
        }
        else if(item.getItemId() == R.id.action_dict){
            if(dictOK){

                //사전 띄우기
                final Intent intent = new Intent(PICK_RESULT_ACTION);
                //intent.putExtra(EXTRA_QUERY, "hello"); //Search Query
                intent.putExtra(EXTRA_FULLSCREEN, false); //
                intent.putExtra(EXTRA_HEIGHT, windowH - (28 * (DP / 160)) ); //400pixel, if you don't specify, fill_parent"
                intent.putExtra(EXTRA_GRAVITY, Gravity.BOTTOM);
                //intent.putExtra(EXTRA_MARGIN_LEFT, 100);
                //startActivity(intent);

                dictOK = false;
                if(isIntentAvailable(LoginedActivity.this, intent)){
                    getWindow().getDecorView().findViewById(android.R.id.content)
                            .animate().scaleX(0.8f).scaleY(0.8f).setDuration(500).setInterpolator(new OvershootInterpolator())
                            .setListener(new Animator.AnimatorListener() {
                                @Override
                                public void onAnimationStart(Animator animation) {

                                }

                                @Override
                                public void onAnimationEnd(Animator animation) {
                                    startActivityForResult(intent, 5);
                                    dictOK = true;
                                }

                                @Override
                                public void onAnimationCancel(Animator animation) {

                                }

                                @Override
                                public void onAnimationRepeat(Animator animation) {

                                }
                            }).start();

                }
                else{
                    Toast.makeText(LoginedActivity.this, "ColorDict 어플을 먼저 설치해주세요", Toast.LENGTH_SHORT).show();
                    dictOK = true;
                }
            }
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onClick(View v) {
        if(v.getId() == txtvName.getId()){
        }
        else if(v.getId() == txtvLogout.getId()){

            preferences = getSharedPreferences("pref", MODE_PRIVATE);
            editor = preferences.edit();
            editor.clear();
            editor.apply();

            Intent intent = new Intent(LoginedActivity.this, MainActivity.class);
            startActivity(intent);

            finish();
        }
    }

    public static boolean isIntentAvailable(Context context, Intent intent) {
        final PackageManager packageManager = context.getPackageManager();
        List list = packageManager.queryIntentActivities(intent, PackageManager.MATCH_DEFAULT_ONLY);
        return list.size() > 0;
    }

    //지도 어플 띄울때 할꺼임 아직 기능 안함
    public void showMap(Uri geoLocation){
        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.setData(geoLocation);
        if(intent.resolveActivity(getPackageManager()) != null){
            startActivity(intent);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        //사전
        if(requestCode == 5){
            if(resultCode == RESULT_OK) {
                String strExtraText = data.getStringExtra(Intent.EXTRA_TEXT);
                Log.d("dict", data.getStringExtra(Intent.EXTRA_SUBJECT));
                //Log.d("dict", strExtraText);
                //String[] cut = strExtraText.split("\n");
                //Toast.makeText(LoginedActivity.this, cut[2], Toast.LENGTH_SHORT).show();
            }
            getWindow().getDecorView().findViewById(android.R.id.content).
                    animate().scaleX(1f).scaleY(1f).setStartDelay(500).setDuration(500).setInterpolator(new OvershootInterpolator()).setListener(null).start();
        }
        //목표점수
        else if(requestCode == 6){
            //값 변경이 있을 경우
            if(resultCode == 5){
                //Log.w("goalActivity", "변경값 있음!@!!");
                member_data = (MEMBER_DATA) data.getExtras().getSerializable("member_data");
                recyvAdapter = new RecyvAdapter(LoginedActivity.this, 5, nCardvId);
                recyclerView.setAdapter(recyvAdapter);
            }
        }
        //단어장
        else if(requestCode == 7){
            if(resultCode == 1){
                member_data = (MEMBER_DATA) data.getExtras().getSerializable("member_data");
            }
        }
    }

    public class ImportToeicDate extends AsyncTask<String, Integer, String> {

        private String strURL = "http://appexam.ybmsisa.com/toeic/info/receipt_schedule.asp";

        public ImportToeicDate() {
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected String doInBackground(String... params) {
            String s = "";

            try {
                URL url = new URL(strURL);

                HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();

                if(httpURLConnection != null){
                    httpURLConnection.setDefaultUseCaches(false);
                    //httpURLConnection.setDoInput(true);
                    httpURLConnection.setRequestMethod("GET");

                    //xml파일로 요청
                    httpURLConnection.setRequestProperty("Accept", "application/xml");


                    //수신
                    InputStreamReader inputStreamReader = new InputStreamReader(httpURLConnection.getInputStream(), "euc-kr");
                    BufferedReader bufferedReader = new BufferedReader(inputStreamReader);

                    String r;
                    while((r = bufferedReader.readLine()) != null){
                        s += r;
                    }

                    bufferedReader.close();
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

            String[] cut = s.split("<!-- 시험 일정 테이블 -->");
            HTMLParsingToeicDate htmlParsingToeicDate = new HTMLParsingToeicDate(cut[1]);
            htmlParsingToeicDate.ParsingData();
            String strData = htmlParsingToeicDate.getStrParsingData();
            toeicdate_handler = htmlParsingToeicDate.getToeicdate_handler();
            if(toeicdate_handler.getSize() != 0){
                txtvTest.setText(String.format("%d / %d / %d ",
                        toeicdate_handler.getNearestToeicDate().getnExamYear(),
                        toeicdate_handler.getNearestToeicDate().getnExamMonth(),
                        toeicdate_handler.getNearestToeicDate().getnExamDay()));
                //txtvTest.setText(userProfile.getNickname());
            }
            else{
                txtvTest.setText("YBM 홈페이지 서버 점검중...");
            }
            cprogressToeicDate.setVisibility(View.GONE);
        }
    }
}
