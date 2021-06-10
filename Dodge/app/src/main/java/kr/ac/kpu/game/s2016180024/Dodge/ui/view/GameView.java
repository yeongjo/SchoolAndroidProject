package kr.ac.kpu.game.s2016180024.Dodge.ui.view;

import android.content.Context;
import android.graphics.Canvas;
import android.media.MediaPlayer;
import android.util.AttributeSet;
import android.util.Log;
import android.view.Choreographer;
import android.view.MotionEvent;
import android.view.View;

import androidx.annotation.Nullable;

import kr.ac.kpu.game.s2016180024.Dodge.framework.Sound;
import kr.ac.kpu.game.s2016180024.Dodge.game.MainGame;

public class GameView extends View {
    private static final String TAG = GameView.class.getSimpleName();

    public static float MULTIPLIER;
    //    private Ball b1, b2;

    private long lastFrame;
    private boolean isRunning = true;
    public static GameView self;

    public GameView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        GameView.self = this;
        Sound.init(context);
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        //super.onSizeChanged(w, h, oldw, oldh);
        Log.d(TAG, "onSize: " + w + "," + h);
        MainGame game = MainGame.get();
        boolean justInitialized = game.initResources();
        if (justInitialized) {
            requestCallback();
        }
    }

    private void update() {
        MainGame game = MainGame.get();
        game.update();

        invalidate();
    }

    private void requestCallback() {
        if (!isRunning) {
            Log.d(TAG, "Not shown. Not calling Choreographer.postFrameCallback()");
            return;
        }
        Choreographer.getInstance().postFrameCallback(new Choreographer.FrameCallback() {
            @Override
            public void doFrame(long time) {
                if (lastFrame == 0) {
                    lastFrame = time;
                }
                MainGame game = MainGame.get();
                game.frameTime = (float) (time - lastFrame) / 1_000_000_000;
                update();
                lastFrame = time;
                requestCallback();
            }
        });
    }

    @Override
    protected void onDraw(Canvas canvas) {
        MainGame game = MainGame.get();
        game.draw(canvas);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        MainGame game = MainGame.get();
        return game.onTouchEvent(event);
    }


    public void pauseGame() {
        isRunning = false;
        MediaPlayer mediaPlayer = MainGame.get().getMainBgmMediaPlayer();
        if(mediaPlayer!=null) {
            mediaPlayer.stop();
        }
    }

    public void resumeGame() {
        if (!isRunning) {
            isRunning = true;
            lastFrame = 0;
            requestCallback();
            MediaPlayer mediaPlayer = MainGame.get().getMainBgmMediaPlayer();
            if(mediaPlayer!=null) {
                mediaPlayer.setOnPreparedListener(mp -> mp.start());
                mediaPlayer.prepareAsync();
            }
        }
    }
}













