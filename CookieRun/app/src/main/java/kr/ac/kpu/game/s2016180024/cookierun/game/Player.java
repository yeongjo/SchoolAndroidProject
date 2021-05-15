package kr.ac.kpu.game.s2016180024.cookierun.game;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.RectF;
import android.util.Log;

import kr.ac.kpu.game.s2016180024.cookierun.R;
import kr.ac.kpu.game.s2016180024.cookierun.framework.BaseGame;
import kr.ac.kpu.game.s2016180024.cookierun.framework.iface.BoxCollidable;
import kr.ac.kpu.game.s2016180024.cookierun.framework.iface.GameObject;
import kr.ac.kpu.game.s2016180024.cookierun.framework.bitmap.IndexedAnimationGameBitmap;
import kr.ac.kpu.game.s2016180024.cookierun.framework.Vector2;
import kr.ac.kpu.game.s2016180024.cookierun.framework.view.GameView;

public class Player implements GameObject, BoxCollidable {
    private static final String TAG = Player.class.getSimpleName();
    private static final int BULLET_SPEED = 1500;
    private static final float FIRE_INTERVAL = 1.0f / 7.5f;
    private static final float LASER_DURATION = FIRE_INTERVAL / 3;
    private static final float GRAVITY = 2500;
    private Vector2 pos= new Vector2();
    private float speed = 800;
    private float vertSpeed = 0;
    private IndexedAnimationGameBitmap charBitmap;
    private Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);
    private float radius = 20 * GameView.MULTIPLIER;

    private enum State{
        running, jump, doubleJump, slide, hit
    }
    private State state = State.running;

    public Player(float x, float y) {
        pos.x = x;
        pos.y = y;
        this.charBitmap = new IndexedAnimationGameBitmap(R.mipmap.cookie, 4.5f, 4);
        charBitmap.setIndices(100,101,102,103);
        paint.setStrokeWidth(14);
        paint.setStyle(Paint.Style.STROKE);
    }

    public void update() {
        BaseGame game = MainGame.get();
        if(state == State.jump){
            float y = pos.y + game.frameTime * vertSpeed;
            vertSpeed += game.frameTime * GRAVITY;
            if(y >= GameView.view.getHeight() - 300){
                y = GameView.view.getHeight() - 300;
                state = State.running;
                charBitmap.setIndices(100,101,102,103);
            }
            pos.y = y;
        }
    }

    public void draw(Canvas canvas) {
        charBitmap.draw(canvas, pos.x, pos.y);
    }

    @Override
    public void getBoundingRect(RectF rect) {
        charBitmap.getBoundingRect(pos.x, pos.y, rect);
    }

    public void jump() {
        if(state != State.running && state != State.slide){
            Log.d(TAG, "jump: Not in a state that can jump: "+state);
            return;
        }
        state = State.jump;
        charBitmap.setIndices(7, 8);
        vertSpeed = -1270;
    }
}
