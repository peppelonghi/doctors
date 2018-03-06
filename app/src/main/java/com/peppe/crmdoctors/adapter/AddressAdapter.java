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
import com.peppe.crmdoctors.model.Category;
import com.peppe.crmdoctors.util.Const;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * i
 * Created by peppe on 27/02/2018.
 */

public class AddressAdapter extends RecyclerView.Adapter<AddressAdapter.ViewHolder> {
    public interface ClickListener {
        void onClickAddress(int position);
    }


    private Context mContext;
    private JSONArray mListAddres;
    private LayoutInflater mInflater;
    public ClickListener mClickListener;


    public AddressAdapter(ClickListener clickLister, Context context, JSONArray list) {
        mClickListener = clickLister;
        mContext = context;
        mListAddres = list;
        mInflater = LayoutInflater.from(mContext);
    }

    public void setAddress(JSONArray list) {
        mListAddres = list;
        notifyDataSetChanged();
    }


    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = mInflater.inflate(R.layout.adapter, null, false);
        return new ViewHolder(view, mClickListener);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        try {
            JSONObject jsonObject = mListAddres.getJSONObject(position);
            if (jsonObject.getString(Const.CITY).equalsIgnoreCase(Const.EMPTY)) {
                holder.mContainer.setBackgroundColor(Color.WHITE);
                holder.mText.setText("Aggiungi un indirizzo");
                holder.mText.setTextColor(mContext.getResources().getColor(R.color.colorPrimaryText));
            } else {
                holder.mText.setTextColor(Color.WHITE);
                holder.mContainer.setBackground(mContext.getDrawable(R.drawable.shape));
                holder.mText.setText(jsonObject.getString(Const.STREET) + " ," + jsonObject.getString(Const.CIVIC) + "\n" + jsonObject.getString(Const.CITY));
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Override
    public int getItemCount() {
        if (mListAddres == null)
            return 0;
        else return mListAddres.length();
    }
    public void removeElement(int position) {
        mListAddres.remove(position);
        notifyItemRemoved(position);
    }

    public void addElement(JSONObject address) {
           mListAddres.put(address);
           notifyDataSetChanged();
    }
    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView mText;
        ImageButton mCloseButton;
        RelativeLayout mContainer;
        ClickListener mClickListener;

        public ViewHolder(View itemView, ClickListener clickListener) {
            super(itemView);
            mClickListener = clickListener;
            mText = itemView.findViewById(R.id.textView);
            mCloseButton = itemView.findViewById(R.id.image);
            mContainer = itemView.findViewById(R.id.container);
            mCloseButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    mClickListener.onClickAddress(getAdapterPosition());
                }
            });

        }
    }
}
