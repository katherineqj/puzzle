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
    private static String[] PERMISSIONS_STORAGE = {
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE };
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.welcome);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);//让windows进行全屏显示
        setContentView(R.layout.welcome);
        Init();
        /*AudioManager audioManager = (AudioManager) this.getSystemService(Context.AUDIO_SERVICE);
        //获取当前系统音量
        float qian = audioManager.getStreamVolume(AudioManager.STREAM_MUSIC);
        //获取系统最大音量
        float max = audioManager.getStreamMaxVolume(AudioManager.STREAM_MUSIC);
        //计算得到播放音量
        soundLoad = max;
        soundPool = new SoundPool(2, AudioManager.STREAM_SYSTEM, 0);
        sound = soundPool.load(this, R.raw.pa, 1);*/
        Button bu = (Button)findViewById(R.id.button);
        bu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                startActivity(intent);
                overridePendingTransition(R.anim.intent,R.anim.intent_out);
                finish();
            }
        });
        soundPool.play(sound, soundLoad, soundLoad, 1, 1, 1.0f);
    }



    public  void Init(){
        AudioManager audioManager = (AudioManager) this.getSystemService(Context.AUDIO_SERVICE);
        //获取当前系统音量
        float qian = audioManager.getStreamVolume(AudioManager.STREAM_MUSIC);
        //获取系统最大音量
        float max = audioManager.getStreamMaxVolume(AudioManager.STREAM_MUSIC);
        //计算得到播放音量
        soundLoad = max;
        soundPool = new SoundPool(1, AudioManager.STREAM_SYSTEM, 0);
        sound = soundPool.load(this, R.raw.welcome, 1);
    }


}