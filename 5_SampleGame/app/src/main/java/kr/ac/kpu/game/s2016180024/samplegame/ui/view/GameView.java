package kr.ac.kpu.game.s2016180024.samplegame.ui.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.util.Log;
import android.view.Choreographer;
import android.view.MotionEvent;
import android.view.View;

import kr.ac.kpu.game.s2016180024.samplegame.game.MainGame;

/**
 * TODO: document your custom view class.
 */
public class GameView extends View {

    private static final String TAG = GameView.class.getSimpleName();

    long lastFrame;
    public static GameView view;

    public GameView(Context context, AttributeSet attrs) {
        super(context, attrs);
        view = this;
        MainGame mainGame = MainGame.get();
//        mainGame.init();
        doGameFrame();
    }


    private void doGameFrame() {
//        update();
        MainGame mainGame = MainGame.get();
        mainGame.update();

//        draw();
        invalidate();

        Choreographer.getInstance().postFrameCallback(frameTimeNanos -> {
            if(lastFrame == 0) {
                lastFrame = frameTimeNanos;
            }
            mainGame.frameTime = (frameTimeNanos - lastFrame)*0.000_000_001f;
            doGameFrame();
            lastFrame = frameTimeNanos;
        });
    }

    @Override
    protected void onDraw(Canvas canvas) {
        // TODO: consider storing these as member variables to reduce
        // allocations per draw cycle.
        int paddingLeft = getPaddingLeft();
        int paddingTop = getPaddingTop();
        int paddingRight = getPaddingRight();
        int paddingBottom = getPaddingBottom();

        int contentWidth = getWidth() - paddingLeft - paddingRight;
        int contentHeight = getHeight() - paddingTop - paddingBottom;

        MainGame mainGame = MainGame.get();
        mainGame.draw(canvas);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        MainGame mainGame = MainGame.get();
        return mainGame.onTouchEvent(event);
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        Log.d(TAG, "onSizeChanged: "+w+","+h);
        super.onSizeChanged(w, h, oldw, oldh);
        MainGame.get().init();
    }
}