package kr.ac.kpu.game.s2016180024.samplegame.game;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.graphics.RectF;

import kr.ac.kpu.game.s2016180024.samplegame.ui.view.GameView;

public class AnimationGameBitmap extends GameBitmap {
    private static final int PIXEL_MULTIPLIER = 4;
    private final Bitmap bitmap;
    private final int imageHeight;
    private final int imageWidth;
    private final int frameWidth;
    private final long createdOn;
    private final float framesPerSecond;
    private final int frameCount;
    private int frameIndex;

    public AnimationGameBitmap(int resId, float framesPerSecond, int frameCount){
        Resources res = GameView.view.getResources();
        BitmapFactory.Options opts = new BitmapFactory.Options();
        opts.inScaled = false;
        bitmap = BitmapFactory.decodeResource(res, resId, opts);
        imageWidth = bitmap.getWidth();
        imageHeight = bitmap.getHeight();
        if(frameCount == 0){
            frameCount = imageWidth / imageHeight;
        }
        frameWidth = imageWidth / frameCount;
        createdOn = System.currentTimeMillis();
        frameIndex = 0;
        this.framesPerSecond = framesPerSecond;
        this.frameCount = frameCount;
    }

    public void update(){

    }

    public void draw(Canvas canvas, float x, float y){
        draw(canvas, x, y, 0);
    }
    public void draw(Canvas canvas, float x, float y, float angle){
        int elapsed = (int)(System.currentTimeMillis() - createdOn);
        frameIndex = Math.round(elapsed * framesPerSecond * 0.001f) % frameCount;
        int fw = frameWidth;
        int fh = imageHeight;
        int hw = fw / 2 * PIXEL_MULTIPLIER;
        int hh = fh / 2 * PIXEL_MULTIPLIER;
        Rect src = new Rect(fw*frameIndex, 0, fw*frameIndex+fw, fh);
        RectF dst = new RectF(x-hw, y-hh,x+hw,y+hh);
        if(angle != 0) {
            float degree = (float) (angle * 180 / Math.PI) + 90;
            canvas.save();
            canvas.rotate(degree, x, y);
        }
        canvas.drawBitmap(bitmap, src, dst, null);
        if(angle != 0) {
            canvas.restore();
        }
    }

    public int getWidth() {
        return frameWidth * PIXEL_MULTIPLIER;
    }

    public int getHeight() {
        return imageHeight * PIXEL_MULTIPLIER;
    }
}
