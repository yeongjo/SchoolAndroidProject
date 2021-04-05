package kr.ac.kpu.game.s2016180024.samplegame.game;

import android.graphics.Canvas;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;

import java.util.ArrayList;
import java.util.Random;

import kr.ac.kpu.game.s2016180024.samplegame.framework.GameObject;

public class MainGame {
    private static final String TAG = MainGame.class.getSimpleName();
    //singleton
    static MainGame instance;
    public static MainGame get(){
        if(instance == null){
            instance = new MainGame();
        }
        return instance;
    }

    private ArrayList<GameObject> gameObjects = new ArrayList<>();
    private Player player;
    public float frameTime;
    public static final int BALL_COUNT = 100;


    public void init(AttributeSet attrs, int defStyle) {
        Random random = new Random();
        player = new Player(100, 100, 0, 0);
        gameObjects.add(player);
        for (int i = 0; i < BALL_COUNT; i++){
            float x = random.nextInt(1000);
            float y = random.nextInt(1000);
            float dx = random.nextFloat() * 1000 - 500;
            float dy = random.nextFloat() * 1000 - 500;
            gameObjects.add(new Ball(x,y,dx,dy));
        }
    }

    public void update() {
        for (GameObject o : gameObjects) {
            o.update();
        }
    }

    public void draw(Canvas canvas) {
        for (GameObject o : gameObjects) {
            o.draw(canvas);
        }
    }

    public boolean onTouchEvent(MotionEvent event) {
        int action = event.getAction();
        Log.d(TAG, "onTouchEvent: "+action+", "+event.getX()+", "+ event.getY());
        if(action == MotionEvent.ACTION_DOWN || action == MotionEvent.ACTION_MOVE){
            player.moveTo(event.getX(), event.getY());
            return true;
        }
        return false;
    }
}
