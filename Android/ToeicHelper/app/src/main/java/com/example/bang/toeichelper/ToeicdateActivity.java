package com.example.bang.toeichelper;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.bang.toeichelper.customdlg.ToeicdateAddDlg;
import com.example.bang.toeichelper.mydata.TOEICDATE;
import com.example.bang.toeichelper.mydata.TOEICDATE_Handler;
import com.example.bang.toeichelper.myfragment.ToeicdateFragment;

import org.w3c.dom.Text;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by BANG on 2015-02-25.
 */
public class ToeicdateActivity extends ActionBarActivity implements View.OnClickListener{

    private ViewPager vpager;
    private ToeicdatePagerAdapter adapter;

    private LinearLayout layYBMHome;

    private TextView txtvGuide;
    private ImageView imgvPrev, imgvNext;

    private TOEICDATE_Handler toeicdate_handler;


    private int callID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_toeicdate);

        Intent intent = getIntent();
        toeicdate_handler = (TOEICDATE_Handler)intent.getSerializableExtra("toeicdate_h");

        setActionBar();

        Init();
    }

    public void Init(){
        layYBMHome = (LinearLayout) findViewById(R.id.layToeicdateYBMHome);

        vpager = (ViewPager) findViewById(R.id.vpagerToeicdate);

        txtvGuide = (TextView) findViewById(R.id.txtvToeicdateGuide);
        imgvPrev = (ImageView) findViewById(R.id.imgvToeicdatePrev);
        imgvNext = (ImageView) findViewById(R.id.imgvToeicdateNext);

        imgNextPrevAnimation();

        adapter = new ToeicdatePagerAdapter(getSupportFragmentManager(), toeicdate_handler.getSize());
        vpager.setAdapter(adapter);

        setViewPagerTrans();

        layYBMHome.setOnClickListener(ToeicdateActivity.this);
    }

    //가이드 화살표 점점 사라지게 하는 애니메션 메소드
    public void imgNextPrevAnimation(){
        Animation animation = new AlphaAnimation(1.f, 0.0f);
        animation.setDuration(1000);
        animation.setStartOffset(2000);

        animation.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                txtvGuide.setVisibility(View.GONE);
                imgvPrev.setVisibility(View.GONE);
                imgvNext.setVisibility(View.GONE);
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });

        txtvGuide.startAnimation(animation);
        imgvNext.startAnimation(animation);
        imgvPrev.startAnimation(animation);
    }

    //vpager 넘김 효과
    public void setViewPagerTrans(){

        vpager.setPageTransformer(false, new ViewPager.PageTransformer() {
            @Override
            public void transformPage(View page, float position) {
                final ToeicdateFragment fragment = (ToeicdateFragment) adapter.getItem(0);
                LinearLayout lay = fragment.getLayTh();

                if(lay == null){
                    Toast.makeText(ToeicdateActivity.this, "NULL", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    @Override
    public void onClick(View v) {
        if(v.getId() == layYBMHome.getId()){
            //YBM 홈페이지 띄우기
            Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("http://appexam.ybmsisa.com/toeic/info/receipt_schedule.asp"));
            startActivity(intent);
        }
    }

    public void setActionBar(){
        android.support.v7.app.ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setBackgroundDrawable(getResources().getDrawable(R.drawable.actionbar_background_black));
        actionBar.setTitle("토익 시험 일정");
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_toeicdate, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        if(item.getItemId() == R.id.action_plus){

            //Toast.makeText(ToeicdateActivity.this, toeicdate_handler.getSize() + " man", Toast.LENGTH_SHORT).show();

            ToeicdateAddDlg dialog = new ToeicdateAddDlg(ToeicdateActivity.this);


            //현재 페이지에 따라 TOEICDATE 따오기
            TOEICDATE toeicdate = toeicdate_handler.getToeicDateByKey(vpager.getCurrentItem());
            //문자열 형태로 만들어줌
            String strDate = String.format("%d.%d.%d/09:20:00", toeicdate.getnExamYear(), toeicdate.getnExamMonth(), toeicdate.getnExamDay());
            //long형으로 데이터 포맷 해주기
            long lDate = 0;
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy.MM.dd/HH:mm:ss");
            try {
                Date date = simpleDateFormat.parse(strDate);
                lDate = date.getTime();
                //Log.w("lDate", lDate + " 임");
            } catch (ParseException e) {
                e.printStackTrace();
            }

            dialog.setLongDate(lDate);
            dialog.show();

            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public Intent getSupportParentActivityIntent() {

        this.finish();

        return super.getSupportParentActivityIntent();
    }




    public class ToeicdatePagerAdapter extends FragmentPagerAdapter {

        private int Count;

        private Fragment[] fragments;

        public ToeicdatePagerAdapter(FragmentManager fm, int Count) {
            super(fm);

            this.Count = Count;

            fragments = new Fragment[Count];

            for(int i=0;i<Count;i++){
                fragments[i] = ToeicdateFragment.create(toeicdate_handler, i);
            }
        }

        @Override
        public Fragment getItem(int position) {
            return fragments[position];
        }

        @Override
        public int getCount() {
            return this.Count;
        }
    }
}
