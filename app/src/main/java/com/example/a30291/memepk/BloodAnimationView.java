package com.example.a30291.memepk;

import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.support.annotation.ColorInt;
import android.support.annotation.ColorRes;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.util.Log;
import android.util.TypedValue;
import android.view.View;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Random;

/**
 * Created by 30291 on 2019/6/16.
 * pk减血量动画
 */

public class BloodAnimationView extends View {
    List<Blood> bloodList;
    Context context;
    int width;
    int height;

    public BloodAnimationView(Context context) {
        this(context, null);
    }

    public BloodAnimationView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public BloodAnimationView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.context = context;
        bloodList = new ArrayList<>();
    }


    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        width = getWidth();
        height = getHeight();
    }

    public void addData(Blood blood) {
        blood.setBloodSize(dp2Px(blood.getBloodSize()));//只是把原始数值重置成dp-px
        blood.initPosition(width, height);
        if (bloodList == null) bloodList = new ArrayList<>();
        bloodList.add(blood);
        blood.startBloodAnimation();
        postInvalidate();
    }

    public void cancel() {
        if (bloodList == null || bloodList.isEmpty()) return;
        Iterator<Blood> it = bloodList.iterator();
        while (it.hasNext()) {
            Blood blood = it.next();
            blood.cancelBloodAnimation();
            it.remove();
        }
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if (bloodList == null || bloodList.isEmpty()) return;
        Iterator<Blood> it = bloodList.iterator();
        while (it.hasNext()) {
            Blood blood = it.next();
            blood.draw(canvas);
            if (blood.isFinish()) {
                it.remove();
            }
            postInvalidate();
        }
    }

    public int dp2Px(float value) {
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, value, context.getResources().getDisplayMetrics());
    }

    public static class Blood {
        private int pointX;
        private int pointY;
        private int rangWidth;
        private int rangHeight;
        @ColorInt
        private int bloodColor = Color.parseColor("#930234");
        private int bloodSize = 10;
        private Paint paint;
        private int duration = 1000;//持续时间
        private ValueAnimator valueAnimator;
        private String text = "";
        private boolean isFinish = false;
        private int textWidth;
        private int textHeight;

        public Blood() {
            paint = new Paint(Paint.ANTI_ALIAS_FLAG | Paint.DITHER_FLAG);
            paint.setColor(bloodColor);
            paint.setTextSize(bloodSize);
            valueAnimator = ValueAnimator.ofFloat(0f, 1.0f);
            valueAnimator.setDuration(duration);
            valueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                @Override
                public void onAnimationUpdate(ValueAnimator animation) {
                    float value = (float) animation.getAnimatedValue();
                    pointY -= rangHeight * value;
                    paint.setAlpha((int) (255 * (1.0f - value)));
                    if (value >= 1.0f) {
                        isFinish = true;
                    }
                }
            });
        }

        private int getStringHeight(String string, Paint paint) {
            Rect rect = new Rect();
            paint.getTextBounds(string, 0, string.length(), rect);
            return rect.height();
        }

        private int getStringWidth(String string, Paint paint) {
//            Rect rect = new Rect();
//            paint.getTextBounds(string, 0, string.length(), rect);
//            return rect.width();
            return Math.round(paint.measureText(string) + 0.5f);//四舍五入，+0.5强行进位，以免画出边界
        }

        //传入view宽高，确定文字随机出现的位置，以及之后移动的范围
        public void initPosition(int viewWidth, int viewHeight) {
            Random random = new Random();
            float randomValue = random.nextFloat();
            pointX = (int) (viewWidth * randomValue - textWidth);
            if (pointX < 0) {
                pointX = 0;
            } else if (pointX > viewHeight - textWidth) {
                pointX = viewHeight - textWidth;
            }
            Log.e("aaa", "initPosition: " + randomValue + "   " + pointX + "   " + textWidth + "   " + viewWidth + "   " + bloodSize);
            pointY = viewHeight - textHeight;
//            pointY = (int) (viewHeight*0.3 * randomValue - getStringHeight(text,paint));//0.3是确保初始化y轴在底部0.3的位置避免一开始y轴太高
            rangWidth = viewWidth - pointX;
            rangHeight = viewHeight - pointY;
        }

        public boolean isFinish() {
            return isFinish;
        }

        public void startBloodAnimation() {
            if (valueAnimator != null && !valueAnimator.isRunning()) {
                valueAnimator.start();
            }
        }

        public void cancelBloodAnimation() {
            if (valueAnimator != null && valueAnimator.isRunning()) {
                valueAnimator.cancel();
            }
        }

        public void draw(Canvas canvas) {
            canvas.drawText(text, pointX, pointY, paint);
        }

        public String getText() {
            return text;
        }

        public void setText(String text) {
            this.text = text;
            this.textWidth = getStringWidth(text, paint);
            this.textHeight = getStringHeight(text, paint);
        }

        public int getBloodSize() {
            return bloodSize;
        }

        public void setBloodSize(int bloodSize) {
            this.bloodSize = bloodSize;
            paint.setTextSize(bloodSize);
            this.textWidth = getStringWidth(text, paint);
            this.textHeight = getStringHeight(text, paint);
        }

        public int getPointX() {
            return pointX;
        }

        public void setPointX(int pointX) {
            this.pointX = pointX;
        }

        public int getPointY() {
            return pointY;
        }

        public void setPointY(int pointY) {
            this.pointY = pointY;
        }

        public int getBloodColor() {
            return bloodColor;
        }

        public void setBloodColor(int bloodColor) {
            this.bloodColor = bloodColor;
            paint.setColor(bloodColor);
        }


        public Paint getPaint() {
            return paint;
        }

        public int getDuration() {
            return duration;
        }

        public void setDuration(int duration) {
            this.duration = duration;
            valueAnimator.setDuration(duration);
        }
    }
}
