package kr.ac.kpu.game.s2016180024.Dodge.game.scene;

import android.app.AlertDialog;
import android.content.SharedPreferences;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.media.MediaPlayer;
import android.util.Log;
import android.view.MotionEvent;
import android.widget.EditText;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Random;

import kr.ac.kpu.game.s2016180024.Dodge.R;
import kr.ac.kpu.game.s2016180024.Dodge.framework.GameObject;
import kr.ac.kpu.game.s2016180024.Dodge.framework.Scene;
import kr.ac.kpu.game.s2016180024.Dodge.framework.Sound;
import kr.ac.kpu.game.s2016180024.Dodge.game.Enemy;
import kr.ac.kpu.game.s2016180024.Dodge.game.EnemyGenerator;
import kr.ac.kpu.game.s2016180024.Dodge.game.HitEffect;
import kr.ac.kpu.game.s2016180024.Dodge.game.Leaderboard;
import kr.ac.kpu.game.s2016180024.Dodge.game.MainGame;
import kr.ac.kpu.game.s2016180024.Dodge.game.Player;
import kr.ac.kpu.game.s2016180024.Dodge.game.PlayerHud;
import kr.ac.kpu.game.s2016180024.Dodge.game.Score;
import kr.ac.kpu.game.s2016180024.Dodge.game.VerticalScrollBackground;
import kr.ac.kpu.game.s2016180024.Dodge.game.item.AttackRangeItem;
import kr.ac.kpu.game.s2016180024.Dodge.game.item.ColliableItem;
import kr.ac.kpu.game.s2016180024.Dodge.game.item.Item;
import kr.ac.kpu.game.s2016180024.Dodge.game.item.LifeStealItem;
import kr.ac.kpu.game.s2016180024.Dodge.game.item.statsItem;
import kr.ac.kpu.game.s2016180024.Dodge.ui.activity.MainActivity;
import kr.ac.kpu.game.s2016180024.Dodge.ui.view.GameView;
import kr.ac.kpu.game.s2016180024.Dodge.utils.CollisionHelper;

public class GamePlayScene extends Scene {
    private static final String SAVE_KEY = "PrevName";
    private static final String TAG = GamePlayScene.class.getSimpleName();
    private static GamePlayScene self;
    private Player player;
    private Score score;
    private PlayerHud playerHud;
    private boolean isPlaying = true;
    private EnemyGenerator enemyGenerator;
    private Leaderboard leaderboard;
    private float leaderboardVisibleRemainTime;
    private MediaPlayer mainBgmMediaPlayer;
    private MediaPlayer attackBgmMediaPlayer;

    public static GamePlayScene get(){
        if(self == null){
            self = new GamePlayScene();
        }
        return self;
    }

    protected GamePlayScene(){}

    public void onEnter(){
        super.onEnter();
        reset();
    }

    public void onExit(){

    }

    public Score getScore(){
        return score;
    }

    public Player getPlayer(){
        return player;
    }

    public boolean initResources() {
        int w = GameView.self.getWidth();
        int h = GameView.self.getHeight();
        int marginX = (int) (5 * GameView.MULTIPLIER);
        int marginY = (int) (20 * GameView.MULTIPLIER);
        leaderboard = Leaderboard.create(w - marginX, marginY + GameView.MULTIPLIER*21, Paint.Align.RIGHT);

        super.initResources();

        attackBgmMediaPlayer = Sound.getMediaPlayer( R.raw.attack_bgm);
        attackBgmMediaPlayer.setLooping(true); // Set looping
        attackBgmMediaPlayer.setVolume(50, 50);

        mainBgmMediaPlayer = Sound.getMediaPlayer(R.raw.bgm);
        mainBgmMediaPlayer.setLooping(true); // Set looping
        mainBgmMediaPlayer.setVolume(50, 50);
        MainGame.playMediaPlayer(mainBgmMediaPlayer);

        player = new Player(w/2.0f, h - 300);
        //layers.get(Layer.player.ordinal()).add(player);
        add(Layer.player, player);
        enemyGenerator = new EnemyGenerator();
        add(Layer.controller, enemyGenerator);

        score = new Score(w - marginX, marginY);
        score.setScore(0);
        add(Layer.ui, score);

        add(Layer.ui, leaderboard);

        playerHud = new PlayerHud(0, 0);
        add(Layer.ui, playerHud);

        VerticalScrollBackground bg = new VerticalScrollBackground(R.mipmap.bg_city, 10);
        add(Layer.bg1, bg);

        VerticalScrollBackground clouds = new VerticalScrollBackground(R.mipmap.clouds, 20);
        add(Layer.bg2, clouds);

        reset();
        return true;
    }

