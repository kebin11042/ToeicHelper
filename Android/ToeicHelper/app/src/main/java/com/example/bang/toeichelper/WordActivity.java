package com.example.bang.toeichelper;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.alexvasilkov.foldablelayout.FoldableListLayout;
import com.example.bang.toeichelper.customdlg.ColorProgressDlg;
import com.example.bang.toeichelper.mydata.MEMBER_DATA;
import com.example.bang.toeichelper.mydata.MYDICT;
import com.example.bang.toeichelper.mydata.MYSCORE;
import com.example.bang.toeichelper.mydata.MYWORD;
import com.example.bang.toeichelper.mydata.MyWord_Handler;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;

/**
 * Created by BANG on 2015-06-17.
 */
public class WordActivity extends ActionBarActivity {

    //사전 API
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
    //

    private MEMBER_DATA member_data;
    private MYDICT MyDict;
    private int MyDictPosition;
    private int MyDictPk;

    private FoldableListLayout layWord;
    private WordAdapter wordAdapter;

    private TextView txtvWordCounter;

    private AddMyWord task_AddMyWord;
    private ImportMyWord task_ImportMyWord;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_word);

        Intent intent = getIntent();
        member_data = (MEMBER_DATA) intent.getExtras().getSerializable("member_data");
        MyDictPosition = intent.getExtras().getInt("position");
        MyDict = member_data.myDict_handler.getMyDictbyIndex(MyDictPosition);
        MyDictPk = MyDict.getPk();

        setWordData();

        setActionBar();

        Init();
    }

    public void setWordData(){
        if(MyDict.myWord_handler == null){
            MyDict.myWord_handler = new MyWord_Handler();
        }
        else{

        }
    }

    public void setActionBar(){
        ActionBar actionBar = getSupportActionBar();
        getSupportActionBar().setElevation(0);  // 요거 그림자 없애기
        //뒤로가기 버튼 만들기
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setBackgroundDrawable(getResources().getDrawable(R.drawable.actionbar_background_black));
        actionBar.setTitle(MyDict.getName());
    }

    public void Init(){
        layWord = (FoldableListLayout) findViewById(R.id.foldlayWord);

        txtvWordCounter = (TextView) findViewById(R.id.txtvWordCounter);


        task_ImportMyWord = new ImportMyWord();
        task_ImportMyWord.execute("");

    }

    //뒤로가기 버튼
    @Override
    public Intent getSupportParentActivityIntent() {
        this.finish();

        return super.getSupportParentActivityIntent();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        getMenuInflater().inflate(R.menu.menu_toeicdate, menu);

        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        if(item.getItemId() == R.id.action_plus){
            final Intent intent = new Intent(PICK_RESULT_ACTION);
            //intent.putExtra(EXTRA_QUERY, "hello"); //Search Query
            intent.putExtra(EXTRA_FULLSCREEN, true); //
            //intent.putExtra(EXTRA_HEIGHT, windowH - (28 * (DP / 160)) ); //400pixel, if you don't specify, fill_parent"
            //intent.putExtra(EXTRA_GRAVITY, Gravity.BOTTOM);
            //intent.putExtra(EXTRA_MARGIN_LEFT, 100);

            if(isIntentAvailable(WordActivity.this, intent)) {
                startActivityForResult(intent, 5);
            }
            else{
                Toast.makeText(WordActivity.this, "ColorDict 어플을 먼저 설치해 주세요", Toast.LENGTH_SHORT).show();
            }
        }

        return super.onOptionsItemSelected(item);
    }

    public static boolean isIntentAvailable(Context context, Intent intent) {
        final PackageManager packageManager = context.getPackageManager();
        List list = packageManager.queryIntentActivities(intent, PackageManager.MATCH_DEFAULT_ONLY);
        return list.size() > 0;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(requestCode == 5){
            if(resultCode == RESULT_OK){
                String strWord = data.getStringExtra(Intent.EXTRA_SUBJECT);
                String strSub = data.getStringExtra(Intent.EXTRA_TEXT);

                //Log.w("strWord", strWord);
                //Log.w("strSub", cut[2]);

                boolean OVERLAP_FLAG = false;

                for(int i=0;i<MyDict.myWord_handler.getMyWordsSize();i++){
                    if(strWord.equals(MyDict.myWord_handler.getMyWordbyIndex(i).getStrWord())){
                        OVERLAP_FLAG = true;
                        break;
                    }
                }

                if(OVERLAP_FLAG){
                    Toast.makeText(WordActivity.this, "이미 등록되어 있는 단어 입니다.", Toast.LENGTH_SHORT).show();
                }
                else{
                    String[] cut = strSub.split("\n");
                    String cuttedSub = "";
                    for(int i=1;i<cut.length;i++){
                        cuttedSub += cut[i];
                        if(i > 5){
                            break;
                        }
                    }

                    Log.w("cuttedSub = ", cuttedSub);
                    task_AddMyWord = new AddMyWord(strWord, cuttedSub);
                    task_AddMyWord.execute("");
                }
            }
        }

        super.onActivityResult(requestCode, resultCode, data);
    }

    public class WordAdapter extends BaseAdapter {

        private Context context;
        private int Count;
        private boolean NULL_FLAG;

        public WordAdapter(Context context, int Count) {
            this.context = context;
            this.Count = Count;

            if(Count == 0){
                NULL_FLAG = true;
                this.Count++;
            }
            else{
                NULL_FLAG = false;
            }
        }

        @Override
        public int getCount() {
            return this.Count;
        }

        @Override
        public Object getItem(int position) {
            return null;
        }

        @Override
        public long getItemId(int position) {
            return MyDict.myWord_handler.getMyWordbyIndex(position).getnPublicDictPk();
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {

            LayoutInflater inflater = (LayoutInflater)context.getSystemService(LAYOUT_INFLATER_SERVICE);

            if(NULL_FLAG){
                convertView = inflater.inflate(R.layout.foldlist_nothing, parent, false);
            }
            else{
                convertView = inflater.inflate(R.layout.foldlist_word, parent, false);

                TextView txtvWord = (TextView) convertView.findViewById(R.id.txtvFoldlistWord);
                TextView txtvSub = (TextView) convertView.findViewById(R.id.txtvFoldlistSub);

                txtvWord.setText(MyDict.myWord_handler.getMyWordbyIndex(position).getStrWord());
                txtvSub.setText(MyDict.myWord_handler.getMyWordbyIndex(position).getStrSub());
            }

            return convertView;
        }
    }




    public class ImportMyWord extends AsyncTask<String, Integer, String> {

        //필요한 POST

        private String strURL = "http://kebin1104.dothome.co.kr/ToeicHelper/ImportWord.php";

        private ColorProgressDlg progressDlg;

        private int nWordSize;

        public ImportMyWord() {
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            progressDlg = new ColorProgressDlg(WordActivity.this, "Loading...");
            progressDlg.setCancelable(false);
            progressDlg.show();
        }

        @Override
        protected String doInBackground(String... params) {
            String s = "";

            try {
                URL url = new URL(strURL);

                HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();

                if(httpURLConnection != null){
                    httpURLConnection.setDefaultUseCaches(false);
                    httpURLConnection.setDoInput(true);
                    httpURLConnection.setDoOutput(true);
                    httpURLConnection.setRequestMethod("POST");

                    //xml파일로 요청
                    httpURLConnection.setRequestProperty("Accept", "application/xml");


                    //--------------------------
                    //  서버로 값 전송
                    //--------------------------

                    StringBuffer buffer = new StringBuffer();

                    buffer.append("dict_pk=").append(MyDictPk + "");

                    OutputStreamWriter outStream = new OutputStreamWriter(httpURLConnection.getOutputStream(), "UTF-8");
                    PrintWriter writer = new PrintWriter(outStream);
                    writer.write(buffer.toString());
                    writer.flush();


                    //수신
                    InputStreamReader inputStreamReader = new InputStreamReader(httpURLConnection.getInputStream(), "UTF-8");
                    BufferedReader bufferedReader = new BufferedReader(inputStreamReader);

                    nWordSize = Integer.parseInt(bufferedReader.readLine());
                    for(int i=0;i<nWordSize;i++){
                        String strPk = bufferedReader.readLine();
                        String strWord = bufferedReader.readLine();
                        String strSub = bufferedReader.readLine();

                        MYWORD d = new MYWORD(Integer.parseInt(strPk), strWord, strSub);

                        MyDict.myWord_handler.AddMyWord(d);
                    }
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

            progressDlg.dismiss();

            wordAdapter = new WordAdapter(WordActivity.this, MyDict.myWord_handler.getMyWordsSize());
            layWord.setAdapter(wordAdapter);

            txtvWordCounter.setText(MyDict.myWord_handler.getMyWordsSize() + " 개");
        }
    }


    public class AddMyWord extends AsyncTask<String, Integer, String> {

        //필요한 POST

        private String strURL = "http://kebin1104.dothome.co.kr/ToeicHelper/AddWord.php";

        private ColorProgressDlg progressDlg;

        private int nMyDictPk;
        private int nPublicDictPk;
        private String strWord;
        private String strSub;

        public AddMyWord(String strWord, String strSub) {
            this.nMyDictPk = MyDict.getPk();
            this.nPublicDictPk = -1;
            this.strWord = strWord;
            this.strSub = strSub;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            progressDlg = new ColorProgressDlg(WordActivity.this, "Loading...");
            progressDlg.setCancelable(false);
            progressDlg.show();
        }

        @Override
        protected String doInBackground(String... params) {
            String s = "";

            try {
                URL url = new URL(strURL);

                HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();

                if(httpURLConnection != null){
                    httpURLConnection.setDefaultUseCaches(false);
                    httpURLConnection.setDoInput(true);
                    httpURLConnection.setDoOutput(true);
                    httpURLConnection.setRequestMethod("POST");

                    //xml파일로 요청
                    httpURLConnection.setRequestProperty("Accept", "application/xml");


                    //--------------------------
                    //  서버로 값 전송
                    //--------------------------

                    StringBuffer buffer = new StringBuffer();

                    buffer.append("dict_pk=").append(nMyDictPk + "&");
                    buffer.append("word=").append(strWord + "&");
                    buffer.append("sub=").append(strSub);

                    OutputStreamWriter outStream = new OutputStreamWriter(httpURLConnection.getOutputStream(), "UTF-8");
                    PrintWriter writer = new PrintWriter(outStream);
                    writer.write(buffer.toString());
                    writer.flush();


                    //수신
                    InputStreamReader inputStreamReader = new InputStreamReader(httpURLConnection.getInputStream(), "UTF-8");
                    BufferedReader bufferedReader = new BufferedReader(inputStreamReader);

                    String strResult = bufferedReader.readLine();
                    if(strResult.equals("error")){
                        s = strResult;
                    }
                    else{
                        nPublicDictPk = Integer.parseInt(bufferedReader.readLine());
                        //Log.w("readLine()", bufferedReader.readLine());
                    }
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

            progressDlg.dismiss();

            if(s.equals("error")){

            }
            else{
                MYWORD d = new MYWORD(nPublicDictPk, strWord, strSub);
                MyDict.myWord_handler.AddMyWord(d);

                wordAdapter = new WordAdapter(WordActivity.this, MyDict.myWord_handler.getMyWordsSize());
                layWord.setAdapter(wordAdapter);

                txtvWordCounter.setText(MyDict.myWord_handler.getMyWordsSize() + " 개");

                Toast.makeText(WordActivity.this, "단어가 추가 되었습니다.", Toast.LENGTH_SHORT).show();
            }
        }
    }
}
