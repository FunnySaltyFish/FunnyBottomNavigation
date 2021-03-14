package com.funny.bottomnavigation.utils;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;

public class BitmapUtils {
    public static Bitmap getBitmapFromResources(Resources re,int id){
        return BitmapFactory.decodeResource(re,id);
    }

    public static Bitmap getScaledBitmap(Bitmap bitmap,int targetWidth,int targetHeight){
        int width = bitmap.getWidth();
        int height = bitmap.getHeight();

        float scaleWidth = ((float)targetWidth)/width;
        float scaleHeight = ((float)targetHeight)/height;
        Matrix matrix = new Matrix();
        matrix.postScale(scaleWidth, scaleHeight);
        return Bitmap.createBitmap(bitmap,0,0,width,height,matrix,true);
    }

    public static Bitmap getBigBitmapFromResources(Resources re, int id, int targetWidth, int targetHeight){
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeResource(re, id, options);
        //现在原始宽高以存储在了options对象的outWidth和outHeight实例域中
        int rawWidth = options.outWidth;
        int rawHeight = options.outHeight;
        int inSampleSize = 1;
        if (rawWidth > targetWidth || rawHeight > targetHeight) {
            float ratioHeight = (float) rawHeight / targetHeight;
            float ratioWidth = (float) rawWidth / targetWidth;
            inSampleSize = (int) Math.min(ratioWidth, ratioHeight);
        }
        options.inSampleSize=inSampleSize;
        options.inJustDecodeBounds=false;
        return BitmapFactory.decodeResource(re,id,options);
    }
}
