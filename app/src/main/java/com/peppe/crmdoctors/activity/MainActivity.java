package com.peppe.crmdoctors.activity;

import android.animation.ValueAnimator;
import android.app.Activity;
import android.app.ActivityOptions;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.support.animation.DynamicAnimation;
import android.support.animation.SpringAnimation;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.DecelerateInterpolator;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TimePicker;

import com.miguelcatalan.materialsearchview.MaterialSearchView;
import com.peppe.crmdoctors.R;
import com.peppe.crmdoctors.adapter.DoctorAdapter;
import com.peppe.crmdoctors.model.Doctor;
import com.peppe.crmdoctors.provider.Provider;
import com.peppe.crmdoctors.task.DoctorTask;
import com.peppe.crmdoctors.task.OnDone;
import com.peppe.crmdoctors.util.Const;
import com.peppe.crmdoctors.util.RecyclerItemTouchHelper;
import com.peppe.crmdoctors.view.RecyclerViewFastScroller;
import com.peppe.crmdoctors.view.RecyclerViewHeader;


import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

import static android.support.animation.SpringForce.DAMPING_RATIO_LOW_BOUNCY;
import static android.support.animation.SpringForce.DAMPING_RATIO_MEDIUM_BOUNCY;
import static android.support.animation.SpringForce.STIFFNESS_LOW;

public class MainActivity extends AppCompatActivity implements OnDone, DoctorAdapter.ClickListener, RecyclerItemTouchHelper.RecyclerItemTouchHelperListener {

    private static final int LOADER_DOCTORS = 1;
    private static final int LOADER_FILTERED_DOCTORS = 2;

    private DoctorAdapter mDoctorAdapter;

    private Button buttonAdd;

