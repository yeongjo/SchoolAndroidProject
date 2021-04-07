package kr.ac.kpu.game.s2016180024.samplegame.game;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;

import kr.ac.kpu.game.s2016180024.samplegame.R;
import kr.ac.kpu.game.s2016180024.samplegame.framework.GameObject;
import kr.ac.kpu.game.s2016180024.samplegame.ui.view.GameView;

public class Bullet implements GameObject {
    private final float speed;
    private float x, y;
    private float dx, dy;
    private static float width, height;
    private static Bitmap heartBitMap;
    private float radius;
    Paint paint = new Paint();

    public Bullet(float x, float y, float tx, float ty) {
        this.x = x;
        this.y = y;
        this.dx = tx;
        this.dy = ty;
        if(heartBitMap == null) {
            Resources res = GameView.view.getResources();
            heartBitMap = BitmapFactory.decodeResource(res, R.mipmap.heart);
            width = heartBitMap.getWidth();
            height = heartBitMap.getHeight();
        }
        paint.setColor(0xFFFF0000);

        radius = 10;
        speed = 1000;
        dx = tx - this.x;
        dy = ty - this.y;
        float move_dist = speed;
        float angle = (float) Math.atan2(dy, dx);
        dx = (float) (move_dist * Math.cos(angle));
        dy = (float) (move_dist * Math.sin(angle));
    }

    public void update() {
        MainGame game = MainGame.get();
        y += dy * game.frameTime;
        x += dx * game.frameTime;
        int w = GameView.view.getWidth();
        int h = GameView.view.getHeight();
        if(x < 0 && dx < 0 || x + width > w && dx > 0) {
            dx = -dx;
            game.remove(this);
        }
        if(y < 0 && dy < 0 || y + height > h && dy > 0){
            dy = -dy;
            game.remove(this);
        }
    }

    public void draw(Canvas canvas) {
        canvas.drawCircle(x,y,radius, paint);
//        canvas.drawBitmap(heartBitMap, x,y,null);
    }
}
