package kr.ac.kpu.game.s2016180024.animation;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;

public class MyView extends View {

    public static final String TAG = MyView.class.getSimpleName();
    Paint paint = new Paint();
    Rect rect = new Rect();

    public MyView(Context context, AttributeSet set) {
        super(context, set);
        paint.setColor(0xff0044ff);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        Log.d(TAG, "onTouch: "+event);
        return true;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        ViewGroup.LayoutParams lp = getLayoutParams();
        int l = 0 + getPaddingLeft();
        int t = 0 + getPaddingTop();
        int w = getWidth() - getRightPaddingOffset();
        int h = getHeight() - getBottomPaddingOffset();
        rect.set(l, t, w, h);
        Log.d(TAG, "draw " + rect);
        canvas.drawRect(rect, paint);
    }
}
