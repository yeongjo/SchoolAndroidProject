package kr.ac.kpu.game.s2016180024.cookierun;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import kr.ac.kpu.game.s2016180024.cookierun.game.MainGame;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        new MainGame();
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }
}