package com.shikhar.edforamusic;

import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.shikhar.edforamusic.Model.Music;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.GET;

public class MainActivity extends AppCompatActivity {

    public static final String BASE_URL = "http://starlord.hackerearth.com/";

    ListView mSongListView;       //ListView to display list of available songs that you can stream
    SongsAdapter mSongsAdapter;   //adapter which will be attached to ListView in order to populate it

    List<Music> mSongsList = new ArrayList<>(); //List of Songs with their information(song name, song url, thumbnail etc.)

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mSongListView = (ListView) findViewById(R.id.list_view);
        mSongsAdapter = new SongsAdapter(this);
        mSongListView.setAdapter(mSongsAdapter);

        //TODO: open new activity when any of the list items is clicked
        mSongListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                //open new activity and start service
                //didn't get enough time to implement this.
                //I just have to start an activity where I should display seekbar with buttons and chosen song
            }
        });

        callWebService();
    }

    //Retrofit Interface
    public interface MusicApiEndpointInterface {
        @GET("edfora/cokestudio")
        Call<List<Music>> getSongsList();
    }

    void callWebService() {

        //create Retrofit Instance
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        MusicApiEndpointInterface serviceRequest = retrofit.create(MusicApiEndpointInterface.class);

        Call<List<Music>> call = serviceRequest.getSongsList();

        call.enqueue(new Callback<List<Music>>() {
            @Override
            public void onResponse(Call<List<Music>> call, Response<List<Music>> response) {
                //Extract the list of songs from the response body and add it to adapter
                mSongsList = response.body();
                mSongsAdapter.addAll(mSongsList);
            }

            @Override
            public void onFailure(Call<List<Music>> call, Throwable t) {
                Log.e(getLocalClassName(),t.getMessage());
            }
        });

    }

    //This is needed when user exists the app(by clicking press) so that when he/she visit next time
    //then he/she will be able to identify the song being played in background by looking at the play/pause button

    @Override
    protected void onStop() {

        //save the current playing song's button state and its adapter position
        SharedPreferences.Editor buttonState = getSharedPreferences("BUTTON_STATE", MODE_PRIVATE).edit();

        buttonState.putInt("DRAWABLE_ID", mSongsAdapter.getButtonState());
        buttonState.putInt("POSITION", mSongsAdapter.getPos());
        buttonState.commit();
        super.onStop();
    }
}
