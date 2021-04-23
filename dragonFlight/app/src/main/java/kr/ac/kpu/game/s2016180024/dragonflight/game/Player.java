package kr.ac.kpu.game.s2016180024.dragonflight.game;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.RectF;

import kr.ac.kpu.game.s2016180024.dragonflight.R;
import kr.ac.kpu.game.s2016180024.dragonflight.framework.GameBitmap;
import kr.ac.kpu.game.s2016180024.dragonflight.framework.GameObject;

public class Player implements GameObject {
    private static final int MULTIPLIER = 4;
    private float x, y;
    private float dx, dy;
    private float tx, ty;
    private float speed;
    private int imageWidth, imageHeight;
    private Bitmap bitmap;
    private float angle;
    private float move_dist;

    public Player(float x, float y, float dx, float dy) {
        this.x = x;
        this.y = y;
        this.dx = dx;
        this.dy = dy;
        this.tx = 0;
        this.ty = 0;
        this.speed = 1000;
            bitmap = GameBitmap.load(R.mipmap.fighter);
            imageWidth = bitmap.getWidth();
            imageHeight = bitmap.getHeight();
    }

    public void moveTo(float x, float y) {
        MainGame game = MainGame.get();
        tx = x;
        ty = this.y;
//        dx = tx - this.x;
//        dy = ty - this.y;
//        move_dist = speed;
        //angle = (float) Math.atan2(x-this.x, y-this.y);
//        dx = (float) (move_dist * Math.cos(angle));
//        dy = (float) (move_dist * Math.sin(angle));
//        Sound.play(R.raw.hadouken);
    }

    public void update() {
        MainGame game = MainGame.get();
        float delta_x = tx - x;
        float delta_y = ty - y;
        float distance = (float) Math.sqrt(delta_x * delta_x + delta_y * delta_y);
        if (distance < move_dist * game.frameTime) {
            x = tx;
            y = ty;
            dx = dy = 0;
        } else {

            x += dx * game.frameTime;
            y += dy * game.frameTime;
        }


//        int w = GameView.view.getWidth();
//        int h = GameView.view.getHeight();
//        if(x < 0 || x + width > w){
//            dx = -dx;
//        }
//        if(y < 0 || y + height > h){
//            dy = -dy;
//        }
    }

    public void draw(Canvas canvas) {
        float sr = x - imageWidth / 2;
        float st = y - imageHeight / 2;
        int hw = imageWidth / 2;
        int hh = imageHeight / 2;
        float dl = x - hw * MULTIPLIER;
        float dt = y - hh * MULTIPLIER;
        float dr = x + hw * MULTIPLIER;
        float db = y + hh * MULTIPLIER;
        RectF dstRect = new RectF(x - hw * MULTIPLIER, y - hh * MULTIPLIER, x + hw * MULTIPLIER, y + hh);
        float degree = (float)(-angle*180/Math.PI)+180;
        canvas.save();
        canvas.rotate(degree, x, y);
        canvas.drawBitmap(bitmap, sr, st, null);
        canvas.restore();

    }
}
