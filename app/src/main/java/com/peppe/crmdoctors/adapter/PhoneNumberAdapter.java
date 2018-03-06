package com.peppe.crmdoctors.adapter;

import android.content.Context;
import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.peppe.crmdoctors.R;
import com.peppe.crmdoctors.util.Const;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by peppe on 26/02/2018.
 */

public class PhoneNumberAdapter extends RecyclerView.Adapter<PhoneNumberAdapter.ViewHolder> {
    public JSONArray mListNumber;
    private Context mContext;
    private LayoutInflater mInflater;
    private ClickListener mClickListern;
    public int mWhichLayout;


    public interface ClickListener {
        void onClickPhoneAdapter(int position, int what);
    }


    public JSONArray getListNumber() {
        return mListNumber;
    }


    public PhoneNumberAdapter(ClickListener clickListener, Context context, JSONArray list, int whichLayout) {
        mListNumber = list;
        mContext = context;
        mInflater = LayoutInflater.from(mContext);
        mClickListern = clickListener;
        mWhichLayout = whichLayout;
    }

    public void setPhoneList(JSONArray list) {
        mListNumber = list;
        notifyDataSetChanged();
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = mInflater.inflate(R.layout.adapter, null, false);
        return new ViewHolder(view, mClickListern, mWhichLayout);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {
        final JSONObject numberPhone;
        try {
            numberPhone = mListNumber.getJSONObject(position);
            if (mWhichLayout == Const.DETAILS_ACTIVITY) {
                setLayoutDetailsActivity(holder, numberPhone.getString(Const.PHONE));
            } else {
                setLayoutAddModifyActivity(holder, numberPhone.getString(Const.PHONE));
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void setLayoutDetailsActivity(ViewHolder holder, String numberPhone) {
        holder.mText.setText(numberPhone);
        holder.mContainer.setBackgroundColor(Color.WHITE);
        holder.mText.setTextColor(mContext.getResources().getColor(R.color.colorSecondaryText));
        holder.mImage.setImageDrawable(mContext.getResources().getDrawable(R.drawable.phone_out));
        holder.mText.setTextIsSelectable(true);
        holder.mText.setTextSize(18);

    }

    public void setLayoutAddModifyActivity(ViewHolder holder, String numberPhone) {
        if (numberPhone.equalsIgnoreCase(Const.EMPTY)) {
            holder.mText.setText("Aggiungi un numero di telefono");
            holder.mContainer.setBackgroundColor(Color.WHITE);
            holder.mText.setTextColor(mContext.getResources().getColor(R.color.colorSecondaryText));
            holder.mImage.setVisibility(View.GONE);
        } else {
            holder.mContainer.setBackground(mContext.getDrawable(R.drawable.shape));
            holder.mText.setTextColor(Color.WHITE);
            holder.mImage.setVisibility(View.VISIBLE);
            holder.mText.setText(numberPhone);

        }
    }

    public void removeElement(int position) {

        mListNumber.remove(position);
        notifyItemRemoved(position);
    }

    public void addElement(String phone) {
        JSONObject jsonObject = new JSONObject();
        try {

            jsonObject.put(Const.PHONE, phone);
            mListNumber.put(jsonObject);

            notifyDataSetChanged();
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Override
    public int getItemCount() {
        if (mListNumber == null)
            return 0;
        else return mListNumber.length();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        TextView mText;
        ImageButton mImage;
        RelativeLayout mContainer;
        private ClickListener mClickListener;
       int mWhichLayout;
        public ViewHolder(View itemView, ClickListener clickListern, final int whichLayout) {
            super(itemView);
            mWhichLayout = whichLayout;
            mClickListener = clickListern;
            mText = itemView.findViewById(R.id.textView);
            mImage = itemView.findViewById(R.id.image);
            mContainer = itemView.findViewById(R.id.container);
            mImage.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    mClickListener.onClickPhoneAdapter(getAdapterPosition(), 0);
                }
            });
            mText.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (whichLayout == Const.ADD_MODOFY_ACTIVITY) {
                        if (getAdapterPosition() != 0)
                            mClickListener.onClickPhoneAdapter(getAdapterPosition(), 1);
                    }
                    else {
                        mClickListener.onClickPhoneAdapter(getAdapterPosition(), 1);

                    }
                }
            });
        }
    }
}
