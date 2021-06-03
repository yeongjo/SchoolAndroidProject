package kr.ac.kpu.game.s2016180024.Dodge.game;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.graphics.Canvas;
import android.graphics.Typeface;
import android.media.MediaPlayer;
import android.util.Log;
import android.view.MotionEvent;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Random;

import kr.ac.kpu.game.s2016180024.Dodge.R;
import kr.ac.kpu.game.s2016180024.Dodge.framework.GameObject;
import kr.ac.kpu.game.s2016180024.Dodge.framework.Recyclable;
import kr.ac.kpu.game.s2016180024.Dodge.framework.Sound;
import kr.ac.kpu.game.s2016180024.Dodge.game.item.AttackRangeItem;
import kr.ac.kpu.game.s2016180024.Dodge.game.item.ColliableItem;
import kr.ac.kpu.game.s2016180024.Dodge.game.item.Item;
import kr.ac.kpu.game.s2016180024.Dodge.game.item.LifeStealItem;
import kr.ac.kpu.game.s2016180024.Dodge.game.item.statsItem;
import kr.ac.kpu.game.s2016180024.Dodge.ui.activity.MainActivity;
import kr.ac.kpu.game.s2016180024.Dodge.ui.view.GameView;
import kr.ac.kpu.game.s2016180024.Dodge.utils.CollisionHelper;

public class MainGame {
    private static final String TAG = MainGame.class.getSimpleName();
    private static final String SAVE_KEY = "PrevNam" + "e";
    private static HashMap<Class, ArrayList<GameObject>> recycleBin = new HashMap<>();
    private static MainGame self;

    public float frameTime;
    public ArrayList<ArrayList<GameObject>> layers;
    private Player player;
    private Score score;
    private PlayerHud playerHud;
    private boolean initialized;
    private boolean isPlaying = true;
    private EnemyGenerator enemyGenerator;
    private Leaderboard leaderboard;
    private MediaPlayer mediaPlayer;

    public enum Layer {
        bg1, bg2, enemy, bullet, item, player, effect, ui, controller, ENEMY_COUNT
    }

    public static MainGame get() {
        if (self == null) {
            self = new MainGame();
        }
        return self;
    }

    public MediaPlayer getMediaPlayer(){
        return mediaPlayer;
    }

    public Score getScore(){
        return score;
    }

    public void recycle(GameObject object) {
        Class clazz = object.getClass();
        ArrayList<GameObject> array = recycleBin.get(clazz);
        if (array == null) {
            array = new ArrayList<>();
            recycleBin.put(clazz, array);
        }
        array.add(object);
    }

    public GameObject get(Class clazz) {
        ArrayList<GameObject> array = recycleBin.get(clazz);
        if (array == null || array.isEmpty()) return null;
        return array.remove(0);
    }

    public Player getPlayer(){
        return player;
    }

    public boolean initResources() {
        if (initialized) {
            mediaPlayer.start();
            return false;
        }
        int w = GameView.self.getWidth();
        int h = GameView.self.getHeight();

        initLayers(Layer.ENEMY_COUNT.ordinal());


        mediaPlayer = MediaPlayer.create(MainActivity.self, R.raw.bgm);
        mediaPlayer.setLooping(true); // Set looping
        mediaPlayer.setVolume(50, 50);
        mediaPlayer.start();

//        Sound.play(R.raw.bgm, 1);

        player = new Player(w/2.0f, h - 300);
        //layers.get(Layer.player.ordinal()).add(player);
        add(Layer.player, player);
        enemyGenerator = new EnemyGenerator();
        add(Layer.controller, enemyGenerator);

        int marginX = (int) (5 * GameView.MULTIPLIER);
        int marginY = (int) (20 * GameView.MULTIPLIER);
        score = new Score(w - marginX, marginY);
        score.setScore(0);
        add(Layer.ui, score);

        leaderboard = new Leaderboard(w - marginX, marginY + GameView.MULTIPLIER*21);
        add(Layer.ui, leaderboard);

//        level = new Level(w - margin, margin + 100);
//        level.setLevel(1);
//        add(Layer.ui, level);

        playerHud = new PlayerHud(0, 0);
        add(Layer.ui, playerHud);

        VerticalScrollBackground bg = new VerticalScrollBackground(R.mipmap.bg_city, 10);
        add(Layer.bg1, bg);

        VerticalScrollBackground clouds = new VerticalScrollBackground(R.mipmap.clouds, 20);
        add(Layer.bg2, clouds);


        initialized = true;

        reset();
        leaderboard.updateLeaderboard();
        return true;
    }

