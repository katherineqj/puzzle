package com.example.katherine_qj.puzzle;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.media.AudioManager;
import android.media.SoundPool;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.widget.Button;
import android.widget.ImageView;

import java.security.PublicKey;

/**
 * Created by Katherine-qj on 2016/8/13.
 */
public class WelecomeActivity extends Activity {

    private SoundPool soundPool;
    private int sound;
    private float soundLoad;

    private static final int REQUEST_EXTERNAL_STORAGE = 1;
    private static String[] PERMISSIONS_STORAGE = {
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE };
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.welcome);
        init();
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);//让windows进行全屏显示
        setContentView(R.layout.welcome);
        verifyStoragePermissions(this);
        ImageView iv = (ImageView) findViewById(R.id.welcome);
        Button bu = (Button)findViewById(R.id.button);
        AlphaAnimation alphaAnimation = new AlphaAnimation(0.3f, 1.0f);
        soundPool.play(1,1, 1, 0, 4, 1);
       /* soundPool.setOnLoadCompleteListener(new SoundPool.OnLoadCompleteListener() {

            @Override
            public void onLoadComplete(SoundPool soundPool, int sampleId, int status) {
                soundPool.play(sound, soundLoad, soundLoad, 1, 4, 1.3f);

            }
        });*/


    alphaAnimation.setDuration(4000);
        iv.setAnimation(alphaAnimation);
        bu.setAnimation(alphaAnimation);
        alphaAnimation.setAnimationListener(new Animation.AnimationListener() {

            public void onAnimationStart(Animation arg0) {
                // TODO Auto-generated method stub

            }

            public void onAnimationRepeat(Animation arg0) {
                // TODO Auto-generated method stub

            }

            public void onAnimationEnd(Animation arg0) {
                // TODO Auto-generated method stub

            }
        });

        bu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                startActivity(intent);
                finish();
            }
        });
    }

    public static void verifyStoragePermissions(Activity activity) {
        // Check if we have write permission
        int permission = ActivityCompat.checkSelfPermission(activity,
                Manifest.permission.WRITE_EXTERNAL_STORAGE);

        if (permission != PackageManager.PERMISSION_GRANTED) {
            // We don't have permission so prompt the user
            ActivityCompat.requestPermissions(activity, PERMISSIONS_STORAGE,
                    REQUEST_EXTERNAL_STORAGE);
        }
    }

public void init(){
 /*   soundPool = new SoundPool(4, AudioManager.STREAM_MUSIC, 5);
    sound = soundPool.load(this, R.raw.welcomemusic, 2);
    AudioManager audioManager = (AudioManager) this.getSystemService(Context.AUDIO_SERVICE);
    //获取当前系统音量
    float qian = audioManager.getStreamVolume(AudioManager.STREAM_MUSIC);
    //获取系统最大音量
    float max = audioManager.getStreamMaxVolume(AudioManager.STREAM_MUSIC);
    //计算得到播放音量
    soundLoad = max;
    //设置播放文件的音量*/


    soundPool= new SoundPool(10,AudioManager.STREAM_SYSTEM,5);
    soundPool.load(this,R.raw.welcomemusic,1);
}
}