package kr.ac.kpu.game.s2016180024.samplegame.game;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.graphics.RectF;

import kr.ac.kpu.game.s2016180024.samplegame.R;
import kr.ac.kpu.game.s2016180024.samplegame.framework.GameObject;
import kr.ac.kpu.game.s2016180024.samplegame.ui.view.GameView;

public class Ball implements GameObject {
    private float x, y;
    private float dx, dy;
    private static AnimationGameBitmap bitmap;
    private static float FRAME_RATE = 8.5f;

    public Ball(float x, float y, float dx, float dy) {
        this.x = x;
        this.y = y;
        this.dx = dx;
        this.dy = dy;
        if(bitmap == null) {
            bitmap = new AnimationGameBitmap(R.mipmap.fireball_128_24f, FRAME_RATE, 0);
        }
    }

    public void update() {
        MainGame game = MainGame.get();
        y += dy * game.frameTime;
        x += dx * game.frameTime;
        int windowWidth = GameView.view.getWidth();
        int windowHeight = GameView.view.getHeight();
        int frameWidth = bitmap.getWidth() / 2;
        int frameHeight = bitmap.getHeight() / 2;
        if(x < frameWidth && dx < 0 || x + frameWidth > windowWidth && dx > 0) {
            dx = -dx;
        }
        if(y < frameHeight && dy < 0 || y + frameHeight > windowHeight && dy > 0){
            dy = -dy;
        }

        bitmap.update();
    }

    public void draw(Canvas canvas) {
        bitmap.draw(canvas, x, y);
    }
}
