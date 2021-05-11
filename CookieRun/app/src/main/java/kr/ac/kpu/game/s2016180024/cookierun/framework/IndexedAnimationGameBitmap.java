package kr.ac.kpu.game.s2016180024.cookierun.framework;

import android.graphics.Canvas;
import android.graphics.Rect;

import java.util.ArrayList;

import kr.ac.kpu.game.s2016180024.cookierun.framework.view.GameView;

public class IndexedAnimationGameBitmap extends AnimationGameBitmap {
    private final int frameHeight;
    private ArrayList<Rect> srcRects;

    public IndexedAnimationGameBitmap(int resId, float framesPerSecond, int frameCount) {
        super(resId, framesPerSecond, frameCount);
        this.frameWidth = 270;
        this.frameHeight = 270;
        setIndices(100, 101, 102, 103);
    }

    public void setIndices(int... indices) {
        srcRects = new ArrayList<>();
        for(int index:indices) {
            int x = index % 100;
            int y = index / 100;
            int l = 2 + x * 272;
            int t = 2 + y * 272;
            int r = l + 270;
            int b = t + 270;
            Rect rect = new Rect(l, t, r, b);
            srcRects.add(rect);
        }
        frameCount = indices.length;
    }


    public void draw(Canvas canvas, float x, float y) {
        int elapsed = (int)(System.currentTimeMillis() - createdOn);
        frameIndex = Math.round(elapsed * 0.001f * framesPerSecond) % frameCount;

        int fw = frameWidth;
        int h = frameHeight;
        float hw = fw / 2 * GameView.MULTIPLIER * scale;
        float hh = h / 2 * GameView.MULTIPLIER * scale;
        srcRect.set(fw * frameIndex, 0, fw * frameIndex + fw, h);
        dstRect.set(x - hw, y - hh, x + hw, y + hh);
        canvas.drawBitmap(bitmap, srcRect, dstRect, null);
    }
}
