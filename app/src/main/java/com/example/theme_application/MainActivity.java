package com.example.theme_application;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.media.Image;
import android.text.Editable;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.os.Bundle;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;


public class MainActivity extends AppCompatActivity {
    public ArrayList<TrumpCard> trumpL=new ArrayList<TrumpCard>();
    public TrumpCard[] tempTcL=new TrumpCard[2];
    public int gameCnt=0,tempCard=-1,ownCard=0,miss=0,memberTurn=0;
    public Boolean startBool=false;
    public ArrayList<Integer> memberL=new ArrayList<Integer>();
    LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(210 ,210);



    class TrumpCard{
        ImageView imgv;
        int num,imgId;
        TrumpCard(int number,ImageView argImgv){
            num=number;
            imgv=argImgv;
         cardReverse();
        }
        void cardTurn(){
            imgv.setImageResource(getResources().getIdentifier("s"+ num%13 , "drawable", getPackageName()));
            imgv.setLayoutParams(layoutParams);
        }
        void cardReverse(){
            imgv.setImageResource(getResources().getIdentifier("sv" , "drawable", getPackageName()));
            imgv.setLayoutParams(layoutParams);
        }
    }
    void cardShuffle(){
        Random rm=new Random();
        for (int i=0;i<100;i++) {//シャッフル
            int a1= rm.nextInt(trumpL.size()-1);
            int a2= rm.nextInt(trumpL.size()-1);
            TrumpCard temp=trumpL.get(a1);
            trumpL.set(a1,trumpL.get(a2));
            trumpL.set(a2,temp);
        }
    }
    void setView(Boolean judge){
        for (int i = 0; i < trumpL.size(); i++) {
            int viewId = getResources().getIdentifier("hlayout" + (i+4) / 4, "id", getPackageName());
            LinearLayout hlay = (LinearLayout) findViewById(viewId);
            if(judge)
            hlay.addView(trumpL.get(i).imgv);
            else
            hlay.removeView(trumpL.get(i).imgv);

        }


        }
    void setDispView(){
        TextView tv=(TextView) findViewById(getResources().getIdentifier("gameCnt", "id", getPackageName()));
        tv.setText("試合回数：:"+gameCnt);
        tv.setVisibility(View.VISIBLE);
        String scoreStr="";
        for(int i=0;i<memberL.size();i++)
            scoreStr+="["+(i+1)+"]:"+memberL.get(i);

        tv=(TextView) findViewById(getResources().getIdentifier("memberScoreText", "id", getPackageName()));
        tv.setText("Score："+scoreStr);
        tv.setVisibility(View.VISIBLE);

        tv=(TextView) findViewById(getResources().getIdentifier("dispTurn", "id", getPackageName()));
        tv.setText((memberTurn+1)+"人目");
        tv.setVisibility(View.VISIBLE);

    }
    void genCard(){
        for(int i=0;i<tempTcL.length-1;i++)
            tempTcL[i]=new TrumpCard(-1,new ImageView(this));

        for(int j=0;j<2;j++) {
            for (int i = 0; i < 13; i++) {
                trumpL.add(new TrumpCard(i+j*13,new ImageView(this)));
                int size = trumpL.size() - 1;

                trumpL.get(size).imgv.setOnClickListener(
                        new View.OnClickListener() {
                            int num=trumpL.get(trumpL.size()-1).num;
                            TrumpCard tc=trumpL.get(trumpL.size()-1);
                            public void onClick(View v) {
                                if(!tc.equals(tempTcL[0])) {//同じカードをクリックしていない

                                    switch (tempCard) {
                                        case -1://1枚目
                                            trumpL.get(trumpL.indexOf(tc)).cardTurn();//めくり
                                            tempTcL[0] = tc;
                                            tempCard =-2;
                                            //Toast.makeText(MainActivity.this, "1枚目：" + num + "がクリックされました", Toast.LENGTH_LONG).show();
                                            break;
                                        case -2:
                                            trumpL.get(trumpL.indexOf(tc)).cardTurn();//めくり
                                            gameCnt++;

                                            tempTcL[1]=tc;
                                            if (tempTcL[0].num % 13 == num % 13) {//同じ番号

                                                for(TrumpCard item:tempTcL)
                                                    item.imgv.setVisibility(View.INVISIBLE);

                                                Toast.makeText(MainActivity.this, "あたりです", Toast.LENGTH_LONG).show();
                                                ownCard+=2;
                                                memberL.set(memberTurn,(memberL.get(memberTurn)+2));
                                                tempCard = -1;
                                                setDispView();



                                                if(ownCard>=trumpL.size()){
                                                    TextView victv=(TextView) findViewById(getResources().getIdentifier("dispeventtext", "id", getPackageName()));

                                                    Button vicbtn=(Button) findViewById(getResources().getIdentifier("trybtn", "id", getPackageName()));
                                                    String scoreStr="勝利： ";
                                                    int max=0;
                                                    for(int item:memberL)
                                                        if(item>=max)
                                                            max=item;
                                                    for(int i=0;i<memberL.size();i++){
                                                            if(memberL.get(i).intValue()==max){
                                                                scoreStr+=(1+i)+"番 ";
                                                        }
                                                    }

                                                    victv.setText(scoreStr);
                                                    victv.setVisibility(View.VISIBLE);
                                                    vicbtn.setVisibility(View.VISIBLE);
                                                }
                                            } else {

                                                //  Toast.makeText(MainActivity.this, "はずれです", Toast.LENGTH_LONG).show();

                                                memberTurn++;
                                                memberTurn%=memberL.size();

                                                tempCard =-3;
                                            }

                                            setDispView();
                                            break;
                                        case -3:

                                            for(TrumpCard item:tempTcL)
                                                item.cardReverse();
                                            tempTcL[0]=null;
                                            tempTcL[1]=null;
                                            tempCard = -1;
                                            break;
                                    }
                                }else{
                                    Toast.makeText(MainActivity.this, "同じカードが選択されました", Toast.LENGTH_LONG).show();
                                }
                            }});

            }
        }
    }


