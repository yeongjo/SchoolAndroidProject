package kr.ac.kpu.game.s2016180024.cookierun.framework.object;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.graphics.RectF;

import kr.ac.kpu.game.s2016180024.cookierun.framework.bitmap.GameBitmap;
import kr.ac.kpu.game.s2016180024.cookierun.framework.iface.GameObject;

public class ImageObject implements GameObject {
    private final Bitmap bitmap;
    protected Rect srcRect = new Rect();
    protected RectF dstRect = new RectF();

    public ImageObject(int resId, float x, float y){
        bitmap = GameBitmap.load(resId);
        int w = bitmap.getWidth();
        int h = bitmap.getHeight();
        srcRect.set(0,0,w,h);
        float l = x - w / 2;
        float t = y - h / 2;
        float r = x + w / 2;
        float b = y + h / 2;
        dstRect.set(l,t,r,b);
    }

    @Override
    public void update() {

    }

    @Override
    public void draw(Canvas canvas) {
        canvas.drawBitmap(bitmap, srcRect, dstRect, null);
    }

    public float getDstWidth() {
        return dstRect.width();
    }
    public float getDstHeight() {
        return dstRect.height();
    }
}
