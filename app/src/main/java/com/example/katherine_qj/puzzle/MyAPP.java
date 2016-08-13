package com.example.katherine_qj.puzzle;

import android.app.Application;
import android.graphics.Bitmap;

/**
 * Created by Katherine-qj on 2016/8/11.
 */
public class MyAPP extends Application {
    private Bitmap mBitmap;

    public Bitmap getBitmap() {
        return mBitmap;
    }

    public void setBitmap(Bitmap bitmap) {
        this.mBitmap = bitmap;
    }

}