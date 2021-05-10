package kr.ac.kpu.game.s2016180024.Dodge.game;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.RectF;

import kr.ac.kpu.game.s2016180024.Dodge.framework.GameObject;

public class PlayerHud  implements GameObject {

    private Player player;
    private RectF dst = new RectF();
    private Paint paint = new Paint();

    @Override
    public void update() {
        MainGame.get().getPlayer();
    }

    @Override
    public void draw(Canvas canvas) {
        if(player != null){
            paint.setColor(0xff0000FF);
            dst.set(0, 0, player.getStamina(), 50);
            canvas.drawRect(dst, paint);
        }
    }
}
