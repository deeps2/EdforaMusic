package com.shikhar.edforamusic;

import android.app.Service;
import android.content.Intent;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.Handler;
import android.os.IBinder;
import android.util.Log;
import android.widget.Toast;

import java.io.IOException;

public class SongIntentService extends Service {

    private MediaPlayer mediaPlayer;
    private Toast bufferingToast;
    private Toast nowPlayingToast;
    private String playingSongUrl;

    public SongIntentService() {
    //empty constructor
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        if (intent != null) {

            String url = intent.getStringExtra("PLAYING_SONG_URL");
            String song = intent.getStringExtra("SONG");
            String mode = intent.getStringExtra("MODE");

            if (mode.equals("playSong")) {
                playingSongUrl = url;
                playSong(url, song);// play the song
            } else {
                pauseSong(playingSongUrl);// pause or stop the song
            }
        }

        return START_STICKY;
    }

    void showBufferingToastText(final String songName){

        Handler handler = new Handler(getMainLooper());
        handler.post(new Runnable() {
            @Override
            public void run() {
                bufferingToast = Toast.makeText(getApplicationContext(), "Buffering Song: " + songName, Toast.LENGTH_LONG);
                bufferingToast.show();
            }
        });

    }

    void nowPlayingToastText(final String songName){

       Handler handler = new Handler(getMainLooper());
        handler.post(new Runnable() {
            @Override
            public void run() {
                nowPlayingToast = Toast.makeText(getApplicationContext(), "Now Playing: " + songName, Toast.LENGTH_LONG);
                nowPlayingToast.show();
            }
        });

    }
    
    public void playSong(String url, final String songName) {

        try {
            if (nowPlayingToast != null) {

                Handler handler = new Handler(getMainLooper());
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        nowPlayingToast.cancel();
                    }
                });

            }

            //create media player instance and start it
            mediaPlayer = new MediaPlayer();
            mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
            mediaPlayer.setDataSource(url);
            mediaPlayer.prepareAsync(); // might take long! (for buffering, etc) so start it on a separate thread
            showBufferingToastText(songName);

        } catch (IOException e) {
            Log.e("Adapter", e.getMessage());
        }

        //when prepareAsync() got completed
        mediaPlayer.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
            @Override //if mediaplayer is prepared, then start it and display the toast
            public void onPrepared(MediaPlayer mediaPlayer) {

                mediaPlayer.start();

                Handler handler = new Handler(getMainLooper());
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        bufferingToast.cancel();
                    }
                });

                nowPlayingToastText(songName);
            }
        });
    }

    void pauseSong(String url){

        if(url == null)
            return;
        else{
            mediaPlayer.stop();
            mediaPlayer.release();
            mediaPlayer = null;
            playingSongUrl = null;
        }

    }
}
