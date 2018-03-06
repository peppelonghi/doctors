package com.peppe.crmdoctors.adapter;

import android.content.Context;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.peppe.crmdoctors.R;
import com.peppe.crmdoctors.activity.DetailsActivity;
import com.peppe.crmdoctors.util.AppUtil;
import com.peppe.crmdoctors.util.Const;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.PipedOutputStream;
import java.util.ArrayList;
import java.util.Calendar;

/**
 * Created by peppe on 25/02/2018.
 */

public class Day_HourAdapter extends RecyclerView.Adapter<Day_HourAdapter.ViewHolder> {

    private Context mContext;
    private JSONArray mListDays_Hours;
    private LayoutInflater mInflater;
    private String mCurrentDay;
    private ClickListener mClickListener;

    public interface ClickListener {
        void onClickDay_Hour(int position);
    }

    public Day_HourAdapter(ClickListener clickListener, Context context, JSONArray list, String currentDay) {
        mContext = context;
        mClickListener = clickListener;
        mListDays_Hours = list;
        mInflater = LayoutInflater.from(mContext);
        mCurrentDay = currentDay;
    }

    public void setData(JSONArray list) {
        mListDays_Hours = list;
        notifyDataSetChanged();
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = mInflater.inflate(R.layout.adapter_hour_day, parent, false);
        return new ViewHolder(itemView, mClickListener);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        try {
            final JSONObject jsonObject = mListDays_Hours.getJSONObject(position);

            holder.mDay.setText(jsonObject.getString(Const.DAY));
            if (jsonObject.getString(Const.MORNING + Const.OPEN).equalsIgnoreCase(Const.DEFAULT_HOUR) )
                holder.mMorning.setText(mContext.getString(R.string.not_avaible));
            else
                holder.mMorning.setText("dalle: " + jsonObject.getString(Const.MORNING + Const.OPEN) + " alle: " + jsonObject.getString(Const.MORNING + Const.CLOSED));
            if (jsonObject.getString(Const.AFTERNOON + Const.OPEN).equalsIgnoreCase(Const.DEFAULT_HOUR) )
                holder.mAfternoon.setText(mContext.getString(R.string.not_avaible));
            else
                holder.mAfternoon.setText("dalle: " + jsonObject.getString(Const.AFTERNOON + Const.OPEN) + " alle: " + jsonObject.getString(Const.AFTERNOON + Const.CLOSED));

            if (mCurrentDay.equalsIgnoreCase(jsonObject.getString(Const.DAY))) {
                holder.mContainer.setBackground(mContext.getDrawable(R.drawable.shape));
//                holder.mDay.setTextColor(mContext.getResources().getColor(R.color.colorPrimary));
             } else {
                holder.mContainer.setBackground(mContext.getDrawable(R.drawable.shape_black));
//                holder.mDay.setTextColor(mContext.getResources().getColor(R.color.colorPrimaryText));
             }


            if (position != mListDays_Hours.length() - 1 && !mCurrentDay.equalsIgnoreCase(jsonObject.getString(Const.DAY))) {
                holder.mLineEnd.setVisibility(View.VISIBLE);
            } else {
                holder.mLineEnd.setVisibility(View.GONE);
            }


        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private static final String TAG = "Day_HourAdapter";

    @Override
    public int getItemCount() {
        if (mListDays_Hours == null)
            return 0;
        else return mListDays_Hours.length();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        View mLineStart;
        View mLineEnd;
        private TextView mDay;
        private TextView mMorning;
        private TextView mAfternoon;
        RelativeLayout mContainer;
         private ClickListener mClickListener;

        public ViewHolder(View itemview, final ClickListener clickListener) {
            super(itemview);
            mClickListener = clickListener;
            // mLineStart = itemview.findViewById(R.id.lineStart);
            mLineEnd = itemview.findViewById(R.id.lineEnd);

             mDay = itemview.findViewById(R.id.day);
            mMorning = itemview.findViewById(R.id.valueMorning);
            mAfternoon = itemview.findViewById(R.id.valueAfternoon);
            mContainer = itemview.findViewById(R.id.container);
            mContainer.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (clickListener != null)
                        mClickListener.onClickDay_Hour(getAdapterPosition());
                }
            });
        }
    }
}
