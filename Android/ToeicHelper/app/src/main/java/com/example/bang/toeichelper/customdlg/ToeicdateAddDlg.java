package com.example.bang.toeichelper.customdlg;

import android.accounts.Account;
import android.accounts.AccountManager;
import android.app.Dialog;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.CalendarContract;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.bang.toeichelper.R;
import com.example.bang.toeichelper.ToeicdateActivity;
import com.example.bang.toeichelper.mydata.TOEICDATE;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;

/**
 * Created by BANG on 2015-03-20.
 */
public class ToeicdateAddDlg extends Dialog implements View.OnClickListener{

    private EditText edtxTitle;
    private EditText edtxSub;

    private Button btnCancle;
    private Button btnOk;

    private String strTitle, strSub;
    private long lDate;

    private int callID;

    public ToeicdateAddDlg(Context context) {
        super(context);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.dialog_toeicdate_addcalendar);

        Init();
    }

    public void Init(){

        strTitle = "토익 시험!!";
        strSub = "토익 시험 날짜 입니다.";

        edtxTitle = (EditText) findViewById(R.id.edtxToeicdateDlgTitle);
        edtxSub = (EditText) findViewById(R.id.edtxToeicdateDlgSub);
        btnCancle = (Button) findViewById(R.id.btnToeicdateDlgCancle);
        btnOk = (Button) findViewById(R.id.btnToeicdateDlgOk);

        btnCancle.setOnClickListener(ToeicdateAddDlg.this);
        btnOk.setOnClickListener(ToeicdateAddDlg.this);
    }

    public void setLongDate(long lDate){
        this.lDate = lDate;
    }

    @Override
    public void onClick(View v) {
        if(v.getId() == btnCancle.getId()){
            this.dismiss();
        }
        else if(v.getId() == btnOk.getId()){

            if(!edtxTitle.getText().toString().equals("")){
                strTitle = edtxTitle.getText().toString();
            }

            if(!edtxSub.getText().toString().equals("")){
                strSub = edtxSub.getText().toString();
            }

            //일정 추가 부분
            ///////////////////////////////////////////////////////////////////

            //계정 불러오기
            AccountManager accountManager = AccountManager.get(getContext());
            Account[] accounts = accountManager.getAccounts();
            int accLength = accounts.length;

            Account acc;
            String accName = "";
            String accType = "";
            for(int i=0;i<accLength;i++){
                acc = accounts[i];
                //Log.w("accName", acc.type);
                if(acc.type.equals("com.google")){
                    accName = acc.name;
                    accType = acc.type;

                   // Log.w("account info", accName + "  , " + accType);

                    break;
                }
            }
            /////////////////////////////////////////////////////////////////////

            //캘린더 가져오기
            final String[] EVENT_PROJECTION = new String[]{
                    CalendarContract.Calendars._ID,
                    CalendarContract.Calendars.CALENDAR_DISPLAY_NAME,
                    CalendarContract.Calendars.OWNER_ACCOUNT
            };
            Cursor cur = null;
            ContentResolver cr = getContext().getContentResolver();
            Uri uri = CalendarContract.Calendars.CONTENT_URI;
            String selection = "((" + CalendarContract.Calendars.ACCOUNT_NAME + " =?)AND ("
                    + CalendarContract.Calendars.ACCOUNT_TYPE + " =?) AND ("
                    + CalendarContract.Calendars.OWNER_ACCOUNT + " = ?))";
            String[] selectionArgs = new String[] {accName, accType, accName};
            cur = cr.query(uri, EVENT_PROJECTION, selection, selectionArgs, null);


            while(!cur.isLast()){
                cur.moveToNext();
                //Log.w("Calendars info", "call ID = " + cur.getInt(0));
                callID = cur.getInt(0);
            }


            cr = getContext().getContentResolver();
            ContentValues cv = new ContentValues();

            //이벤트 이름
            cv.put(CalendarContract.Events.TITLE, strTitle);
            //이벤트 설명
            cv.put(CalendarContract.Events.DESCRIPTION, strSub);
            //캘린더 아이디
            cv.put(CalendarContract.Events.CALENDAR_ID, callID);
            //이벤트 시작 시간 입력, 테스트코드이므로 바꿔서 사용 가능
            //cv.put(CalendarContract.Events.DTSTART, System.currentTimeMillis());
            cv.put(CalendarContract.Events.DTSTART, lDate);
            //Log.w("currentTimeMillis()", System.currentTimeMillis() + "");
            //이벤트 종료 시간
            cv.put(CalendarContract.Events.DTEND, lDate);
            //휴대폰의 기본 지정된 시간대를 이용
            cv.put(CalendarContract.Events.EVENT_TIMEZONE, TimeZone.getDefault().getID());
            //Uri로 위의 값들을 추가
            uri = cr.insert(CalendarContract.Events.CONTENT_URI, cv);

            Toast.makeText(getContext(), "일정이 추가 되었습니다.", Toast.LENGTH_SHORT).show();

            this.dismiss();
        }
    }
}
