package kr.ac.kpu.game.s2016180024.Dodge.game;

import android.graphics.Canvas;
import android.graphics.RectF;
import android.util.Log;

import java.util.Vector;

import kr.ac.kpu.game.s2016180024.Dodge.R;
import kr.ac.kpu.game.s2016180024.Dodge.framework.AnimationGameBitmap;
import kr.ac.kpu.game.s2016180024.Dodge.framework.BoxCollidable;
import kr.ac.kpu.game.s2016180024.Dodge.framework.CircleCollidable;
import kr.ac.kpu.game.s2016180024.Dodge.framework.CircleCollider;
import kr.ac.kpu.game.s2016180024.Dodge.framework.GameBitmap;
import kr.ac.kpu.game.s2016180024.Dodge.framework.GameObject;
import kr.ac.kpu.game.s2016180024.Dodge.framework.Recyclable;
import kr.ac.kpu.game.s2016180024.Dodge.framework.Vector2;
import kr.ac.kpu.game.s2016180024.Dodge.ui.view.GameView;

public class Enemy implements GameObject, CircleCollidable, Recyclable {
    private static final float FRAMES_PER_SECOND = 8.0f;
    private static final int[] RESOURCE_IDS = {
            R.mipmap.enemy_01, R.mipmap.enemy_02, R.mipmap.enemy_03, R.mipmap.enemy_04, R.mipmap.enemy_05,
            R.mipmap.enemy_06, R.mipmap.enemy_07, R.mipmap.enemy_08, R.mipmap.enemy_09, R.mipmap.enemy_10,
            R.mipmap.enemy_11, R.mipmap.enemy_12, R.mipmap.enemy_13, R.mipmap.enemy_14, R.mipmap.enemy_15,
            R.mipmap.enemy_16, R.mipmap.enemy_17, R.mipmap.enemy_18, R.mipmap.enemy_19, R.mipmap.enemy_20,
    };
    private static final String TAG = Enemy.class.getSimpleName();
    protected Vector2 pos = new Vector2();
    private GameBitmap bitmap;
    protected int level;
    protected int speed;
    private float damage = 1.0f;
    private CircleCollider circleCollider = new CircleCollider();
    private float radius = 20 * GameView.MULTIPLIER;

    protected Enemy() {
        Log.d(TAG, "Enemy constructor");
    }

    public float getDamage(){
        return damage;
    }

    public static Enemy get(int level, int x, int y, int speed) {
        MainGame game = MainGame.get();
        Enemy enemy = (Enemy) game.get(Enemy.class);
        if (enemy == null) {
            enemy = new Enemy();
        }

        enemy.init(level, x, y, speed);
        return enemy;
    }

    protected void init(int level, int x, int y, int speed) {
        pos.x = x;
        pos.y = y;
        this.speed = speed;
        this.level = level;
        damage = level;

        int resId = RESOURCE_IDS[level - 1];

        this.bitmap = new AnimationGameBitmap(resId, FRAMES_PER_SECOND, 0);
        this.bitmap.setScale(0.4f);
    }

    @Override
    public void update() {
        MainGame game = MainGame.get();
        pos.y += speed * game.frameTime;

        if (pos.y > GameView.view.getHeight()) {
            game.remove(this);
        }
    }

    @Override
    public void draw(Canvas canvas) {
        bitmap.draw(canvas, pos.x, pos.y);
    }

    @Override
    public void recycle() {
        // 재활용통에 들어가는 시점에 불리는 함수. 현재는 할일없음.
    }

    @Override
    public CircleCollider getCollider() {
        circleCollider.pos.set(pos);
        circleCollider.radius = radius;
        return circleCollider;
    }
}
