package kr.ac.kpu.game.s2016180024.Dodge.game;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Path;
import android.util.Log;

import java.util.ArrayList;
import java.util.Random;

import kr.ac.kpu.game.s2016180024.Dodge.R;
import kr.ac.kpu.game.s2016180024.Dodge.framework.CircleCollidable;
import kr.ac.kpu.game.s2016180024.Dodge.framework.CircleCollider;
import kr.ac.kpu.game.s2016180024.Dodge.framework.GameBitmap;
import kr.ac.kpu.game.s2016180024.Dodge.framework.GameObject;
import kr.ac.kpu.game.s2016180024.Dodge.framework.Sound;
import kr.ac.kpu.game.s2016180024.Dodge.framework.Vector2;
import kr.ac.kpu.game.s2016180024.Dodge.game.item.Item;
import kr.ac.kpu.game.s2016180024.Dodge.ui.view.GameView;

import static kr.ac.kpu.game.s2016180024.Dodge.ui.activity.MainActivity.RECIPROCAL_PIXEL_MULTIPLIER;

public class Player implements GameObject, CircleCollidable {
    private static final String TAG = Player.class.getSimpleName();
    private static final int BULLET_SPEED = 1500;
    private static final float FIRE_INTERVAL = 1.0f / 7.5f;
    private static final float Hit_EFFECT_DURATION = FIRE_INTERVAL / 3;
    private static final float INIT_HP = 3;
    private static final float INIT_STAMINA = 2;
    private static final float INIT_RADIUS = 20;
    private static final float DEFAULT_SPEED = 450;

