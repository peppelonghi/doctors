package com.peppe.crmdoctors.activity;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.IntRange;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.Html;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.beloo.widget.chipslayoutmanager.ChipsLayoutManager;
import com.beloo.widget.chipslayoutmanager.SpacingItemDecoration;
import com.beloo.widget.chipslayoutmanager.gravity.IChildGravityResolver;
import com.beloo.widget.chipslayoutmanager.layouter.breaker.IRowBreaker;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.UiSettings;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.peppe.crmdoctors.R;
import com.peppe.crmdoctors.adapter.Day_HourAdapter;
import com.peppe.crmdoctors.adapter.PhoneNumberAdapter;
import com.peppe.crmdoctors.model.Category;
import com.peppe.crmdoctors.model.Doctor;
import com.peppe.crmdoctors.util.AppUtil;
import com.peppe.crmdoctors.util.Const;
import com.peppe.crmdoctors.view.CustomInfoWindowAdapter;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.DateFormat;
import java.text.DateFormatSymbols;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;


public class DetailsActivity extends AppCompatActivity implements OnMapReadyCallback, PhoneNumberAdapter.ClickListener {
    private static final int LOADER_CATEGORY = 1;

    Doctor mDoctor;
    private TextView mNameDoctor;
    private TextView mCategory;
    private RelativeLayout mContainerOval;
    private TextView mFSNDoctor;
    private GoogleMap mMap;
    private ImageView mModify;
    private ImageView mBack;
    private RecyclerView mRecyclerViewPhoneNumbers;
    private RecyclerView mRecyclerViewDays_Hours;
    private DetailsActivity detailsActivity;
    private TextView mNote;
    private JSONArray jsonArray;
    private TextView mRelevance;
    private TextView mNumbVisit;
    private TextView mDayDisp;
    private RelativeLayout mContainerOval2;
    private TextView mLastVisit;
    private TextView mAppointment;
    private TextView mFirstCategory;
    private CardView mCardContainerMap;
    private ScrollView mScrollView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        detailsActivity = this;
        //try {
        setContentView(R.layout.activity_details);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        if (getIntent().hasExtra(Const.DOCTOR)) {
            mDoctor = getIntent().getParcelableExtra(Const.DOCTOR);
            findViewById();
            setView();

        }

    }


    private static final String TAG = "DetailsActivity";

    private void findViewById() {
        mBack = findViewById(R.id.back);
        mScrollView = findViewById(R.id.scrollView);
        mModify = findViewById(R.id.menu);
        mNameDoctor = findViewById(R.id.nameDoctor);
        mFSNDoctor = findViewById(R.id.fschar);
        mContainerOval = findViewById(R.id.containerOval);
        mFirstCategory = findViewById(R.id.category);
        mRelevance = findViewById(R.id.relevance);
        mNumbVisit = findViewById(R.id.numbVisit);
        mDayDisp = findViewById(R.id.dayDisp);
        mLastVisit = findViewById(R.id.lastVisit);
        mAppointment = findViewById(R.id.nextAppointment);
        mNote = findViewById(R.id.note);
        mCategory = findViewById(R.id.categories);
        mRecyclerViewPhoneNumbers = findViewById(R.id.recyclerViewPhoneNumbers);
        mRecyclerViewDays_Hours = findViewById(R.id.recyclerViewAvailability);
        mCardContainerMap = findViewById(R.id.cardContainerMap);
     }

    private void setView() {
        try {
            mBack.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    onBackPressed();
                }
            });
            mModify.setImageDrawable(getResources().getDrawable(R.drawable.pen));
            mModify.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(getApplicationContext(), AddModifyActivity.class);
                    intent.putExtra(Const.DOCTOR, mDoctor);
                    startActivity(intent);
                    finish();
                }
            });
            String in = String.valueOf(mDoctor.getName().charAt(0));
            in = in + String.valueOf(mDoctor.getSurname().charAt(0));
            mFSNDoctor.setText(in);
            mFSNDoctor.setBackground(AppUtil.getDrawableFromColor(mDoctor.getColor()));

            mNameDoctor.setText(mDoctor.getSurname() + " " + mDoctor.getName());
            //mContainerOval.setBackground(AppUtil.getDrawableFromColor(mDoctor.getColor()));
            jsonArray = new JSONArray(mDoctor.getListCategories());
            mFirstCategory.setText(jsonArray.getJSONObject(0).getString(Category.COLUMN_CATEGORY_NAME));
            JSONObject jsonObject;
            for (int i = 0; i < jsonArray.length(); i++) {
                jsonObject = jsonArray.getJSONObject(i);
                if (i == 0)
                    mCategory.setText(jsonObject.getString(Category.COLUMN_CATEGORY_NAME));
                else
                    mCategory.setText(mCategory.getText() + ", " + jsonObject.getString(Category.COLUMN_CATEGORY_NAME));
            }
            mRelevance.setText(String.valueOf(mDoctor.getRelevance()));
            mNumbVisit.setText(String.valueOf(mDoctor.getNumbVisit()));
            jsonArray = new JSONArray(mDoctor.getListDays_Hours());
            jsonObject = AppUtil.getCurrentAvaibility(jsonArray);
            String s = AppUtil.checkAvaibility(jsonObject);
            switch (s) {
                case Const.CLOSED:
                    mDayDisp.setTextColor(Color.RED);
                    mDayDisp.setText(getResources().getString(R.string.closed));
                    break;
                case Const.OPEN:
                    mDayDisp.setTextColor(Color.GREEN);
                    mDayDisp.setText(getResources().getString(R.string.open));
                    break;
                case Const.NOT_AVAIBLE:
                    mDayDisp.setText(getString(R.string.not_avaible));
                    mDayDisp.setTextColor(Color.DKGRAY);
                    break;
            }

            if (mDoctor.getLast_visit() != null) {
                mLastVisit.setText(Html.fromHtml("<b><u>Ultima visita effettuata: </b></u>    <br><br>   " + mDoctor.getLast_visit()));
            } else {
                mLastVisit.setText(Html.fromHtml("<b><u>Ultima visita effettuata: </b></u>    <br><br>   Nessuna "));

            }

            if (mDoctor.getAppointment() != null) {
                jsonObject = new JSONObject(mDoctor.getAppointment());

                String date =  "Giorno " + jsonObject.getString(Const.DAY) + "/" + jsonObject.getString(Const.MONTH) + "/" + jsonObject.getString(Const.YEAR) + " alle ore: " + jsonObject.getString(Const.HOUR) + ":" + jsonObject.getString(Const.MINUTE);

                mAppointment.setText(Html.fromHtml("<b><u>Prossimo appuntamento </b></u>:  <br><br>"+date));

             } else {
                mAppointment.setText(Html.fromHtml("<b><u>Prossimo appuntamento: </b></u>   <br><br>     Nessuno"));

            }
            if (mDoctor.getNote() != null) {
                mNote.setText(Html.fromHtml("<b><u>Note: </b></u>   <br><br>" + mDoctor.getNote()));
            } else {
                mNote.setText(Html.fromHtml("<b><u>Note: </b></u>   <br><br> " + "Nessuna nota"));

            }


            jsonArray = new JSONArray(mDoctor.getPhoneList());
            PhoneNumberAdapter phoneNumberAdapter = new PhoneNumberAdapter(this, getApplicationContext(), jsonArray, Const.DETAILS_ACTIVITY);
            ChipsLayoutManager chipsLayoutManagerAddress = ChipsLayoutManager.newBuilder(getApplicationContext())
                    //set vertical gravity for all items in a row. Default = Gravity.CENTER_VERTICAL
                    .setChildGravity(Gravity.TOP)
                    //whether RecyclerView can scroll. TRUE by default
                    .setScrollingEnabled(true)
                    //set maximum views count in a particular row
                    .setMaxViewsInRow(2)
                    //set gravity resolver where you can determine gravity for item in position.
                    //This method have priority over previous one
                    .setGravityResolver(new IChildGravityResolver() {
                        @Override
                        public int getItemGravity(int position) {
                            return Gravity.CENTER;
                        }
                    })
                    //you are able to break row due to your conditions. Row breaker should return true for that views
                    .setRowBreaker(new IRowBreaker() {
                        @Override
                        public boolean isItemBreakRow(@IntRange(from = 0) int position) {
                            return position == 6 || position == 11 || position == 2;
                        }
                    })
                    //a layoutOrientation of layout manager, could be VERTICAL OR HORIZONTAL. HORIZONTAL by default
                    .setOrientation(ChipsLayoutManager.HORIZONTAL)
                    // row strategy for views in completed row, could be STRATEGY_DEFAULT, STRATEGY_FILL_VIEW,
                    //STRATEGY_FILL_SPACE or STRATEGY_CENTER
                    .setRowStrategy(ChipsLayoutManager.STRATEGY_FILL_SPACE)
                    // whether strategy is applied to last row. FALSE by default
                    .withLastRow(true)
                    .build();

            mRecyclerViewPhoneNumbers.setLayoutManager(chipsLayoutManagerAddress);
            mRecyclerViewPhoneNumbers.addItemDecoration(new SpacingItemDecoration(getResources().getDimensionPixelOffset(R.dimen.item_space),
                    getResources().getDimensionPixelOffset(R.dimen.item_space)));
            mRecyclerViewPhoneNumbers.setAdapter(phoneNumberAdapter);
            jsonArray = new JSONArray(mDoctor.getListDays_Hours());
            Calendar c = Calendar.getInstance();
            int dayOfWeek = c.get(Calendar.DAY_OF_WEEK);
            DateFormatSymbols dfs = new DateFormatSymbols(new Locale("it"));
            String weekdays[] = dfs.getWeekdays();
            Day_HourAdapter day_hourAdapter = new Day_HourAdapter(null, getApplicationContext(), jsonArray, weekdays[dayOfWeek]);
            mRecyclerViewDays_Hours.setLayoutManager(new LinearLayoutManager(getApplicationContext(), LinearLayoutManager.HORIZONTAL, false));
            mRecyclerViewDays_Hours.setAdapter(day_hourAdapter);
            day_hourAdapter.setData(jsonArray);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        mCardContainerMap.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                return false;
            }
        });

    }


    @Override
    public void onMapReady(GoogleMap googleMap) {
        try {

            mMap = googleMap;
            // Add a marker in Sydney and move the camera
            LatLng pos = new LatLng(-34, 151);
            jsonArray = new JSONArray(mDoctor.getListAddresses());
            for (int i = 0; i < jsonArray.length(); i++) {
                Double latitude = Double.parseDouble(jsonArray.getJSONObject(i).getString(Const.LAT));
                Double longitude = Double.parseDouble(jsonArray.getJSONObject(i).getString(Const.LONG));
                pos = new LatLng(latitude, longitude);
                MarkerOptions marker = new MarkerOptions().position(pos).title("" + jsonArray.getJSONObject(i).getString(Const.STREET) + "," +
                        jsonArray.getJSONObject(i).getString(Const.CIVIC) + "");
                mMap.addMarker(marker);
                //CustomInfoWindowAdapter adapter = new CustomInfoWindowAdapter(DetailsActivity.this);
                //  mMap.setInfoWindowAdapter(adapter);

                mMap.addMarker(marker).showInfoWindow();

            }
            //  mMap.moveCamera(CameraUpdateFactory.newLatLng(pos));

            UiSettings mUiSettings = mMap.getUiSettings();

            // Keep the UI Settings state in sync with the checkboxes.
            mUiSettings.setZoomControlsEnabled(true);
            mUiSettings.setCompassEnabled(true);
            mUiSettings.setMyLocationButtonEnabled(true);
            mUiSettings.setScrollGesturesEnabled(true);
            mUiSettings.setZoomGesturesEnabled(true);
            mUiSettings.setTiltGesturesEnabled(true);
            mUiSettings.setRotateGesturesEnabled(true);
            mMap.animateCamera(CameraUpdateFactory.zoomTo(10.0f));
            mMap.setOnMapClickListener(new GoogleMap.OnMapClickListener() {

                @Override
                public void onMapClick(LatLng arg0) {
                    mScrollView.requestDisallowInterceptTouchEvent(true);

                }
            });

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }


    @Override
    public void onClickPhoneAdapter(int position, int what) {
        try {
            jsonArray = new JSONArray(mDoctor.getPhoneList());
            showAlertOpenPhone(jsonArray.getJSONObject(position).getString(Const.PHONE));

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void showAlertOpenPhone(final String phone) {
        AlertDialog.Builder builder1 = new AlertDialog.Builder(detailsActivity);
        builder1.setCancelable(true);
        builder1.setTitle("Vuoi chiamare il num: " + phone);
        builder1.setPositiveButton(
                getApplicationContext().getResources().getString(R.string.ok),
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        Intent intent = new Intent(Intent.ACTION_DIAL);
                        intent.setData(Uri.parse("tel:" + phone));
                        startActivity(intent);
                        dialog.cancel();

                    }
                });

        builder1.setNegativeButton(
                getApplicationContext().getResources().getString(R.string.cancel),
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();
                    }
                });

        AlertDialog alert11 = builder1.create();
        alert11.show();
    }
}
