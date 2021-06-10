package kr.ac.kpu.game.s2016180024.Dodge.game;

import android.app.AlertDialog;
import android.content.SharedPreferences;
import android.graphics.Canvas;
import android.media.MediaPlayer;
import android.util.Log;
import android.view.MotionEvent;
import android.widget.EditText;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Random;
import java.util.Stack;

import kr.ac.kpu.game.s2016180024.Dodge.R;
import kr.ac.kpu.game.s2016180024.Dodge.framework.GameObject;
import kr.ac.kpu.game.s2016180024.Dodge.framework.Recyclable;
import kr.ac.kpu.game.s2016180024.Dodge.framework.Scene;
import kr.ac.kpu.game.s2016180024.Dodge.framework.Sound;
import kr.ac.kpu.game.s2016180024.Dodge.game.item.AttackRangeItem;
import kr.ac.kpu.game.s2016180024.Dodge.game.item.ColliableItem;
import kr.ac.kpu.game.s2016180024.Dodge.game.item.Item;
import kr.ac.kpu.game.s2016180024.Dodge.game.item.LifeStealItem;
import kr.ac.kpu.game.s2016180024.Dodge.game.item.statsItem;
import kr.ac.kpu.game.s2016180024.Dodge.ui.activity.MainActivity;
import kr.ac.kpu.game.s2016180024.Dodge.ui.view.GameView;
import kr.ac.kpu.game.s2016180024.Dodge.utils.CollisionHelper;

public class MainGame {
    private static final String TAG = MainGame.class.getSimpleName();
    private static MainGame self;
    private static MediaPlayer lastPlayedMediaPlayer;
    private static boolean isInitialized = false;

    public float frameTime;

    public static MainGame get() {
        if (self == null) {
            self = new MainGame();
        }
        return self;
    }

    public static void playMediaPlayer(MediaPlayer media){
        lastPlayedMediaPlayer = media;
        media.start();
    }
    public static void stopMediaPlayer(MediaPlayer media){
        lastPlayedMediaPlayer = null;
        media.stop();
    }
    public static void pauseMediaPlayer(MediaPlayer media){
        lastPlayedMediaPlayer = null;
        media.pause();
    }

    public boolean initResources() {
        Scene scene = Scene.getActiveScene();
        if(scene == null){
            return false;
        }
        isInitialized = true;
        return scene.initResources();
    }

    public void update(){
        if(!isInitialized){
            return;
        }
        Scene scene = Scene.getActiveScene();
        if(scene == null){
            return;
        }
        scene.update();
    }

    public void draw(Canvas canvas) {
        if(!isInitialized){
            return;
        }
        Scene scene = Scene.getActiveScene();
        if(scene == null){
            return;
        }
        scene.draw(canvas);

    }

    public boolean onTouchEvent(MotionEvent event) {
        if(!isInitialized){
            return false;
        }
        Scene scene = Scene.getActiveScene();
        if(scene == null){
            return false;
        }
        return scene.onTouchEvent(event);
    }


    public static void pauseGame() {
        MediaPlayer mediaPlayer = lastPlayedMediaPlayer;
        if(mediaPlayer!=null) {
            mediaPlayer.stop();
        }

        Scene scene = Scene.getActiveScene();
        if(scene == null){
            return;
        }
        scene.onPause();
    }

    public static void resumeGame() {
        MediaPlayer mediaPlayer = lastPlayedMediaPlayer;
        if(mediaPlayer!=null) {
            mediaPlayer.setOnPreparedListener(MediaPlayer::start);
            mediaPlayer.prepareAsync();
        }

        Scene scene = Scene.getActiveScene();
        if(scene == null){
            return;
        }
        scene.onResume();
    }

    public boolean handleBackKey() {
        return Scene.popScene() != null;
    }
}
