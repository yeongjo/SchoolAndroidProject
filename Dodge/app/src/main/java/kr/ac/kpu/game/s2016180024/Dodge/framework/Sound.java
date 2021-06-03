package kr.ac.kpu.game.s2016180024.Dodge.framework;

import android.content.Context;
import android.media.AudioAttributes;
import android.media.AudioManager;
import android.media.SoundPool;
import android.util.Log;

import java.util.HashMap;

import kr.ac.kpu.game.s2016180024.Dodge.R;

public class Sound {
    private static final String TAG = Sound.class.getSimpleName();
    private static SoundPool soundPool;
    private static final int[] SOUND_IDS = {
            R.raw.bgm, R.raw.btn_click, R.raw.get_exp_item, R.raw.item_selected
    };
    private static HashMap<Integer, Integer> soundIdMap = new HashMap<>();

    public static void init(Context context) {
        AudioAttributes audioAttributes;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP) {
            audioAttributes = new AudioAttributes.Builder()
                    .setUsage(AudioAttributes.USAGE_GAME)
                    .setContentType(AudioAttributes.CONTENT_TYPE_MUSIC)
                    .build();
            Sound.soundPool = new SoundPool.Builder()
                    .setAudioAttributes(audioAttributes)
                    .setMaxStreams(3)
                    .build();
        } else {
            Sound.soundPool = new SoundPool(3, AudioManager.STREAM_MUSIC, 0);
        }

        for (int resId: SOUND_IDS) {
            int soundId = soundPool.load(context, resId, 1);
            soundIdMap.put(resId, soundId);
        }
    }
    public static int play(int resId) {
        return play(resId, 0);
    }
    public static int play(int resId, int isLoop) {
        Log.d(TAG, "play: " + resId);
        int soundId = soundIdMap.get(resId);
        int streamId = soundPool.play(soundId, 1f, 1f, 1, isLoop, 1f);
        return streamId;
    }
    public static void stop(int resId) {
        Log.d(TAG, "stop: " + resId);
        int soundId = soundIdMap.get(resId);
        soundPool.stop(soundId);
    }
}
