package kr.ac.kpu.game.s2016180024.Dodge.utils;

import android.graphics.RectF;
import android.util.Log;

import kr.ac.kpu.game.s2016180024.Dodge.framework.BoxCollidable;
import kr.ac.kpu.game.s2016180024.Dodge.framework.CircleCollidable;
import kr.ac.kpu.game.s2016180024.Dodge.framework.CircleCollider;

public class CollisionHelper {
    private static final String TAG = CollisionHelper.class.getSimpleName();
    private static RectF rect1 = new RectF();
    private static RectF rect2 = new RectF();
    public static boolean collides(BoxCollidable o1, BoxCollidable o2) {
        o1.getBoundingRect(rect1);
        o2.getBoundingRect(rect2);

        if (rect1.left > rect2.right) return false;
        if (rect1.top > rect2.bottom) return false;
        if (rect1.right < rect2.left) return false;
        if (rect1.bottom < rect2.top) return false;

        Log.d(TAG, "1:" + rect1 + " 2:" + rect2);
        return true;
    }

    public static boolean collides(CircleCollidable o1, CircleCollidable o2) {
        CircleCollider c1 = o1.getCollider();
        CircleCollider c2 = o2.getCollider();
        if(c1.pos.dist(c2.pos) > c1.radius + c2.radius){
            return false;
        }

        Log.d(TAG, "1:" + c1 + " 2:" + c2);
        return true;
    }
}
