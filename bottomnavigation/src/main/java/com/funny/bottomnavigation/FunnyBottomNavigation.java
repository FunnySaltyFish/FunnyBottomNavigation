package com.funny.bottomnavigation;

import android.animation.Animator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PathMeasure;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

import androidx.annotation.Nullable;

import com.funny.bottomnavigation.bean.IconButton;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class FunnyBottomNavigation extends View {
    int mViewWidth,mViewHeight;
    int paddingTop=0;
    int imageWidth=0,imageHeight=0;
    int highlightColor;
    int normalColor;
    int backgroundColor;
    int animationDuration;
    int startPage;

    List<IconButton> iconButtonList;

    Context mContext;
    OnItemClickListener mOnItemClickListener;
    OnAnimationUpdateListener mOnAnimationUpdateListener;

    //双缓冲画布
    Canvas mCacheCanvas;
    Bitmap mCacheBitmap;
    Paint mPaint;

    int mLastPage;
    IconButton mLastClickedIconButton;
    IconButton mNeedToClickIconButton;

    //以下和转移的动画效果相关
    ValueAnimator mValueAnimator;
    float startX,startY,endX,endY;//转移动画的开始和结束位置
    ArrayList<Path> transformPaths;
    PathMeasure transformPathMeasure;

    Random random;

    final String TAG = "FunnyBottomNavigation";

    public FunnyBottomNavigation(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs,0);
    }

    public FunnyBottomNavigation(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        mContext = context;
        TypedArray typedArray = context.obtainStyledAttributes(attrs,R.styleable.FunnyBottomNavigation);
        normalColor = typedArray.getColor(R.styleable.FunnyBottomNavigation_normalColor, Color.parseColor("#6e6c6f"));
        highlightColor = typedArray.getColor(R.styleable.FunnyBottomNavigation_highlightColor,Color.parseColor("#069270"));
        paddingTop = typedArray.getDimensionPixelOffset(R.styleable.FunnyBottomNavigation_paddingTop,0);
        imageWidth = typedArray.getDimensionPixelOffset(R.styleable.FunnyBottomNavigation_imageWidth,80);
        imageHeight = typedArray.getDimensionPixelOffset(R.styleable.FunnyBottomNavigation_imageHeight,80);
        startPage = mLastPage = typedArray.getInteger(R.styleable.FunnyBottomNavigation_startPage,0);
        backgroundColor = typedArray.getColor(R.styleable.FunnyBottomNavigation_backgroundColor,Color.WHITE);
        animationDuration = typedArray.getInt(R.styleable.FunnyBottomNavigation_animationDuration,500);
        typedArray.recycle();

        initGraphics();
        initAnimators();

        setLayerType(View.LAYER_TYPE_SOFTWARE, null);
        random = new Random();
    }

    /**
     * 初始化底部按钮
     * @param iconIds 图片id的集合（ArrayList形式)
     */
    public void initIconButtons(ArrayList<Integer> iconIds){
        if(mViewWidth==0||mViewHeight==0){
            postDelayed(() -> initIconButtons(iconIds),100);
            return;
        }

        int itemWidth = mViewWidth / iconIds.size();
        iconButtonList = new ArrayList<>();
        for (int i=0;i<iconIds.size();i++){
            int currentX =  i * itemWidth;
            IconButton iconButton = new IconButton(mContext,iconIds.get(i),currentX,paddingTop,itemWidth,mViewHeight-paddingTop,imageWidth,imageHeight,highlightColor);
            iconButton.setCanvas(mCacheCanvas);
            iconButtonList.add(iconButton);
        }

        mLastClickedIconButton = iconButtonList.get(mLastPage);
        mLastClickedIconButton.setClickProgress(100);

        invalidate();
    }

    /**
     * 初始化底部按钮
     * @param iconIds 图片id的集合（ArrayList形式)
     */
    public void initIconButtons(int[] iconIds){
        if(mViewWidth==0||mViewHeight==0){
            postDelayed(() -> initIconButtons(iconIds),100);
            return;
        }

        int itemWidth = mViewWidth / iconIds.length;
        iconButtonList = new ArrayList<>();
        for (int i=0;i<iconIds.length;i++){
            int currentX =  i * itemWidth;
            IconButton iconButton = new IconButton(mContext,iconIds[i],currentX,paddingTop,itemWidth,mViewHeight-paddingTop,imageWidth,imageHeight,highlightColor);
            iconButton.setCanvas(mCacheCanvas);
            iconButtonList.add(iconButton);
        }

        mLastClickedIconButton = iconButtonList.get(mLastPage);
        mLastClickedIconButton.setClickProgress(100);

        invalidate();
    }

    /**
     * 初始化绘图相关
     */
    private void initGraphics(){
        if(mViewWidth==0||mViewHeight==0){
            postDelayed(this::initGraphics,100);
            return;
        }

        mCacheCanvas = new Canvas();
        mCacheBitmap = Bitmap.createBitmap(mViewWidth,mViewHeight, Bitmap.Config.ARGB_8888);
        mCacheCanvas.setBitmap(mCacheBitmap);

        mPaint = new Paint();

        transformPaths = new ArrayList<>();
        transformPathMeasure = new PathMeasure();
    }



    private void initAnimators(){
        mValueAnimator = ValueAnimator.ofInt(0,100);
        mValueAnimator.setDuration(animationDuration);
        mValueAnimator.addUpdateListener(animation -> {
            int progress = (Integer) animation.getAnimatedValue();
            mNeedToClickIconButton.setClickProgress(progress);
            mLastClickedIconButton.setTransformProgress(progress);
            if(mOnAnimationUpdateListener!=null){
                mOnAnimationUpdateListener.onUpdate(progress);
            }
            invalidate();
        });
        mValueAnimator.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {

            }

            @Override
            public void onAnimationEnd(Animator animation) {
                resetProgress();
            }

            @Override
            public void onAnimationCancel(Animator animation) {

            }

            @Override
            public void onAnimationRepeat(Animator animation) {

            }
        });
    }

    /**
     * 清空已经进行的动画参数
     */
    private void resetProgress(){
        mNeedToClickIconButton.setClickProgress(100);
        mNeedToClickIconButton.setTransformProgress(0);
        mLastClickedIconButton.setClickProgress(0);
        mLastClickedIconButton.setTransformProgress(0);
        invalidate();
    }

    private int getRandomInt(int min,int max){
        return min + random.nextInt(max - min);
    }

    private float getRandomFloat(float min,float max){
        return  min+random.nextFloat()*(max-min);
    }

    private void startClickAnimation(){
        if(!mValueAnimator.isRunning()){
            mValueAnimator.start();
        }
    }

    /**
     * 绘制转移时的动画
     * @param canvas 绘制的画布（此处为缓冲画布）
     */
    private void drawTransformAnimation(Canvas canvas){
        if(mLastClickedIconButton==null||mNeedToClickIconButton==null)return;
        if(mNeedToClickIconButton==mLastClickedIconButton)return;
        int progress = (int) mValueAnimator.getAnimatedValue();
        if(progress == 0){
            Direction direction = mNeedToClickIconButton.getImageX() < mLastClickedIconButton.getImageX() ? Direction.RIGHT_TO_LEFT : Direction.LEFT_TO_RIGHT;
            if (direction == Direction.LEFT_TO_RIGHT){
                float[] rightCenter = mLastClickedIconButton.getImageRightCenter();
                startX = rightCenter[0];
                startY = rightCenter[1];
                float[] leftCenter = mNeedToClickIconButton.getImageLeftCenter();
                endX = leftCenter[0];
                endY = leftCenter[1];
            }else{
                float[] rightCenter = mNeedToClickIconButton.getImageRightCenter();
                endX = rightCenter[0];
                endY = rightCenter[1];
                float[] leftCenter = mLastClickedIconButton.getImageLeftCenter();
                startX = leftCenter[0];
                startY = leftCenter[1];
            }
            int num = getRandomInt(4,8);
            transformPaths.clear();
            for (int i = 0; i < num; i++) {
                Path path = new Path();
                path.moveTo(startX,startY);
                path.quadTo(getRandomFloat(startX,endX),getRandomFloat(startY,endY)+getRandomInt(-imageHeight*2,imageHeight*2),endX,endY);
                transformPaths.add(path);
            }
        }else if(progress>=1){
            float[] position = new float[2];
            float[] tan = new float[2];
            float radius;
            for(Path path:transformPaths){
                transformPathMeasure.setPath(path,false);
                transformPathMeasure.getPosTan(progress/100f*transformPathMeasure.getLength(),position,tan);
                if(progress<=50){
                    radius = progress/100f*imageHeight/2;
                }else {
                    radius = (1-progress/100f)*imageHeight/2;
                }
                mPaint.setColor(highlightColor);
                canvas.drawCircle(position[0],position[1],radius,mPaint);
            }
        }
    }

    //测量View
    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        int widthMode = MeasureSpec.getMode(widthMeasureSpec);
        int heightMode = MeasureSpec.getMode(heightMeasureSpec);
        mViewWidth = MeasureSpec.getSize(widthMeasureSpec);
        if(heightMode==MeasureSpec.AT_MOST||heightMode==MeasureSpec.EXACTLY){
            mViewHeight = 2 * imageHeight;
        }else mViewHeight = MeasureSpec.getSize(heightMeasureSpec);
        setMeasuredDimension(mViewWidth,mViewHeight);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if(iconButtonList==null||iconButtonList.size()==0)return;

        mCacheCanvas.drawColor(backgroundColor);

        for(IconButton iconButton : iconButtonList){
            iconButton.drawSelf(mCacheCanvas);
        }

        drawTransformAnimation(mCacheCanvas);

        canvas.drawBitmap(mCacheBitmap,0,0,mPaint);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getAction()){
            case MotionEvent.ACTION_UP:
                for (int i = 0; i < iconButtonList.size(); i++) {
                    IconButton iconButton = iconButtonList.get(i);
                    iconButton.setCurrentPage(false);
                    if (iconButton.isClicked(event.getX(),event.getY())){
                        moveTo(i,true);
                        if(mOnItemClickListener !=null){
                            mOnItemClickListener.onClick(i);
                        }
                        return true;
                    }
                }
                return performClick();
        }
        return true;
    }

    @Override
    public boolean performClick() {
        return super.performClick();
    }

    /**
     * 跳转到对应页面
     * @param page 需要跳转的页面，取值 [0,页面总数-1]
     * @param hasAnimation 是否有动画效果
     * @param performClick 是否同时执行点击事件【请确保点击事件不会造成方法死循环】
     */
    public void moveTo(int page, boolean hasAnimation, boolean performClick) {
        if(iconButtonList == null)throw new RuntimeException("Button list has not been initialized! Please make sure you have called initIconButtons(...) before or wait a moment and try again.");
        if (page < 0 || page >= iconButtonList.size()) {
            throw new IllegalArgumentException("Illegal page index! Please make sure that page is from 0 to (the number of buttons - 1).");
        }
        if (mValueAnimator.isRunning()) return;

        if (mLastPage != page) {
            mLastClickedIconButton = iconButtonList.get(mLastPage);
            mNeedToClickIconButton = iconButtonList.get(page);
            Direction direction = mNeedToClickIconButton.getImageX() < mLastClickedIconButton.getImageX() ? Direction.RIGHT_TO_LEFT : Direction.LEFT_TO_RIGHT;
            mLastClickedIconButton.setDirection(direction);
            mNeedToClickIconButton.setDirection(direction);
            if (hasAnimation) startClickAnimation();
            else resetProgress();
            mLastPage = page;
        }
        if(performClick && mOnItemClickListener!=null){
            mOnItemClickListener.onClick(page);
        }
    }

    public void moveTo(int page){
        moveTo(page,true,false);
    }

    public void moveTo(int page, boolean hasAnimation){
        moveTo(page,hasAnimation,false);
    }

    public int getStartPage() {
        return startPage;
    }

    @Override
    public int getPaddingTop() {
        return paddingTop;
    }

    public void setPaddingTop(int paddingTop) {
        this.paddingTop = paddingTop;
    }

    public int getImageWidth() {
        return imageWidth;
    }

    public void setImageWidth(int imageWidth) {
        this.imageWidth = imageWidth;
    }

    public int getImageHeight() {
        return imageHeight;
    }

    public void setImageHeight(int imageHeight) {
        this.imageHeight = imageHeight;
    }

    public int getHighlightColor() {
        return highlightColor;
    }

    public void setHighlightColor(int highlightColor) {
        this.highlightColor = highlightColor;
    }

    public int getNormalColor() {
        return normalColor;
    }

    public void setNormalColor(int normalColor) {
        this.normalColor = normalColor;
    }

    public int getBackgroundColor() {
        return backgroundColor;
    }

    @Override
    public void setBackgroundColor(int backgroundColor) {
        this.backgroundColor = backgroundColor;
    }

    public int getAnimationDuration() {
        return animationDuration;
    }

    public void setAnimationDuration(int animationDuration) {
        this.animationDuration = animationDuration;
    }

    public void setStartPage(int startPage) {
        this.startPage = startPage;
    }

    /*
     当点击到底部按钮时会回调此接口
     参数 position 为当前点击的按钮位置，取值为[0,总数-1]
     注意，当动画仍在进行时点击无效，此时不会触发此回调
    */
    public interface OnItemClickListener{
        void onClick(int position);
    }

    public OnItemClickListener getOnItemClickListener() {
        return mOnItemClickListener;
    }

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.mOnItemClickListener = onItemClickListener;
    }

    //当动画进行时会回调此接口
    //参数 progress 值为[0,100]整数，代表当前动画进行的百分比
    public interface OnAnimationUpdateListener{
        void onUpdate(int progress);
    }

    public void setOnAnimationUpdateListener(OnAnimationUpdateListener onAnimationUpdateListener) {
        this.mOnAnimationUpdateListener = onAnimationUpdateListener;
    }

    public OnAnimationUpdateListener getOnAnimationUpdateListener() {
        return mOnAnimationUpdateListener;
    }

    public enum Direction{
        LEFT_TO_RIGHT,
        RIGHT_TO_LEFT
    }
}
