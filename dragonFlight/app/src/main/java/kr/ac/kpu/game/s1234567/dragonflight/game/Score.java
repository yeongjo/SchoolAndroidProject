package kr.ac.kpu.game.s1234567.dragonflight.game;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Rect;

import kr.ac.kpu.game.s1234567.dragonflight.R;
import kr.ac.kpu.game.s1234567.dragonflight.framework.GameBitmap;
import kr.ac.kpu.game.s1234567.dragonflight.framework.GameObject;
import kr.ac.kpu.game.s1234567.dragonflight.ui.view.GameView;

public class Score implements GameObject {

    private final Bitmap bitmap;
    private final int right;
    private final int top;
    private int displayScore;

    public void setScore(int score) {
        this.score = score;
        this.displayScore = score;
    }

    int score;

    public Score(int right, int top){
        bitmap = GameBitmap.load(R.mipmap.number_24x32);
        this.right = right;
        this.top = top;
    }

    @Override
    public void update() {
        if(displayScore < score){
            displayScore++;
        }
    }

    @Override
    public void draw(Canvas canvas) {
        int value = this.displayScore;
        int nw = bitmap.getWidth()/10;
        int nh = bitmap.getHeight();
        Rect src =new Rect();
        Rect dst =new Rect();
        int x = right;
        int dw = (int) (nw* GameView.MULTIPLIER);
        int dh = (int) (nh* GameView.MULTIPLIER);
        while(value > 0){
            int digit = value % 10;
            src.set(digit * nw, 0, (digit+1)*nw, nh);
            x -= dw;
            dst.set(x, top, x+dw, top+dh);
            canvas.drawBitmap(bitmap, src, dst, null);
            value /= 10;
        }
    }

    public void addScore(int amount) {
        score += amount;
    }
}
