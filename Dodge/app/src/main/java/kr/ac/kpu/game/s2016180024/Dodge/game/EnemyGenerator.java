package kr.ac.kpu.game.s2016180024.Dodge.game;

import android.graphics.Canvas;

import java.util.ArrayList;
import java.util.Random;

import kr.ac.kpu.game.s2016180024.Dodge.framework.GameObject;
import kr.ac.kpu.game.s2016180024.Dodge.ui.view.GameView;

public class EnemyGenerator implements GameObject {

    private static final float INITIAL_SPAWN_INTERVAL = 2.0f;
    private static final String TAG = EnemyGenerator.class.getSimpleName();
    private static final int ENEMY_TYPE_COUNT = 5;
    private float time;
    private float spawnInterval;
    private int wave;
    private int nextTargetLevel;
    private int level;
    private int chapter = 1;

    public EnemyGenerator() {
        reset();
    }
    @Override
    public void update() {
        MainGame game = MainGame.get();
        if(nextTargetLevel == level){
            if(checkEveryEnemyDestroyed()){
                MainGame.get().askItem();
                nextTargetLevel += 1;
                Level.self.setLevel(level);
            }
            return;
        }
        time += game.frameTime;
        if (time >= spawnInterval) {
            generate();
            time -= spawnInterval;
        }
    }

    public int getChapter(){
        return chapter;
    }

    boolean checkEveryEnemyDestroyed(){
        ArrayList<GameObject> enemies = MainGame.get().layers.get(MainGame.Layer.enemy.ordinal());
        return enemies.isEmpty();
    }

    private void generate() {
        wave++;
        level = wave / 5 + 1;
        MainGame.get().getScore().addScore(level);
        chapter = level / ENEMY_TYPE_COUNT + 1;
        //Log.d(TAG, "Generate now !!");
        MainGame game = MainGame.get();
        int tenth = GameView.view.getWidth() / 10;
        Random r = new Random();
        for (int i = 1; i <= 9; i += 2) {
            int x = tenth * i + r.nextInt(tenth) - tenth/2;
            int y = 0;
            int level = wave / 5 - r.nextInt(3);
            int localChapter = level / ENEMY_TYPE_COUNT + 1;
            if (level < 1) level = 1;
            if (level > 20) level = 20;
            Enemy enemy;
            if(level % ENEMY_TYPE_COUNT == 0){
                enemy = LaserEnemy.get(level, x, y, (int)(2000 * (localChapter*0.2f+1)), 10.0f/localChapter, 2.0f);
            }else if(level % ENEMY_TYPE_COUNT == 4){
                enemy = ParentEnemy.get(level, x, y, (int)(200 * (localChapter*0.5f+1)), 10.0f/localChapter);
            }else if(level % ENEMY_TYPE_COUNT == 3){
                enemy = RandomMoveEnemy.get(level, x, y, (int)(300 * (localChapter*0.5f+1)), 3.0f * localChapter, 0.3f, 2.5f * localChapter);
            }else if(level % ENEMY_TYPE_COUNT == 2) {
                enemy = FollowEnemy.get(level, x, y, (int)(300 * (localChapter*0.5f+1)), 3.0f * localChapter);
            }else {
                enemy = Enemy.get(level, x, y, (int)(300 * (localChapter*0.5f+1)));
            }
            game.add(MainGame.Layer.enemy, enemy);
        }
    }

    @Override
    public void draw(Canvas canvas) {
        // does nothing
    }

    public void reset() {
        wave = 0;
        time = INITIAL_SPAWN_INTERVAL;
        spawnInterval = INITIAL_SPAWN_INTERVAL;
        chapter = level = nextTargetLevel = 1;
    }
}
