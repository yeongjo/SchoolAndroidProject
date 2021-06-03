package kr.ac.kpu.game.s2016180024.Dodge.game;

import android.graphics.Canvas;
import android.util.Log;

import kr.ac.kpu.game.s2016180024.Dodge.R;
import kr.ac.kpu.game.s2016180024.Dodge.framework.AnimationGameBitmap;
import kr.ac.kpu.game.s2016180024.Dodge.framework.CircleCollidable;
import kr.ac.kpu.game.s2016180024.Dodge.framework.CircleCollider;
import kr.ac.kpu.game.s2016180024.Dodge.framework.GameBitmap;
import kr.ac.kpu.game.s2016180024.Dodge.framework.GameObject;
import kr.ac.kpu.game.s2016180024.Dodge.framework.Recyclable;
import kr.ac.kpu.game.s2016180024.Dodge.framework.Sound;
import kr.ac.kpu.game.s2016180024.Dodge.framework.Vector2;
import kr.ac.kpu.game.s2016180024.Dodge.ui.view.GameView;

import static kr.ac.kpu.game.s2016180024.Dodge.ui.activity.MainActivity.RECIPROCAL_PIXEL_MULTIPLIER;

public class Enemy implements GameObject, CircleCollidable, Recyclable {
    protected static final float FRAMES_PER_SECOND = 8.0f;
    private static final int[] RESOURCE_IDS = {
            R.mipmap.enemy_01, R.mipmap.enemy_02, R.mipmap.enemy_03, R.mipmap.enemy_04, R.mipmap.enemy_05,
            R.mipmap.enemy_06, R.mipmap.enemy_07, R.mipmap.enemy_08, R.mipmap.enemy_09, R.mipmap.enemy_10,
            R.mipmap.enemy_11, R.mipmap.enemy_12, R.mipmap.enemy_13, R.mipmap.enemy_14, R.mipmap.enemy_15,
            R.mipmap.enemy_16, R.mipmap.enemy_17, R.mipmap.enemy_18, R.mipmap.enemy_19, R.mipmap.enemy_20,
    };
    private static final String TAG = Enemy.class.getSimpleName();
    protected Vector2 pos = new Vector2();
    protected GameBitmap bitmap;
    private GameBitmap hitEffectBitmap;
    protected int level;
    protected int speed;
    private float damage = 1.0f;
    private CircleCollider circleCollider = new CircleCollider();
    private float radius = 20 * GameView.MULTIPLIER;
    private boolean isHitPlayer = false;

    protected Enemy() {
        Log.d(TAG, "Enemy constructor");
    }

    public void setDamage(float damage){this.damage = damage;}
    public float getDamage(){
        isHitPlayer= true;
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
        isHitPlayer= false;

        int resId = RESOURCE_IDS[level - 1];

        hitEffectBitmap = new GameBitmap(R.mipmap.enemy_hit_effect);
        this.bitmap = new AnimationGameBitmap(resId, FRAMES_PER_SECOND, 0);
//        this.bitmap.setScale(0.4f);
    }

    public void destroy(){
        MainGame game = MainGame.get();
        game.add(MainGame.Layer.effect, HitEffect.get(R.mipmap.enemy_hit_effect, pos, 0.1f));
        Sound.play(R.raw.enemy_hit, 0);
    }

    @Override
    public void update() {
        MainGame game = MainGame.get();
        pos.y += speed * game.frameTime;

        if (pos.y > GameView.self.getHeight()) {
            game.remove(this);
        }
    }

    @Override
    public void draw(Canvas canvas) {
        if(isHitPlayer) {
            hitEffectBitmap.draw(canvas, pos.x, pos.y);
        }
        bitmap.draw(canvas, pos.x, pos.y);
    }

    @Override
    public void recycle() {
        // 재활용통에 들어가는 시점에 불리는 함수. 현재는 할일없음.
    }

    @Override
    public CircleCollider getCollider() {
        circleCollider.pos.set(pos);
        circleCollider.radius = radius * RECIPROCAL_PIXEL_MULTIPLIER;
        return circleCollider;
    }
}
