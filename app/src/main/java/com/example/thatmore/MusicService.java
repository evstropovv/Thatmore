package com.example.thatmore;

import android.app.Service;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Environment;
import android.os.IBinder;
import android.util.Log;

import com.example.thatmore.R;

import java.io.IOException;

public class MusicService extends Service {

    public MusicService() {
    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) { //запускается при старте сервиса

        if (intent.getExtras().getBoolean("record")) {
            playMusic(R.raw.record);
        }else {
            if (intent.getExtras().getBoolean("answer")) {
                playMusic(R.raw.odnoklassniki);
            } else if (!intent.getExtras().getBoolean("answer")) {
                playMusic(R.raw.wrong);
            }
        }
        return super.onStartCommand(intent, flags, startId);
    }


    private void playMusic(int direcory) {
        MediaPlayer mediaPlayer = new MediaPlayer().create(this, direcory);
        mediaPlayer.start();
    }
}
