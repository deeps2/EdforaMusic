# EdforaMusic <img src="app/src/main/res/mipmap-hdpi/ic_launcher.png" />
Edfora Assignment for Android Developer Profile(HackerEarth)

# ScreenShots
<img src="https://firebasestorage.googleapis.com/v0/b/delhi06-31a81.appspot.com/o/edfora_1.jpg?alt=media&token=2e8143af-43f8-491b-8745-5450d00a455b" width=280>&nbsp;&nbsp;&nbsp;&nbsp;
<img src="https://firebasestorage.googleapis.com/v0/b/delhi06-31a81.appspot.com/o/edfora_2.jpg?alt=media&token=f815c3c7-325e-44c9-8d40-743d0e64c845" width=280>&nbsp;&nbsp;&nbsp;&nbsp;
<img src="https://firebasestorage.googleapis.com/v0/b/delhi06-31a81.appspot.com/o/edfora_4.jpg?alt=media&token=c4bbe87e-d22d-4f7d-b9ea-4296ffea0f5a" width=280></br></br>

# Components Used
- RetroFit2 with GSON converter (for calling web service and parsing JSON)
- ListView (to display list of songs)
- Model class (to represent Song Information that we got after calling the given URL endpoint)
- ArrayAdapter<> (for populating ListView)
- SharedPreferences (to store the state of play/pause button when app is closed)
- Service (to play the song in a background thread)
- MediaPlayer (to play,pause songs)
- Toast (to display current streaming song)

Click on play button to start a song. 
If you close the app and revisit it, you will still find the same song being played(notice the play/pause button image to identify current running song).
Click on any other song to start playing a new one and stop the older one.

Ps: I was out of time so I wasn't able to implement details activity(which shows current chosen song with seekbar and controls) and notification but the app
still has the ability to play the song in the background when its closed.





