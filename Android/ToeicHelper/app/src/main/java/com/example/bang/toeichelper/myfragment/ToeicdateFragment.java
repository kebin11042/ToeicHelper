package com.example.bang.toeichelper.myfragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.bang.toeichelper.R;
import com.example.bang.toeichelper.mydata.TOEICDATE;
import com.example.bang.toeichelper.mydata.TOEICDATE_Handler;

/**
 * Created by BANG on 2015-01-10.
 */
public class ToeicdateFragment extends Fragment implements View.OnClickListener{

    private TOEICDATE toeicdate;
    private int position;

    private LinearLayout layTh, layDate, layAnn, layApp;
    private TextView txtvTh, txtvDate, txtvAnn, txtvApp;

    public static ToeicdateFragment create(TOEICDATE_Handler toeicdate_handler, int position){
        ToeicdateFragment fragment = new ToeicdateFragment();

        Bundle args = new Bundle();
        args.putSerializable("toeicdate_handler", toeicdate_handler);
        args.putInt("position", position);

        fragment.setArguments(args);

        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        position = getArguments().getInt("position");

        TOEICDATE_Handler h;
        h = (TOEICDATE_Handler)getArguments().getSerializable("toeicdate_handler");

        toeicdate = h.getToeicDateByKey(position);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        ViewGroup rootView = (ViewGroup) inflater.inflate(R.layout.fragment_toeicdate, container, false);

        layTh = (LinearLayout) rootView.findViewById(R.id.layToeicdateTH);
        layDate = (LinearLayout) rootView.findViewById(R.id.layToeicdateDate);
        layAnn = (LinearLayout) rootView.findViewById(R.id.layToeicdateAnn);
        layApp = (LinearLayout) rootView.findViewById(R.id.layToeicdateApp);

        txtvTh = (TextView)  rootView.findViewById(R.id.txtvToeicdateTH);
        txtvDate = (TextView) rootView.findViewById(R.id.txtvToeicdateDay);
        txtvAnn = (TextView) rootView.findViewById(R.id.txtvToeicdateAnnouncement);
        txtvApp = (TextView) rootView.findViewById(R.id.txtvToeicdateApplicationPeriod);

        setTextViews();

        return rootView;
    }

    public void setTextViews(){
        txtvTh.setText(toeicdate.getStrTH());
        txtvDate.setText(toeicdate.getStrDATE());
        txtvAnn.setText(toeicdate.getStrAnnouncement());
        txtvApp.setText(toeicdate.getStrApplicationPeriod());
    }

    public LinearLayout getLayTh() {
        return layTh;
    }

    public LinearLayout getLayDate() {
        return layDate;
    }

    public LinearLayout getLayAnn() {
        return layAnn;
    }

    public LinearLayout getLayApp() {
        return layApp;
    }

    @Override
    public void onClick(View v) {

    }
}
