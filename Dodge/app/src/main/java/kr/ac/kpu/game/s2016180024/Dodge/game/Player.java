package kr.ac.kpu.game.s2016180024.Dodge.game;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.RectF;
import android.util.Log;

import java.lang.reflect.Array;
import java.util.ArrayList;

import kr.ac.kpu.game.s2016180024.Dodge.R;
import kr.ac.kpu.game.s2016180024.Dodge.framework.BoxCollidable;
import kr.ac.kpu.game.s2016180024.Dodge.framework.CircleCollidable;
import kr.ac.kpu.game.s2016180024.Dodge.framework.CircleCollider;
import kr.ac.kpu.game.s2016180024.Dodge.framework.GameBitmap;
import kr.ac.kpu.game.s2016180024.Dodge.framework.GameObject;
import kr.ac.kpu.game.s2016180024.Dodge.framework.Vector2;
import kr.ac.kpu.game.s2016180024.Dodge.game.item.Item;
import kr.ac.kpu.game.s2016180024.Dodge.ui.view.GameView;

public class Player implements GameObject, CircleCollidable {
    private static final String TAG = Player.class.getSimpleName();
    private static final int BULLET_SPEED = 1500;
    private static final float FIRE_INTERVAL = 1.0f / 7.5f;
    private static final float LASER_DURATION = FIRE_INTERVAL / 3;
    private static final float INIT_HP = 3;
    private static final float INIT_STAMINA = 2;
    private static final float INIT_RADIUS = 20;
    private float fireTime = 0.0f;
    private Vector2 pos = new Vector2();
    private Vector2 initPos = new Vector2();
    private Vector2 draggingTargetDelta = new Vector2();
    private Vector2 targetDelta = new Vector2();
    private Vector2 startDragPos = new Vector2();
    private float speed = 300;
    private boolean isMoving = false;
    private boolean isDragging = false;
    private float dragMultiplier = 1.5f;
    private GameBitmap planeBitmap;
    private GameBitmap fireBitmap;
    private Vector2 normalizedTargetDelta = new Vector2();
    private Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);
    private float radius = INIT_RADIUS * GameView.MULTIPLIER;
    private float additiveAttackRadius = 1;
    private float totalStamina = INIT_STAMINA;
    private float stamina = INIT_STAMINA;
    private float totalHp = INIT_HP;
    private float hp = INIT_HP;
    private float staminaRegenSpeed = 1;
    private Vector2 targetRightVector = new Vector2();
    private CircleCollider circleCollider = new CircleCollider();
    private boolean isDead = false;
    private boolean isDieNextFrame = false;
    private ArrayList<Item> items = new ArrayList<>();

    public Player(float x, float y) {
        initPos.set(pos.set(x,y));
        this.planeBitmap = new GameBitmap(R.mipmap.fighter);
        this.fireBitmap = new GameBitmap(R.mipmap.laser_0);
        paint.setStrokeWidth(14);
        paint.setStyle(Paint.Style.STROKE);
    }

    public void reset(){
        pos.set(initPos);
        radius = INIT_RADIUS * GameView.MULTIPLIER;
        totalHp = hp = INIT_HP;
        totalStamina = stamina = INIT_STAMINA;
        draggingTargetDelta.set(0,0);
        targetDelta.set(0,0);
        isMoving = false;
        isDragging = false;
        isDieNextFrame = isDead = false;
        additiveAttackRadius = 1;
        items.clear();
    }

    public float getStamina(){
        return stamina;
    }

    public float getTotalStamina(){
        return totalStamina;
    }

    public Vector2 getPos(){return pos;}

    public void addItem(Item item){
        item.enterActiveEffect(this);
        items.add(item);
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
        if(!isMoving){
            for (Item item : items){
                item.startMove(this);
            }
        }
        isMoving = true;
        isDragging = false;
    }

    public void onAttack(Enemy enemy){
        for (Item item : items){
            item.onAttack(this, enemy);
        }
    }

    public void update() {
        if(isDieNextFrame && !isDead){
            die();
        }
        if(isDead()){
            return;
        }
        MainGame game = MainGame.get();

        for (Item item : items){
            item.updateActivateEffect(this);
        }

        if(isMoving && stamina > 0) {
            normalizedTargetDelta.set(targetDelta);
            Vector2 delta = normalizedTargetDelta.normalize().mul(speed * game.frameTime);
            targetDelta.sub(delta);
            stamina -= game.frameTime;
            pos.add(delta);
            if ((((delta.x > 0 && 0 > targetDelta.x) || (delta.x < 0 && 0 < targetDelta.x)) ||
                    ((delta.y > 0 && 0 > targetDelta.y) || (delta.y < 0 && 0 < targetDelta.y))) ||
                    (targetDelta.x == 0 && targetDelta.y == 0)) {
                pos.add(targetDelta);
                Log.d(TAG, "update: targetDelta:"+targetDelta+" delta:"+delta);
                isMoving = false;
                for (Item item : items){
                    item.stopMove(this);
                }
            }
        }else{
            stamina += game.frameTime * staminaRegenSpeed;
            isMoving = false;
            for (Item item : items){
                item.stopMove(this);
            }
            if(stamina > totalStamina){
                stamina = totalStamina;
            }
        }

        fireTime += game.frameTime;
//        if (fireTime >= FIRE_INTERVAL) {
//            fireBullet();
//            fireTime -= FIRE_INTERVAL;
//        }
    }

    public void takeDamage(float damage){
        if(isDead()){
            return;
        }
        hp -= damage;
        if(hp <= 0){
            hp = 0;
            isDieNextFrame = true;
        }
        Log.d(TAG, "player takeDamage: "+damage);
    }

    public void showAttackEffect(){
        fireTime = 0;
    }


    public boolean isDead(){
        return isDead;
    }

    public void draw(Canvas canvas) {
        planeBitmap.draw(canvas, pos.x, pos.y);
        float radius = this.radius * additiveAttackRadius;
        if(isDragging){
            paint.setColor(0xff00ff00);   //color.Green
            targetRightVector.set(draggingTargetDelta.x, draggingTargetDelta.y).normalize().rotate(90).mul(radius);
            canvas.drawLine(pos.x+targetRightVector.x, pos.y+targetRightVector.y, pos.x + draggingTargetDelta.x+targetRightVector.x, pos.y + draggingTargetDelta.y+targetRightVector.y, paint);
            canvas.drawLine(pos.x-targetRightVector.x, pos.y-targetRightVector.y, pos.x + draggingTargetDelta.x-targetRightVector.x, pos.y + draggingTargetDelta.y-targetRightVector.y, paint);
            canvas.drawCircle(pos.x + draggingTargetDelta.x, pos.y + draggingTargetDelta.y, radius, paint);
        }
        if(isMoving){
            paint.setColor(0xffff0000);   //color.RED
            targetRightVector.set(targetDelta.x, targetDelta.y).normalize().rotate(90).mul(radius);
            canvas.drawLine(pos.x+targetRightVector.x, pos.y+targetRightVector.y, pos.x + targetDelta.x+targetRightVector.x, pos.y + targetDelta.y+targetRightVector.y, paint);
            canvas.drawLine(pos.x-targetRightVector.x, pos.y-targetRightVector.y, pos.x + targetDelta.x-targetRightVector.x, pos.y + targetDelta.y-targetRightVector.y, paint);
            canvas.drawCircle(pos.x + targetDelta.x, pos.y + targetDelta.y, radius, paint);
            canvas.drawCircle(pos.x, pos.y, radius, paint);
        }

        if (fireTime < LASER_DURATION) {
            fireBitmap.draw(canvas, pos.x, pos.y - 50);
        }
    }

    public boolean isMoving() {
        return isMoving;
    }

    public float getTotalHp() {
        return totalHp;
    }

    public float getHp(){
        return hp;
    }
    public void heal(float amount){
        hp += amount;
        hp = Math.min(hp, totalHp);
    }
    public void addHp(float amount){
        hp += amount;
        totalHp += amount;
    }
    public void addStamina(float amount){
        stamina += amount;
        totalStamina += amount;
    }
    public void addRadius(float amount){
        radius += amount;
    }
    public float getRadius(){
        return radius;
    }
    public void addAdditiveRadius(float amount){
        additiveAttackRadius += amount;
    }
    public void resetAdditiveRadius(){
        additiveAttackRadius = 1;
    }
    public float getAdditiveRadius(){
        return additiveAttackRadius;
    }

    @Override
    public CircleCollider getCollider() {
        circleCollider.pos.set(pos);
        circleCollider.radius = radius * additiveAttackRadius;
        return circleCollider;
    }

    private void die() {
        isDead = true;
        Log.d(TAG, "player dead");
    }

    private void fireBullet() {
        Bullet bullet = Bullet.get(pos.x, pos.y, BULLET_SPEED);
        MainGame game = MainGame.get();
        game.add(MainGame.Layer.bullet, bullet);
    }

    public void addSpeed(float amount) {
        speed += amount;
    }
}
