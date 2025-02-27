package kr.ac.kpu.game.s2016180024.Dodge.game;

import android.graphics.Canvas;
import android.graphics.Paint;

import java.util.Random;

import kr.ac.kpu.game.s2016180024.Dodge.R;
import kr.ac.kpu.game.s2016180024.Dodge.framework.AnimationGameBitmap;
import kr.ac.kpu.game.s2016180024.Dodge.framework.Scene;
import kr.ac.kpu.game.s2016180024.Dodge.framework.Sound;

// 다른 적들을 만듭니다
public class ParentEnemy extends Enemy {
    private static Random random = new Random();
    private static int chargeSound;
    private final Paint paint = new Paint();
    private float remainTime;

    protected ParentEnemy(){
        super();
        paint.setStrokeWidth(14);
        paint.setStyle(Paint.Style.STROKE);
        paint.setColor(0xffff0000);   //color.RED
    }

    public static ParentEnemy get(int level, int x, int y, int speed, float remainTime) {
        ParentEnemy enemy = (ParentEnemy) Scene.getActiveScene().get(ParentEnemy.class);
        if (enemy == null) {
            enemy = new ParentEnemy();
        }

        enemy.init(level, x, y, speed);
        enemy.remainTime = (0.5f-random.nextFloat())*0.5f + remainTime;
        int frameCount = 4;
        float secondPerFrame = frameCount / remainTime;
        secondPerFrame -= secondPerFrame * (1.0f/frameCount);
        chargeSound = Sound.play(R.raw.parent_enemy_charge, 0);
        enemy.bitmap = new AnimationGameBitmap(R.mipmap.enemy_04, secondPerFrame, frameCount);
        return enemy;
    }

    @Override
    public void update() {
        MainGame game = MainGame.get();
        remainTime -= game.frameTime;
        if(remainTime < 0){
            for (int x = -1; x <= 1; ++x){
                for (int y = -1; y <= 1; ++y){
                    if(x == 0 && y == 0){
                        continue;
                    }
                    Enemy enemy = Enemy.get(1, (int) pos.x + x * 100, (int) pos.y + y * 100, 300+50*(level/5+1));
                    enemy.setDamage(getDamage()/4);
                    Scene.getActiveScene().add(Scene.Layer.enemy, enemy);
                }
            }
            Sound.stop(chargeSound);
            Sound.play(R.raw.parent_enemy_pop, 0);
            Scene.getActiveScene().remove(this);
        }
        super.update();
    }

    @Override
    public void recycle() {
        super.recycle();
        Sound.stop(chargeSound);
    }

    @Override
    public void draw(Canvas canvas) {
        super.draw(canvas);
//        canvas.drawCircle(pos.x, pos.y, remainTime*10, paint);
    }
}
