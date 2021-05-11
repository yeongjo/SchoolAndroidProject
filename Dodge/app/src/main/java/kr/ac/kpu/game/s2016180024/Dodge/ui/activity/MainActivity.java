package kr.ac.kpu.game.s2016180024.Dodge.ui.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;

import kr.ac.kpu.game.s2016180024.Dodge.R;
import kr.ac.kpu.game.s2016180024.Dodge.ui.view.GameView;

public class MainActivity extends AppCompatActivity {

    public static MainActivity self;
    private static final String TAG = MainActivity.class.getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        self = this;
        setContentView(R.layout.activity_main);
        DisplayMetrics metrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(metrics);
        Log.d(TAG, "Density: " + metrics.density + " DPI:" + metrics.densityDpi);
        GameView.MULTIPLIER = metrics.density;
    }
}