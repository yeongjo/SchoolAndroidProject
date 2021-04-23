package kr.ac.kpu.game.s2016180024.samplegame.game;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.RectF;

import java.util.Random;

import kr.ac.kpu.game.s2016180024.samplegame.R;
import kr.ac.kpu.game.s2016180024.samplegame.framework.GameObject;
import kr.ac.kpu.game.s2016180024.samplegame.ui.view.GameView;

public class Bullet implements GameObject {
    private final float speed;
    private final float angle;
    private float x, y;
    private float dx, dy;
    private static AnimationGameBitmap bitmap;
    private static float FRAME_RATE = 8.5f;

    public Bullet(float x, float y, float tx, float ty) {
        this.x = x;
        this.y = y;
        this.dx = tx;
        this.dy = ty;
        Random r = new Random();
        float frameRate = FRAME_RATE * (r.nextFloat() * 0.2f + 0.9f);
        bitmap = new AnimationGameBitmap(R.mipmap.bullet_hadoken, frameRate, 6);

        speed = 1000;
        dx = tx - this.x;
        dy = ty - this.y;
        float move_dist = speed;
        angle = (float) Math.atan2(dy, dx);
        dx = (float) (move_dist * Math.cos(angle));
        dy = (float) (move_dist * Math.sin(angle));
    }

    public void update() {
        MainGame game = MainGame.get();
        y += dy * game.frameTime;
        x += dx * game.frameTime;
        int w = GameView.view.getWidth();
        int h = GameView.view.getHeight();
        int frameWidth = bitmap.getWidth() / 2;
        int framHeight = bitmap.getHeight() / 2;
        if(x < -frameWidth && dx < 0 || x - frameWidth > w && dx > 0) {
            dx = -dx;
            game.remove(this);
        }
        if(y < -frameWidth && dy < 0 || y - framHeight > h && dy > 0){
            dy = -dy;
            game.remove(this);
        }
    }

    public void draw(Canvas canvas) {
        bitmap.draw(canvas, x, y, angle);
    }
}
