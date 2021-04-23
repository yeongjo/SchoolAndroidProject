package kr.ac.kpu.game.s2016180024.samplegame.game;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;

import java.util.HashMap;

import kr.ac.kpu.game.s2016180024.samplegame.framework.GameObject;
import kr.ac.kpu.game.s2016180024.samplegame.ui.view.GameView;

public class GameBitmap {
    private static HashMap<Integer, Bitmap> bitmaps = new HashMap<Integer, Bitmap>();

    protected static Bitmap load(int resId) {
        Bitmap bitmap = bitmaps.get(resId);
        if(bitmap == null) {
            Resources res = GameView.view.getResources();
            BitmapFactory.Options opts = new BitmapFactory.Options();
            opts.inScaled = false;
            bitmap = BitmapFactory.decodeResource(res, resId, opts);
            bitmaps.put(resId, bitmap);
        }
        return bitmap;
    }
}
