package com.example.bang.toeichelper;

import android.animation.Animator;
import android.content.Intent;
import android.graphics.Typeface;
import android.graphics.drawable.TransitionDrawable;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.LinearInterpolator;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.bang.toeichelper.myfragment.JoinFragment;
import com.example.bang.toeichelper.myfragment.LoginFragment;
import com.google.android.gms.plus.Plus;

import java.lang.reflect.Field;


public class MainActivity extends FragmentActivity implements View.OnClickListener{

    private static final int REQUEST_CODE_RESOLVE_ERR = 9000;
    /* Request code used to invoke sign in user interactions. */
    private static final int RC_SIGN_IN = 0;


    /* A flag indicating that a PendingIntent is in progress and prevents
    * us from starting further intents.
    */
    //private boolean mIntentInProgress;

    //private GoogleApiClient mGoogleApiClient;

    private Typeface typeface;


    private TextView txtvMainTitle1, txtvMainTitle2;

    private noTouchViewPager vpagerMain;
    private MainPagerAdapter pagerAdapter;

    private LinearLayout layMainText;

    private ImageView imgvMainBackground;

    //로그인
    private EditText edtxLoginID, edtxLoginPW;
    //회원가입
    private EditText edtxJoinID, edtxJoinName, edtxJoinPW, edtxJoinPwCon;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        typeface = Typeface.createFromAsset(getAssets(), "fonts/aritta_bold.ttf");

