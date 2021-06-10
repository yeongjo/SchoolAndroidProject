package kr.ac.kpu.game.s2016180024.Dodge.game;

import android.animation.ArgbEvaluator;
import android.graphics.Canvas;
import android.graphics.Paint;

import kr.ac.kpu.game.s2016180024.Dodge.R;
import kr.ac.kpu.game.s2016180024.Dodge.framework.AnimationGameBitmap;
import kr.ac.kpu.game.s2016180024.Dodge.framework.Scene;
import kr.ac.kpu.game.s2016180024.Dodge.framework.Sound;
import kr.ac.kpu.game.s2016180024.Dodge.framework.Vector2;
import kr.ac.kpu.game.s2016180024.Dodge.game.scene.GamePlayScene;
import kr.ac.kpu.game.s2016180024.Dodge.ui.view.GameView;

public class LaserEnemy extends FollowEnemy {

    private final Paint paint = new Paint();
    private ArgbEvaluator argbEvaluator = new ArgbEvaluator();
    private float straightMoveRemainTime;
    private float initalStraightMoveRemainTime;
    private float remainTime;
    private float initalRemainTime;
    private float strokeWidth = 14;
    private boolean isPlayedShotSound;

    protected LaserEnemy(){
        super();
        paint.setStyle(Paint.Style.STROKE);
    }

    public static LaserEnemy get(int level, int x, int y, int speed, float minAngle, float remainTime) {
        LaserEnemy enemy = (LaserEnemy) Scene.getActiveScene().get(LaserEnemy.class);
        if (enemy == null) {
            enemy = new LaserEnemy();
        }

        enemy.init(level, x, y, speed);
        enemy.vel.y = 1;
        enemy.minAngle = minAngle;
        enemy.initalRemainTime = enemy.remainTime = remainTime;
        enemy.initalStraightMoveRemainTime = enemy.straightMoveRemainTime = remainTime;
        enemy.strokeWidth = 5;
        enemy.isPlayedShotSound = false;
        enemy.paint.setStrokeWidth(enemy.strokeWidth);
        enemy.bitmap = new AnimationGameBitmap(R.mipmap.enemy_05, FRAMES_PER_SECOND, 0);
        return enemy;
    }

    @Override
    public void update() {
        MainGame game = MainGame.get();
        straightMoveRemainTime -= game.frameTime;
        if(straightMoveRemainTime > 0){
            pos.y += speed*0.1f * game.frameTime;
            return;
        }
        remainTime -= game.frameTime;
        if(remainTime > 0) {
            Player player = GamePlayScene.get().getPlayer();
            float velocityAngle = vel.angle();
            float deltaAngle = player.getPos().cpy().sub(pos).angle() - velocityAngle;
            if (Math.abs(deltaAngle) > minAngle) {
                deltaAngle = deltaAngle > 0 ? minAngle : -minAngle;
            }
            vel.rotate(deltaAngle * game.frameTime);
            return;
        }
        if(!isPlayedShotSound){
            isPlayedShotSound = true;
            Sound.play(R.raw.laser_shot, 0);
        }
        pos.add(vel.cpy().mul(speed * game.frameTime));

        if (pos.y > GameView.self.getHeight()) {
            Scene.getActiveScene().remove(this);
        }
    }

    @Override
    public void draw(Canvas canvas) {
        if(straightMoveRemainTime < 0 && remainTime > 0) {
            paint.setColor((int) argbEvaluator.evaluate(remainTime / initalRemainTime,
                    0xffff0000, 0xff00ff00));
            paint.setStrokeWidth(strokeWidth * remainTime / initalRemainTime + 2);
            Vector2 targetVec = vel.cpy().mul(5000).add(pos);
            canvas.drawLine(pos.x, pos.y, targetVec.x, targetVec.y, paint);
        }
        super.draw(canvas);
    }
}
