package com.peppe.crmdoctors.view;

/**
 * Created by peppe on 03/03/2018.
 */

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.drawable.ShapeDrawable;
import android.graphics.drawable.shapes.PathShape;
import android.util.AttributeSet;
import android.view.View;

import com.peppe.crmdoctors.R;

public class TrapezoidView extends View {

    private ShapeDrawable mTrapezoid;

    public TrapezoidView(Context context, AttributeSet attrs) {
        super(context, attrs);

        Path path = new Path();
        path.moveTo(8.0f, 0.0f);
        path.lineTo(80.0f, 0.0f);
        path.lineTo(200.0f, 100.0f);
        path.lineTo(0.0f, 100.0f);
        path.lineTo(0.0f, 0.0f);
        mTrapezoid = new ShapeDrawable(new PathShape(path, 200.0f, 100.0f));
        mTrapezoid.getPaint().setStyle(Paint.Style.FILL_AND_STROKE);
        mTrapezoid.getPaint().setColor(Color.WHITE);
    }

   public void setTrapColor(String color){
        mTrapezoid.getPaint().setColor(Color.parseColor(color));
        invalidate();
   }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        mTrapezoid.setBounds(0, 0, w, h);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        mTrapezoid.draw(canvas);
    }
}