    private void initLayers(int layerCount) {
        layers = new ArrayList<>();
        for (int i = 0; i < layerCount; i++) {
            layers.add(new ArrayList<>());
        }
    }

    public void update() {
        if (!isPlaying) return;
        for (ArrayList<GameObject> objects: layers) {
            for (GameObject o : objects) {
                o.update();
            }
        }
        if(player.isDead()){
            askRestart();
            return;
        }

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
                    MainGame game = MainGame.get();
                    game.add(MainGame.Layer.effect, HitEffect.get(R.mipmap.player_hit_effect, enemy.pos, 0.1f));
                }else{
                    player.onAttack(enemy);
                    player.addExp(enemy.getDamage()*0.1f+1);
                    enemy.destroy();
                    score.addScore((int)(enemy.getDamage()*10));
                }
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

    public void draw(Canvas canvas) {
        //if (!initialized) return;
        for (ArrayList<GameObject> objects: layers) {
            for (GameObject o : objects) {
                o.draw(canvas);
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
            Sound.play(R.raw.player_move_start, 0);
            player.startDrag(event.getX(), event.getY());
            return true;
        }
        if(action == MotionEvent.ACTION_MOVE){
            player.dragging(event.getX(), event.getY());
            return true;
        }
        if (action == MotionEvent.ACTION_UP){
            Sound.play(R.raw.player_move_end, 0);
            player.endDrag(event.getX(), event.getY());
            return true;
        }
        return false;
    }

    public void add(Layer layer, GameObject gameObject) {
        GameView.self.post(new Runnable() {
            @Override
            public void run() {
                ArrayList<GameObject> objects = layers.get(layer.ordinal());
                objects.add(gameObject);
            }
        });
//        Log.d(TAG, "<A> object count = " + objects.size());
    }

    public void remove(GameObject gameObject) {
        remove(gameObject, true);
    }
    public void remove(GameObject gameObject, boolean delayed) {
        Runnable runnable = () -> {
            for (ArrayList<GameObject> objects: layers) {
                boolean removed = objects.remove(gameObject);
                if (removed) {
                    if (gameObject instanceof Recyclable) {
                        ((Recyclable) gameObject).recycle();
                        recycle(gameObject);
                    }
                    //Log.d(TAG, "Removed: " + gameObject);
                    break;
                }
            }
        };
        if (delayed) {
            GameView.self.post(runnable);
        } else {
            runnable.run();
        }
    }

    void askItem(){
        Log.d(TAG, "askItem");
        isPlaying = false;
        AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.self);
        builder.setCancelable(false);
        builder.setTitle(R.string.select_item);
        int chapter = enemyGenerator.getChapter();
        float difficulty = EnemyGenerator.difficultyMultiplier(chapter/6.0f);
        ArrayList<Item> itemLists = new ArrayList<>(Arrays.asList(new statsItem(statsItem.Type.heal, difficulty*1+3),
                new statsItem(statsItem.Type.addHp, 1 * difficulty),
                new statsItem(statsItem.Type.addStamina, 1 * difficulty),
                new statsItem(statsItem.Type.addRadius, 10 * difficulty),
                new statsItem(statsItem.Type.addRadius, -5 * difficulty),
                new statsItem(statsItem.Type.addSpeed, 80 * difficulty),
                new statsItem(statsItem.Type.subSpeedAddStamina, 1.5f * difficulty)));
        itemLists.add(new LifeStealItem(0.1f));
        itemLists.add(new AttackRangeItem(0.2f));
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
        for (int i=0; i< 3; ++i) {
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
                    items.get(id) + " 선택했습니다.",
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
        input.setHint(prevName == null ? MainActivity.self.getString(R.string.type_name) : prevName);


        // Get from the SharedPreferences
        input.setText(prevName == null ? "P"+new Random().nextInt(50000) : prevName);

        builder.setView(input);
        builder.setTitle(MainActivity.self.getString(R.string.your_score_is)+score.getScore());
        builder.setMessage(MainActivity.self.getString(R.string.do_you_want_to_restart_game));
        builder.setPositiveButton(MainActivity.self.getString(R.string.yes), (dialog, which) -> {
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
        builder.setNegativeButton(MainActivity.self.getString(R.string.no), ((dialog, which) -> {
            Sound.play(R.raw.btn_click, 0);
        }));

        AlertDialog alert = builder.create();
        alert.show();
    }

    void reset(){
        player.reset();
        playerHud.reset();
        score.setScore(0);
        enemyGenerator.reset();
        isPlaying = true;
    }
}
