package com.peppe.crmdoctors.adapter;

import android.content.Context;
import android.graphics.Color;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.peppe.crmdoctors.R;
import com.peppe.crmdoctors.model.Category;
import com.peppe.crmdoctors.model.Doctor;
import com.peppe.crmdoctors.util.AppUtil;
import com.peppe.crmdoctors.util.Const;
import com.peppe.crmdoctors.view.RecyclerViewFastScroller;
import com.peppe.crmdoctors.view.TrapezoidView;


import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.DateFormat;
import java.text.DateFormatSymbols;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;


public class DoctorAdapter extends RecyclerView.Adapter<DoctorAdapter.ViewHolder> implements RecyclerViewFastScroller.BubbleTextGetter {
    LayoutInflater mInflater;
    Context mContext;
    ArrayList<Doctor> mDoctorList;
    ClickListener mClickListener;
    View itemView;
    ViewHolder holder;
    @Override
    public String getTextToShowInBubble(int pos) {
        return mDoctorList == null ? "" : String.valueOf(mDoctorList.get(pos).getName().charAt(0));
    }


    public interface ClickListener {
        void onClick(int position, ViewHolder holder);

        void onDoubleClick(int position, ViewHolder holder);

        void notifyHeader(ArrayList<Doctor> list);

    }

    public void setDoctors(ArrayList<Doctor> list) {
        mDoctorList = list;
        notifyDataSetChanged();
    }


    public DoctorAdapter(Context context, ArrayList<Doctor> list, ClickListener clickListener) {
        mContext = context;
        mDoctorList = list;
        mClickListener = clickListener;
        mInflater = LayoutInflater.from(mContext);

    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        itemView = mInflater.inflate(R.layout.adapter_doctor, parent, false);
        return new ViewHolder(itemView, mClickListener, mContext);
    }


    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        final Doctor doctor = mDoctorList.get(position);
        holder.mNameDoctor.setText(doctor.getSurname() + "\n" + doctor.getName());
        JSONArray jsonArray = null;
        try {
            jsonArray = new JSONArray(doctor.getListCategories());
            holder.mFirstCategory.setText("\"" + jsonArray.getJSONObject(0).getString(Category.COLUMN_CATEGORY_NAME) + "\"");
            jsonArray = new JSONArray(doctor.getListDays_Hours());
            JSONObject jsonObject = AppUtil.getCurrentAvaibility(jsonArray);
            String s = AppUtil.checkAvaibility(jsonObject);

            switch (s) {
                case Const.CLOSED:
                    holder.mDayDisp.setTextColor(Color.RED);
                    holder.mDayDisp.setText(mContext.getResources().getString(R.string.closed));
                    break;
                case Const.OPEN:
                    holder.mDayDisp.setTextColor(Color.GREEN);
                    holder.mDayDisp.setText(mContext.getResources().getString(R.string.open));
                    break;
                case Const.NOT_AVAIBLE:
                    holder.mDayDisp.setText(mContext.getString(R.string.not_avaible));
                    holder.mDayDisp.setTextColor(Color.DKGRAY);
                    holder.mDayDisp.setTextSize(10);
                    break;
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }
        holder.mNumbVisit.setText(String.valueOf(doctor.getNumbVisit()));
        holder.mRelevance.setText(String.valueOf(doctor.getRelevance()));
        String in = String.valueOf(doctor.getName().charAt(0));
        in = in + String.valueOf(doctor.getSurname().charAt(0));
        holder.mFSNDoctor.setBackground(AppUtil.getDrawableFromColor(doctor.getColor()));
        holder.mFSNDoctor.setText(in);
        holder.mFSNDoctor.setTextColor(Color.WHITE);

        // holder.mContainerInitilias.setBackgroundColor(Color.WHITE);


    }

    public void removeItem(int position) {
        mDoctorList.remove(position);
        // notify the item removed by position
        // to perform recycler view delete animations
        mClickListener.notifyHeader(mDoctorList);
        notifyItemRemoved(position);
    }

    public void restoreItem(Doctor doctor, int position) {
        mDoctorList.add(position, doctor);
        // notify item added by position
        notifyItemInserted(position);
    }

    @Override
    public int getItemCount() {
        return mDoctorList == null ? 0 : mDoctorList.size();

    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public static final String TAG = "ViewHolder";
        public final TextView mNameDoctor;
        public final TextView mFSNDoctor;
        public final TextView mRelevance;
        public final RelativeLayout mContainerInitilias;
        public final TextView mFirstCategory;
        public final TextView mNumbVisit;
        public final TextView mDayDisp;
        //  private final TrapezoidView mTrap;
        public RelativeLayout background_delete;
        public RelativeLayout background_calendar;

        public ClickListener mClickListener;
        public Context mContext;

        public ViewHolder(View itemView, ClickListener clickListener, Context context) {
            super(itemView);
            mClickListener = clickListener;
            mContext = context;
            mNameDoctor = itemView.findViewById(R.id.nameDoctor);
            mFirstCategory = itemView.findViewById(R.id.firstCategory);
            mFSNDoctor = itemView.findViewById(R.id.fschar);
            mRelevance = itemView.findViewById(R.id.relevance);
            mContainerInitilias = itemView.findViewById(R.id.containerInitials);
            mNumbVisit = itemView.findViewById(R.id.numbVisit);
            mDayDisp = itemView.findViewById(R.id.dayDisp);
            background_delete = itemView.findViewById(R.id.background_delete);
            background_calendar = itemView.findViewById(R.id.background_calendar);
            final GestureDetector gestureDetector;
            final GestureListener gestureListener = new GestureListener(mClickListener, this);
            gestureDetector = new GestureDetector(mContext, gestureListener);
            //  mTrap = itemView.findViewById(R.id.transparent_view);
            mContainerInitilias.setOnTouchListener(new View.OnTouchListener() {
                @Override
                public boolean onTouch(View view, MotionEvent motionEvent) {
                    gestureListener.setmIndex(getAdapterPosition());
                    return gestureDetector.onTouchEvent(motionEvent);
                }
            });
//            mTrap.setOnTouchListener(new View.OnTouchListener() {
//                @Override
//                public boolean onTouch(View view, MotionEvent motionEvent) {
//                    gestureListener.setmIndex(getAdapterPosition());
//                    return gestureDetector.onTouchEvent(motionEvent);
//                }
//            });
        }
    }

    private static final String TAG = "DoctorAdapter";

    private static class GestureListener extends GestureDetector.SimpleOnGestureListener {
        int mIndex = 10;
        ClickListener mClickListener;
        ViewHolder mItemView;

        public GestureListener(ClickListener clickListener, ViewHolder itemView) {
            super();
            mClickListener = clickListener;
            mItemView = itemView;

        }

        public void setmIndex(int mIndex) {
            this.mIndex = mIndex;
        }

        @Override
        public boolean onDown(MotionEvent e) {
            return true;
        }

        @Override
        public boolean onSingleTapConfirmed(MotionEvent e) {
            mClickListener.onClick(mIndex, mItemView);
            return super.onSingleTapConfirmed(e);
        }

        // event when double tap occurs
        @Override
        public boolean onDoubleTap(MotionEvent e) {
            mClickListener.onDoubleClick(mIndex, mItemView);
            return true;
        }

    }
}