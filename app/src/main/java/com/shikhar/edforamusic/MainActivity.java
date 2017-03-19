package com.shikhar.edforamusic;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
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

    ListView mSongListView;
    SongsAdapter mSongsAdapter;

    List<Music> mSongsList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mSongListView = (ListView) findViewById(R.id.list_view);
        mSongsAdapter = new SongsAdapter(this);
        mSongListView.setAdapter(mSongsAdapter);
        mSongListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                //open new activity and start service
                int x = 0;
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

                mSongsList = response.body();
                mSongsAdapter.addAll(mSongsList);
            }

            @Override
            public void onFailure(Call<List<Music>> call, Throwable t) {

            }
        });

    }
}
