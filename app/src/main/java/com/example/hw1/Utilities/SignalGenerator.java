package com.example.hw1.Utilities;

import android.content.Context;
import android.media.MediaPlayer;
import android.os.Build;
import android.os.VibrationEffect;
import android.os.Vibrator;
import android.widget.Toast;

import com.example.hw1.R;

public class SignalGenerator {
    private static SignalGenerator instance;
    private Context context;
    private static Vibrator vibrator;
    private static MediaPlayer mediaPlayer;

    private SignalGenerator(Context context) {
        this.context = context;
    }

    public static void init(Context context) {
        if (instance == null) {
            instance = new SignalGenerator(context);
            vibrator = (Vibrator) context.getSystemService(Context.VIBRATOR_SERVICE);
            mediaPlayer = MediaPlayer.create(context, R.raw.crash_sound);
        }
    }

    public static SignalGenerator getInstance() {
        return instance;
    }

    public void toast(String text, int length) {
        Toast.makeText(context, text, length).show();
    }

    public void playSound(int soundId) {
        mediaPlayer.release();
        mediaPlayer = MediaPlayer.create(context, soundId);
        mediaPlayer.start();
    }

    public void vibrate(long ms) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            vibrator.vibrate(VibrationEffect.createOneShot(ms, VibrationEffect.DEFAULT_AMPLITUDE));
        } else {
            //deprecated in API 26
            vibrator.vibrate(ms);
        }
    }
}
