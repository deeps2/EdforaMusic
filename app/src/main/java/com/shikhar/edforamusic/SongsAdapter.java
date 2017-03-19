package com.shikhar.edforamusic;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.AudioManager;
import android.media.Image;
import android.media.MediaPlayer;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.shikhar.edforamusic.Model.Music;

import java.io.IOException;

import static android.R.attr.start;
import static android.content.Context.MODE_PRIVATE;

public class SongsAdapter extends ArrayAdapter<Music> {

    private Context mContext;
    private ImageView nowPlaying;
    private int pos = -1;
    private Toast bufferingToast;
    private Toast nowPlayingToast;
    private String playingSongUrl;
    private MediaPlayer mediaPlayer;
    private ImageView lastPlaying;

    public SongsAdapter(Context context) {
        super(context, -1);

        this.mContext = context;
    }

    public int getButtonState(){

        if(nowPlaying != null)
            return (int)nowPlaying.getTag();
        else
            return -1;
    }

    public int getPos(){
        return pos;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {

        Music song = getItem(position);

        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.list_songs, parent, false);
        }

        ImageView songCover = (ImageView) convertView.findViewById(R.id.cover_image);
        TextView songName = (TextView) convertView.findViewById(R.id.song_name);
        TextView artists = (TextView) convertView.findViewById(R.id.artists);
        final ImageView playPause = (ImageView) convertView.findViewById(R.id.play_pause);

        Glide.with(mContext).load(song.getCoverImage()).error(R.mipmap.ic_launcher).into(songCover);

        if (song.getSong() != null)
            songName.setText(song.getSong());
        if (song.getArtists() != null)
            artists.setText(song.getArtists());

        final SharedPreferences prefs = mContext.getSharedPreferences("BUTTON_STATE", MODE_PRIVATE);
        int restoredDrawableId = prefs.getInt("DRAWABLE_ID", -1);
        int restoredPosition = prefs.getInt("POSITION", -1);
        if (restoredPosition != -1 && restoredPosition == position) {
            playPause.setImageResource(restoredDrawableId);
            playPause.setTag(restoredDrawableId);
            lastPlaying = playPause;
           // prefs.edit().clear();
           // prefs.edit().commit();

        }else {
            playPause.setImageResource(R.drawable.play_button);
            playPause.setTag(R.drawable.play_button);
        }

        if (nowPlaying != null && position == pos) {
            playPause.setImageResource(R.drawable.pause_button);
            playPause.setTag(R.drawable.pause_button);
        }

        playPause.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if ((int) view.getTag() == R.drawable.play_button) {
                    playPause.setImageResource(R.drawable.pause_button);
                    playPause.setTag(R.drawable.pause_button);

                    if(lastPlaying != null){
                        lastPlaying.setImageResource(R.drawable.play_button);
                        lastPlaying.setTag(R.drawable.play_button);
                        prefs.edit().clear();
                        prefs.edit().commit();
                        lastPlaying = null;
                    }

                    pos = position;

                    if (nowPlaying != null) {
                        nowPlaying.setImageResource(R.drawable.play_button);
                        nowPlaying.setTag(R.drawable.play_button);
                    }
                    nowPlaying = playPause;

                   // pauseSong(playingSongUrl);
                    Intent intent0 = new Intent(mContext,SongIntentService.class);
                    intent0.putExtra("PLAYING_SONG_URL",playingSongUrl);
                    intent0.putExtra("MODE","pauseSong");
                    mContext.startService(intent0);

                    playingSongUrl = getItem(position).getUrl();

                    //playSong(playingSongUrl, getItem(position).getSong());
                    Intent intent = new Intent(mContext,SongIntentService.class);
                    intent.putExtra("PLAYING_SONG_URL",playingSongUrl);//start
                    intent.putExtra("SONG",getItem(position).getSong());
                    intent.putExtra("MODE","playSong");
                    mContext.startService(intent);

                } else {
                    playPause.setImageResource(R.drawable.play_button);
                    playPause.setTag(R.drawable.play_button);
                    pos = position;
                    nowPlaying = null;
                    //pauseSong(playingSongUrl);
                   Intent intent = new Intent(mContext,SongIntentService.class);
                    intent.putExtra("PLAYING_SONG_URL",playingSongUrl);
                   intent.putExtra("MODE","pauseSong");
                   mContext.startService(intent);
                }
            }
        });



        return convertView;
    }

    public void playSong(String url, final String songName) {

        try {
            if (nowPlayingToast != null)
                nowPlayingToast.cancel();

            mediaPlayer = new MediaPlayer();
            mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
            mediaPlayer.setDataSource(url);
            mediaPlayer.prepareAsync(); // might take long! (for buffering, etc)
            bufferingToast = Toast.makeText(mContext, "Buffering Song: " + songName, Toast.LENGTH_SHORT);
            bufferingToast.show();

        } catch (IOException e) {
            Log.e("Adapter", e.getMessage());
        }

        mediaPlayer.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
            @Override
            public void onPrepared(MediaPlayer mediaPlayer) {
                //mediaPlayer.seekTo(0);
                //Intent intent = new Intent(mContext, SongIntentService.class);
                //intent.putExtra("mediaplayer",mediaPlayer);
                //mContext.startService(intent)
                //OLD ONE NOT NEEDED

                mediaPlayer.start();
                bufferingToast.cancel();
                nowPlayingToast = Toast.makeText(mContext, "Now Playing: " + songName, Toast.LENGTH_SHORT);
                nowPlayingToast.show();
            }
        });
    }

    public void pauseSong(String url){
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
