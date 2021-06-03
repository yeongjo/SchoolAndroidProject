package kr.ac.kpu.game.s2016180024.Dodge.game;

import java.util.Random;

import kr.ac.kpu.game.s2016180024.Dodge.R;
import kr.ac.kpu.game.s2016180024.Dodge.framework.AnimationGameBitmap;

public class RandomMoveEnemy extends FollowEnemy {

    private float randomWaitTime;
    private boolean isMovingState = true;
    private Random random = new Random();
    private float minDelay;
    private float maxDelay;

    protected RandomMoveEnemy(){
        super();
    }

    public static RandomMoveEnemy get(int level, int x, int y, int speed, float minAngle, float minDelay, float maxDelay) {
        MainGame game = MainGame.get();
        RandomMoveEnemy enemy = (RandomMoveEnemy) game.get(RandomMoveEnemy.class);
        if (enemy == null) {
            enemy = new RandomMoveEnemy();
        }

        enemy.init(level, x, y, speed);
        enemy.vel.y = 1;
        enemy.minAngle = minAngle;
        enemy.minDelay = minDelay;
        enemy.maxDelay = maxDelay;
        enemy.bitmap = new AnimationGameBitmap(R.mipmap.enemy_03, FRAMES_PER_SECOND, 0);
        return enemy;
    }

    @Override
    public void update() {
        MainGame game = MainGame.get();
        randomWaitTime -= game.frameTime;
        if(randomWaitTime < 0){
            isMovingState = !isMovingState;
            randomWaitTime = minDelay + random.nextFloat() * (maxDelay - minDelay);
        }

        if(isMovingState){
            super.update();
        }
    }
}