    public void update() {
        if (!isPlaying) return;
        super.update();
        if(player.isDead()){
            askRestart();
            return;
        }

        MainGame game = MainGame.get();
        leaderboardVisibleRemainTime -= game.frameTime;
        leaderboard.setVisible(leaderboardVisibleRemainTime > 0);

        if(player.checkExpToLevelUp()){
            ArrayList<GameObject> enemies = layers.get(Layer.enemy.ordinal());
            for(GameObject object : enemies) {
                remove(object);
            }
            return;
        }

        ArrayList<GameObject> enemies = layers.get(Layer.enemy.ordinal());
        ArrayList<GameObject> items = layers.get(Layer.item.ordinal());
        for (GameObject o1: enemies) {
            Enemy enemy = (Enemy) o1;
            boolean collided = false;
            if (collided) {
                break;
            }
            if (CollisionHelper.collides(enemy, player)) {
                if(!player.isMoving()) {
                    player.takeDamage(enemy.getDamage());
                    add(Layer.effect, HitEffect.get(R.mipmap.player_hit_effect, enemy.getPos(), 0.1f));
                }else{
                    player.onAttack(enemy);
                    player.addExp(enemy.getDamage()*0.1f+1);
                    enemy.destroy();
                }
                score.addScore((int)(enemy.getDamage()*10.0f/8.0f));
                remove(enemy, false);
                break;
            }
        }
        if(items != null) {
            for (GameObject o1 : items) {
                ColliableItem item = (ColliableItem) o1;
                if (CollisionHelper.collides(item, player)) {
                    player.addItem(item);
                    remove(item, false);
                    break;
                }
            }
        }
    }

    public boolean onTouchEvent(MotionEvent event) {
        int action = event.getAction();
        if (action == MotionEvent.ACTION_DOWN) {
            if(player.isDead()){
                askRestart();
                return true;
            }
            Sound.play(R.raw.player_move_start, 0, 0.5f);
            player.startDrag(event.getX(), event.getY());
            return true;
        }
        if(action == MotionEvent.ACTION_MOVE){
            player.dragging(event.getX(), event.getY());
            return true;
        }
        if (action == MotionEvent.ACTION_UP){
            Sound.play(R.raw.player_move_end, 0, 0.5f);
            player.endDrag(event.getX(), event.getY());
            return true;
        }
        return false;
    }


