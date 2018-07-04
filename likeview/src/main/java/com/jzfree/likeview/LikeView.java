package com.jzfree.likeview;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;
import android.view.animation.BounceInterpolator;

/**
 * Created by Wang Jiuzhou on 2018/7/1 14:41
 */
public class LikeView extends View {
    private static final int CIRCLE_COLOR = Color.parseColor("#88e24d3d");
    private static final int SCALE_DURATION = 150;

    private boolean isLike;

    private Matrix matrix;
    private Paint mPaint;
    private Paint mPaintCircle;

    private Bitmap mBitmapLike;
    private Bitmap mBitmapUnLike;
    private Bitmap mBitmapShining;

    private LvPoint mPointLike;
    private LvPoint mPointUnLike;
    private LvPoint mPointShining;
    private LvPoint mPointCircle;

    private float scaleLike = 1;
    private float scaleUnLike = 1;
    private float scaleShining = 1;

    private boolean isScaleLike;
    private boolean isScaleUnLike;

    private float mCircleRadiusMax;
    private float mCircleRadiusMin;
    private float circleRadius;

    private AnimatorSet animatorLike;
    private AnimatorSet animatorUnLike;

    public LikeView(Context context) {
        this(context, null);
    }

    public LikeView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public LikeView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        matrix = new Matrix();

        animatorLike = new AnimatorSet();
        animatorUnLike = new AnimatorSet();

