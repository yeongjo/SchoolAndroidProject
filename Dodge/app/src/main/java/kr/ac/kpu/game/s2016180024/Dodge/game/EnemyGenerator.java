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
    private int nextChapterCount= 3;
    public static EnemyGenerator self;

    public EnemyGenerator() {
        self = this;
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
                MainGame.get().getScore().addScore(level*10);
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
        level = wave / nextChapterCount + 1;
        chapter = level / ENEMY_TYPE_COUNT + 1;
        //Log.d(TAG, "Generate now !!");
        MainGame game = MainGame.get();
        int tenth = GameView.view.getWidth() / 10;
        Random r = new Random();
        for (int i = 1; i <= 9; i += 2) {
            int x = tenth * i + r.nextInt(tenth) - tenth/2;
            int y = 0;
            int enemyRandom = r.nextInt(4);
            int level = Math.min(wave / nextChapterCount, 20) - enemyRandom + 1;
            level = Math.max(1, Math.min(level,20));
            int localChapter = level / ENEMY_TYPE_COUNT + 1;
            float difficulty = difficultyMultiplier(localChapter);
            int visualLevel = wave / nextChapterCount - enemyRandom + 1;
            visualLevel = Math.min(Math.max(1, visualLevel), 20);
            Enemy enemy;
            if(level % ENEMY_TYPE_COUNT == 0){
                enemy = LaserEnemy.get(visualLevel, x, y, (int)(1500 * ((localChapter-1)*0.2f+1)), 10.0f*((localChapter-1)*0.7f+1), 2.0f);
            }else if(level % ENEMY_TYPE_COUNT == 4){
                enemy = ParentEnemy.get(visualLevel, x, y, (int)(200 * ((localChapter-1)*0.5f+1)), 3.0f + 5.0f*(1-difficulty));
            }else if(level % ENEMY_TYPE_COUNT == 3){
                enemy = RandomMoveEnemy.get(visualLevel, x, y, (int)(450 * ((localChapter-1)*0.5f+1)), 3.0f * localChapter, 0.1f, 1.5f);
            }else if(level % ENEMY_TYPE_COUNT == 2) {
                enemy = FollowEnemy.get(visualLevel, x, y, (int)(300 * ((localChapter-1)*0.5f+1)), 3.0f * localChapter);
            }else {
                enemy = Enemy.get(visualLevel, x, y, (int)(300 * ((localChapter-1)*1.5f+1)));
            }
            enemy.setDamage(localChapter);
            game.add(MainGame.Layer.enemy, enemy);
        }
    }

    public static float difficultyMultiplier(float chapter){
        return 1.0f/(-0.92f - 1.5f*chapter)+1.1f;
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
