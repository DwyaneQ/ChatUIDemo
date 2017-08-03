package com.example.administrator.chatdemo;

import android.media.MediaPlayer;
import android.util.Log;

import java.io.IOException;

/**
 * Created by lzw on 14/12/19.
 */
public class AudioHelper {
    private static AudioHelper audioHelper;
    private MediaPlayer mediaPlayer;
    private Runnable finishCallback;
    private String audioPath;
    private boolean onceStart = false;
    private boolean isPlaying = false;

    private AudioHelper() {

    }

    public static AudioHelper getInstance() {
        if (audioHelper == null) {
            audioHelper = new AudioHelper();
        }
        return audioHelper;
    }

    public String getAudioPath() {
        return audioPath;
    }

    public void stopPlayer() {
        if (mediaPlayer != null) {
            mediaPlayer.stop();
            mediaPlayer.release();
        }
        isPlaying = false;
        mediaPlayer = null;
    }

    public void pausePlayer() {
        if (mediaPlayer != null) {
            mediaPlayer.pause();
            isPlaying = false;
        }
    }

    public boolean isPlaying() {
        return isPlaying;
    }

    public void restartPlayer() {
        if (mediaPlayer != null && !mediaPlayer.isPlaying()) {
            mediaPlayer.start();
        }
    }

    public synchronized void playAudio(String path, Runnable finishCallback) {
        if (mediaPlayer != null && onceStart) {
            mediaPlayer.reset();
        } else {
            mediaPlayer = new MediaPlayer();
        }
//        tryRunFinishCallback();
        audioPath = path;
        AudioHelper.this.finishCallback = finishCallback;
        try {
            mediaPlayer.setDataSource(path);
            mediaPlayer.prepare();
            mediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                @Override
                public void onCompletion(MediaPlayer mp) {
                    tryRunFinishCallback();
                }
            });
            isPlaying = true;
            mediaPlayer.start();
            onceStart = true;
        } catch (IOException e) {
            Log.e("AudioHelper", e.toString());
        }
    }

    public void tryRunFinishCallback() {
        if (finishCallback != null) {
            finishCallback.run();
            finishCallback = null;
        }
    }
}
