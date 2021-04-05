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

    public Player(float x, float y, float dx, float dy) {
        this.x = x;
        this.y = y;
        this.dx = dx;
        this.dy = dy;
        this.tx = 0;
        this.ty = 0;
        this.speed = 1000;
        if(bitmap == null) {
            Resources res = GameView.view.getResources();
            bitmap = BitmapFactory.decodeResource(res, R.mipmap.plane_240);
            width = bitmap.getWidth();
            height = bitmap.getHeight();
        }
    }

    public void moveTo(float x, float y){
        this.tx = x;
        this.ty = y;
    }

    public void update() {
        MainGame game = MainGame.get();
        dx = tx - x;
        dy = ty - y;
        float distance = (float)Math.sqrt(dx*dx+dy*dy);
        float move_dist = speed * game.frameTime;
        if(distance < move_dist){
            x =tx;
            y =ty;
        }else{
            float angle = (float)Math.atan2(dy, dx);
            float mx= (float)(move_dist * Math.cos(angle));
            float my= (float)(move_dist * Math.sin(angle));
            x += mx;
            y += my;
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

    public void draw(Canvas canvas)
    {
        float left = x - width / 2;
        float top = y - height / 2;
        canvas.drawBitmap(bitmap, left, top,null);
    }
}
