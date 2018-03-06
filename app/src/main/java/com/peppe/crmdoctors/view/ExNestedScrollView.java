package com.peppe.crmdoctors.view;

import android.content.Context;
import android.support.v4.widget.NestedScrollView;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;

/**
 * Created by peppe on 01/03/2018.
 */

public class ExNestedScrollView extends NestedScrollView
{


    public void setGoogleMap(GoogleMap googleMap){
        mapView = googleMap;
    }

    public GoogleMap mapView;

    public ExNestedScrollView(Context context, AttributeSet attrs)
    {
        super(context, attrs);
    }



    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev)
    {
        if (mapView == null)
            return super.onInterceptTouchEvent(ev);

//        if (inRegion(ev.getRawX(), ev.getRawY(), mapView.get))
//            return false;
//
      return super.onInterceptTouchEvent(ev);
    }

    private boolean inRegion(float x, float y, View v)
    {
        int[] mCoordBuffer = new int[]
                { 0, 0 };

        v.getLocationOnScreen(mCoordBuffer);

        return mCoordBuffer[0] + v.getWidth() > x && // right edge
                mCoordBuffer[1] + v.getHeight() > y && // bottom edge
                mCoordBuffer[0] < x && // left edge
                mCoordBuffer[1] < y; // top edge
    }
}
