package kr.ac.kpu.game.s2016180024.Dodge.game;

import android.graphics.Canvas;
import android.graphics.Paint;

import java.util.Random;

// 다른 적들을 만듭니다
public class ParentEnemy extends Enemy {
    private static Random random = new Random();
    private final Paint paint = new Paint();
    private float remainTime;

    protected ParentEnemy(){
        super();
        paint.setStrokeWidth(14);
        paint.setStyle(Paint.Style.STROKE);
        paint.setColor(0xffff0000);   //color.RED
    }

    public static ParentEnemy get(int level, int x, int y, int speed, float remainTime) {
        MainGame game = MainGame.get();
        ParentEnemy enemy = (ParentEnemy) game.get(ParentEnemy.class);
        if (enemy == null) {
            enemy = new ParentEnemy();
        }

        enemy.init(level, x, y, speed);
        enemy.remainTime = random.nextFloat() * remainTime;
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
                    Enemy enemy = Enemy.get(Math.max(1, level-random.nextInt(4)-1), (int) pos.x + x * 100, (int) pos.y + y * 100, 100);
                    game.add(MainGame.Layer.enemy, enemy);
                }
            }
            MainGame.get().remove(this);
        }
        super.update();
    }

    @Override
    public void draw(Canvas canvas) {
        super.draw(canvas);
        canvas.drawCircle(pos.x, pos.y, remainTime*10, paint);
    }
}
