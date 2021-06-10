package kr.ac.kpu.game.s2016180024.Dodge.framework;

import android.graphics.Canvas;
import android.view.MotionEvent;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Stack;

import kr.ac.kpu.game.s2016180024.Dodge.game.MainGame;
import kr.ac.kpu.game.s2016180024.Dodge.ui.activity.MainActivity;
import kr.ac.kpu.game.s2016180024.Dodge.ui.view.GameView;

public class Scene {
    public enum Layer {
        bg1, bg2, enemy, bullet, item, player, effect, ui, controller, LAYER_COUNT
    }
    public ArrayList<ArrayList<GameObject>> layers = new ArrayList<>();
    private boolean initialized;
    private static HashMap<Class, ArrayList<GameObject>> recycleBin = new HashMap<>();
    private static Stack<Scene> scenes = new Stack<>();

    public static void addScene(Scene scene){
        if(!scenes.isEmpty()) {
            scene.onEnter();
            scene.onResume();
            Scene prevScene = scenes.peek();
            prevScene.onExit();
            prevScene.onPause();
        }
        scenes.push(scene);
    }

    public static Scene popScene(){
        if(scenes.size() <= 1) {
            return null;
        }
        Scene prevScene = scenes.pop();
        prevScene.onExit();
        prevScene.onPause();
        return prevScene;
    }

    public static Scene getActiveScene(){
        if(scenes.isEmpty()) {
            return null;
        }
        return scenes.peek();
    }

    public void onEnter(){
        initResources();
    }

    public void onExit(){

    }

    public void onResume(){

    }

    public void onPause(){

    }

    public ArrayList<GameObject> getLayerObjects(Layer layer){
        return layers.get(layer.ordinal());
    }

    public boolean initResources() {
        if (initialized) {
            return false;
        }
        initialized = true;
        initLayers(Layer.LAYER_COUNT.ordinal());
        return true;
    }

    private void initLayers(int layerCount) {
        layers.clear();
        for (int i = 0; i < layerCount; i++) {
            layers.add(new ArrayList<>());
        }
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

    public void update(){
        for (ArrayList<GameObject> objects: layers) {
            for (GameObject o : objects) {
                o.update();
            }
        }
    }

    public void draw(Canvas canvas){
        for (ArrayList<GameObject> objects: layers) {
            for (GameObject o : objects) {
                o.draw(canvas);
            }
        }
    }

    public boolean onTouchEvent(MotionEvent event) {
        return true;
    }

    public void add(Layer layer, GameObject gameObject) {
        GameView.self.post(() -> {
            ArrayList<GameObject> objects = layers.get(layer.ordinal());
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

    protected String getString(int id){
        return MainActivity.self.getString(id);
    }
}
