package com.example.a30291.memepk;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.RectF;
import android.graphics.drawable.GradientDrawable;
import android.os.Build;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.widget.TextView;

/**
 * Created by 30291 on 2019/6/16.
 */

@SuppressLint("AppCompatCustomView")
public class BloodProgressTextView extends TextView {
    private Context context;
    private GradientDrawable bgDrawable = new GradientDrawable();
    private int progressColor;
    private int secondProgressColor;
    private int radius;
    private int maxProgress;
    private int progress;
    private Paint secondProgressPaint;
    private RectF rectF;
    private int borderWidth;//第二进度条与外部的间距
    public BloodProgressTextView(Context context) {
        this(context, null);
    }

    public BloodProgressTextView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public BloodProgressTextView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.context = context;
        TypedArray ta = context.obtainStyledAttributes(attrs, R.styleable.BloodProgressTextView);
        progressColor = ta.getColor(R.styleable.BloodProgressTextView_progressColor, 0);
        secondProgressColor = ta.getColor(R.styleable.BloodProgressTextView_secondProgressColor, 0);
        radius = ta.getDimensionPixelSize(R.styleable.BloodProgressTextView_radius, 0);
        maxProgress = ta.getInteger(R.styleable.BloodProgressTextView_maxProgress, 100);
        progress = ta.getInteger(R.styleable.BloodProgressTextView_progress, 0);
        borderWidth=ta.getDimensionPixelSize(R.styleable.BloodProgressTextView_borderWidth,0);
        ta.recycle();

        secondProgressPaint = new Paint();
        secondProgressPaint.setAntiAlias(true);
        secondProgressPaint.setDither(true);
        secondProgressPaint.setColor(secondProgressColor);

        rectF = new RectF();
    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        super.onLayout(changed, left, top, right, bottom);
        setBgDrawable();
    }


    @Override
    protected void onDraw(Canvas canvas) {
        if (progress>=maxProgress)progress=maxProgress;
        float radio=progress/(float)maxProgress;
        int secondPwidth= (int) (radio*getWidth());
        rectF.set(0+borderWidth,0+borderWidth,secondPwidth-borderWidth,getHeight()-borderWidth);
        canvas.drawRoundRect(rectF, radius, radius, secondProgressPaint);
        super.onDraw(canvas);
    }

    public void setProgressColor(int progressColor) {
        this.progressColor = progressColor;
        setBgDrawable();
    }

    public void setRadius(int radius) {
        this.radius = radius;
        setBgDrawable();
    }


    public void setSecondProgressColor(int secondProgressColor) {
        this.secondProgressColor = secondProgressColor;
        secondProgressPaint.setColor(secondProgressColor);
        invalidate();
    }

    public void setBorderWidth(int borderWidth){
        this.borderWidth=borderWidth;
        invalidate();
    }

    public int getMaxProgress() {
        return maxProgress;
    }

    public void setMaxProgress(int maxProgress) {
        this.maxProgress = maxProgress;
        invalidate();
    }

    public int getProgress() {
        return progress;
    }

    public void setProgress(int progress) {
        this.progress = progress;
        invalidate();
    }

    private void setBgDrawable() {
        bgDrawable.setColor(progressColor);
        bgDrawable.setCornerRadius(radius);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {//16
            setBackground(bgDrawable);
        } else {
            setBackgroundDrawable(bgDrawable);
        }
    }


    protected int dp2px(float dp) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dp * scale + 0.5f);
    }

    protected int sp2px(float sp) {
        final float scale = this.context.getResources().getDisplayMetrics().scaledDensity;
        return (int) (sp * scale + 0.5f);
    }
}
