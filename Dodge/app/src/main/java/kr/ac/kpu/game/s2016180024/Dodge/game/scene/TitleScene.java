package kr.ac.kpu.game.s2016180024.Dodge.game.scene;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.media.MediaPlayer;
import android.view.MotionEvent;

import kr.ac.kpu.game.s2016180024.Dodge.R;
import kr.ac.kpu.game.s2016180024.Dodge.framework.Scene;
import kr.ac.kpu.game.s2016180024.Dodge.framework.Sound;
import kr.ac.kpu.game.s2016180024.Dodge.game.Leaderboard;
import kr.ac.kpu.game.s2016180024.Dodge.game.MainGame;
import kr.ac.kpu.game.s2016180024.Dodge.game.Player;
import kr.ac.kpu.game.s2016180024.Dodge.game.VerticalScrollBackground;
import kr.ac.kpu.game.s2016180024.Dodge.ui.activity.MainActivity;
import kr.ac.kpu.game.s2016180024.Dodge.ui.view.GameView;

public class TitleScene extends Scene {
    private static TitleScene self;
    private MediaPlayer mainBgmMediaPlayer;
    private Typeface tf;
    private Paint textPaint = new Paint();
    private Leaderboard leaderboard;

    public static TitleScene get(){
        if(self == null){
            self = new TitleScene();
        }
        return self;
    }

    public void onEnter(){
        super.onEnter();
        MainGame.playMediaPlayer(mainBgmMediaPlayer);
    }

    public void onExit(){
        MainGame.playMediaPlayer(mainBgmMediaPlayer);
    }

    public boolean initResources() {
        int w = GameView.self.getWidth();
        int h = GameView.self.getHeight();
        int marginY = (int) (41 * GameView.MULTIPLIER);
        leaderboard = Leaderboard.create(w/2, marginY, Paint.Align.CENTER);
        leaderboard.addUpdateLeaderboardCallback();

        super.initResources();

        mainBgmMediaPlayer = Sound.getMediaPlayer(R.raw.bgm);
        mainBgmMediaPlayer.setLooping(true); // Set looping
        mainBgmMediaPlayer.setVolume(50, 50);
        MainGame.playMediaPlayer(mainBgmMediaPlayer);

        add(Layer.ui, leaderboard);

        Player player = new Player(w / 2.0f, h - 300);
        add(Layer.player, player);
        
        VerticalScrollBackground bg = new VerticalScrollBackground(R.mipmap.bg_city, 10);
        add(Layer.bg1, bg);

        VerticalScrollBackground clouds = new VerticalScrollBackground(R.mipmap.clouds, 20);
        add(Layer.bg2, clouds);

        tf = Typeface.createFromAsset(GameView.self.getResources().getAssets(), "pfstardust.ttf");
        textPaint.setTextSize(GameView.MULTIPLIER * 20);
        textPaint.setTypeface(tf);
        textPaint.setColor(0xffffffff);
        textPaint.setTextAlign(Paint.Align.CENTER);

        return true;
    }

    public boolean onTouchEvent(MotionEvent event) {
        int action = event.getAction();
        if (action == MotionEvent.ACTION_DOWN) {
            Sound.play(R.raw.player_move_start, 0);
            return true;
        }
        if (action == MotionEvent.ACTION_UP) {
            Sound.play(R.raw.player_move_end, 0);
            Scene.addScene(GamePlayScene.get());
            return true;
        }
        return false;
    }

    @Override
    public void draw(Canvas canvas) {
        super.draw(canvas);
        int w = GameView.self.getWidth();
        int h = GameView.self.getHeight();
        textPaint.setTextSize(GameView.MULTIPLIER * 30);
        canvas.drawText(getString(R.string.game_name), w >> 1,(h >> 2) + (GameView.MULTIPLIER * 110), textPaint);
        textPaint.setTextSize(GameView.MULTIPLIER * 8);
        canvas.drawText(getString(R.string.game_description), w >> 1,h - GameView.MULTIPLIER * 83, textPaint);
        textPaint.setTextSize(GameView.MULTIPLIER * 13);
        canvas.drawText(getString(R.string.press_any_key), w >> 1,h - GameView.MULTIPLIER * 70, textPaint);
    }

    protected TitleScene(){}
}
