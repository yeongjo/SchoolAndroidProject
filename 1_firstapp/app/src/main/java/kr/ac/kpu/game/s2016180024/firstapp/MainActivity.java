package kr.ac.kpu.game.s2016180024.firstapp;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    private TextView mainTextView;
    private ImageView mainImageView;
    private int currentPageIdx = 0;
    private int totalPageIdx = 5;

    int[] imageIds = {R.drawable.cat1, R.drawable.cat2, R.drawable.cat3, R.drawable.cat4, R.drawable.cat5};
    private ImageButton prevButton;
    private ImageButton nextButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mainTextView = findViewById(R.id.mainTextView);
        mainImageView = findViewById(R.id.mainImageView);
        prevButton = findViewById(R.id.prevButton);
        nextButton = findViewById(R.id.nextButton);

        PrintImageState();

        prevButton.setOnTouchListener((v, event) -> {
            switch(event.getAction()) {
                case MotionEvent.ACTION_DOWN :
                    pp("터치 - 다운");
                    ((ImageView)v).setImageResource(R.drawable.prev_p);
                    break;
                case MotionEvent.ACTION_MOVE :
                    pp("터치 - 무브");
                    break;
                case MotionEvent.ACTION_UP   :
                    pp("터치 - 업  ");
                    ((ImageView)v).setImageResource(R.drawable.prev);
                    break;
            }
            return false;
        });

        nextButton.setOnTouchListener((v, event) -> {
            switch(event.getAction()) {
                case MotionEvent.ACTION_DOWN :
                    pp("터치 - 다운");
                    ((ImageView)v).setImageResource(R.drawable.next_p);
                    break;
                case MotionEvent.ACTION_MOVE :
                    pp("터치 - 무브");
                    break;
                case MotionEvent.ACTION_UP   :
                    pp("터치 - 업  ");
                    ((ImageView)v).setImageResource(R.drawable.next);
                    break;
            }
            return false;
        });
    }

    public void onButtonPrev(View view){
        --currentPageIdx;
        if(currentPageIdx <= 0){
            currentPageIdx = 0;
            prevButton.setImageResource(R.drawable.prev_d);
        }else{
            nextButton.setImageResource(R.drawable.next);
        }
        PrintImageState();
        mainImageView.setImageResource(imageIds[currentPageIdx]);
    }

    public void onButtonNext(View view){
        ++currentPageIdx;
        if(currentPageIdx >= totalPageIdx - 1){
            currentPageIdx = totalPageIdx - 1;
            nextButton.setImageResource(R.drawable.next_d);
        }else{
            prevButton.setImageResource(R.drawable.prev);
        }
        PrintImageState();
        mainImageView.setImageResource(imageIds[currentPageIdx]);
    }

    void PrintImageState(){
        mainTextView.setText((currentPageIdx+1)+"/"+totalPageIdx);
    }

    void pp(String t){
        System.out.println(t);
    }
}