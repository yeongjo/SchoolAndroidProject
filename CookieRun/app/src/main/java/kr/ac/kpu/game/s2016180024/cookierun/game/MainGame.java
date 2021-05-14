package kr.ac.kpu.game.s2016180024.cookierun.game;

import android.view.MotionEvent;

import kr.ac.kpu.game.s2016180024.cookierun.R;
import kr.ac.kpu.game.s2016180024.cookierun.framework.BaseGame;
import kr.ac.kpu.game.s2016180024.cookierun.framework.iface.GameObject;
import kr.ac.kpu.game.s2016180024.cookierun.framework.object.HorizontalScrollBackground;
import kr.ac.kpu.game.s2016180024.cookierun.framework.view.GameView;

public class MainGame extends BaseGame {
    private boolean initialized;
    private Player player;
    private Score score;

    public enum Layer {
        bg1, enemy, bullet, player, bg2, ui, controller, ENEMY_COUNT
    }

    public void add(Layer layer, GameObject obj){
        add(layer.ordinal(), obj);
    }

    public Player getPlayer(){
        return player;
    }

    @Override
    public boolean initResources() {
        if (initialized) {
            return false;
        }
        int w = GameView.view.getWidth();
        int h = GameView.view.getHeight();

        initLayers(Layer.ENEMY_COUNT.ordinal());

        player = new Player(w/2, h - 300);
        //layers.get(Layer.player.ordinal()).add(player);
        add(Layer.player, player);

        int margin = (int) (20 * GameView.MULTIPLIER);
        score = new Score(w - margin, margin);
        score.setScore(0);
        add(Layer.ui, score);

        HorizontalScrollBackground bg = new HorizontalScrollBackground(R.mipmap.cookie_run_bg_1, 10);
        add(Layer.bg1, bg);
        HorizontalScrollBackground bg2 = new HorizontalScrollBackground(R.mipmap.cookie_run_bg_1, 20);
        add(Layer.bg1, bg2);

        HorizontalScrollBackground clouds = new HorizontalScrollBackground(R.mipmap.cookie_run_bg_2, 30);
        add(Layer.bg2, clouds);

        initialized = true;
        return true;
    }

    @Override
    public void update() {
        super.update();
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        int action = event.getAction();
        if (action == MotionEvent.ACTION_DOWN) {
            player.startDrag(event.getX(), event.getY());
            return true;
        }
        if(action == MotionEvent.ACTION_MOVE){
            player.dragging(event.getX(), event.getY());
            return true;
        }
        if (action == MotionEvent.ACTION_UP){
            player.endDrag(event.getX(), event.getY());
            return true;
        }
        return false;
    }
}
