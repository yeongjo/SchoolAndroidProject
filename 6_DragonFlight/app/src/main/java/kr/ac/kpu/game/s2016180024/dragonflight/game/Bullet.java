package kr.ac.kpu.game.s2016180024.dragonflight.game;

import android.graphics.Bitmap;
import android.graphics.Canvas;

import kr.ac.kpu.game.s2016180024.dragonflight.R;
import kr.ac.kpu.game.s2016180024.dragonflight.framework.GameBitmap;
import kr.ac.kpu.game.s2016180024.dragonflight.framework.GameObject;

public class Bullet implements GameObject {
    private final int speed;
    private final float x;
    private final Bitmap bitmap;
    private float y;

    public Bullet(float x, float y, int speed){
        this.x = x;
        this.y = y;
        this.speed= speed;
        this.bitmap = GameBitmap.load(R.mipmap.laser_1);
    }

    @Override
    public void update() {
        MainGame game = MainGame.get();
        y += speed * game.frameTime;
    }

    @Override
    public void draw(Canvas canvas) {

    }
}
