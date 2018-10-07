package com.example.bang.toeichelper;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.example.bang.toeichelper.customdlg.MydictAddDlg;
import com.example.bang.toeichelper.customdlg.MydictDeleteDlg;
import com.example.bang.toeichelper.mydata.MEMBER_DATA;
import com.example.bang.toeichelper.mydata.MYDICT;

/**
 * Created by BANG on 2015-05-23.
 */
public class MydictActivity extends ActionBarActivity {

    static private MEMBER_DATA member_data;

    private ListView lisvDicts;
    private DictsAdapter dictsAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_mydict);

        Intent intent = getIntent();
        member_data = (MEMBER_DATA) intent.getExtras().getSerializable("member_data");

        //액션바 설정
        setActionBar();

        Init();
    }

    public void setActionBar(){
        ActionBar actionBar = getSupportActionBar();
        //뒤로가기 버튼 만들기
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setBackgroundDrawable(getResources().getDrawable(R.drawable.actionbar_background_black));
        actionBar.setTitle("내 단어장 목록");
    }

    @Override
    public Intent getSupportParentActivityIntent() {
        finish();

        return super.getSupportParentActivityIntent();
    }

    public void Init(){
        lisvDicts = (ListView) findViewById(R.id.listvMydictDicts);
        dictsAdapter = new DictsAdapter(MydictActivity.this, member_data.myDict_handler.getDictSize() + 1); //마지막 리스트 뷰는 추가의 리스트뷰
        lisvDicts.setAdapter(dictsAdapter);
    }

    public void setResultMemberData(){
        Intent intent = new Intent();
        intent.putExtra("member_data", member_data);
        this.setResult(1, intent);
    }

    public class DictsAdapter extends BaseAdapter {

        private Context context;
        private int Count;

        public DictsAdapter(Context context, int Count) {
            this.context = context;
            this.Count = Count;
        }

        @Override
        public int getCount() {
            return Count;
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
        public View getView(final int position, View convertView, ViewGroup parent) {

            if(position != Count -1){
                LayoutInflater inflater = (LayoutInflater)context.getSystemService(LAYOUT_INFLATER_SERVICE);
                convertView = inflater.inflate(R.layout.listv_mydict, parent, false);

                final TextView txtvDictName = (TextView) convertView.findViewById(R.id.txtvDictName);
                txtvDictName.setText(member_data.myDict_handler.getMyDictbyIndex(position).getName());

                LinearLayout layMyDict = (LinearLayout) convertView.findViewById(R.id.layMydictDict);
                ///////////////////////////////////////////////////////클릭 리스너
                layMyDict.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        //단어장 실행
                        Intent intent = new Intent(MydictActivity.this, WordActivity.class);
                        intent.putExtra("member_data", member_data);
                        intent.putExtra("position", position);
                        startActivity(intent);
                    }
                });
                ///////////////////////////////////////////////////////롱클릭 리스너
                layMyDict.setOnLongClickListener(new View.OnLongClickListener() {
                    @Override
                    public boolean onLongClick(View v) {

                        MydictDeleteDlg dialog = new MydictDeleteDlg(MydictActivity.this, member_data.myDict_handler.getMyDictbyIndex(position).getPk(), txtvDictName.getText().toString());
                        dialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
                            @Override
                            public void onDismiss(DialogInterface dialog) {
                                MydictDeleteDlg mydictDeleteDlg = (MydictDeleteDlg) dialog;

                                if(mydictDeleteDlg.getCANCLE_FLAG()){

                                }
                                else{

                                    //로그인 화면으로의 결과값 설정
                                    setResultMemberData();

                                    //Log.w("다이얼로 dismiss", "딕트 사이즈 = " + member_data.myDict_handler.getDictSize());
                                    member_data.myDict_handler.deleteMydict(member_data.myDict_handler.getMyDictbyIndex(position).getPk());
                                    dictsAdapter = new DictsAdapter(MydictActivity.this, member_data.myDict_handler.getDictSize() + 1);
                                    lisvDicts.setAdapter(dictsAdapter);
                                }
                            }
                        });
                        dialog.show();

                        //Log.w("다이얼로 시작", "딕트 사이즈 = " + member_data.myDict_handler.getDictSize());

                        return true;
                    }
                });
            }
            else{
                LayoutInflater inflater = (LayoutInflater)context.getSystemService(LAYOUT_INFLATER_SERVICE);
                convertView = inflater.inflate(R.layout.listv_mydict_add, parent, false);

                LinearLayout layAddDict = (LinearLayout) convertView.findViewById(R.id.layMydictAdddict);
                layAddDict.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        MydictAddDlg dialog = new MydictAddDlg(MydictActivity.this, Integer.parseInt(member_data.getPk()));
                        dialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
                            @Override
                            public void onDismiss(DialogInterface dialog) {
                                MydictAddDlg mydictAddDlg = (MydictAddDlg) dialog;

                                if (mydictAddDlg.getCANCLE_FLAG()) {

                                } else {
                                    //로그인드 액티비에 줄 데이터 셋
                                    setResultMemberData();


                                    //Log.w("onDissmiss", "CANCLE_FLAG = " + mydictAddDlg.getCANCLE_FLAG());
                                    MYDICT mydict = mydictAddDlg.getMyDict();
                                    member_data.myDict_handler.addMydict(mydict);

                                    dictsAdapter = new DictsAdapter(MydictActivity.this, member_data.myDict_handler.getDictSize() + 1);
                                    lisvDicts.setAdapter(dictsAdapter);
                                }
                            }
                        });
                        dialog.show();
                    }
                });
            }

            return convertView;
        }
    }
}
