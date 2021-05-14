package kr.ac.kpu.game.s2016180024.cookierun.framework.bitmap;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.RectF;

import java.util.HashMap;

import kr.ac.kpu.game.s2016180024.cookierun.framework.view.GameView;

public class GameBitmap {
    private static HashMap<Integer, Bitmap> bitmaps = new HashMap<Integer, Bitmap>();

    public static Bitmap load(int resId) {
        Bitmap bitmap = bitmaps.get(resId);
        if (bitmap == null) {
            Resources res = GameView.view.getResources();
            BitmapFactory.Options opts = new BitmapFactory.Options();
            opts.inScaled = false;
            bitmap = BitmapFactory.decodeResource(res, resId, opts);
            bitmaps.put(resId, bitmap);
        }
        return bitmap;
    }

    protected final Bitmap bitmap;
    protected RectF dstRect = new RectF();
    protected float scale = 1;
    public GameBitmap(int resId) {
        bitmap = load(resId);
    }

    public void setScale(float scale){
        this.scale = scale;
    }

    public void draw(Canvas canvas, float x, float y) {
        int hw = getWidth() / 2;
        int hh = getHeight() / 2;
        //Rect srcRect = new Rect(left, )
        float dl = x - hw * GameView.MULTIPLIER * scale;
        float dt = y - hh * GameView.MULTIPLIER * scale;
        float dr = x + hw * GameView.MULTIPLIER * scale;
        float db = y + hh * GameView.MULTIPLIER * scale;
        dstRect.set(dl, dt, dr, db);
        canvas.drawBitmap(bitmap, null, dstRect, null);
    }

    public int getHeight() {
        return bitmap.getHeight();
    }

    public int getWidth() {
        return bitmap.getWidth();
    }

    public void getBoundingRect(float x, float y, RectF rect) {
        int hw = getWidth() / 2;
        int hh = getHeight() / 2;
        //Rect srcRect = new Rect(left, )
        float dl = x - hw * GameView.MULTIPLIER * scale;
        float dt = y - hh * GameView.MULTIPLIER * scale;
        float dr = x + hw * GameView.MULTIPLIER * scale;
        float db = y + hh * GameView.MULTIPLIER * scale;
        rect.set(dl, dt, dr, db);
    }

}
