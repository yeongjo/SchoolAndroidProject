package kr.ac.kpu.game.s2016180024.cookierun.framework.view;

import android.content.Context;
import android.graphics.Canvas;
import android.util.AttributeSet;
import android.util.Log;
import android.view.Choreographer;
import android.view.MotionEvent;
import android.view.View;

import androidx.annotation.Nullable;

import kr.ac.kpu.game.s2016180024.cookierun.framework.BaseGame;
import kr.ac.kpu.game.s2016180024.cookierun.framework.utils.Sound;

public class GameView extends View {
    private static final String TAG = GameView.class.getSimpleName();

    public static float MULTIPLIER = 1.2f;
    //    private Ball b1, b2;

    private long lastFrame;
    public static GameView view;

    public GameView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        GameView.view = this;
        Sound.init(context);
//        startUpdating();
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        //super.onSizeChanged(w, h, oldw, oldh);
        Log.d(TAG, "onSize: " + w + "," + h);
        BaseGame game = BaseGame.get();
        boolean justInitialized = game.initResources();
        if (justInitialized) {
            requestCallback();
        }
    }

    private void update() {
        BaseGame game = BaseGame.get();
        game.update();

        invalidate();
    }

    private void requestCallback() {
        if (!isShown()) {
            Log.d(TAG, "Not shown. Not calling Choreographer.postFrameCallback()");
            return;
        }
        Choreographer.getInstance().postFrameCallback(new Choreographer.FrameCallback() {
            @Override
            public void doFrame(long time) {
                if (lastFrame == 0) {
                    lastFrame = time;
                }
                BaseGame game = BaseGame.get();
                game.frameTime = (float) (time - lastFrame) / 1_000_000_000;
                update();
                lastFrame = time;
                requestCallback();
            }
        });
    }

    @Override
    protected void onDraw(Canvas canvas) {
        BaseGame game = BaseGame.get();
        game.draw(canvas);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        BaseGame game = BaseGame.get();
        return game.onTouchEvent(event);
    }
}













