package kr.ac.kpu.game.s2016180024.Dodge.game;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.RectF;

import kr.ac.kpu.game.s2016180024.Dodge.R;
import kr.ac.kpu.game.s2016180024.Dodge.framework.BoxCollidable;
import kr.ac.kpu.game.s2016180024.Dodge.framework.GameBitmap;
import kr.ac.kpu.game.s2016180024.Dodge.framework.GameObject;
import kr.ac.kpu.game.s2016180024.Dodge.framework.Vector2;
import kr.ac.kpu.game.s2016180024.Dodge.ui.view.GameView;

public class Player implements GameObject, BoxCollidable {
    private static final String TAG = Player.class.getSimpleName();
    private static final int BULLET_SPEED = 1500;
    private static final float FIRE_INTERVAL = 1.0f / 7.5f;
    private static final float LASER_DURATION = FIRE_INTERVAL / 3;
    private float fireTime = 0.0f;
    private Vector2 pos = new Vector2();
    private Vector2 draggingTargetDelta = new Vector2();
    private Vector2 targetDelta = new Vector2();
    private Vector2 startDragPos = new Vector2();
    private float speed = 800;
    private boolean isMoving = false;
    private boolean isDragging = false;
    private float dragMultiplier = 1.5f;
    private GameBitmap planeBitmap;
    private GameBitmap fireBitmap;
    private float stamina = 10;
    private Vector2 normalizedTargetDelta = new Vector2();
    private Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);
    private float radius = 20 * GameView.MULTIPLIER;

    public Player(float x, float y) {
        pos.x = x;
        pos.y = y;
        this.planeBitmap = new GameBitmap(R.mipmap.fighter);
        this.fireBitmap = new GameBitmap(R.mipmap.laser_0);
        paint.setStrokeWidth(14);
        paint.setStyle(Paint.Style.STROKE);
    }

    public float getStamina(){
        return stamina;
    }

    public void startDrag(float x, float y) {
        startDragPos.x = x;
        startDragPos.y = y;
        draggingTargetDelta.set(0,0);
        isDragging = true;
    }

    public void dragging(float x, float y) {
        draggingTargetDelta.x = (x - startDragPos.x) * dragMultiplier;
        draggingTargetDelta.y = (y - startDragPos.y) * dragMultiplier;
        if(draggingTargetDelta.x + pos.x < 0){
            draggingTargetDelta.x = -pos.x;
        }
        if(draggingTargetDelta.x + pos.x > GameView.view.getWidth()){
            draggingTargetDelta.x = GameView.view.getWidth() - pos.x;
        }
        if(draggingTargetDelta.y + pos.y < 0){
            draggingTargetDelta.y = -pos.y;
        }
        if(draggingTargetDelta.y + pos.y > GameView.view.getHeight()){
            draggingTargetDelta.y = GameView.view.getHeight() - pos.y;
        }
    }

    public void endDrag(float x, float y) {
        targetDelta.set(draggingTargetDelta);
        isMoving = true;
        isDragging = false;
    }

    public void update() {
        if(isMoving) {
            MainGame game = MainGame.get();
            normalizedTargetDelta.set(targetDelta);
            Vector2 delta = normalizedTargetDelta.normalize().mul(speed * game.frameTime);
            targetDelta.sub(delta);
            stamina -= game.frameTime;
            pos.add(delta);
            if (((delta.x > 0 && 0 > targetDelta.x) || (delta.x < 0 && 0 < targetDelta.x))&&
                    ((delta.y > 0 && 0 > targetDelta.y) || (delta.y < 0 && 0 < targetDelta.y))) {
                pos.sub(targetDelta);
                isMoving = false;
            }
        }

//        fireTime += game.frameTime;
//        if (fireTime >= FIRE_INTERVAL) {
//            fireBullet();
//            fireTime -= FIRE_INTERVAL;
//        }
    }

    private void fireBullet() {
        Bullet bullet = Bullet.get(pos.x, pos.y, BULLET_SPEED);
        MainGame game = MainGame.get();
        game.add(MainGame.Layer.bullet, bullet);
    }

    public void draw(Canvas canvas) {
        planeBitmap.draw(canvas, pos.x, pos.y);
        if(isDragging){
            paint.setColor(0xff00ff00);   //color.Green
            canvas.drawLine(pos.x, pos.y, pos.x + draggingTargetDelta.x, pos.y + draggingTargetDelta.y, paint);
        }else {
            paint.setColor(0xffff0000);   //color.RED
            canvas.drawLine(pos.x, pos.y, pos.x + targetDelta.x, pos.y + targetDelta.y, paint);
        }
        if(isMoving){
            canvas.drawCircle(pos.x, pos.y, radius, paint);
        }
//        if (fireTime < LASER_DURATION) {
//            fireBitmap.draw(canvas, pos.x, pos.y - 50);
//        }
    }

    @Override
    public void getBoundingRect(RectF rect) {
        planeBitmap.getBoundingRect(pos.x, pos.y, rect);
    }
}