    @Override
    protected void onCreate(final Bundle savedInstanceState) {





        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
//

        Button btn=findViewById(R.id.trybtn);
        btn.setOnClickListener(new View.OnClickListener()  {

            @Override
            public void onClick(View v) {
             //   Toast.makeText(MainActivity.this, "あたりです", Toast.LENGTH_LONG).show();
                ownCard=0;
                setView(false);
                trumpL=new ArrayList<TrumpCard>();
               genCard();
                cardShuffle();
                setView(true);
                TextView victv=(TextView) findViewById(getResources().getIdentifier("dispeventtext", "id", getPackageName()));
                Button vicbtn=(Button) findViewById(getResources().getIdentifier("trybtn", "id", getPackageName()));
                TextView tv=(TextView) findViewById(getResources().getIdentifier("gameCnt", "id", getPackageName()));

                tv.setText("試合回数："+gameCnt);
                victv.setVisibility(View.INVISIBLE);
                vicbtn.setVisibility(View.INVISIBLE);
                memberTurn=0;
                gameCnt=0;
                int total=memberL.size();
                memberL=new ArrayList<Integer>();
                for (int i = 0; i < total; i++)
                    memberL.add(0);
                setDispView();

            }

        });


        Button startBtn=findViewById(R.id.startBtn);
        startBtn.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v) {
                EditText et=(EditText)findViewById(R.id.editText);
                if(!et.getText().toString().equals("")) {
                    for (int i = 0; i < Integer.parseInt(et.getText().toString()); i++)
                        memberL.add(0);
                    Toast.makeText(MainActivity.this, memberL.size() + "人です", Toast.LENGTH_LONG).show();
                    ((Button)findViewById(R.id.startBtn)).setVisibility(View.INVISIBLE);
                    ((TextView)findViewById(R.id.startText)).setVisibility(View.INVISIBLE);
                    ((EditText)findViewById(R.id.editText)).setVisibility(View.INVISIBLE);

                    genCard();
                    cardShuffle();
                    setView(true);
                    setDispView();
                }
        }});
    }

}
