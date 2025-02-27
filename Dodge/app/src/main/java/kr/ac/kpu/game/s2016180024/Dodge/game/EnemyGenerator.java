package kr.ac.kpu.game.s2016180024.Dodge.game;

import android.graphics.Canvas;

import java.util.ArrayList;
import java.util.Random;

import kr.ac.kpu.game.s2016180024.Dodge.framework.GameObject;
import kr.ac.kpu.game.s2016180024.Dodge.framework.Scene;
import kr.ac.kpu.game.s2016180024.Dodge.game.item.ExpItem;
import kr.ac.kpu.game.s2016180024.Dodge.game.scene.GamePlayScene;
import kr.ac.kpu.game.s2016180024.Dodge.ui.view.GameView;

public class EnemyGenerator implements GameObject {

    private static final float INITIAL_SPAWN_INTERVAL = 5.0f;
    private static final String TAG = EnemyGenerator.class.getSimpleName();
    private static final int ENEMY_TYPE_COUNT = 5;
    private float time;
    private float spawnInterval;
    private int wave;
    private int nextTargetLevel;
    private int level;
    private int chapter = 1;
    private int nextChapterCount = 3;
    public static EnemyGenerator self;
    Random random;

    public EnemyGenerator() {
        self = this;
        random = new Random();
        reset();
    }

    @Override
    public void update() {
        MainGame game = MainGame.get();
        if(nextTargetLevel == level){
            nextTargetLevel += 1;
//                Level.self.setLevel(level);
            GamePlayScene.get().getScore().addScore(level*10);
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
        ArrayList<GameObject> enemies = GamePlayScene.get().layers.get(Scene.Layer.enemy.ordinal());
        return enemies.isEmpty();
    }

    private void generate() {
        wave++;
        level = wave / nextChapterCount + 1;
        chapter = level / ENEMY_TYPE_COUNT + 1;
        updateSpawnInterval();
        //Log.d(TAG, "Generate now !!");
        MainGame game = MainGame.get();
        int tenth = GameView.self.getWidth() / 10;
        for (int i = 1; i <= 9; i += 2) {
            int x = tenth * i + random.nextInt(tenth) - tenth/2;
            int y = 0;
            int enemyRandom = random.nextInt(6);
            int level = Math.min(wave / nextChapterCount, 20) - enemyRandom + 1;
            level = Math.max(1, Math.min(level,20));
            int localChapter = level / ENEMY_TYPE_COUNT + 1;
            float difficulty = difficultyMultiplier(localChapter);
            int visualLevel = Math.min(wave / nextChapterCount - enemyRandom, 5)%ENEMY_TYPE_COUNT + 1;
            visualLevel = Math.min(Math.max(1, visualLevel), 5);
            Enemy enemy;
            if(level % ENEMY_TYPE_COUNT == 0){
                enemy = LaserEnemy.get(visualLevel, x, y, (int)(1000 * ((localChapter-1)*0.42f+1)), 10.0f*((localChapter-1)*1.7f+1), 2.0f);
            }else if(level % ENEMY_TYPE_COUNT == 4){
                int speed = (int) (200 * ((localChapter - 1) * 0.5f + 1));
                enemy = ParentEnemy.get(visualLevel, x, y,speed , 880.0f / speed);
            }else if(level % ENEMY_TYPE_COUNT == 3){
                enemy = RandomMoveEnemy.get(visualLevel, x, y, (int)(450 * ((localChapter-1)*0.5f+1)), 3.0f + ((localChapter-1)*2.7f), 0.1f, 1.5f);
            }else if(level % ENEMY_TYPE_COUNT == 2) {
                enemy = FollowEnemy.get(visualLevel, x, y, (int)(300 * ((localChapter-1)*0.5f+1)), 3.0f + ((localChapter-1)*2.7f));
            }else {
                enemy = Enemy.get(visualLevel, x, y, (int)(300 * ((localChapter-1)*0.4f+1)));
            }
            enemy.setDamage(0.5f+localChapter*0.2f);
            Scene.getActiveScene().add(Scene.Layer.enemy, enemy);
        }
        ExpItem item = ExpItem.get(100 + random.nextInt(GameView.self.getWidth() - 100), chapter*0.5f+1);
        Scene.getActiveScene().add(Scene.Layer.item, item);
    }

    private void updateSpawnInterval() {
        spawnInterval = Math.max(INITIAL_SPAWN_INTERVAL-difficultyMultiplier((chapter-1) * 0.17f)*2, 1);
    }

    // difficultyMultiplier(0) == 0
    // difficultyMultiplier(0.5) == 0.9
    // difficultyMultiplier(1) == 0.992
    public static float difficultyMultiplier(float chapter){
        return 1.0f/(-0.92f - 9.f*chapter)+1.1f;
    }

    @Override
    public void draw(Canvas canvas) {
        // does nothing
    }

    public void reset() {
        wave = 0;
//        wave = 200;
        time = INITIAL_SPAWN_INTERVAL;
        spawnInterval = INITIAL_SPAWN_INTERVAL;
        chapter = level = nextTargetLevel = 1;
        ArrayList<GameObject> enemys = Scene.getActiveScene().getLayerObjects(Scene.Layer.enemy);
        for (GameObject enemy : enemys){
            Scene.getActiveScene().remove(enemy);
        }
    }
}
