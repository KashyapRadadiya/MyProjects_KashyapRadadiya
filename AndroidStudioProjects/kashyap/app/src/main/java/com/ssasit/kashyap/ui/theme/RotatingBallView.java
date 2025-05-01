package com.ssasit.kashyap.ui.theme;

import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.View;
import android.os.Handler;


public class RotatingBallView extends View {
    private Paint paint;
    private int radius = 12;
    private int color = Color.RED;
    private float angle = 0f;
    private Handler handler;
    private Runnable updateTask;
    private float centerx;
    private float centery;

    public RotatingBallView(Context context) {
        super(context);
        init();
    }

    private void init() {
        paint = new Paint();
        paint.setStyle(Paint.Style.FILL);
        paint.setAntiAlias(true);

        handler = new Handler();

        updateTask = new Runnable() {
            @Override
            public void run() {changeBallProperties();}

        };
        handler.postDelayed(updateTask,6000); // 60000 = 1 minute

        startRotation(); // Auto start

    }

    @Override
    protected void onDraw( Canvas canvas) {
        super.onDraw(canvas);

        paint.setColor(color);

        float circleRadius = 200; // 200
        centerx = getWidth() / 2 + (float)(circleRadius * Math.cos(Math.toRadians(angle)));
        centery = getHeight() / 2 + (float)(circleRadius * Math.sin(Math.toRadians(angle)));

        canvas.drawCircle(centerx,centery,radius,paint);
    }

    public void startRotation(){
        ObjectAnimator rotationAnimator = ObjectAnimator.ofFloat(this,"angle",0,360);
        rotationAnimator.setDuration(6000); // 60000 = 1 minute
        rotationAnimator.setRepeatCount(ObjectAnimator.INFINITE);

        AnimatorSet animatorSet = new AnimatorSet();
        animatorSet.play(rotationAnimator);
        animatorSet.start();
    }

    public void setAngle(float angle){
        this.angle = angle;
        invalidate();
    }

    private void changeBallProperties() {

        if (radius==12){
            radius = 24;
            color = Color.BLUE;
        } else if (radius == 24) {
            radius = 48;
            color = Color.GREEN;
        } else {
            radius = 96;
            color = Color.RED;
        }
        invalidate();

        handler.postDelayed(updateTask,6000); // 60000 = 1 minute

    }

    public RotatingBallView(Context context, AttributeSet attrs) {
        super(context , attrs);
        init();
    }
}