        initBitmap();
        initCircle();
    }

    private void initBitmap() {
        mPaint = new Paint();
        mPaint.setAntiAlias(true);

        mBitmapLike = BitmapFactory.decodeResource(getResources(), R.drawable.ic_messages_like_selected);
        mBitmapUnLike = BitmapFactory.decodeResource(getResources(), R.drawable.ic_messages_like_unselected);
        mBitmapShining = BitmapFactory.decodeResource(getResources(), R.drawable.ic_messages_like_selected_shining);

        calculatePointLike();
        calculatePointShining();
    }

    private void calculatePointLike() {
        float x = getPaddingLeft();
        float y = getPaddingTop() + mBitmapShining.getHeight() / 2;
        mPointLike = new LvPoint(x, y);
        mPointUnLike = mPointLike;
    }

    private void calculatePointShining() {
        float x = getPaddingLeft() + (mBitmapLike.getWidth() - mBitmapShining.getWidth()) / 2;
        float y = getPaddingTop();
        mPointShining = new LvPoint(x, y);
    }

    private void initCircle() {
        mPaintCircle = new Paint();
        mPaintCircle.setAntiAlias(true);
        mPaintCircle.setStyle(Paint.Style.STROKE);
        mPaintCircle.setStrokeWidth(dip2px(getContext(), 2));

        mPointCircle = new LvPoint(mPointLike.x + mBitmapLike.getWidth() / 2, mPointLike.y + mBitmapLike.getHeight() / 2);
        mCircleRadiusMax = Math.max(mPointCircle.x - getPaddingLeft(), mPointCircle.y - getPaddingTop());
        mCircleRadiusMin = dip2px(getContext(), 8);
    }

    public boolean setLike(boolean like) {
        if (animatorLike.isRunning() || animatorUnLike.isRunning()) {
            return false;
        }

        this.isLike = like;
        if (like) {
            startLikeAnim();
        } else {
            startUnLikeAnim();
        }
        return true;
    }

    public boolean isLike() {
        return isLike;
    }

    public void setScaleLike(float scaleLike) {
        this.scaleLike = scaleLike;
        postInvalidate();
    }

    public void setScaleUnLike(float scaleUnLike) {
        this.scaleUnLike = scaleUnLike;
        postInvalidate();
    }

    public void setScaleShining(float scaleShining) {
        this.scaleShining = scaleShining;
    }

    public void setCircleRadius(float circleRadius) {
        this.circleRadius = circleRadius;
        mPaintCircle.setColor(CIRCLE_COLOR);
        mPaintCircle.setAlpha((int) ((mCircleRadiusMax - circleRadius) / mCircleRadiusMax * 255));
    }

    @Override
    protected void onDraw(Canvas canvas) {
        matrix.reset();
        if (isLike) {
            if (isScaleUnLike) {
                canvas.save();
                matrix.postScale(scaleUnLike, scaleUnLike, mPointUnLike.x + mBitmapUnLike.getWidth() / 2,
                        mPointUnLike.y + mBitmapUnLike.getHeight() / 2);
                canvas.setMatrix(matrix);
                canvas.drawBitmap(mBitmapUnLike, mPointUnLike.x, mPointUnLike.y, mPaint);
                canvas.restore();
            } else {
                canvas.save();
                matrix.postScale(scaleLike, scaleLike, mPointLike.x + mBitmapLike.getWidth() / 2,
                        mPointLike.y + mBitmapLike.getHeight() / 2);
                canvas.setMatrix(matrix);
                canvas.drawBitmap(mBitmapLike, mPointLike.x, mPointLike.y, mPaint);
                canvas.restore();

                canvas.drawCircle(mPointCircle.x, mPointCircle.y, circleRadius, mPaintCircle);

                canvas.save();
                matrix.reset();
                matrix.postScale(scaleShining, scaleShining, mPointShining.x + mBitmapShining.getWidth() / 2,
                        mPointShining.y + mBitmapShining.getHeight() / 2);
                canvas.setMatrix(matrix);
                canvas.drawBitmap(mBitmapShining, mPointShining.x, mPointShining.y, mPaint);
                canvas.restore();
            }
        } else {
            if (isScaleLike) {
                matrix.postScale(scaleLike, scaleLike, mPointLike.x + mBitmapLike.getWidth() / 2,
                        mPointLike.y + mBitmapLike.getHeight() / 2);
                canvas.save();
                canvas.setMatrix(matrix);
                canvas.drawBitmap(mBitmapLike, mPointLike.x, mPointLike.y, mPaint);
                canvas.restore();
            } else {
                matrix.postScale(scaleUnLike, scaleUnLike, mPointUnLike.x + mBitmapUnLike.getWidth() / 2,
                        mPointUnLike.y + mBitmapUnLike.getHeight() / 2);
                canvas.save();
                canvas.setMatrix(matrix);
                canvas.drawBitmap(mBitmapUnLike, mPointUnLike.x, mPointUnLike.y, mPaint);
                canvas.restore();
            }
        }
    }

    private void startLikeAnim() {
        ObjectAnimator scaleUnLike = ObjectAnimator.ofFloat(this, "scaleUnLike", 1, 0.9f);
        scaleUnLike.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationStart(Animator animation) {
                isScaleUnLike = true;
            }

            @Override
            public void onAnimationEnd(Animator animation) {
                isScaleUnLike = false;
            }
        });
        scaleUnLike.setDuration(SCALE_DURATION);

        ObjectAnimator scaleLike = ObjectAnimator.ofFloat(this, "scaleLike", 0.8f, 1);
        scaleLike.setInterpolator(new BounceInterpolator());
        scaleLike.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationStart(Animator animation) {
                isScaleLike = true;
            }

            @Override
            public void onAnimationEnd(Animator animation) {
                isScaleLike = false;
            }
        });
        scaleLike.setDuration(SCALE_DURATION);

        ObjectAnimator scaleShining = ObjectAnimator.ofFloat(this, "scaleShining", 0.3f, 1);
        scaleShining.setDuration(SCALE_DURATION);

        ObjectAnimator scaleCircle = ObjectAnimator.ofFloat(this, "circleRadius", mCircleRadiusMin, mCircleRadiusMax);
        scaleCircle.setDuration(SCALE_DURATION);

        animatorLike = new AnimatorSet();
        animatorLike.play(scaleLike).with(scaleShining).with(scaleCircle);
        animatorLike.play(scaleLike).after(scaleUnLike);
        animatorLike.start();
    }

    private void startUnLikeAnim() {
        ObjectAnimator scaleLike = ObjectAnimator.ofFloat(this, "scaleLike", 1, 0.9f);
        scaleLike.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationStart(Animator animation) {
                isScaleLike = true;
            }

            @Override
            public void onAnimationEnd(Animator animation) {
                isScaleLike = false;
            }
        });
        scaleLike.setDuration(SCALE_DURATION);

        ObjectAnimator scaleUnLike = ObjectAnimator.ofFloat(this, "scaleUnLike", 0.9f, 1);
        scaleUnLike.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationStart(Animator animation) {
                isScaleUnLike = true;
            }

            @Override
            public void onAnimationEnd(Animator animation) {
                isScaleUnLike = false;
            }
        });
        scaleUnLike.setDuration(SCALE_DURATION);

        animatorLike = new AnimatorSet();
        animatorLike.play(scaleUnLike).after(scaleLike);
        animatorLike.start();
    }

    public static int dip2px(Context context, float dpValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dpValue * scale + 0.5f);
    }
}
