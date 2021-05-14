package kr.ac.kpu.game.s2016180024.cookierun.framework;

import android.graphics.Canvas;
import android.util.Log;
import android.view.MotionEvent;

import java.util.ArrayList;
import java.util.HashMap;

import kr.ac.kpu.game.s2016180024.cookierun.framework.iface.GameObject;
import kr.ac.kpu.game.s2016180024.cookierun.framework.iface.Recyclable;
import kr.ac.kpu.game.s2016180024.cookierun.framework.view.GameView;
import kr.ac.kpu.game.s2016180024.cookierun.game.Player;

public class BaseGame {
    public float frameTime;
    private ArrayList<ArrayList<GameObject>> layers;

    private static HashMap<Class, ArrayList<GameObject>> recycleBin = new HashMap<>();
    private static final String TAG = BaseGame.class.getSimpleName();
    // singleton
    private static BaseGame instance;

    public static BaseGame get() {
        if (instance == null) {
            instance = new BaseGame();
        }
        return instance;
    }

    protected BaseGame(){
        instance = this;
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

    public boolean initResources() {
        Log.d(TAG, "initResources: need to implements this in extends class");
        return false;
    }

    protected void initLayers(int layerCount) {
        layers = new ArrayList<>();
        for (int i = 0; i < layerCount; i++) {
            layers.add(new ArrayList<>());
        }
    }

    public void update() {
        for (ArrayList<GameObject> objects: layers) {
            for (GameObject o : objects) {
                o.update();
            }
        }
    }

    public void draw(Canvas canvas) {
        for (ArrayList<GameObject> objects: layers) {
            for (GameObject o : objects) {
                o.draw(canvas);
            }
        }
    }

    public boolean onTouchEvent(MotionEvent event) {
        return false;
    }

    public void add(int layerIndex, GameObject gameObject) {
        GameView.view.post(() -> {
            ArrayList<GameObject> objects = layers.get(layerIndex);
            objects.add(gameObject);
        });
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
            GameView.view.post(runnable);
        } else {
            runnable.run();
        }
    }
}