    private RecyclerView mRecyclerView;
    private ArrayList<Doctor> doctors;
    private RecyclerViewFastScroller fastScroller;
    private MainActivity mainActivity;
    private Context mContext;
    private RecyclerViewHeader sectionItemDecoration;
    private RelativeLayout mContainer;
    private FloatingActionButton addDoc;
    private ImageView mBack;
    private ImageView mMenu;
    private MaterialSearchView mSearchView;
    private Toolbar mMainToolbar;
    private RelativeLayout mContainerFilter;
    private Toolbar mToolbarFilter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        init();
        findView();
        setView();
        isUp = false;

    }

    void init() {
        setContentView(R.layout.activity_main);
        mainActivity = this;
        mContext = getApplicationContext();
        getSupportLoaderManager().initLoader(LOADER_DOCTORS, null, mLoaderCallbacks);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);

        return true;
    }

    boolean isUp;

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.filter:
//                  Intent i = new Intent(this, FilterActivity.class);
//                startActivityForResult(i, Const.FILTER);
//                overridePendingTransition(0,0);

                slideDown();

                return true;
            case R.id.action_search:
                mSearchView.setMenuItem(item);
                return true;
            default:
                return super.onOptionsItemSelected(item);

        }
    }


    public void slideDown() {
        mContainerFilter.setVisibility(View.VISIBLE);
        final SpringAnimation anim1Y = new SpringAnimation(mContainerFilter, DynamicAnimation.TRANSLATION_Y);
        anim1Y.setStartValue(-mContainerFilter.getHeight());
        anim1Y.animateToFinalPosition(0);
        anim1Y.start();
        anim1Y.getSpring().setStiffness(STIFFNESS_LOW);
        anim1Y.getSpring().setDampingRatio(DAMPING_RATIO_LOW_BOUNCY);
    }

    // slide the view from its current position to below itself
    public void slideUp() {
        final SpringAnimation anim1Y = new SpringAnimation(mContainerFilter, DynamicAnimation.TRANSLATION_Y);
        anim1Y.setStartValue(0);
        anim1Y.animateToFinalPosition(-mContainerFilter.getHeight());
        anim1Y.start();
        anim1Y.getSpring().setStiffness(STIFFNESS_LOW);
        anim1Y.getSpring().setDampingRatio(DAMPING_RATIO_MEDIUM_BOUNCY);
    }

    private void findView() {
        mRecyclerView = findViewById(R.id.list);
        fastScroller = findViewById(R.id.fastscroller);
        mContainer = findViewById(R.id.container);
        addDoc = findViewById(R.id.addDoc);
        mBack = findViewById(R.id.back);
        mMenu = findViewById(R.id.menu);
        mSearchView = findViewById(R.id.search_view);
        mMainToolbar = findViewById(R.id.toolbar);
        mContainerFilter = findViewById(R.id.containerFilter);
        mToolbarFilter = findViewById(R.id.toolbarFilter);
    }

    ItemTouchHelper itemTouchHelper;

    private void setView() {
        mMainToolbar.setTitle("Find Doctors");
        setSupportActionBar(mMainToolbar);
        mContainerFilter.setVisibility(View.INVISIBLE);

        mDoctorAdapter = new DoctorAdapter(this, doctors, this);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mRecyclerView.setAdapter(mDoctorAdapter);
        fastScroller.setRecyclerView(mRecyclerView);
        fastScroller.setViewsToUse(R.layout.recycler_view_fast_scroller__fast_scroller, R.id.fastscroller_bubble, R.id.fastscroller_handle);
        ItemTouchHelper.SimpleCallback itemTouchHelperCallback = new RecyclerItemTouchHelper(0, ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT, mainActivity);
        new ItemTouchHelper(itemTouchHelperCallback).attachToRecyclerView(mRecyclerView);
        addDoc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(mainActivity, AddModifyActivity.class));
            }
        });
        mSearchView.setOnQueryTextListener(new MaterialSearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                Snackbar.make(findViewById(R.id.container), "Query: " + query, Snackbar.LENGTH_LONG)
                        .show();
                proj = query;
                getSupportLoaderManager().restartLoader(LOADER_FILTERED_DOCTORS, null, mLoaderCallbacks);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                getSupportLoaderManager().initLoader(LOADER_FILTERED_DOCTORS, null, mLoaderCallbacks);

                if (!TextUtils.isEmpty(newText)) {
                    proj = "%" + newText + "%";
                    getSupportLoaderManager().restartLoader(LOADER_FILTERED_DOCTORS, null, mLoaderCallbacks);

                }

                return false;
            }
        });

        mSearchView.setOnSearchViewListener(new MaterialSearchView.SearchViewListener() {
            @Override
            public void onSearchViewShown() {
                getSupportLoaderManager().restartLoader(LOADER_DOCTORS, null, mLoaderCallbacks);
            }

            @Override
            public void onSearchViewClosed() {
                getSupportLoaderManager().restartLoader(LOADER_DOCTORS, null, mLoaderCallbacks);

            }
        });
        RelativeLayout.LayoutParams lp = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, 300);
        lp.addRule(RelativeLayout.ALIGN_PARENT_TOP);
        mContainerFilter.setLayoutParams(lp);
        mToolbarFilter.inflateMenu(R.menu.menu_filter);
        mToolbarFilter.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {

            @Override
            public boolean onMenuItemClick(MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.close_filter:
                        slideUp();
                        break;
                }


                return false;
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        for (int i = 0; i < deletedDoctor.size(); i++) {
            Log.d(TAG, "onResume: " + deletedDoctor.get(i).getSurname());
            new DoctorTask(this, deletedDoctor.get(i).contentValuesFromDoctor(deletedDoctor.get(i)), getContentResolver()).execute(Const.DELETE_DOCTOR);
        }

    }

    String proj = "";
    private LoaderManager.LoaderCallbacks<Cursor> mLoaderCallbacks =
            new LoaderManager.LoaderCallbacks<Cursor>() {

                @Override
                public Loader<Cursor> onCreateLoader(int id, Bundle args) {
                    switch (id) {
                        case LOADER_DOCTORS:
                            return new CursorLoader(getApplicationContext(),
                                    Provider.URI_DOCTOR,
                                    new String[]{Doctor.COLUMN_NAME},
                                    null, null, Doctor.COLUMN_NAME);
                        case LOADER_FILTERED_DOCTORS:
                            final String[] projection = {Doctor.COLUMN_SURNAME, Doctor.COLUMN_SURNAME};
                            Log.d(TAG, "onCreateLoader: " + proj);
                            return new CursorLoader(getApplicationContext(),
                                    Uri.parse(Provider.URI_DOCTOR + "/3"),
                                    projection,
                                    Doctor.COLUMN_SURNAME + " LIKE ? ", new String[]{proj},
                                    null);

                        default:
                            throw new IllegalArgumentException();
                    }
                }

                @Override
                public void onLoadFinished(Loader<Cursor> loader, Cursor cursor) {
                    switch (loader.getId()) {
                        case LOADER_DOCTORS:
                            setDoctorAdapter(cursor);
                            break;

                        case LOADER_FILTERED_DOCTORS:

                            setDoctorAdapter(cursor);

                            break;
                    }
                }

                @Override
                public void onLoaderReset(Loader<Cursor> loader) {
                    switch (loader.getId()) {
                        case LOADER_DOCTORS:
                            //mDoctorAdapter.setCursorDoctor(null);
                            mDoctorAdapter.setDoctors(null);
                            break;
                        case LOADER_FILTERED_DOCTORS:
                            getSupportLoaderManager().initLoader(LOADER_DOCTORS, null, mLoaderCallbacks);


                            break;

                    }
                }

            };

    private static final String TAG = "MainActivity";

    private void setDoctorAdapter(Cursor cursor) {
        mRecyclerView.setAdapter(null);

        doctors = new ArrayList<>();
        Doctor doctor;
        while (cursor.moveToNext()) {
            doctor = new Doctor();
            doctor.setId(cursor.getLong(cursor.getColumnIndexOrThrow(Doctor.COLUMN_ID)));
            doctor.setName(cursor.getString(cursor.getColumnIndexOrThrow(Doctor.COLUMN_NAME)));
            doctor.setSurname(cursor.getString(cursor.getColumnIndexOrThrow(Doctor.COLUMN_SURNAME)));
            doctor.setListCategories(cursor.getString(cursor.getColumnIndexOrThrow(Doctor.COLUMN_CATEGORIES)));
            doctor.setListAddresses(cursor.getString(cursor.getColumnIndexOrThrow(Doctor.COLUMN_ADDRESSES)));
            doctor.setListDays_Hours(cursor.getString(cursor.getColumnIndexOrThrow(Doctor.COLUMN_DAY_HOURS)));
            doctor.setRelevance(cursor.getInt(cursor.getColumnIndexOrThrow(Doctor.COLUMN_RELEVANCE)));
            doctor.setNumbVisit(cursor.getInt(cursor.getColumnIndexOrThrow(Doctor.COLUMN_NUMB_VISIT)));
            doctor.setPhoneList(cursor.getString(cursor.getColumnIndexOrThrow(Doctor.COLUMN_PHONE)));
            doctor.setColor(cursor.getString(cursor.getColumnIndexOrThrow(Doctor.COLUMN_COLOR)));
            doctor.setNote(cursor.getString(cursor.getColumnIndexOrThrow(Doctor.COLUMN_NOTE)));
            doctor.setAppointment(cursor.getString(cursor.getColumnIndexOrThrow(Doctor.COLUMN_APPOINTMENT)));
            doctor.setLast_visit(cursor.getString(cursor.getColumnIndexOrThrow(Doctor.COLUMN_DATE_LAST_VISIT)));
            doctors.add(doctor);
        }
        if (doctors.size() != 0) {
            Collections.sort(doctors, new Comparator<Doctor>() {
                @Override
                public int compare(Doctor item1, Doctor item2) {
                    String s1 = item1.getSurname();
                    String s2 = item2.getSurname();
                    return s1.compareToIgnoreCase(s2);
                }

            });
            mDoctorAdapter.setDoctors(doctors);

            mRecyclerView.setAdapter(mDoctorAdapter);
            mRecyclerView.removeItemDecoration(sectionItemDecoration);
            sectionItemDecoration =
                    new RecyclerViewHeader(getResources().getDimensionPixelSize(R.dimen.recycler_section_header_height),
                            true,
                            getSectionCallback(doctors));
            mRecyclerView.addItemDecoration(sectionItemDecoration);
        }

    }


    @Override
    public void onDone() {
        getSupportLoaderManager().restartLoader(LOADER_DOCTORS, null, mLoaderCallbacks);
        deletedDoctor = new ArrayList<>();

    }

    private RecyclerViewHeader.SectionCallback getSectionCallback(final ArrayList<Doctor> doctorList) {
        return new RecyclerViewHeader.SectionCallback() {
            @Override
            public boolean isSection(int position) {
                if (doctorList != null) {
                    return position == 0
                            || doctorList.get(position)
                            .getSurname()
                            .charAt(0) != doctorList.get(position - 1)
                            .getSurname()
                            .charAt(0);
                } else return position == 0;
            }

            @Override
            public CharSequence getSectionHeader(int position) {
                if (doctorList != null) {
                    return doctorList.get(position)
                            .getSurname()
                            .subSequence(0,
                                    1);
                } else return "";
            }
        };
    }

    @Override
    public void notifyHeader(final ArrayList<Doctor> docs) {
        mRecyclerView.removeItemDecoration(sectionItemDecoration);
        mRecyclerView.postDelayed(new Runnable() {
            @Override
            public void run() {
                sectionItemDecoration = new RecyclerViewHeader(getResources().getDimensionPixelSize(R.dimen.recycler_section_header_height),
                        true,
                        getSectionCallback(docs));
                mRecyclerView.addItemDecoration(sectionItemDecoration);
            }
        }, 500);

    }

    @Override
    public void onClick(int position, DoctorAdapter.ViewHolder viewHolder) {
//        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP) {
//            ActivityOptions options = ActivityOptions.makeSceneTransitionAnimation((Activity) mContext, view, mContext.getString(R.string.transition));
//            Intent intent = DetailsActivity.makeIntent(mContext, view);
//            mContext.startActivity(intent, options.toBundle());
//        Intent intent = new Intent(mainActivity, DetailsActivity.class);
//        intent.putExtra(Const.DOCTOR, doctors.get(position));
//        startActivity(intent);
        ActivityOptions options = null;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP) {
            options = ActivityOptions.makeSceneTransitionAnimation(mainActivity, (viewHolder.mFSNDoctor), mContext.getString(R.string.transition));
            Intent intent = new Intent(mainActivity, DetailsActivity.class);
            intent.putExtra(Const.DOCTOR, doctors.get(position));
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent, options.toBundle());
        } else {
            Intent intent = new Intent(mainActivity, DetailsActivity.class);
            intent.putExtra(Const.DOCTOR, doctors.get(position));
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
        }

    }

    @Override
    public void onDoubleClick(int position, final DoctorAdapter.ViewHolder viewHolder) {
        Doctor doctor = doctors.get(position);
        int numb = doctor.getNumbVisit();
        numb++;
        doctor.setNumbVisit(numb);
        ValueAnimator animator = new ValueAnimator();
        animator.setObjectValues(doctor.getNumbVisit(), numb);
        animator.setDuration(1500);
        animator.setInterpolator(new DecelerateInterpolator(3f));
        animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            public void onAnimationUpdate(ValueAnimator animation) {

                viewHolder.mNumbVisit.setText(String.valueOf(animation.getAnimatedValue()));
            }
        });
        animator.start();

    }

