package com.dotsanimation;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.annotation.TargetApi;
import android.content.Context;
import android.os.Build;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.DecelerateInterpolator;
import android.view.animation.LinearInterpolator;
import android.widget.FrameLayout;
import android.widget.ImageView;

/**
 * Created by timothyhe on 2017/10/12.
 */


public class SigninButtonView extends FrameLayout implements View.OnClickListener {

    private static final AccelerateDecelerateInterpolator ACCELERATE_DECELERATE_INTERPOLATOR = new AccelerateDecelerateInterpolator();
    private static final DecelerateInterpolator DECCELERATE_INTERPOLATOR = new DecelerateInterpolator();
    private static final LinearInterpolator LINEAR_INTERPOLATOR = new LinearInterpolator();
    private static final AccelerateInterpolator ACCELERATE_INTERPOLATOR = new AccelerateInterpolator();

    private AnimatorSet animatorSet;
    private ImageView ivStar;
    private CDotsView vDotsView;
    private int starStartX;
    private int starStartY;
    private int starEndX;
    private int starEndY;


    public SigninButtonView(Context context) {
        super(context);
        init();
    }

    public SigninButtonView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public SigninButtonView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public SigninButtonView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init();
    }

    private void init() {
        LayoutInflater.from(getContext()).inflate(R.layout.view_signin_button, this, true);
        ivStar = (ImageView)findViewById(R.id.ivStar);
        vDotsView = (CDotsView) findViewById(R.id.vDotsView);
        ivStar.setOnClickListener(this);
        setOnClickListener(this);
        int[] location = new int[2];
        ivStar.getLocationInWindow(location);
        starStartX = location[0];
        starStartY = location[1];
        starEndX = starStartX + 400;
        starEndY = starStartY + 800;
    }


    @Override
    public void onClick(View v) {
        if (animatorSet != null) {
            animatorSet.cancel();
        }

        animatorSet = new AnimatorSet();

        ObjectAnimator starAnimator = ObjectAnimator.ofFloat(ivStar, "rotationY", 0, 180, 0);
        starAnimator.setDuration(1200);
        starAnimator.setStartDelay(50);
        starAnimator.setInterpolator(DECCELERATE_INTERPOLATOR);


        ObjectAnimator dotsAnimator = ObjectAnimator.ofFloat(vDotsView, CDotsView.DOTS_PROGRESS, 0, 1f);
        dotsAnimator.setDuration(1200);
        dotsAnimator.setStartDelay(50);
        dotsAnimator.setInterpolator(ACCELERATE_DECELERATE_INTERPOLATOR);
        vDotsView.reset();


        ObjectAnimator starXAnimator = ObjectAnimator.ofFloat(ivStar, "translationX", 0, starEndX - starStartX);
        starXAnimator.setDuration(1000);
        starXAnimator.setInterpolator(LINEAR_INTERPOLATOR);

        ObjectAnimator starYAnimator = ObjectAnimator.ofFloat(ivStar, "translationY", 0, starEndY - starStartY);
        starYAnimator.setDuration(1000);
        starYAnimator.setInterpolator(ACCELERATE_INTERPOLATOR);

        ObjectAnimator scaleX = ObjectAnimator.ofFloat(ivStar, "scaleX", 1, 0);
        scaleX.setDuration(1000);
        ObjectAnimator scaleY = ObjectAnimator.ofFloat(ivStar, "scaleY", 1, 0);
        scaleY.setDuration(1000);

        animatorSet.playTogether(
                starAnimator,
                dotsAnimator
        );

        animatorSet.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationCancel(Animator animation) {
                vDotsView.setCurrentProgress(0);
                ivStar.setScaleX(1);
                ivStar.setScaleY(1);
            }
        });

        animatorSet.play(starXAnimator).with(starYAnimator).with(scaleX).with(scaleY).after(dotsAnimator);
        animatorSet.start();

    }
}
