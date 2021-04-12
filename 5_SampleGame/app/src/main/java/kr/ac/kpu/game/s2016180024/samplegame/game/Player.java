package kr.ac.kpu.game.s2016180024.samplegame.game;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;

import kr.ac.kpu.game.s2016180024.samplegame.R;
import kr.ac.kpu.game.s2016180024.samplegame.framework.GameObject;
import kr.ac.kpu.game.s2016180024.samplegame.ui.view.GameView;

public class Player implements GameObject {
    private float x, y;
    private float dx, dy;
    private float tx, ty;
    private float speed;
    private static float width, height;
    private static Bitmap bitmap;
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
        if (bitmap == null) {
            Resources res = GameView.view.getResources();
            bitmap = BitmapFactory.decodeResource(res, R.mipmap.plane_240);
            width = bitmap.getWidth();
            height = bitmap.getHeight();
        }
    }

    public void moveTo(float x, float y) {
        Bullet bullet = new Bullet(this.x, this.y, x, y );
        MainGame game = MainGame.get();
        game.add(bullet);
//        tx = x;
//        ty = y;
//        dx = tx - this.x;
//        dy = ty - this.y;
//        move_dist = speed;
        angle = (float) Math.atan2(x-this.x, y-this.y);
//        dx = (float) (move_dist * Math.cos(angle));
//        dy = (float) (move_dist * Math.sin(angle));
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
        float left = x - width / 2;
        float top = y - height / 2;
        float degree = (float)(-angle*180/Math.PI)+180;
        canvas.save();
        canvas.rotate(degree, x, y);
        canvas.drawBitmap(bitmap, left, top, null);
        canvas.restore();

    }
}
