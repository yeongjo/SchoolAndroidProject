package kr.ac.kpu.game.s2016180024.Dodge.game;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.RectF;

import kr.ac.kpu.game.s2016180024.Dodge.framework.GameObject;
import kr.ac.kpu.game.s2016180024.Dodge.ui.view.GameView;

public class PlayerHud  implements GameObject {

    private static final float HEALTH_DECREASE_SPEED = 20;
    private static final int BAR_HEIGHT = 40;
    private Player player;
    private RectF dst = new RectF();
    private Paint paint = new Paint();
    private RectF initialDstRect = new RectF();
    private float hpRectRight;
    private float prevHpRectRight = 0;
    private float totalHpRectRight;
    private float rectLeft;
    private float staminaRectRight;
    private float totalStaminaRectRight;
    private float sizeMultiplier = 21.0f;
    private float maxHealth = 88;

    public PlayerHud(int left, int top){
        this.rectLeft = left;
        dst.set(left, top, left, top+BAR_HEIGHT);
        initialDstRect.set(dst);
    }

    @Override
    public void update() {
        player = MainGame.get().getPlayer();
        sizeMultiplier = GameView.view.getWidth() / (GameView.MULTIPLIER * 25);
        float frameTime = MainGame.get().frameTime;
        if(player != null) {
            staminaRectRight = rectLeft + (player.getStamina() * GameView.MULTIPLIER * sizeMultiplier);
            totalStaminaRectRight = rectLeft + (player.getTotalStamina() * GameView.MULTIPLIER * sizeMultiplier);
            hpRectRight = rectLeft + (player.getHp() * GameView.MULTIPLIER * sizeMultiplier);
            prevHpRectRight += (hpRectRight > prevHpRectRight? 1:-1) *frameTime*HEALTH_DECREASE_SPEED;
            totalHpRectRight = rectLeft + (player.getTotalHp() * GameView.MULTIPLIER * sizeMultiplier);
        }
    }

    @Override
    public void draw(Canvas canvas) {
        paint.setColor(0xff000000);
        dst.right = totalHpRectRight;
        canvas.drawRect(dst, paint);
        paint.setColor(0xffffffff);
        dst.right = prevHpRectRight;
        canvas.drawRect(dst, paint);
        paint.setColor(0xffFF7777);
        dst.right = hpRectRight;
        canvas.drawRect(dst, paint);

        moveRectToNextLine();
        paint.setColor(0xff000000);
        dst.right = totalStaminaRectRight;
        canvas.drawRect(dst, paint);
        paint.setColor(0xff77FF77);
        dst.right = staminaRectRight;
        canvas.drawRect(dst, paint);

        resetRectPos();
    }

    public void reset(){
        player = MainGame.get().getPlayer();
        if(player != null) {
            sizeMultiplier = GameView.view.getWidth() / (GameView.MULTIPLIER * 25);
            totalHpRectRight = rectLeft + (player.getTotalHp() * GameView.MULTIPLIER * sizeMultiplier);
        }
        prevHpRectRight = totalHpRectRight;
    }

    void moveRectToNextLine(){
        dst.top += BAR_HEIGHT+10;
        dst.bottom += BAR_HEIGHT+10;
    }

    void resetRectPos(){
        dst.set(initialDstRect);
    }
}
