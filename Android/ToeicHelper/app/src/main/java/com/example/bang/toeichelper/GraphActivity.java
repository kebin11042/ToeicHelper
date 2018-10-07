package com.example.bang.toeichelper;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.HorizontalScrollView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.bang.toeichelper.mydata.MEMBER_DATA;
import com.example.bang.toeichelper.mydata.MYSCORE;

/**
 * Created by BANG on 2015-05-28.
 */
public class GraphActivity extends ActionBarActivity implements View.OnClickListener{

    private static MEMBER_DATA member_data;

    //
    private ViewTreeObserver vto;

    private int layGraphWidth, layGraphHeight;

    //디스플레이
    private int DP;
    private DisplayMetrics displayMetrics;

    private HorizontalScrollView hscrollvGraph;
    private LinearLayout layGraph, layText;
    private LinearLayout[] layGraphBar;
    private TextView[] txtvDate;

    private int myScoreSize;

    private boolean FIRST_FALG;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_graph);

        FIRST_FALG = true;
        myScoreSize = 0;

        setActionBar();

        Init();
    }

    public void setActionBar(){
        ActionBar actionBar = getSupportActionBar();
        //뒤로가기 버튼 만들기
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setBackgroundDrawable(getResources().getDrawable(R.drawable.actionbar_background_black));
        actionBar.setTitle("토익 성적 보기");
    }

    //뒤로가기 버튼 만들기
    @Override
    public Intent getSupportParentActivityIntent() {
        this.finish();

        return super.getSupportParentActivityIntent();
    }

    public void Init(){

        //화면 정보 받아오기 DP값
        displayMetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        DP = displayMetrics.densityDpi;

        Log.w("DP", "DP = " + DP);

        Intent intent = getIntent();
        member_data = (MEMBER_DATA) intent.getExtras().getSerializable("member_data");

        hscrollvGraph = (HorizontalScrollView) findViewById(R.id.hscrollvGraph);
        layGraph = (LinearLayout) findViewById(R.id.layMyscoreGraph);
        layText = (LinearLayout) findViewById(R.id.layMyscoreText);

    }

    @Override
    protected void onResume() {
        super.onResume();

        vto = layGraph.getViewTreeObserver();

        //layGraph의 크기를 알기 위해 사용한다

        vto.addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {

                if(FIRST_FALG){
                    layGraphWidth = layGraph.getWidth();
                    layGraphHeight = layGraph.getHeight();

                    Log.w("onGlobalLayout", "layGraphWidth = " + layGraphWidth + ", layGraphHeight = " + layGraphHeight);

                    setGraph();

                    FIRST_FALG  = false;
                }
            }
        });

    }

    public void setGraph(){

        myScoreSize = member_data.myScore_handler.getMyScoreSize();

        layGraphBar = new LinearLayout[myScoreSize];
        txtvDate = new TextView[myScoreSize];

        for(int i=0;i<myScoreSize;i++){
            int myScore = member_data.myScore_handler.getMyScorebyIndex(i).getTotal();
            int graphHeight = (int) ( (myScore * (layGraphHeight - (26 * (DP / 160) ) ) ) / 200.0 );
            String myScoreDate = member_data.myScore_handler.getMyScorebyIndex(i).getStrDate();
            myScoreDate = myScoreDate.replace("-", "/");
            Log.w("myScoreDate!!!!!!!!!", "i = " + myScoreDate);

            LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams((35 * (DP / 160) ), graphHeight);
            layoutParams.setMargins(10 * (DP / 160), 0, 10 * (DP / 160), 0);
            layGraphBar[i] = new LinearLayout(GraphActivity.this);
            layGraphBar[i].setLayoutParams(layoutParams);
            layGraphBar[i].setBackgroundColor(Color.parseColor("#dc9898"));

            txtvDate[i] = new TextView(GraphActivity.this);
            txtvDate[i].setLayoutParams(layoutParams);
            txtvDate[i].setText(myScoreDate);

            layGraph.addView(layGraphBar[i]);
            layText.addView(txtvDate[i]);
            txtvDate[i].invalidate();

            layGraphBar[i].setOnClickListener(GraphActivity.this);
            //layGraphBar id가 다 -1임....
            //Log.w("layGraphBar", i + " getID = " + layGraphBar[i].getId());
            layGraphBar[i].setId(3000 + i);
            txtvDate[i].setId(6000 + i);
        }

        layGraph.invalidate();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_toeicdate, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        if(item.getItemId() == R.id.action_plus){
            //Toast.makeText(GraphActivity.this, "토익 성적 추가", Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(GraphActivity.this, MyscoreAddActivity.class);
            intent.putExtra("member_pk", member_data.getPk());
            startActivityForResult(intent, 30);
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onClick(View v) {
        for(int i=0;i<member_data.myScore_handler.getMyScoreSize();i++){
            if(v.getId() == layGraphBar[i].getId()){
                Intent intent = new Intent(GraphActivity.this, MyscoreDetailActivity.class);
                intent.putExtra("position", i);
                intent.putExtra("myscore", member_data.myScore_handler.getMyScorebyIndex(i));
                startActivity(intent);
            }
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == 30){
            if(resultCode == 1){
                MYSCORE myscore = (MYSCORE) data.getExtras().getSerializable("myscore");

                member_data.myScore_handler.addMyScore(myscore);

                //싹 비우고 해야함
                layGraph.removeAllViews();
                layGraph.invalidate();
                GraphActivity.this.setGraph();
            }
        }
    }
}
