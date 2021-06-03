package kr.ac.kpu.game.s2016180024.Dodge.game;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.RectF;

import kr.ac.kpu.game.s2016180024.Dodge.framework.GameObject;
import kr.ac.kpu.game.s2016180024.Dodge.ui.view.GameView;

public class PlayerHud  implements GameObject {

    private static final float HEALTH_DECREASE_SPEED = 20;
    private static final float EXP_INCREASE_SPEED = 1;
    private static final int BAR_HEIGHT = 25;
    private static final int BAR_NEXT_LINE_OFFSET = 0;
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
    private float expRectRight;
    private float prevExpRectRight;
    private float totalExpRectRight;
    private float sizeMultiplier = 21.0f;
    private float maxHealth = 88;
    private int movingStaminaColor = 0xffdbcb38;
    private int defaultStaminaColor = 0xff48ffaa;
    private int staminaColor = defaultStaminaColor;

    public PlayerHud(int left, int top){
        this.rectLeft = left;
        dst.set(left, top, left, top+BAR_HEIGHT);
        initialDstRect.set(dst);
    }

    @Override
    public void update() {
        player = MainGame.get().getPlayer();
        sizeMultiplier = GameView.self.getWidth() / 25.0f;
        float frameTime = MainGame.get().frameTime;
        if(player != null) {
            staminaRectRight = rectLeft + (player.getStamina() * sizeMultiplier);
            totalStaminaRectRight = rectLeft + (player.getTotalStamina() * sizeMultiplier);
            hpRectRight = rectLeft + (player.getHp() * sizeMultiplier);
            prevHpRectRight += (hpRectRight > prevHpRectRight? 1:-1) *frameTime*HEALTH_DECREASE_SPEED;
            totalHpRectRight = rectLeft + (player.getTotalHp() * sizeMultiplier);
            totalExpRectRight = GameView.self.getWidth();
            expRectRight = rectLeft + (totalExpRectRight - rectLeft) * player.getExp() / player.getTotalExp();
            if(expRectRight > prevExpRectRight){
                prevExpRectRight += (expRectRight - prevExpRectRight)*frameTime*EXP_INCREASE_SPEED;
            }else{
                prevExpRectRight = expRectRight;
            }
            staminaColor = player.isMoving() ? movingStaminaColor : defaultStaminaColor;
        }
    }

    @Override
    public void draw(Canvas canvas) {
        //5fd1ff
        paint.setColor(0xff000000);
        dst.right = totalExpRectRight;
        canvas.drawRect(dst, paint);
        paint.setColor(0xffffffff);
        dst.right = expRectRight;
        canvas.drawRect(dst, paint);
        paint.setColor(0xff2faada);
        dst.right = prevExpRectRight;
        canvas.drawRect(dst, paint);

        moveRectToNextLine();
        paint.setColor(0xff000000);
        dst.right = totalHpRectRight;
        canvas.drawRect(dst, paint);
        paint.setColor(0xffffffff);
        dst.right = prevHpRectRight;
        canvas.drawRect(dst, paint);
        paint.setColor(0xffaa0d24);
        dst.right = hpRectRight;
        canvas.drawRect(dst, paint);

        moveRectToNextLine();
        paint.setColor(0xff000000);
        dst.right = totalStaminaRectRight;
        canvas.drawRect(dst, paint);
        paint.setColor(staminaColor);
        dst.right = staminaRectRight;
        canvas.drawRect(dst, paint);

        resetRectPos();
    }

    public void reset(){
        player = MainGame.get().getPlayer();
        if(player != null) {
            sizeMultiplier = GameView.self.getWidth() / (GameView.MULTIPLIER * 25);
            totalHpRectRight = rectLeft + (player.getTotalHp() * GameView.MULTIPLIER * sizeMultiplier);
        }
        prevHpRectRight = totalHpRectRight;
        prevExpRectRight = 0;
    }

    void moveRectToNextLine(){
        dst.top += BAR_HEIGHT+BAR_NEXT_LINE_OFFSET;
        dst.bottom += BAR_HEIGHT+BAR_NEXT_LINE_OFFSET;
    }

    void resetRectPos(){
        dst.set(initialDstRect);
    }
}