    private GameBitmap playerBitmap;
    private GameBitmap playerAttackBitmap;
    private GameBitmap hitEffectBitmap;
    private float hitEffectDuration = 0.0f;
    private Vector2 pos = new Vector2();
    private Vector2 initPos = new Vector2();
    private Vector2 draggingTargetDelta = new Vector2();
    private Vector2 targetDelta = new Vector2();
    private Vector2 startDragPos = new Vector2();
    private float speed = 300;
    private boolean isMoving = false;
    private boolean isDragging = false;
    private float dragMultiplier = 1.5f;// 드래그할때 드래그 된 위치보다 얼마나 더 이동할지의 비율
    private Vector2 normalizedTargetDelta = new Vector2();
    private Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);
    private Paint zigzagPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
    private float radius = INIT_RADIUS * GameView.MULTIPLIER;
    private float additiveAttackRadius = 1;
    private float radiusMultiplier;
    private float totalStamina = INIT_STAMINA;
    private float stamina = INIT_STAMINA;
    private float totalHp = INIT_HP;
    private float hp = INIT_HP;
    private float exp = 0;
    private int level = 1;
    private float staminaRegenSpeed = 1;
    private Vector2 targetRightVector = new Vector2();
    private CircleCollider circleCollider = new CircleCollider();
    private boolean isDead = false;
    private boolean isDieNextFrame = false;
    private ArrayList<Item> items = new ArrayList<>();
    private Path path = new Path();
    private Random random = new Random();
    private int attackBgm = -1;

    public Player(float x, float y) {
        initPos.set(pos.set(x,y));
        this.playerBitmap = new GameBitmap(R.mipmap.player);
        this.hitEffectBitmap = new GameBitmap(R.mipmap.player_hit_effect);
        playerAttackBitmap = new GameBitmap(R.mipmap.player_attack);
        Vector2 circleVec = new Vector2(100,0);

        path.moveTo(circleVec.x,circleVec.y);
        float zigzagSize = 0.3f;
        float rotate = random.nextFloat()*10+5;
        for (float rotation = 0; rotation < 350; ) {
            circleVec.rotate(rotate);
            Vector2 linePos = circleVec.cpy();
            linePos.mul(random.nextFloat()*zigzagSize+(1-zigzagSize*0.5f));
            path.lineTo(linePos.x, linePos.y);
            rotate = random.nextFloat()*10+5;
            rotation += rotate;
        }
        path.close();
        zigzagPaint.setStyle(Paint.Style.STROKE);
        paint.setStrokeWidth(2);
        paint.setStyle(Paint.Style.STROKE);
        reset();
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
        exp = 0;
        level = 1;
        speed = DEFAULT_SPEED;
        isDieNextFrame = isDead = false;
        additiveAttackRadius = 1;
        radiusMultiplier = 1;
        items.clear();
    }

    public void update() {
        if(isDieNextFrame && !isDead){ // 아직 살아있고 다음 프레임에 죽는다면
            die();
        }
        if(isDead()){
            return;
        }
        MainGame game = MainGame.get();

        for (Item item : items){
            item.onUpdateActivationEffect(this);
        }

        if(isMoving && stamina > 0) { // 스테미나가 있고 움직이는중
            normalizedTargetDelta.set(targetDelta);
            Vector2 delta = normalizedTargetDelta.normalize().mul(speed * game.frameTime);
            targetDelta.sub(delta);
            stamina -= game.frameTime;
            radiusMultiplier += game.frameTime*0.5f;
            pos.add(delta);
            if ((((delta.x > 0 && 0 > targetDelta.x) || (delta.x < 0 && 0 < targetDelta.x)) ||
                    ((delta.y > 0 && 0 > targetDelta.y) || (delta.y < 0 && 0 < targetDelta.y))) ||
                    (targetDelta.x == 0 && targetDelta.y == 0)) {
                // Log.d(TAG, "update: targetDelta:"+targetDelta+" delta:"+delta);
                pos.add(targetDelta);
                isMoving = false;
                for (Item item : items){
                    item.stopMove(this);
                }
            }
        }else{
            if(attackBgm != -1) {
//                Log.d(TAG, "update: stop play attack bgm: "+attackBgm);
//                Sound.stop(attackBgm);
                MainGame.get().playNormalBGM();
                attackBgm = -1;
            }
            radiusMultiplier = 1;
            stamina += game.frameTime * staminaRegenSpeed;
            isMoving = false;
            if(stamina > totalStamina){
                stamina = totalStamina;
            }
            for (Item item : items){
                item.stopMove(this);
            }
        }

        hitEffectDuration += game.frameTime;
    }

    public void draw(Canvas canvas) {
        playerBitmap.setScale(radius * 0.0111f);
        playerAttackBitmap.setScale(radius * 0.0111f);
        playerBitmap.draw(canvas, pos.x, pos.y);
        float radius = this.radius * additiveAttackRadius * radiusMultiplier * RECIPROCAL_PIXEL_MULTIPLIER;
        if(isDragging){
            paint.setColor(0xff48ffaa);   //color.Green
            targetRightVector.set(draggingTargetDelta.x, draggingTargetDelta.y).normalize().rotate(90).mul(radius);
            canvas.drawLine(pos.x+targetRightVector.x, pos.y+targetRightVector.y, pos.x + draggingTargetDelta.x+targetRightVector.x, pos.y + draggingTargetDelta.y+targetRightVector.y, paint);
            canvas.drawLine(pos.x-targetRightVector.x, pos.y-targetRightVector.y, pos.x + draggingTargetDelta.x-targetRightVector.x, pos.y + draggingTargetDelta.y-targetRightVector.y, paint);
            canvas.drawCircle(pos.x + draggingTargetDelta.x, pos.y + draggingTargetDelta.y, radius, paint);
            canvas.drawCircle(pos.x, pos.y, radius, paint);
//            drawAttackRange(canvas, radius);
        }
        if(isMoving){
            paint.setColor(0xfff83530);   //color.yellow
            playerAttackBitmap.draw(canvas, pos.x, pos.y);
            targetRightVector.set(targetDelta.x, targetDelta.y).normalize().rotate(90).mul(radius);
            canvas.drawLine(pos.x+targetRightVector.x, pos.y+targetRightVector.y, pos.x + targetDelta.x+targetRightVector.x, pos.y + targetDelta.y+targetRightVector.y, paint);
            canvas.drawLine(pos.x-targetRightVector.x, pos.y-targetRightVector.y, pos.x + targetDelta.x-targetRightVector.x, pos.y + targetDelta.y-targetRightVector.y, paint);
            canvas.drawCircle(pos.x + targetDelta.x, pos.y + targetDelta.y, radius, paint);
//            canvas.drawCircle(pos.x, pos.y, radius, paint);
            drawAttackRange(canvas, radius);
        }
        if (hitEffectDuration < Hit_EFFECT_DURATION) {
            hitEffectBitmap.draw(canvas, pos.x, pos.y);
        }
    }

    void drawAttackRange(Canvas canvas, float radius){
        canvas.save();
        zigzagPaint.setColor(paint.getColor());
        zigzagPaint.setStrokeWidth(1/radius*500);
        canvas.translate(pos.x, pos.y);
        canvas.rotate(random.nextFloat()*360);
        canvas.scale(radius*0.01f, radius*0.01f);
        canvas.drawPath(path, zigzagPaint);
        canvas.restore();

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
        if(draggingTargetDelta.x + pos.x > GameView.self.getWidth()){
            draggingTargetDelta.x = GameView.self.getWidth() - pos.x;
        }
        if(draggingTargetDelta.y + pos.y < 0){
            draggingTargetDelta.y = -pos.y;
        }
        if(draggingTargetDelta.y + pos.y > GameView.self.getHeight()){
            draggingTargetDelta.y = GameView.self.getHeight() - pos.y;
        }
    }

    public void endDrag(float x, float y) {
        targetDelta.set(draggingTargetDelta);
        if(!isMoving){
            for (Item item : items){
                item.startMove(this);
            }
            MainGame.get().playAttackBgm();
            attackBgm = 1;
//            attackBgm = Sound.play(R.raw.attack_bgm);
        }
        isMoving = true;
        isDragging = false;
    }

    // 공격하는 타이밍에 공격한 적을 기반으로 아이템 효과 발동
    public void onAttack(Enemy enemy){
        for (Item item : items){
            item.onAttack(this, enemy);
        }
    }

    public void takeDamage(float damage){
        if(isDead()){
            return;
        }
        Sound.play(R.raw.player_hit, 0);
        hitEffectDuration = 0;
        hp -= damage;
        if(hp <= 0){
            hp = 0;
            Sound.play(R.raw.player_die, 0);
            isDieNextFrame = true;
        }
        Log.d(TAG, "player takeDamage: "+damage);
    }

    public void addItem(Item item){
        item.enterActiveEffect(this);
        items.add(item);
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
        radius = Math.max(10, radius);
    }

    public void addAdditiveRadius(float amount){
        additiveAttackRadius += amount;
    }

    public void resetAdditiveRadius(){
        additiveAttackRadius = 1;
    }

    public void addSpeed(float amount) {
        speed += amount;
    }

    public void addExp(float exp){
        this.exp += exp;
    }

    public boolean checkExpToLevelUp(){
        float requireExp = getTotalExp();
        if(this.exp >= requireExp){
            this.exp -= requireExp;
            level++;
            MainGame.get().askItem();
            return true;
        }
        return false;
    }

    private void die() {
        isDead = true;
        Log.d(TAG, "player dead");
    }

    public boolean isDead(){ return isDead; }
    public boolean isMoving() {
        return isMoving;
    }
    public float getTotalHp() {
        return totalHp;
    }
    public float getHp(){
        return hp;
    }
    public float getStamina(){
        return stamina;
    }
    public float getTotalStamina(){
        return totalStamina;
    }
    public float getRadius(){
        return radius;
    }
    public Vector2 getPos(){return pos;}
    public float getAdditiveRadius(){
        return additiveAttackRadius;
    }
    public float getExp() {
        return exp;
    }
    public float getTotalExp(){
        return level+level*6;
    }
    @Override
    public CircleCollider getCollider() {
        circleCollider.pos.set(pos);
        circleCollider.radius = radius * additiveAttackRadius * radiusMultiplier * RECIPROCAL_PIXEL_MULTIPLIER;
        return circleCollider;
    }
}
