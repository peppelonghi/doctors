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
 * Created by peppe on 26/02/2018.
 */

public class CategoryAdapter extends RecyclerView.Adapter<CategoryAdapter.ViewHolder> {
    private Context mContext;
    private LayoutInflater mInflater;
    private JSONArray categories;
    private ClickListener mClickListener;


    public interface ClickListener {
        void onClickCategory(int position);
    }

    public JSONArray getCategories(){
        return categories;
    }

    public CategoryAdapter(ClickListener clickListener, Context context, JSONArray list) {
        mContext = context;
        mInflater = LayoutInflater.from(mContext);
        categories = list;
        mClickListener = clickListener;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = mInflater.inflate(R.layout.adapter, null, false);
        return new ViewHolder(view, mClickListener);//, mClickListener);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {
        try {
            JSONObject jsonObject = categories.getJSONObject(position);
            if (jsonObject.getString(Category.COLUMN_CATEGORY_NAME).equalsIgnoreCase(Const.EMPTY)) {
                holder.mContainer.setBackgroundColor(Color.WHITE);
                holder.mNameCategory.setText("Aggiungi una specializzazione");
                holder.mNameCategory.setTextColor(mContext.getResources().getColor(R.color.colorPrimaryText));
            } else {
                holder.mNameCategory.setTextColor(Color.WHITE);
                holder.mContainer.setBackground(mContext.getDrawable(R.drawable.shape));
                holder.mNameCategory.setText(jsonObject.getString(Category.COLUMN_CATEGORY_NAME));
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Override
    public int getItemCount() {
        if (categories == null)
            return 0;
        else return categories.length();
    }

    public void setCategory(JSONArray docCategory) {
        categories = docCategory;
        notifyDataSetChanged();
    }


    public void removeElement(int position) {
        categories.remove(position);
        notifyItemRemoved(position);

    }

    public void addElement(Category category) {
        try {
            JSONObject jsonObject = new JSONObject();
            jsonObject.put(Category.COLUMN_CATEGORY_NAME, category.getName());
            jsonObject.put(Category.COLUMN_ID, category.getId());
            categories.put(jsonObject);
            notifyDataSetChanged();
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }


    static class ViewHolder extends RecyclerView.ViewHolder {
        TextView mNameCategory;
        ImageButton mCloseButton;
        RelativeLayout mContainer;
        ClickListener mClickListener;

        public ViewHolder(View itemView, ClickListener clickListener) {//, ClickListener clickListener) {
            super(itemView);
            mClickListener = clickListener;
            mNameCategory = itemView.findViewById(R.id.textView);
            mCloseButton = itemView.findViewById(R.id.image);
            mContainer = itemView.findViewById(R.id.container);
            mCloseButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    mClickListener.onClickCategory(getAdapterPosition());
                }
            });
        }
    }
}
