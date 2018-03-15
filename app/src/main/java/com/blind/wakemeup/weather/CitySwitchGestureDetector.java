package com.blind.wakemeup.weather;

import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ViewFlipper;

import com.blind.wakemeup.R;

public class CitySwitchGestureDetector extends GestureDetector.SimpleOnGestureListener {

    private final Animation slide_in_left;
    private final Animation slide_in_right;
    private final Animation slide_out_left;
    private final Animation slide_out_right;

    private ViewFlipper view;
    public CitySwitchGestureDetector(ViewFlipper view) {
        this.view = view;
        slide_in_left = AnimationUtils.loadAnimation(view.getContext(), R.anim.slide_in_left);
        slide_in_right = AnimationUtils.loadAnimation(view.getContext(), R.anim.slide_in_right);
        slide_out_left = AnimationUtils.loadAnimation(view.getContext(), R.anim.slide_out_left);
        slide_out_right = AnimationUtils.loadAnimation(view.getContext(), R.anim.slide_out_right);
    }

    @Override
    public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {

        // Swipe left (next)
        if (e1.getX() > e2.getX()) {
            view.setInAnimation(slide_in_right);
            view.setOutAnimation(slide_out_left);
            view.showNext();
        }

        // Swipe right (previous)
        if (e1.getX() < e2.getX()) {
            view.setInAnimation(slide_in_left);
            view.setOutAnimation(slide_out_right);
            view.showPrevious();
        }

        return super.onFling(e1, e2, velocityX, velocityY);
    }
}