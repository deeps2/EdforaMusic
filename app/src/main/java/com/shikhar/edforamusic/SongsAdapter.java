package com.shikhar.edforamusic;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;

import com.shikhar.edforamusic.Model.Music;

/**
 * Created by shikhar on 19-03-2017.
 */

public class SongsAdapter extends ArrayAdapter<Music> {

    Context context;

    public SongsAdapter(Context){
        this.context = context;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {


        return view;
    }
}
