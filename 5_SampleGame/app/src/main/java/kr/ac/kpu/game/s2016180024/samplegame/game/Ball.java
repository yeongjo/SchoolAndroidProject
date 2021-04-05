package kr.ac.kpu.game.s2016180024.samplegame.game;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;

import kr.ac.kpu.game.s2016180024.samplegame.R;
import kr.ac.kpu.game.s2016180024.samplegame.framework.GameObject;
import kr.ac.kpu.game.s2016180024.samplegame.ui.view.GameView;

public class Ball implements GameObject {
    private float x, y;
    private float dx, dy;
    private static float width, height;
    private static Bitmap heartBitMap;

    public Ball(float x, float y, float dx, float dy) {
        this.x = x;
        this.y = y;
        this.dx = dx;
        this.dy = dy;
        if(heartBitMap == null) {
            Resources res = GameView.view.getResources();
            heartBitMap = BitmapFactory.decodeResource(res, R.mipmap.heart);
            width = heartBitMap.getWidth();
            height = heartBitMap.getHeight();
        }
    }

    public void update() {
        MainGame game = MainGame.get();
        y += dy * game.frameTime;
        x += dx * game.frameTime;
        int w = GameView.view.getWidth();
        int h = GameView.view.getHeight();
        if(x < 0 || x + width > w){
            dx = -dx;
        }
        if(y < 0 || y + height > h){
            dy = -dy;
        }
    }

    public void draw(Canvas canvas) {
        canvas.drawBitmap(heartBitMap, x,y,null);
    }
}
