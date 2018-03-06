package com.peppe.crmdoctors.activity;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Window;
import android.widget.LinearLayout;

import com.peppe.crmdoctors.R;
import com.peppe.crmdoctors.util.Const;

public class FilterActivity extends AppCompatActivity {
   LinearLayout mContainer;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_filter);
        mContainer = findViewById(R.id.container);
        mContainer.animate()
                .translationY(mContainer.getHeight())
                .alpha(1.0f)
                .setListener(null);

    }

    @Override
    public void onBackPressed() {
         Intent returnIntent = new Intent();
        setResult(Activity.RESULT_CANCELED, returnIntent);
        finish();
    }
}
