package kr.ac.kpu.game.s2016180024.animation;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.util.Log;
import android.view.View;

public class MyView extends View {

    public static final String TAG = MyView.class.getSimpleName();

    public MyView(Context context) {
        super(context);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        Paint paint = new Paint();
        paint.setColor(0xff0044ff);
        Rect rect = new Rect(10, 20, 300, 70);
        Log.d(TAG, "draw " + rect);
        canvas.drawRect(rect, paint);
    }
}
