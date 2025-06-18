package com.example.tapcounter;

import android.content.Context;
import android.content.SharedPreferences;
import android.media.SoundPool;
import android.os.Build;
import android.os.Bundle;
import android.os.VibrationEffect;
import android.os.Vibrator;
import android.view.View;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import java.util.Objects;

public class MainActivity extends AppCompatActivity {
    private long cnt = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Objects.requireNonNull(getSupportActionBar()).setTitle(getString(R.string.app_name));
        cnt = getPreviousCount();

        setContentView(R.layout.activity_main);
        ((TextView) findViewById(R.id.TV)).setText(String.valueOf(cnt));
    }

    private void playSound(int soundToPlay) {
        SoundPool soundPool = new SoundPool.Builder().setMaxStreams(1).build();
        int sound_id = soundPool.load(this, soundToPlay, 1);
        soundPool.setOnLoadCompleteListener(new SoundPool.OnLoadCompleteListener() {
            @Override
            public void onLoadComplete(SoundPool soundPool, int sampleId, int status) {
                if (status == 0) {
                    soundPool.play(
                            sound_id, 1.0f, 1.0f, 1, 0, 1.0f
                    );
                }
            }
        });
    }

    public void incrementCount(View v) {
        cnt++;
        ((TextView) findViewById(R.id.TV)).setText(String.valueOf(cnt));

        String PREFS_NAME = "tap_cnt";
        SharedPreferences sharedPreferences = getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putLong("taps", cnt);
        editor.apply();

        Vibrator vibrator = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            vibrator.vibrate(VibrationEffect.createOneShot(
                    1, VibrationEffect.DEFAULT_AMPLITUDE
            ));
        }
    }

    protected long getPreviousCount() {
        String PREFS_NAME = "tap_cnt";
        SharedPreferences sharedPreferences = getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
        return sharedPreferences.getLong("taps", 0);
    }

    public void resetCount(View v) {
        cnt = 0;
        ((TextView) findViewById(R.id.TV)).setText(String.valueOf(cnt));

        String PREFS_NAME = "tap_cnt";
        SharedPreferences sharedPreferences = getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putLong("taps", cnt);
        editor.apply();

        playSound(R.raw.reset_sound);
    }
}