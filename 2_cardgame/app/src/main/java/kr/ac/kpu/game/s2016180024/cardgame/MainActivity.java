package kr.ac.kpu.game.s2016180024.cardgame;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import java.util.Random;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = MainActivity.class.getSimpleName();
    private static final int [] buttonIds={
            R.id.card_00, R.id.card_01, R.id.card_02, R.id.card_03,
            R.id.card_10, R.id.card_11, R.id.card_12, R.id.card_13,
            R.id.card_20, R.id.card_21, R.id.card_22, R.id.card_23,
            R.id.card_30, R.id.card_31, R.id.card_32, R.id.card_33,
            R.id.card_40, R.id.card_41, R.id.card_42, R.id.card_43,
    };

    private int[] cards={
            R.mipmap.pae0,R.mipmap.pae0,R.mipmap.pae1,R.mipmap.pae1,
            R.mipmap.pae2,R.mipmap.pae2,R.mipmap.pae3,R.mipmap.pae3,
            R.mipmap.pae4,R.mipmap.pae4,R.mipmap.pae5,R.mipmap.pae5,
            R.mipmap.pae6,R.mipmap.pae6,R.mipmap.pae7,R.mipmap.pae7,
            R.mipmap.pae8,R.mipmap.pae8,R.mipmap.pae9,R.mipmap.pae9,
    };
    ImageButton prevButton;
    private int visibleCardCount;

    public void setFlips(int flips) {
        this.flips = flips;
        scoreTextView.setText("Flips: " + flips);
    }

    int flips;
    private TextView scoreTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        scoreTextView = findViewById(R.id.scoreTextView);

        startGame();
    }

    public void onBtnCard(View view) {
        if(view == prevButton){
            return;
        }

        int prevCard = 0;
        if(prevButton != null) {
            prevButton.setImageResource(R.mipmap.pae_back);
            prevCard = (Integer) prevButton.getTag();
        }

        int buttonIndex=getButtonIndex((view.getId()));
        Log.d(TAG,"onBtnCard() has been called ID="+view.getId()+"buttonIndex="+buttonIndex);

        int card = cards[buttonIndex];
        ImageButton imageButton = (ImageButton)view;
        imageButton.setImageResource(card);

        if(card == prevCard){
            imageButton.setVisibility(View.INVISIBLE);
            prevButton.setVisibility(View.INVISIBLE);
            prevButton = null;
            visibleCardCount -= 2;
            if(visibleCardCount == 0){
                askRestart();
            }
            return;
        }
        if(prevButton != null) {
            setFlips(flips + 1);
        }
        prevButton = imageButton;
    }
    private int getButtonIndex(int resId){
        for(int i = 0; i < buttonIds.length; i++){
            if(buttonIds[i] == resId){
                return i;
            }
        }
        return -1;
    }

    public void onBtnRestart(View view) {
        askRestart();
    }

    void askRestart(){
        // 알람 다이어그램을 사용해서 팜업질문을 할 수 있다.
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Restart");
        builder.setMessage("Do you want restart game?");
        builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                startGame();
            }
        });

        builder.setNegativeButton("No", null);
        AlertDialog alert = builder.create();
        alert.show();
    }

    private void startGame() {
        Random random = new Random();
        for(int i = 0; i< cards.length; i++){
            int ri = random.nextInt(cards.length);
            int t = cards[i];
            cards[i] = cards[ri];
            cards[ri] = t;
        }

        for (int i = 0; i < buttonIds.length; i++){
            ImageButton b = findViewById(buttonIds[i]);
            b.setTag(cards[i]);
            b.setVisibility(View.VISIBLE);
            b.setImageResource(R.mipmap.pae_back);
        }

        prevButton = null;
        visibleCardCount = cards.length;
        setFlips(0);
    }
}