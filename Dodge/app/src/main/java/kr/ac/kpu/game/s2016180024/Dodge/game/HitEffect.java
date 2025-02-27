package kr.ac.kpu.game.s2016180024.Dodge.game;

import android.graphics.Canvas;

import kr.ac.kpu.game.s2016180024.Dodge.framework.GameBitmap;
import kr.ac.kpu.game.s2016180024.Dodge.framework.GameObject;
import kr.ac.kpu.game.s2016180024.Dodge.framework.Recyclable;
import kr.ac.kpu.game.s2016180024.Dodge.framework.Scene;
import kr.ac.kpu.game.s2016180024.Dodge.framework.Vector2;
import kr.ac.kpu.game.s2016180024.Dodge.game.scene.GamePlayScene;

public class HitEffect implements GameObject, Recyclable {
    private float remainTime;
    private Vector2 pos = new Vector2();
    private GameBitmap bitmap;

    protected HitEffect() {
    }

    public static HitEffect get(int resId, Vector2 pos, float remainTime) {
        GamePlayScene game = GamePlayScene.get();
        HitEffect hitEffect = (HitEffect) game.get(HitEffect.class);
        if (hitEffect == null) {
            hitEffect = new HitEffect();
        }

        hitEffect.init(resId, pos, remainTime);
        return hitEffect;
    }

    protected void init(int resId, Vector2 pos, float remainTime){
        this.remainTime = remainTime;
        this.pos = pos;
        bitmap = new GameBitmap(resId);
    }

    @Override
    public void update() {
        MainGame game = MainGame.get();
        remainTime -= game.frameTime;
        if(remainTime <= 0){
            Scene.getActiveScene().remove(this);
        }
    }

    @Override
    public void draw(Canvas canvas) {
        bitmap.draw(canvas, pos.x, pos.y);
    }

    @Override
    public void recycle() {

    }
}
