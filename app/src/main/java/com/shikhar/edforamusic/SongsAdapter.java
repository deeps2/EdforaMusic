package com.shikhar.edforamusic;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.shikhar.edforamusic.Model.Music;

import static android.os.Build.VERSION_CODES.M;

/**
 * Created by shikhar on 19-03-2017.
 */

public class SongsAdapter extends ArrayAdapter<Music> {

    Context mContext;
    ImageView nowPlaying;


    public SongsAdapter(Context context){
        super(context, -1);

        this.mContext = context;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        Music song = getItem(position);

        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.list_songs, parent, false);
        }

        ImageView songCover = (ImageView)convertView.findViewById(R.id.cover_image);
        TextView  songName = (TextView)convertView.findViewById(R.id.song_name);
        TextView artists = (TextView)convertView.findViewById(R.id.artists);
        final ImageView playPause = (ImageView)convertView.findViewById(R.id.play_pause);

        Glide.with(mContext).load(song.getCoverImage()).into(songCover);

        //check for nulls
        songName.setText(song.getSong());
        artists.setText(song.getArtists());
        playPause.setImageResource(R.drawable.play_button);
        playPause.setTag(R.drawable.play_button);


        playPause.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if((int)view.getTag() == R.drawable.play_button){
                    playPause.setImageResource(R.drawable.pause_button);
                    playPause.setTag(R.drawable.pause_button);

                    if(nowPlaying != null) {
                        nowPlaying.setImageResource(R.drawable.play_button);
                        nowPlaying.setTag(R.drawable.play_button);
                    }
                    nowPlaying = playPause;




                   // if(lastClickedSong != null && lastClickedSong != playPause)
                   //     lastClickedSong.setImageResource(R.drawable.play_button);
                   // lastClickedSong = playPause;

                }else{
                    playPause.setImageResource(R.drawable.play_button);
                    playPause.setTag(R.drawable.play_button);
                    nowPlaying = null;


                  //  if(lastClickedSong != null && lastClickedSong != playPause)
                 //       lastClickedSong.setImageResource(R.drawable.pause_button);
                  //  lastClickedSong = playPause;
                }

            }
        });
        return convertView;
    }
}
