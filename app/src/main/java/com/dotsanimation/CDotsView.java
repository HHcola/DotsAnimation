package com.dotsanimation;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.util.Log;
import android.util.Property;
import android.view.View;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by timothyhe on 2017/10/12.
 */


public class CDotsView extends View {

    private static final int DOTS_MIN_COUNT = 10;
    private static final int DOTS_MAX_COUNT = 20;

    private int dotsCount = 7;
    private int OUTER_DOTS_POSITION_ANGLE = 360 / dotsCount;


    private static final int COLOR_1 = 0xFFFFC107;
    private static final int COLOR_2 = 0xFFFF9800;
    private static final int COLOR_3 = 0xFFFF5722;
    private static final int COLOR_4 = 0xFFF44336;

    private final Paint[] circlePaints = new Paint[4];

    private List<Integer> dotsSizeList = new ArrayList();
    private List<Integer> dotsColorIndex = new ArrayList();
    private List<Integer> dotsRadiusRandomList = new ArrayList();
    private List<Integer> dotsRadiusList = new ArrayList();

    private int centerX;
    private int centerY;

    private float maxOuterDotsRadius;
    private int maxDotSize = 20;
    private float minDotSize = 5;
    private float currentRadius = 0;
    private float currentProgress = 0;

    public CDotsView(Context context) {
        super(context);
        init();
    }

    public CDotsView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public CDotsView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        for (int i = 0; i < circlePaints.length; i++) {
            circlePaints[i] = new Paint();
            circlePaints[i].setStyle(Paint.Style.FILL);
        }
        setDotsPaints();
        reset();
    }

    /**
     * reset dots
     */
    public void reset() {
        dotsCount = (int) Math.round(Math.random()*(DOTS_MAX_COUNT - DOTS_MIN_COUNT) + DOTS_MIN_COUNT);
        OUTER_DOTS_POSITION_ANGLE = 360 / dotsCount;
        dotsSizeList.clear();
        dotsColorIndex.clear();
        dotsRadiusRandomList.clear();
        dotsRadiusList.clear();
        for(int i = 0; i < dotsCount; i ++) {
            dotsSizeList.add(i, (int) Math.round(Math.random()*(maxDotSize - minDotSize) + minDotSize));
            dotsColorIndex.add(i, (int)Math.round(Math.random()*3));
            dotsRadiusRandomList.add(i, (int)Math.round(Math.random() * 10));
        }
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        centerX = w / 2;
        centerY = h / 2;
        maxDotSize = 20;
        maxOuterDotsRadius = w / 2 - maxDotSize * 2;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        drawDotsFrame(canvas);
    }


    private void setDotsPaints() {
        circlePaints[0].setColor(COLOR_1);
        circlePaints[1].setColor(COLOR_2);
        circlePaints[2].setColor(COLOR_3);
        circlePaints[3].setColor(COLOR_4);
    }


    private void updateDotsAlpha() {
        float progress = (float) Utils.clamp(currentProgress, 0.6f, 1f);
        int alpha = (int) Utils.mapValueFromRangeToRange(progress, 0.6f, 1f, 255, 0);
        circlePaints[0].setAlpha(alpha);
        circlePaints[1].setAlpha(alpha);
        circlePaints[2].setAlpha(alpha);
        circlePaints[3].setAlpha(alpha);
    }


    private void drawDotsFrame(Canvas canvas) {
        for (int i = 0; i < dotsCount; i++) {
            float radius = (i < dotsRadiusList.size()) ? dotsRadiusList.get(i) : currentRadius;
            int cX = (int) (centerX + radius * Math.cos(i * OUTER_DOTS_POSITION_ANGLE * Math.PI / 180));
            int cY = (int) (centerY + radius * Math.sin(i * OUTER_DOTS_POSITION_ANGLE * Math.PI / 180));
            int dotSize = (i < dotsSizeList.size()) ? dotsSizeList.get(i) : maxDotSize;
            int colorIndex = (i < dotsColorIndex.size()) ? dotsColorIndex.get(i): i % circlePaints.length;
            Log.d("setCurrentProgress", "dotSize = " + dotSize + " colorIndex = " + colorIndex + " radius = " + radius + " cx = " + cX);
            canvas.drawCircle(cX, cY, dotSize, circlePaints[colorIndex]);
        }
    }

    private void updateDotsPosition() {
        this.currentRadius = (float) Utils.mapValueFromRangeToRange(currentProgress, 0.0f, 1f, 0, maxOuterDotsRadius);
        for (int i = 0; i < dotsCount; i ++) {
            float radiusRandom = (i < dotsRadiusRandomList.size()) ? (dotsRadiusRandomList.get(i) / 10.0f) : 1;
            dotsRadiusList.add(i, Math.round(radiusRandom * currentRadius));
        }
    }


    public void setCurrentProgress(float currentProgress) {
        this.currentProgress = currentProgress;
        updateDotsPosition();
        updateDotsAlpha();
        postInvalidate();
    }

    public float getCurrentProgress() {
        return currentProgress;
    }


    public static final Property<CDotsView, Float> DOTS_PROGRESS = new Property<CDotsView, Float>(Float.class, "dotsProgress") {
        @Override
        public Float get(CDotsView object) {
            return object.getCurrentProgress();
        }

        @Override
        public void set(CDotsView object, Float value) {
            object.setCurrentProgress(value);
        }
    };
}
