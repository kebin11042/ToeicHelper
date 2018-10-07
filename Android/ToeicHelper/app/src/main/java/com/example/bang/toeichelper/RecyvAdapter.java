package com.example.bang.toeichelper;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.bang.toeichelper.customprogress.CircularProgress;

/**
 * Created by BANG on 2015-01-27.
 */
public class RecyvAdapter extends RecyclerView.Adapter<RecyvAdapter.LoginedViewHolder> {

    private Context context;
    private int Count;
    private int[] nCardViewId;
    private int position;

    public RecyvAdapter(Context context, int Count, int[] nCardViewId) {
        this.context = context;
        this.Count = Count;
        this.nCardViewId = nCardViewId;
        position = 0;
    }

    @Override
    public LoginedViewHolder onCreateViewHolder(final ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(nCardViewId[position], viewGroup, false);

        if(position == 0){
            //Log.w("position", "if ok");
            //0번째 카드뷰 눌렀을 때 토익시험 날짜 정보
            view.findViewById(R.id.ripplelayLoginedToeicDate).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(context, ToeicdateActivity.class);
                    intent.putExtra("toeicdate_h", ((LoginedActivity)context).getToeicdate_handler());
                    context.startActivity(intent);
                }
            });
        }
        //1 내 성적 그래프
        else if(position == 1){
            view.findViewById(R.id.cardvLoginedGraph).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(context, GraphActivity.class);
                    intent.putExtra("member_data", ((LoginedActivity)context).getMember_data());
                    context.startActivity(intent);
                }
            });
        }
        //2 카드뷰 단어장
        else if(position == 2){
           view.findViewById(R.id.cardvLoginedMydict).setOnClickListener(new View.OnClickListener() {
               @Override
               public void onClick(View v) {
                   Intent intent = new Intent(context, MydictActivity.class);
                   intent.putExtra("member_data", ((LoginedActivity)context).getMember_data());
                   ((LoginedActivity) context).startActivityForResult(intent, 7);
               }
           });
        }
        else if(position == 3){
            view.findViewById(R.id.cardvLoginedPopWord).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(context, PopWordActivity.class);
                    context.startActivity(intent);
                }
            });
        }
        //4번째 카드뷰 설정
        else if(position == 4){
            view.findViewById(R.id.cardvLoginedGoalScore).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(context, GoalscoreActivity.class);
                    intent.putExtra("member_data", ((LoginedActivity)context).getMember_data());
                    ((LoginedActivity) context).startActivityForResult(intent, 6);

                }
            });
        }

        position++;

        return new LoginedViewHolder(view);
    }

    //이게 시작되는 조건이 화면에서 보였을때임
    @Override
    public void onBindViewHolder(LoginedViewHolder loginedViewHolder, int i) {
        //loginedViewHolder.txtvCardID.setText("ID = " + i);
        if(i == 0){
            ((LoginedActivity)context).executeTaskImportToeicDate(loginedViewHolder.txtvLoginedToeicdate2, loginedViewHolder.cprogressToeicDate);
        }
        else if(i==4){
            loginedViewHolder.txtvLoginedGoalLC.setText( ((LoginedActivity)context).getMember_data().getGoal_data().getstrLCtotal() + " 개");
            loginedViewHolder.txtvLoginedGoalRC.setText( ((LoginedActivity)context).getMember_data().getGoal_data().getstrRCtotal() + " 개");
        }
        loginedViewHolder.rootView.setRotationX(30);
        loginedViewHolder.rootView.animate().rotationX(0);
    }

    @Override
    public void onViewDetachedFromWindow(LoginedViewHolder holder) {
        super.onViewDetachedFromWindow(holder);
    }

    @Override
    public int getItemCount() {
        return this.Count;
    }

    public final static class LoginedViewHolder extends RecyclerView.ViewHolder {

        View rootView;
        //0
        CircularProgress cprogressToeicDate;
        TextView txtvLoginedToeicdate1;
        TextView txtvLoginedToeicdate2;

        TextView txtvLoginedGoalLC;
        TextView txtvLoginedGoalRC;

        public LoginedViewHolder(View itemView) {
            super(itemView);

            rootView = itemView;
            //position == 0
            cprogressToeicDate = (CircularProgress) itemView.findViewById(R.id.cprogressToeicDate);
            txtvLoginedToeicdate1 = (TextView) itemView.findViewById(R.id.txtvLoginedToeicdate1);
            txtvLoginedToeicdate2 = (TextView) itemView.findViewById(R.id.txtvLoginedToeicdate2);
            //position == 4
            txtvLoginedGoalLC = (TextView) itemView.findViewById(R.id.txtvCardGoalLC);
            txtvLoginedGoalRC = (TextView) itemView.findViewById(R.id.txtvCardGoalRC);
        }
    }
}
