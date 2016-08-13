package com.example.katherine_qj.puzzle;

import android.graphics.Bitmap;

/**
 * Created by Katherine-qj on 2016/8/12.
 */
public class PaPa  {
    private Bitmap bitmap;
    public PaPa(Bitmap bitmap){
        this.bitmap = bitmap;
    }

    public void setBitmap(Bitmap bitmap) {
        this.bitmap = bitmap;
    }

    public Bitmap getBitmap() {
        return bitmap;
    }
}
