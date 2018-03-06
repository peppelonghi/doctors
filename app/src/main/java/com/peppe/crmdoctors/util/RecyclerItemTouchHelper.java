package com.peppe.crmdoctors.util;

import android.graphics.Canvas;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.util.Log;
import android.view.View;

import com.peppe.crmdoctors.adapter.DoctorAdapter;
import com.peppe.crmdoctors.model.Doctor;

import static android.content.ContentValues.TAG;

/**
 * Created by peppe on 24/02/2018.
 */

public class RecyclerItemTouchHelper extends ItemTouchHelper.SimpleCallback {
    private final int mSwypeDirs;
    private RecyclerItemTouchHelperListener listener;
    View background_delete;
    View background_calendar;

    public RecyclerItemTouchHelper(int dragDirs, int swipeDirs, RecyclerItemTouchHelperListener listener) {
        super(dragDirs, swipeDirs);
        this.listener = listener;
        mSwypeDirs = swipeDirs;
    }

    @Override
    public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {
        return true;
    }

    @Override
    public void onSelectedChanged(RecyclerView.ViewHolder viewHolder, int actionState) {
        if (viewHolder != null) {
            background_delete = ((DoctorAdapter.ViewHolder) viewHolder).background_delete;
            background_calendar = ((DoctorAdapter.ViewHolder) viewHolder).background_calendar;

            final View foregroundView = ((DoctorAdapter.ViewHolder) viewHolder).mContainerInitilias;

            getDefaultUIUtil().onSelected(foregroundView);
        }
    }


    @Override
    public void onChildDrawOver(Canvas c, RecyclerView recyclerView,
                                RecyclerView.ViewHolder viewHolder, float dX, float dY,
                                int actionState, boolean isCurrentlyActive) {
        Log.d(TAG, "onChildDrawOver: ");
        final View foregroundView = ((DoctorAdapter.ViewHolder) viewHolder).mContainerInitilias;
        getDefaultUIUtil().onDrawOver(c, recyclerView, foregroundView, dX, dY,
                actionState, isCurrentlyActive);
    }

    @Override
    public void clearView(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder) {
        final View foregroundView = ((DoctorAdapter.ViewHolder) viewHolder).mContainerInitilias;
        getDefaultUIUtil().clearView(foregroundView);
    }

    @Override
    public void onChildDraw(Canvas c, RecyclerView recyclerView,
                            RecyclerView.ViewHolder viewHolder, float dX, float dY,
                            int actionState, boolean isCurrentlyActive) {
        final View foregroundView = ((DoctorAdapter.ViewHolder) viewHolder).mContainerInitilias;
        if (dX< 0) {
            background_calendar.setVisibility(View.GONE);
            background_delete.setVisibility(View.VISIBLE);

        } else {
            background_calendar.setVisibility(View.VISIBLE);
            background_delete.setVisibility(View.GONE);
        }
        getDefaultUIUtil().onDraw(c, recyclerView, foregroundView, dX, dY,
                actionState, isCurrentlyActive);
    }

    @Override
    public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction) {

        listener.onSwiped(viewHolder, direction, viewHolder.getAdapterPosition());


    }

    @Override
    public int convertToAbsoluteDirection(int flags, int layoutDirection) {
        return super.convertToAbsoluteDirection(flags, layoutDirection);
    }

    public interface RecyclerItemTouchHelperListener {
        void onSwiped(RecyclerView.ViewHolder viewHolder, int direction, int position);
    }
}