package kr.ac.kpu.game.s2016180024.Dodge.game;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.graphics.RectF;

import kr.ac.kpu.game.s2016180024.Dodge.R;
import kr.ac.kpu.game.s2016180024.Dodge.framework.GameBitmap;
import kr.ac.kpu.game.s2016180024.Dodge.framework.GameObject;
import kr.ac.kpu.game.s2016180024.Dodge.ui.view.GameView;

public class Level implements GameObject {

    public static Level self;
    private final Bitmap bitmap;
    private final int right;
    private final int top;
    private Rect src = new Rect();
    private RectF dst = new RectF();
    private int level;

    public void setLevel(int score) {
        this.level = score;
    }
    public void sendNextLevel() {
        this.level += 1;
    }

    public Level(int right, int top) {
        bitmap = GameBitmap.load(R.mipmap.number_24x32);
        this.right = right;
        this.top = top;
        self = this;
    }

    @Override
    public void update() {

    }

    @Override
    public void draw(Canvas canvas) {
        int value = this.level;
        int nw = bitmap.getWidth() / 10;
        int nh = bitmap.getHeight();
        int x = right;
        int dw = (int) (nw * GameView.MULTIPLIER);
        int dh = (int) (nh * GameView.MULTIPLIER);
        while (value > 0) {
            int digit = value % 10;
            src.set(digit * nw, 0, (digit + 1) * nw, nh);
            x -= dw;
            dst.set(x, top, x + dw, top + dh);
            canvas.drawBitmap(bitmap, src, dst, null);
            value /= 10;
        }
    }
}