    public void askItem(){
        Log.d(TAG, "askItem");
        isPlaying = false;
        AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.self);
        builder.setCancelable(false);
        builder.setTitle(R.string.select_item);
        int chapter = enemyGenerator.getChapter();
        float difficulty = EnemyGenerator.difficultyMultiplier(chapter/6.0f);
        ArrayList<Item> itemLists = new ArrayList<>();
        itemLists.add(new statsItem(statsItem.Type.heal, difficulty*1+3));
        itemLists.add(new statsItem(statsItem.Type.addHp, 1 * difficulty));
        itemLists.add(new statsItem(statsItem.Type.addStamina, 1 * difficulty));
        itemLists.add(new statsItem(statsItem.Type.addRadius, 33 * difficulty));
        itemLists.add(new statsItem(statsItem.Type.addRadius, -23 * difficulty));
        itemLists.add(new statsItem(statsItem.Type.addSpeed, 210 * difficulty));
        itemLists.add(new statsItem(statsItem.Type.subSpeedAddStamina, 1.5f * difficulty));
        itemLists.add(new LifeStealItem(0.1f));
        itemLists.add(new AttackRangeItem(0.6f));
        Random random = new Random();
        ArrayList<Item> items = new ArrayList<>();
        for (int i=0; i< itemLists.size(); ++i) {
            int randomIndex =random.nextInt(itemLists.size());
            if(i != randomIndex) {
                Item temp = itemLists.get(i);
                itemLists.set(i, itemLists.get(randomIndex));
                itemLists.set(randomIndex, temp);
            }
        }
        for (int i=0; i< Math.min(3, itemLists.size()); ++i) {
            items.add(itemLists.get(i));
        }
        String[] itemNames = new String[items.size()];
        int i = 0;
        for (Item item : items){
            itemNames[i] = item.toString();
            ++i;
        }
        builder.setItems(itemNames, (dialog, id) -> {
            // 프로그램을 종료한다
            player.addItem(items.get(id));
            Log.d(TAG, "askItem: selected item: "+ items.get(id));
            isPlaying = true;
            Sound.play(R.raw.btn_click, 0);
            Sound.play(R.raw.item_selected, 0);
            Toast.makeText(MainActivity.self,
                    items.get(id) + getString(R.string.selected),
                    Toast.LENGTH_SHORT).show();
            dialog.dismiss();
        });
        AlertDialog alert = builder.create();
        alert.show();
    }

    void askRestart(){
        // 알람 다이어그램을 사용해서 팜업질문을 할 수 있다.
        Log.d(TAG, "askRestart");
        isPlaying = false;
        AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.self);
        EditText input =new EditText(MainActivity.self);
        String prevName = "";
        SharedPreferences settings = MainActivity.self.getSharedPreferences(SAVE_KEY, 0);
        prevName = settings.getString("prevName", prevName);
        input.setHint(prevName == null ? getString(R.string.type_name) : prevName);

        // Get from the SharedPreferences
        input.setText(prevName == null ? "" : prevName);

        builder.setView(input);
        builder.setTitle(getString(R.string.your_score_is)+score.getScore());
        builder.setMessage(getString(R.string.do_you_want_to_restart_game));
        builder.setPositiveButton(getString(R.string.yes), (dialog, which) -> {
            Sound.play(R.raw.btn_click, 0);
            String name = input.getText().toString();
            SharedPreferences.Editor editor = settings.edit();
            editor.putString("prevName", name);
            editor.apply();

            leaderboard.addToLeaderBoard(name, score.getScore());
            ArrayList<GameObject> enemies = layers.get(Layer.enemy.ordinal());
            ArrayList<GameObject> items = layers.get(Layer.item.ordinal());
            for (GameObject object : enemies) {
                remove(object);
            }
            for (GameObject object : items) {
                remove(object);
            }
            reset();
        });
        builder.setNegativeButton(getString(R.string.no), ((dialog, which) -> {
            Sound.play(R.raw.btn_click, 0);
        }));

        AlertDialog alert = builder.create();
        alert.show();
    }

    public void playAttackBgm(){
        MainGame.pauseMediaPlayer(mainBgmMediaPlayer);
        MainGame.playMediaPlayer(attackBgmMediaPlayer);
    }

    public void playNormalBGM(){
        MainGame.pauseMediaPlayer(attackBgmMediaPlayer);
        MainGame.playMediaPlayer(mainBgmMediaPlayer);
    }

    void reset(){
        player.reset();
        playerHud.reset();
        score.setScore(0);
        enemyGenerator.reset();
        isPlaying = true;
        leaderboardVisibleRemainTime = 3;
        leaderboard.addUpdateLeaderboardCallback();
    }

}
