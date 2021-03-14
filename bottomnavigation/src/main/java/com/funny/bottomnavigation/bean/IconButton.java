package com.funny.bottomnavigation.bean;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.RectF;

import com.funny.bottomnavigation.FunnyBottomNavigation.Direction;
import com.funny.bottomnavigation.utils.BitmapUtils;

public class IconButton {
    Context context;
    int resId;
    float x,y;//左上角位置
    int width,height;
    int srcColor;
    int backgroundColor = Color.parseColor("#6e6c6f");

    int paddingLeft=0,paddingRight=0,paddingTop=0,paddingBottom=0;
    float imageX,imageY;
    int imageWidth,imageHeight;

    boolean isCurrentPage = false;

    Bitmap bitmap;
    Paint paint;
    PorterDuffXfermode xfermode;
    Canvas canvas;

    int clickProgress = 0;
    int transformProgress = 0;
    RectF progressRect;
    float scaleTimes = 1.5f;
    Direction direction;

    final String TAG = "IconButton";

    public IconButton(Context context,int resId, float x, float y, int width, int height, int imageWidth, int imageHeight, int srcColor) {
        this.context = context;
        this.resId = resId;
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
        this.imageWidth = imageWidth;
        this.imageHeight = imageHeight;
        this.srcColor = srcColor;

        initVars();
        initGraphics();
    }

    private void initVars(){
        imageX = x + (width - imageWidth) / 2f;
        imageY = y + (height - imageHeight) / 2f;
    }
    
    private void initGraphics(){
        bitmap = BitmapUtils.getBitmapFromResources(context.getResources(),resId);
        bitmap = BitmapUtils.getScaledBitmap(bitmap,imageWidth-paddingLeft-paddingRight,imageHeight-paddingTop-paddingBottom);
        paint = new Paint();
        paint.setColor(backgroundColor);
        paint.setColor(srcColor);
        paint.setAlpha(0);
        paint.setAntiAlias(true);

        progressRect = new RectF();
        xfermode = new PorterDuffXfermode(PorterDuff.Mode.DST_IN);
    }

    public boolean isClicked(float actionX, float actionY){
        return (actionX>=imageX&&actionX<=imageX+imageWidth&&actionY>=imageY&&actionY<=imageY+imageHeight);
    }

    
    /**
     * @description 绘制基本的bitmap和动画
     * @param canvas 绘制的画板
     * @return void
     */
    public void drawSelf(Canvas canvas){
        if(clickProgress > 0){//正在处于被点击状态
            paint.setAlpha(255);
            canvas.save();

            if(clickProgress>=60&&clickProgress<=80){
                canvas.scale(1+ (scaleTimes-1) * clickProgress / 100,1+ (scaleTimes-1) * clickProgress /100 , imageX + imageWidth/2f , imageY + imageHeight/2f);
            }else if(clickProgress>80){
                canvas.scale(scaleTimes -  (scaleTimes-1) * clickProgress / 100,scaleTimes -  (scaleTimes-1) * clickProgress /100 , imageX + imageWidth/2f , imageY + imageHeight/2f);
            }

            if(direction == Direction.LEFT_TO_RIGHT){//从左向右增加
                progressRect.set(imageX,imageY,imageX+imageWidth* clickProgress /100f,imageY+imageHeight);
            }else{//从右向左增加
                progressRect.set(imageX+imageWidth * (100-clickProgress) /100f,imageY,imageX+imageWidth,imageY+imageHeight);
            }
            canvas.drawBitmap(bitmap,imageX,imageY,paint);
            paint.setStyle(Paint.Style.FILL);

            //创造出新的“图层”实现部分遮挡效果
            int layerId = canvas.saveLayer(imageX,imageY,imageX+imageWidth,imageY+imageHeight,paint);
            paint.setColor(srcColor);
            canvas.drawRect(progressRect,paint);
            paint.setXfermode(xfermode);
            paint.setColor(backgroundColor);
            canvas.drawBitmap(bitmap,imageX,imageY,paint);
            paint.setXfermode(null);
            canvas.restoreToCount(layerId);

            //恢复到缩放前状态
            canvas.restore();

            paint.setColor(srcColor);

            paint.setStyle(Paint.Style.STROKE);
            if(clickProgress<=50){
                paint.setAlpha(clickProgress * 255 / 100);
                paint.setStrokeWidth(24 * clickProgress / 100f);
            }else {
                paint.setAlpha(255 - clickProgress * 255 / 100);
                paint.setStrokeWidth(24 * (1 - clickProgress / 100f));
            }
            canvas.drawCircle(imageX+imageWidth/2f,imageY+imageHeight/2f, 0.75f * imageHeight * clickProgress / 100f,paint);
        }else{
            paint.setColor(backgroundColor);
            canvas.drawBitmap(bitmap,imageX,imageY,paint);
        }

        if(transformProgress>0){
            paint.setAlpha(255);
            if(direction == Direction.RIGHT_TO_LEFT){//向左减少
                progressRect.set(imageX,imageY,imageX+imageWidth* (100-transformProgress) /100f,imageY+imageHeight);
            }else{//向右减少
                progressRect.set(imageX+imageWidth * transformProgress /100f,imageY,imageX+imageWidth,imageY+imageHeight);
            }
            canvas.drawBitmap(bitmap,imageX,imageY,paint);
            paint.setStyle(Paint.Style.FILL);

            int layerId = canvas.saveLayer(imageX,imageY,imageX+imageWidth,imageY+imageHeight,paint);
            paint.setColor(srcColor);
            canvas.drawRect(progressRect,paint);
            paint.setXfermode(xfermode);
            paint.setColor(backgroundColor);
            canvas.drawBitmap(bitmap,imageX,imageY,paint);
            paint.setXfermode(null);
            canvas.restoreToCount(layerId);
        }
    }

    public void setClickProgress(int clickProgress) {
        this.clickProgress = clickProgress;
    }

    public void setTransformProgress(int transformProgress) {
        this.transformProgress = transformProgress;
    }

    public void setCurrentPage(boolean currentPage) {
        isCurrentPage = currentPage;
    }

    public void setCanvas(Canvas canvas) {
        this.canvas = canvas;
    }

    public float getImageX() {
        return imageX;
    }

    public float getImageY() {
        return imageY;
    }

    public void setDirection(Direction direction) {
        this.direction = direction;
    }

    public float[] getImageLeftCenter(){
        return new float[]{imageX,imageY+imageHeight/2f};
    }

    public float[] getImageRightCenter(){
        return new float[]{imageX+imageWidth,imageY+imageHeight/2f};
    }
}
