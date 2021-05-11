package kr.ac.kpu.game.s2016180024.Dodge.game;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.graphics.Canvas;
import android.util.Log;
import android.view.MotionEvent;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;

import kr.ac.kpu.game.s2016180024.Dodge.R;
import kr.ac.kpu.game.s2016180024.Dodge.framework.GameObject;
import kr.ac.kpu.game.s2016180024.Dodge.framework.Recyclable;
import kr.ac.kpu.game.s2016180024.Dodge.game.item.AttackRangeItem;
import kr.ac.kpu.game.s2016180024.Dodge.game.item.Item;
import kr.ac.kpu.game.s2016180024.Dodge.game.item.LifeStealItem;
import kr.ac.kpu.game.s2016180024.Dodge.game.item.statsItem;
import kr.ac.kpu.game.s2016180024.Dodge.ui.activity.MainActivity;
import kr.ac.kpu.game.s2016180024.Dodge.ui.view.GameView;
import kr.ac.kpu.game.s2016180024.Dodge.utils.CollisionHelper;

public class MainGame {
    private static final String TAG = MainGame.class.getSimpleName();
    // singleton
    public float frameTime;
    private static MainGame instance;
    private static HashMap<Class, ArrayList<GameObject>> recycleBin = new HashMap<>();
    ArrayList<ArrayList<GameObject>> layers;
    private Player player;
    private Score score;
    private Level level;
    private PlayerHud playerHud;
    private boolean initialized;
    private boolean isPlaying = true;
    private EnemyGenerator enemyEngerator;
    private boolean isAskingItem;

    public enum Layer {
        bg1, bg2, enemy, bullet, player, ui, controller, ENEMY_COUNT
    }

    public static MainGame get() {
        if (instance == null) {
            instance = new MainGame();
        }
        return instance;
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
            return false;
        }
        int w = GameView.view.getWidth();
        int h = GameView.view.getHeight();

        initLayers(Layer.ENEMY_COUNT.ordinal());

        player = new Player(w/2, h - 300);
        //layers.get(Layer.player.ordinal()).add(player);
        add(Layer.player, player);
        enemyEngerator = new EnemyGenerator();
        add(Layer.controller, enemyEngerator);

        int margin = (int) (20 * GameView.MULTIPLIER);
        score = new Score(w - margin, margin);
        score.setScore(0);
        add(Layer.ui, score);

        level = new Level(w - margin, margin + 100);
        level.setLevel(1);
        add(Layer.ui, level);

        playerHud = new PlayerHud(margin, margin);
        add(Layer.ui, playerHud);

        VerticalScrollBackground bg = new VerticalScrollBackground(R.mipmap.bg_city, 10);
        add(Layer.bg1, bg);

        VerticalScrollBackground clouds = new VerticalScrollBackground(R.mipmap.clouds, 20);
        add(Layer.bg2, clouds);

        initialized = true;
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

        ArrayList<GameObject> enemies = layers.get(Layer.enemy.ordinal());
        ArrayList<GameObject> bullets = layers.get(Layer.bullet.ordinal());
        for (GameObject o1: enemies) {
            Enemy enemy = (Enemy) o1;
            boolean collided = false;
//            for (GameObject o2: bullets) {
//                Bullet bullet = (Bullet) o2;
//                if (CollisionHelper.collides(enemy, bullet)) {
//                    remove(bullet, false);
//                    remove(enemy, false);
//                    score.addScore(10);
//                    collided = true;
//                    break;
//                }
//            }
            if (collided) {
                break;
            }
            if (CollisionHelper.collides(enemy, player)) {
                if(!player.isMoving()) {
                    player.takeDamage(enemy.getDamage());
                    player.showAttackEffect();
                }else{
                    player.onAttack(enemy);
                    score.addScore((int)(enemy.getDamage()));
                }
                remove(enemy, false);
                break;
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

    public void add(Layer layer, GameObject gameObject) {
        GameView.view.post(new Runnable() {
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
        Runnable runnable = new Runnable() {
            @Override
            public void run() {
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
            }
        };
        if (delayed) {
            GameView.view.post(runnable);
        } else {
            runnable.run();
        }
    }

    void askItem(){
        Log.d(TAG, "askItem");
        isPlaying = false;
        isAskingItem = true;
        AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.self);
        builder.setCancelable(false);
        builder.setTitle("select item");
        int chapter = enemyEngerator.getChapter();
        ArrayList<Item> items = new ArrayList<>(Arrays.asList(new statsItem(statsItem.Type.heal, 0),
                new statsItem(statsItem.Type.addHp, 2 * chapter),
                new statsItem(statsItem.Type.addStamina, 1 * chapter),
                new statsItem(statsItem.Type.addRadius, 10 * chapter),
                new statsItem(statsItem.Type.addRadius, -10 * chapter),
                new statsItem(statsItem.Type.addSpeed, 70 * chapter),
                new statsItem(statsItem.Type.subSpeedAddStamina, 2 * chapter)));
        items.add(new LifeStealItem(0.1f));
        items.add(new AttackRangeItem(0.1f));
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
            isAskingItem = false;
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
        builder.setTitle("Your score is "+score.getScore());
        builder.setMessage("Do you want restart game?");
        builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                ArrayList<GameObject> enemies = layers.get(Layer.enemy.ordinal());
                ArrayList<GameObject> bullets = layers.get(Layer.bullet.ordinal());
                for(GameObject object : enemies) {
                    remove(object);
                }
                for(GameObject object : bullets) {
                    remove(object);
                }
                player.reset();
                level.setLevel(1);
                score.setScore(0);
                enemyEngerator.reset();
                isPlaying = true;
            }
        });

        builder.setNegativeButton("No", null);
        AlertDialog alert = builder.create();
        alert.show();
    }
}
