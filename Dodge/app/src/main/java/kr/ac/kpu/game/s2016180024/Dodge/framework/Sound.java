package kr.ac.kpu.game.s2016180024.Dodge.framework;

import android.content.Context;
import android.media.AudioAttributes;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.SoundPool;
import android.util.Log;

import java.util.HashMap;

import kr.ac.kpu.game.s2016180024.Dodge.R;
import kr.ac.kpu.game.s2016180024.Dodge.ui.activity.MainActivity;

public class Sound {
    private static final String TAG = Sound.class.getSimpleName();
    private static SoundPool soundPool;
    private static final int[] SOUND_IDS = {
            R.raw.bgm, R.raw.btn_click,R.raw.enemy_hit, R.raw.get_exp_item, R.raw.item_selected,
            R.raw.laser_shot,R.raw.player_die,R.raw.player_hit,R.raw.player_move_end,R.raw.player_move_start,
            R.raw.parent_enemy_pop, R.raw.parent_enemy_charge, R.raw.attack_bgm
    };
    private static final int[] MEDIA_IDS = {
            R.raw.bgm, R.raw.attack_bgm
    };
    private static HashMap<Integer, Integer> soundIdMap = new HashMap<>();
    private static HashMap<Integer, MediaPlayer> mediaPlayerIdMap = new HashMap<>();

    private static int maxStreams = 10;

    public static void init(Context context) {
        AudioAttributes audioAttributes;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP) {
            audioAttributes = new AudioAttributes.Builder()
                    .setUsage(AudioAttributes.USAGE_GAME)
                    .setContentType(AudioAttributes.CONTENT_TYPE_MUSIC)
                    .build();
            Sound.soundPool = new SoundPool.Builder()
                    .setAudioAttributes(audioAttributes)
                    .setMaxStreams(maxStreams)
                    .build();
        } else {
            Sound.soundPool = new SoundPool(maxStreams, AudioManager.STREAM_MUSIC, 0);
        }

        for (int resId: SOUND_IDS) {
            int soundId = soundPool.load(context, resId, 1);
            soundIdMap.put(resId, soundId);
        }
        for(int resId:MEDIA_IDS){
            mediaPlayerIdMap.put(resId, MediaPlayer.create(MainActivity.self, resId));
        }
    }
    public static MediaPlayer getMediaPlayer(int resId){
        return mediaPlayerIdMap.get( resId);
    }

    public static int play(int resId) {
        return play(resId, 0);
    }
    public static int play(int resId, int isLoop) {
        return play(resId, isLoop, 1);
    }
    public static int play(int resId, int isLoop, float volume) {
        Log.d(TAG, "play: " + resId);
        int soundId = soundIdMap.get(resId);
        int streamId = soundPool.play(soundId, volume, volume, 1, isLoop, 1f);
        return streamId;
    }
    public static void stop(int streamId) {
        Log.d(TAG, "stop: " + streamId);
        soundPool.stop(streamId);
    }
}
