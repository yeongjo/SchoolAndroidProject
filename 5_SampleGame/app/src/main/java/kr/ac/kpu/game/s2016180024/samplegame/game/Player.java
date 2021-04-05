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
    private static float width, height;
    private static Bitmap bitmap;

    public Player(float x, float y, float dx, float dy) {
        this.x = x;
        this.y = y;
        this.dx = dx;
        this.dy = dy;
        if(bitmap == null) {
            Resources res = GameView.view.getResources();
            bitmap = BitmapFactory.decodeResource(res, R.mipmap.plane_240);
            width = bitmap.getWidth();
            height = bitmap.getHeight();
        }
    }

    public void moveTo(float x, float y){
        this.x = x;
        this.y = y;
    }

    public void update() {
        y += dy * GameView.frameTime;
        x += dx * GameView.frameTime;
        int w = GameView.view.getWidth();
        int h = GameView.view.getHeight();
        if(x < 0 || x + width > w){
            dx = -dx;
        }
        if(y < 0 || y + height > h){
            dy = -dy;
        }
    }

    public void draw(Canvas canvas)
    {
        float left = x - width / 2;
        float top = y - height / 2;
        canvas.drawBitmap(bitmap, left, top,null);
    }
}
