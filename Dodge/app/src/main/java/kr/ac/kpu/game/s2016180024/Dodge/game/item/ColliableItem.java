package kr.ac.kpu.game.s2016180024.Dodge.game.item;

import android.graphics.Canvas;

import kr.ac.kpu.game.s2016180024.Dodge.framework.CircleCollidable;
import kr.ac.kpu.game.s2016180024.Dodge.framework.CircleCollider;
import kr.ac.kpu.game.s2016180024.Dodge.framework.GameBitmap;
import kr.ac.kpu.game.s2016180024.Dodge.framework.GameObject;
import kr.ac.kpu.game.s2016180024.Dodge.framework.Recyclable;
import kr.ac.kpu.game.s2016180024.Dodge.framework.Vector2;
import kr.ac.kpu.game.s2016180024.Dodge.game.MainGame;
import kr.ac.kpu.game.s2016180024.Dodge.ui.activity.MainActivity;
import kr.ac.kpu.game.s2016180024.Dodge.ui.view.GameView;

public class ColliableItem extends Item implements GameObject, Recyclable, CircleCollidable {
    protected Vector2 pos = new Vector2();
    private float speed = 300;
    private static GameBitmap bitmap;
    private CircleCollider circleCollider = new CircleCollider();
    private float radius = 20 * GameView.MULTIPLIER;
    private static float radiusMultiplier;

    protected ColliableItem(int redId){
        radiusMultiplier = 1/ MainActivity.PIXEL_MULTIPLIER;
        bitmap = new GameBitmap(redId);
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
        bitmap.draw(canvas, pos.x, pos.y);
    }

    @Override
    public void recycle() {

    }

    @Override
    public CircleCollider getCollider() {
        circleCollider.pos.set(pos);
        circleCollider.radius = radius * radiusMultiplier;
        return circleCollider;
    }
}
