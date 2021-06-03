package kr.ac.kpu.game.s2016180024.Dodge.framework;

import android.graphics.Canvas;
import android.graphics.Rect;

import kr.ac.kpu.game.s2016180024.Dodge.ui.view.GameView;

public class AnimationGameBitmap extends kr.ac.kpu.game.s2016180024.Dodge.framework.GameBitmap {
    //private Bitmap bitmap;
    private final int imageWidth;
    private final int imageHeight;
    private final int frameWidth;
    private final long createdOn;
    private int frameIndex;
    private final float secondPerFrame;
    private final int frameCount;

    protected Rect srcRect = new Rect();

    public AnimationGameBitmap(int resId, float secondPerFrame, int frameCount) {
        super(resId);
        //bitmap = GameBitmap.load(resId);
        imageWidth = bitmap.getWidth();
        imageHeight = bitmap.getHeight();
        if (frameCount == 0) {
            frameCount = imageWidth / imageHeight;
        }
        if (frameCount == 0) {
            frameCount = 1;
        }
        frameWidth = imageWidth / frameCount;
        this.secondPerFrame = secondPerFrame;
        this.frameCount = frameCount;
        createdOn = System.currentTimeMillis();
        frameIndex = 0;
    }

    public void draw(Canvas canvas, float x, float y) {
        int elapsed = (int)(System.currentTimeMillis() - createdOn);
        frameIndex = Math.round(elapsed * 0.001f * secondPerFrame) % frameCount;

        int fw = frameWidth;
        int h = imageHeight;
        float hw = fw / 2.0f * GameView.MULTIPLIER * scale;
        float hh = h / 2.0f * GameView.MULTIPLIER * scale;
        srcRect.set(fw * frameIndex, 0, fw * frameIndex + fw, h);
        dstRect.set(x - hw, y - hh, x + hw, y + hh);
        canvas.drawBitmap(bitmap, srcRect, dstRect, null);
    }

    public int getWidth() {
        return frameWidth;
    }

    public int getHeight() {
        return imageHeight;
    }
}












