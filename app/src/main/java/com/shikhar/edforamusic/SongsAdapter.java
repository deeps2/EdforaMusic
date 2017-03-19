package com.shikhar.edforamusic;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.shikhar.edforamusic.Model.Music;

import static android.content.Context.MODE_PRIVATE;

public class SongsAdapter extends ArrayAdapter<Music> {

    private Context mContext;
    private ImageView nowPlaying;  //ImageView corresponding to current playing song
    private int pos = -1;        //position of current playing song in the adapter
    private String playingSongUrl;
    private ImageView lastPlaying;  //ImageView corresponding to last Playing song

    public SongsAdapter(Context context) {
        super(context, -1);

        this.mContext = context;
    }

    public int getButtonState(){    //to save the button tag corresponding to currently playing song into SharedPreference

        if(nowPlaying != null)
            return (int)nowPlaying.getTag();
        else//initially no song will be playing
            return -1;
    }

    public int getPos(){ //get position of the current playing song..This will also go into SharedPreference
        return pos;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {

        Music song = getItem(position);

        //if it's a new view, then inflate it
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.list_songs, parent, false);
        }

        //find View inside layout.list_songs
        ImageView songCover = (ImageView) convertView.findViewById(R.id.cover_image);
        TextView songName = (TextView) convertView.findViewById(R.id.song_name);
        TextView artists = (TextView) convertView.findViewById(R.id.artists);
        final ImageView playPause = (ImageView) convertView.findViewById(R.id.play_pause);

        //load the thumbanail image using Glide
        Glide.with(mContext).load(song.getCoverImage()).error(R.mipmap.ic_launcher).into(songCover);

        //set name of song and artists name if they are not null
        if (song.getSong() != null)
            songName.setText(song.getSong());
        if (song.getArtists() != null)
            artists.setText(song.getArtists());

        //read SharedPreferences
        final SharedPreferences prefs = mContext.getSharedPreferences("BUTTON_STATE", MODE_PRIVATE);
        int restoredDrawableId = prefs.getInt("DRAWABLE_ID", -1);
        int restoredPosition = prefs.getInt("POSITION", -1);

        //if restored Position is a valid value(not -1) and this position in Adapter == restoredPosition(i.e. position of last playing song)
        //then restore the state of play/pause button image
        if (restoredPosition != -1 && restoredPosition == position) {
            playPause.setImageResource(restoredDrawableId);
            playPause.setTag(restoredDrawableId);
            lastPlaying = playPause;
        }else {
            //for all other adapter position, the image will be of play button as those songs are not being played
            //NOTE- play button image means song is not being played
            //NOTE- pause button image means that song is being played
            playPause.setImageResource(R.drawable.play_button);
            playPause.setTag(R.drawable.play_button);
        }

        //if this adapter position == position of current playing song in the adapter(pos) and there is a song being played(nowPlaying != null)
       //then change the button image to pause button
        if (nowPlaying != null && position == pos) {
            playPause.setImageResource(R.drawable.pause_button);
            playPause.setTag(R.drawable.pause_button);
        }

        //attach a click listener to play/pause button
        playPause.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                //if the image that is clicked is a play image, then change it to pause
                if ((int) view.getTag() == R.drawable.play_button) {
                    playPause.setImageResource(R.drawable.pause_button);
                    playPause.setTag(R.drawable.pause_button);

                    //if there is a song being played already "when the app was reopened"
                    //then put play image at that adapter position(play image at that adapter position means that now that old song will be stopped)
                    if(lastPlaying != null){
                        lastPlaying.setImageResource(R.drawable.play_button);
                        lastPlaying.setTag(R.drawable.play_button);
                        prefs.edit().clear();
                        prefs.edit().commit();
                        lastPlaying = null;
                    }

                    pos = position;

                    //if there is a song being played
                    //then put a play button image corresponding to that song
                    if (nowPlaying != null) {
                        nowPlaying.setImageResource(R.drawable.play_button);
                        nowPlaying.setTag(R.drawable.play_button);
                    }
                    nowPlaying = playPause;//nowPlaying now holds the ImageView of song which was clicked for play

                   //put the url of song which is now playing(old one) and pause it(if it exists) by calling the service
                    //MODE will tell why service was called. for playing/stopping a song
                    Intent intent0 = new Intent(mContext,SongIntentService.class);
                    intent0.putExtra("PLAYING_SONG_URL",playingSongUrl);
                    intent0.putExtra("MODE","pauseSong");
                    mContext.startService(intent0);

                    //put the url of the song corresponding to the song which is clicked just now
                    playingSongUrl = getItem(position).getUrl();

                    //put that url,song name inside intent and start playing that by launching a service
                    Intent intent = new Intent(mContext,SongIntentService.class);
                    intent.putExtra("PLAYING_SONG_URL",playingSongUrl);
                    intent.putExtra("SONG",getItem(position).getSong());
                    intent.putExtra("MODE","playSong");
                    mContext.startService(intent);

                } else {
                    //if the button that is clicked is a pause button
                    //then put play button image and pause the song
                    playPause.setImageResource(R.drawable.play_button);
                    playPause.setTag(R.drawable.play_button);
                    pos = position;
                    nowPlaying = null; //as song is paused therefore there is no current playing song

                    //put the url of the song which is to be paused and start the service
                    Intent intent = new Intent(mContext,SongIntentService.class);
                    intent.putExtra("PLAYING_SONG_URL",playingSongUrl);
                    intent.putExtra("MODE","pauseSong");
                    mContext.startService(intent);
                }
            }
        });

        return convertView;
    }
}
