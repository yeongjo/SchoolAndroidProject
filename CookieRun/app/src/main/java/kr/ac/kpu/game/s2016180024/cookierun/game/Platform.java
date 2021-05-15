package kr.ac.kpu.game.s2016180024.cookierun.game;

import kr.ac.kpu.game.s2016180024.cookierun.R;
import kr.ac.kpu.game.s2016180024.cookierun.framework.object.ImageObject;
import kr.ac.kpu.game.s2016180024.cookierun.framework.view.GameView;

public class Platform extends ImageObject {
    public enum Type{
        T_10x2, T_2x2, T_3x1
    }

    static int[] platformResIds = new int[]{R.mipmap.cookierun_platform_480x48, R.mipmap.cookierun_platform_124x120, R.mipmap.cookierun_platform_480x48};

    public Platform(Type type, float x, float y) {
        super(platformResIds[type.ordinal()], x, y);
        final float UNIT = 40 * GameView.MULTIPLIER;
        float w = UNIT*10;
        float h = UNIT*2;
        dstRect.set(x, y, x+w, y+h);
    }
}