        Init();
    }

    @Override
    protected void onStop() {
        super.onStop();
//        if(mGoogleApiClient.isConnected()){
//            mGoogleApiClient.disconnect();
//        }
        if(((JoinFragment)pagerAdapter.getItem(1)).getmGoogleApiClient() != null){
            if(((JoinFragment)pagerAdapter.getItem(1)).getmGoogleApiClient().isConnected()){
                ((JoinFragment)pagerAdapter.getItem(1)).getmGoogleApiClient().disconnect();
                Log.w("Google", "disconnet()");
            }
        }
    }

    @Override
    public void finish() {
        super.finish();

        if(((JoinFragment)pagerAdapter.getItem(1)).getmGoogleApiClient() != null){
            if(((JoinFragment)pagerAdapter.getItem(1)).getmGoogleApiClient().isConnected()){
                Plus.AccountApi.clearDefaultAccount(((JoinFragment)pagerAdapter.getItem(1)).getmGoogleApiClient());
                ((JoinFragment)pagerAdapter.getItem(1)).getmGoogleApiClient().disconnect();
                Log.w("Google", "clearDefaultAccount()");
            }
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    public void Init(){
        vpagerMain = (noTouchViewPager) findViewById(R.id.vpagerMain);

        //스크롤러 재정의
        Field mScroller;
        try {
            mScroller = ViewPager.class.getDeclaredField("mScroller");
            mScroller.setAccessible(true);
            slowScroller scroller = new slowScroller(vpagerMain.getContext());
            mScroller.set(vpagerMain, scroller);
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }

        //스크롤 금지
        vpagerMain.setPagingDisabled();

        //메인 타이틀
        txtvMainTitle1 = (TextView) findViewById(R.id.txtvMainTitle1);
        txtvMainTitle1.setTypeface(typeface);
        txtvMainTitle2 = (TextView) findViewById(R.id.txtvMainTitle2);
        txtvMainTitle2.setTypeface(typeface);

        //
        imgvMainBackground = (ImageView) findViewById(R.id.imgvMainBackground);
        //애니메이션 시작
        backgroundImageAnimation(15000);


        layMainText = (LinearLayout) findViewById(R.id.layMainText);


        //어댑터 할당
        pagerAdapter = new MainPagerAdapter(getSupportFragmentManager(), 2);
        //뷰페이져에 어댑터 등록
        vpagerMain.setAdapter(pagerAdapter);

        //로그인 View

        //클릭 리스너 등록
//        btnLogin.setOnClickListener(MainActivity.this);
//        btnJoin.setOnClickListener(MainActivity.this);


        //뷰페이져 이동 애니메이션 구현
        vpagerMain.setPageTransformer(false, new ViewPager.PageTransformer() {
            @Override
            public void transformPage(View view, float v) {
                //상단 제목
                layMainText.setTranslationY( (v-1) * (view.getHeight() * 0.05f) );

                if(v < -1){
                    view.setTranslationX(0);
                    view.setTranslationY(0);
                    view.setAlpha(0);
                }
                //[-1, -0.000001]
                else if(v < 0){
                    view.setTranslationX(view.getWidth() * -v);
                    view.setTranslationY(view.getHeight() * -v);
                    view.setScaleX( (1 + v));
                    view.setScaleY( (1 + v));
                    view.setAlpha(1 + v);
                }
                //[0, 0.999999]
                else if(v < 1){
                    view.setTranslationX(view.getWidth() * -v);
                    view.setTranslationY(view.getHeight() * v);
                    view.setScaleX(1 - v);
                    view.setScaleY(1 - v);
                    view.setAlpha(1 - v);
                }
                //[1, ]
                else{
                    view.setTranslationX(view.getWidth() * -1);
                    view.setTranslationY(view.getHeight());
                    view.setAlpha(0);
                }
            }
        });
    }

    public void backgroundImageAnimation(final int duration){
        imgvMainBackground.animate().
                translationX(200)
                .setDuration(duration)
                .setInterpolator(new LinearInterpolator())
                .setListener(new Animator.AnimatorListener() {
                    @Override
                    public void onAnimationStart(Animator animation) {

                    }

                    @Override
                    public void onAnimationEnd(Animator animation) {
                        TransitionDrawable drawable0 = (TransitionDrawable) getResources().getDrawable(R.drawable.main_trans_back_0to1);
                        imgvMainBackground.setImageDrawable(drawable0);
                        drawable0.startTransition(1000);
                        imgvMainBackground.animate()
                                .setDuration(duration)
                                .translationX(-200)
                                .setInterpolator(new LinearInterpolator())
                                .setStartDelay(1000)
                                .setListener(new Animator.AnimatorListener() {
                                    @Override
                                    public void onAnimationStart(Animator animation) {

                                    }

                                    @Override
                                    public void onAnimationEnd(Animator animation) {
                                        TransitionDrawable drawable1 = (TransitionDrawable) getResources().getDrawable(R.drawable.main_trans_back_1to2);
                                        imgvMainBackground.setImageDrawable(drawable1);
                                        drawable1.startTransition(1000);
                                        imgvMainBackground.animate()
                                                .setDuration(duration)
                                                .translationX(200)
                                                .setInterpolator(new LinearInterpolator())
                                                .setStartDelay(1000)
                                                .setListener(new Animator.AnimatorListener() {
                                                    @Override
                                                    public void onAnimationStart(Animator animation) {

                                                    }

                                                    @Override
                                                    public void onAnimationEnd(Animator animation) {
                                                        TransitionDrawable drawable2 = (TransitionDrawable) getResources().getDrawable(R.drawable.main_trans_back_2to3);
                                                        imgvMainBackground.setImageDrawable(drawable2);
                                                        drawable2.startTransition(1000);
                                                        imgvMainBackground.animate()
                                                                .setDuration(duration)
                                                                .translationX(-200)
                                                                .setInterpolator(new LinearInterpolator())
                                                                .setStartDelay(1000)
                                                                .setListener(null)
                                                                .start();
                                                    }

                                                    @Override
                                                    public void onAnimationCancel(Animator animation) {

                                                    }

                                                    @Override
                                                    public void onAnimationRepeat(Animator animation) {

                                                    }
                                                })
                                                .start();
                                    }

                                    @Override
                                    public void onAnimationCancel(Animator animation) {

                                    }

                                    @Override
                                    public void onAnimationRepeat(Animator animation) {

                                    }
                                })
                                .start();
                    }

                    @Override
                    public void onAnimationCancel(Animator animation) {

                    }

                    @Override
                    public void onAnimationRepeat(Animator animation) {

                    }
                })
                .start();
    }

    @Override
    public void onClick(View v) {
//        if(v.getId() == btnLogin.getId()){
//            if(vpagerMain.getCurrentItem() == 0) if (IsLoginOK()) {
//
//                preferences = getSharedPreferences("pref", MODE_PRIVATE);
//                SharedPreferences.Editor editor = preferences.edit();
//                editor.putString("ID", edtxLoginID.getText().toString());
//                editor.putString("PW", edtxLoginPW.getText().toString());
//                editor.apply();
//
//                Intent intent = new Intent(MainActivity.this, LoginedActivity.class);
//                startActivity(intent);
//
//                finish();
//            } else {
//                Toast.makeText(MainActivity.this, "비어 있는 입력란이 있습니다.", Toast.LENGTH_SHORT).show();
//                //카카오톡 로그인 화면으로 넘어감
////                    Intent intent = new Intent(this, SampleLoginActivity.class);
////                    startActivity(intent);
//
//                //구글
//                mGoogleApiClient = new GoogleApiClient.Builder(this)
//                        .addConnectionCallbacks(this)
//                        .addOnConnectionFailedListener(this)
//                        .addApi(Plus.API)
//                        .addScope(Plus.SCOPE_PLUS_LOGIN)
//                        .build();
//
//                mGoogleApiClient.connect();
//            }
//            else{
//                vpagerMain.setCurrentItem(0, true);
//            }
//
//        }
//        else if(v.getId() == btnJoin.getId()){
//            if(vpagerMain.getCurrentItem() == 0){
//                vpagerMain.setCurrentItem(1, true);
//            }
//            else{
//                //회원가입 시작
//                boolean a = IsJoinOK();
//            }
//        }
    }



    //로그인 가능한가
    public boolean IsLoginOK() {
        edtxLoginID = ((LoginFragment)pagerAdapter.getItem(0)).getEdtxID();
        edtxLoginPW = ((LoginFragment)pagerAdapter.getItem(0)).getEdtxPW();

        //문자열 따오기
        String strID = edtxLoginID.getText().toString();
        String strPW = edtxLoginPW.getText().toString();

        return !(strIsEmpty(strID) || strIsEmpty(strPW));
    }

    //회원가입 가능한가
    public boolean IsJoinOK() {
        boolean ret = false;

        //회원 가입 editText 등록
//        edtxJoinID = ((JoinFragment)pagerAdapter.getItem(1)).getEdtxID();
//        edtxJoinName = ((JoinFragment)pagerAdapter.getItem(1)).getEdtxName();
//        edtxJoinPW = ((JoinFragment)pagerAdapter.getItem(1)).getEdtxPW();
//        edtxJoinPwCon = ((JoinFragment)pagerAdapter.getItem(1)).getEdtxPwCon();

        //문자열 따오기
        String strID = edtxJoinID.getText().toString();
        String strName = edtxJoinName.getText().toString();
        String strPW = edtxJoinPW.getText().toString();
        String strPwCon = edtxJoinPwCon.getText().toString();

        //공백 검사
        if(strIsEmpty(strID) || strIsEmpty(strName) || strIsEmpty(strPW) || strIsEmpty(strPwCon)){
            Toast.makeText(MainActivity.this, "비어 있는 입력란이 있습니다.", Toast.LENGTH_SHORT).show();
        }
        //빈칸 검사
        else if(strSpaceCheck(strID) || strSpaceCheck(strName) || strSpaceCheck(strPW) || strSpaceCheck(strPwCon)){
            Toast.makeText(MainActivity.this, "입력란에 공백을 포함할 수 없습니다.", Toast.LENGTH_SHORT).show();
        }
        else{

        }

        return ret;
    }

    //문자열 빔 체크
    public boolean strIsEmpty(String str){
        return (str==null) || (str.length() == 0);
    }
    //문자열 공백 체크
    public boolean strSpaceCheck(String str){
        for(int i=0;i<str.length();i++){
            if(str.charAt(i) == ' '){
                return true;
            }
        }
        return false;
    }

    public MainPagerAdapter getPagerAdapter(){ return this.pagerAdapter; }

    //뷰페이져 어댑터
    public class MainPagerAdapter extends FragmentPagerAdapter{

        private Fragment[] fragments;
        private int count;

        public MainPagerAdapter(FragmentManager fm, int count) {
            super(fm);

            this.count = count;
            this.fragments = new Fragment[count];

            fragments[0] = LoginFragment.create();
            fragments[1] = JoinFragment.create();
        }

        @Override
        public Fragment getItem(int i) {
            return this.fragments[i];
        }

        @Override
        public int getCount() {
            return this.count;
        }
    }

    @Override
    public void onBackPressed() {
        if(vpagerMain.getCurrentItem() != 0){
            vpagerMain.setCurrentItem(0, true);
        }
        else{
            super.onBackPressed();
        }
    }

        @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
//        int id = item.getItemId();
//
//        //noinspection SimplifiableIfStatement
//        if (id == R.id.action_settings) {
//            return true;
//        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        //Log.w("onActivityResult", "MainActivity");
        //구글
        if(requestCode == RC_SIGN_IN){
            ((JoinFragment)pagerAdapter.getItem(1)).mIntentInProgress = false;
            if( !((JoinFragment)pagerAdapter.getItem(1)).getmGoogleApiClient().isConnecting() && resultCode != 0){
                ((JoinFragment)pagerAdapter.getItem(1)).getmGoogleApiClient().connect();
            }
        }
    }
}
