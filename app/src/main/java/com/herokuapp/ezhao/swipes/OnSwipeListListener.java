package com.herokuapp.ezhao.swipes;

import android.view.MotionEvent;
import android.view.View;
import android.view.ViewParent;

public class OnSwipeListListener implements View.OnTouchListener {
    float startX;
    boolean active;

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        ViewParent viewParent = v.getParent();
        int action = event.getActionMasked();
        switch (action) {
            case MotionEvent.ACTION_DOWN:
                startX = event.getX();
                active = true;
                viewParent.requestDisallowInterceptTouchEvent(true);
                break;
            case MotionEvent.ACTION_MOVE:
                if (active) {
                    float newX = event.getX();
                    onMove(startX, newX);
                    startX = newX;
                }
                break;
            case MotionEvent.ACTION_UP:
            case MotionEvent.ACTION_CANCEL:
                active = false;
                onRelease();
                break;
        }
        return true;
    }

    public void onMove(float start, float end) { }

    public void onRelease() { }
}