//    public void scaleView(final View view, final MainActivity mainActivity, final Doctor doctor) {
//        view.animate()
//                .translationY(view.getHeight())
//                .alpha(0.0f)
//                .setDuration(1000)
//                .setListener(new AnimatorListenerAdapter() {
//                    @Override
//                    public void onAnimationEnd(Animator animation) {
//                        super.onAnimationEnd(animation);
//                        view.setVisibility(View.GONE);
//                        new DoctorTask(mainActivity, doctor.contentValuesFromDoctor(doctor), getContentResolver()).execute(Const.UPDATE_DOCTOR);
//
//                    }
//
//                    @Override
//                    public void onAnimationStart(Animator animation) {
//                        super.onAnimationStart(animation);
//                        view.setVisibility(View.VISIBLE);
//
//                    }
//                });
//
//    }

    boolean deleted = true;

    @Override
    public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction, int position) {
        if (direction == ItemTouchHelper.RIGHT) {
            showDialogCalendar(position);
        } else {
            showDialogDelete(position);

        }
    }

    private void showDialogDelete(final int position) {
        final AlertDialog.Builder builder = new AlertDialog.Builder(mainActivity);
        builder.setCancelable(false);
        builder.setTitle("Sei sicuro di voler cancellare l' elemento?");
        builder.setPositiveButton(getApplicationContext().getResources().getString(R.string.ok), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                deleteElemet(position);
                alert11.dismiss();
            }
        });
        builder.setNegativeButton(getApplicationContext().getResources().getString(R.string.cancel), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                alert11.dismiss();
                mDoctorAdapter.notifyItemChanged(position);
            }
        });
        alert11 = builder.create();
        alert11.show();
    }

    private void deleteElemet(final int position) {
        String name = doctors.get(position).getSurname();

        // backup of removed item for undo purpose
        final Doctor deletedItem = doctors.get(position);
        final int deletedIndex = position;

        // remove the item from recycler view
        mDoctorAdapter.removeItem(position);

        // showing snack bar with Undo option
        Snackbar snackbar = Snackbar
                .make(mContainer, name + " " + getResources().getString(R.string.removed), Snackbar.LENGTH_LONG);
        snackbar.setAction("Indietro", new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                deleted = false;
                // undo is selected, restore the deleted item
                mDoctorAdapter.restoreItem(deletedItem, deletedIndex);
            }
        });
        snackbar.addCallback(new Snackbar.Callback() {

            @Override
            public void onDismissed(Snackbar snackbar, int event) {
                if (deleted)
                    new DoctorTask(mainActivity, doctors.get(position).contentValuesFromDoctor(doctors.get(position)), getContentResolver()).execute(Const.DELETE_DOCTOR);
            }
        });
        snackbar.setActionTextColor(Color.YELLOW);
        snackbar.show();
    }

    AlertDialog alert11;

    int month = 0;
    int dayOfMonth = 0;
    int year = 0;
    int hour = 0;
    int minute = 0;

    private void showDialogCalendar(final int position) {
        LayoutInflater inflater = LayoutInflater.from(getApplicationContext());
        View dialogView = inflater.inflate(R.layout.alert_calendar, null);
        final TimePicker timePicker = dialogView.findViewById(R.id.time);
        final DatePicker datePicker = dialogView.findViewById(R.id.date);
        Button ok = dialogView.findViewById(R.id.ok);
        Button cancel = dialogView.findViewById(R.id.cancel);
        timePicker.setVisibility(View.GONE);
        timePicker.setIs24HourView(true);
        datePicker.setVisibility(View.VISIBLE);

        ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (datePicker.getVisibility() == View.VISIBLE) {
                    month = datePicker.getMonth();
                    dayOfMonth = datePicker.getDayOfMonth();
                    year = datePicker.getYear();

                    datePicker.setVisibility(View.GONE);
                    timePicker.setVisibility(View.VISIBLE);
                } else {
                    hour = timePicker.getCurrentHour();
                    minute = timePicker.getCurrentMinute();
                    JSONObject jsonObject = new JSONObject();
                    try {
                        jsonObject.put(Const.DAY, dayOfMonth);
                        jsonObject.put(Const.MONTH, month);
                        jsonObject.put(Const.YEAR, year);
                        jsonObject.put(Const.HOUR, hour);
                        jsonObject.put(Const.MINUTE, minute);

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    doctors.get(position).setAppointment(jsonObject.toString());
                    Log.d(TAG, "onClick: " + doctors.get(position).getAppointment());
                    new DoctorTask(mainActivity, doctors.get(position).contentValuesFromDoctor(doctors.get(position)), getContentResolver()).execute(Const.UPDATE_DOCTOR);
                    alert11.dismiss();
                    mDoctorAdapter.notifyItemChanged(position);
                }
            }


        });
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mDoctorAdapter.notifyItemChanged(position);
                alert11.dismiss();
            }
        });
        final AlertDialog.Builder builder1 = new AlertDialog.Builder(mainActivity);
        builder1.setTitle("Fissa un appuntamento");
        builder1.setView(dialogView);
        builder1.setCancelable(false);
        alert11 = builder1.create();
        alert11.show();
    }

    ArrayList<Doctor> deletedDoctor = new ArrayList<>();

    @Override
    protected void onDestroy() {
        super.onDestroy();
        for (int i = 0; i < deletedDoctor.size(); i++) {
            new DoctorTask(this, deletedDoctor.get(i).contentValuesFromDoctor(deletedDoctor.get(i)), getContentResolver()).execute(Const.DELETE_DOCTOR);
        }


    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {


        if (requestCode == Const.FILTER) {

            if (resultCode == Activity.RESULT_OK) {
                String result = data.getStringExtra("result");
                if (resultCode == Activity.RESULT_CANCELED) {
                }
            }

        }
    }

    @Override
    public void onBackPressed() {
        if (mSearchView.isSearchOpen()) {
            mSearchView.closeSearch();
            getSupportLoaderManager().restartLoader(LOADER_DOCTORS, null, mLoaderCallbacks);
        } else {
            super.onBackPressed();
        }
    }
}
