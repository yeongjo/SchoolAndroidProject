package kr.ac.kpu.game.s2016180024.samplegame.framework;

import android.media.MediaPlayer;
import android.util.Log;

import kr.ac.kpu.game.s2016180024.samplegame.ui.view.GameView;

public class Sound {
    private static final String TAG = Sound.class.getSimpleName();
    private static MediaPlayer mediaPlayer;

    public static void play(int resId){
        Log.d(TAG, "play: "+resId);
        mediaPlayer = MediaPlayer.create(GameView.view.getContext(), resId);
    }

    public static void init() {
    }
}
