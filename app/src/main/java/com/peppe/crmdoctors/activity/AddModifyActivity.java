package com.peppe.crmdoctors.activity;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Rect;
import android.os.Handler;
import android.support.annotation.IntRange;
import android.support.annotation.Nullable;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.beloo.widget.chipslayoutmanager.ChipsLayoutManager;
import com.beloo.widget.chipslayoutmanager.SpacingItemDecoration;
import com.beloo.widget.chipslayoutmanager.gravity.IChildGravityResolver;
import com.beloo.widget.chipslayoutmanager.layouter.breaker.IRowBreaker;
import com.google.android.gms.common.GooglePlayServicesNotAvailableException;
import com.google.android.gms.common.GooglePlayServicesRepairableException;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.places.AutocompleteFilter;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.ui.PlaceAutocomplete;
import com.peppe.crmdoctors.R;
import com.peppe.crmdoctors.adapter.AddressAdapter;
import com.peppe.crmdoctors.adapter.CategoryAdapter;
import com.peppe.crmdoctors.adapter.Day_HourAdapter;
import com.peppe.crmdoctors.adapter.PhoneNumberAdapter;
import com.peppe.crmdoctors.model.Category;
import com.peppe.crmdoctors.model.Doctor;
import com.peppe.crmdoctors.provider.Provider;
import com.peppe.crmdoctors.task.DoctorTask;
import com.peppe.crmdoctors.task.OnDone;
import com.peppe.crmdoctors.util.AppUtil;
import com.peppe.crmdoctors.util.Const;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class AddModifyActivity extends AppCompatActivity implements OnDone, CategoryAdapter.ClickListener, PhoneNumberAdapter.ClickListener,
        Day_HourAdapter.ClickListener, AddressAdapter.ClickListener {
    AddModifyActivity addModifyActivity;
    private RelativeLayout mContainerOval;
    private TextView mFSNDoctor;

    private EditText mEditTextName;
    private EditText mEditTextSurname;
    private RecyclerView mRecyclerViewPhoneNumbers;
    private ImageView mButtonAddPhone;
    private ImageView mButtonAddCategory;
    private RecyclerView mRecyclerViewCategory;
    private TextView mRelevance;
    private ImageView mEnancheRelevance;
    private ImageView mDecreaseRelevance;
    private TextView mNumbVisit;
    private ImageView mEnancheNumbVisit;
    private ImageView mDecreaseNumbVisit;
    private EditText mEditTextNote;
    private RecyclerView mRecyclerViewAvailability;
    private Doctor mDoctor;
    CategoryAdapter adapterCategory;
    JSONArray phoneNumber;
    JSONArray docCategory;
    PhoneNumberAdapter adapterPhoneNumber;
    private static final int LOADER_CATEGORY = 1;
    private ArrayList<String> strings;
    private JSONArray day_hour;
    private Day_HourAdapter day_hourAdapter;
    private RecyclerView mRecyclerViewAddress;
    private JSONArray arrayAddress;
    private AddressAdapter adapterAddress;
    private ImageView mAddaddress;
    private ImageView mSave;
    private ImageView mBack;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_modify);
        getSupportLoaderManager().initLoader(LOADER_CATEGORY, null, mLoaderCallbacks);
        addModifyActivity = this;
        findViewById();

        try {
            setView();
            setListener();
            if (getIntent().hasExtra(Const.DOCTOR)) {
                mDoctor = getIntent().getParcelableExtra(Const.DOCTOR);
                setDoctorView(mDoctor);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }


    }

    String initials;

    private void setDoctorView(Doctor mDoctor) throws JSONException {
        mEditTextName.setText(mDoctor.getName());
        mEditTextSurname.setText(mDoctor.getSurname());
        mContainerOval.setBackground(AppUtil.getDrawableFromColor(mDoctor.getColor()));
        String in = String.valueOf(mDoctor.getName().charAt(0));
        in = in + String.valueOf(mDoctor.getSurname().charAt(0));
        mFSNDoctor.setText(in);
        mRelevance.setText(String.valueOf(mDoctor.getRelevance()));
        mNumbVisit.setText(String.valueOf(mDoctor.getNumbVisit()));
        phoneNumber = AppUtil.concatArray(phoneNumber, new JSONArray(mDoctor.getPhoneList()));
        adapterPhoneNumber.setPhoneList(phoneNumber);
        docCategory = AppUtil.concatArray(docCategory, new JSONArray(mDoctor.getListCategories()));
        adapterCategory.setCategory(docCategory);
        arrayAddress = AppUtil.concatArray(arrayAddress, new JSONArray(mDoctor.getListAddresses()));
        adapterAddress.setAddress(arrayAddress);
        day_hour = new JSONArray(mDoctor.getListDays_Hours());
        if (day_hour.length() != 0) {
            day_hourAdapter.setData(day_hour);
        }

        mEditTextNote.setText(mDoctor.getNote());
    }


    private void findViewById() {
        mEditTextName = findViewById(R.id.editTextName);
        mEditTextSurname = findViewById(R.id.editTextSurname);
        mRecyclerViewPhoneNumbers = findViewById(R.id.recyclerViewPhoneNumbers);
        mButtonAddPhone = findViewById(R.id.add_phone);
        mRecyclerViewCategory = findViewById(R.id.recyclerViewCategory);
        mButtonAddCategory = findViewById(R.id.add_category);
        mNumbVisit = findViewById(R.id.numbVisit);
        mEnancheNumbVisit = findViewById(R.id.enancheNumbVisit);
        mDecreaseNumbVisit = findViewById(R.id.decreaseNumbVisit);
        mRelevance = findViewById(R.id.relevance);
        mEnancheRelevance = findViewById(R.id.enancheRelevance);
        mDecreaseRelevance = findViewById(R.id.decreaseRelevance);
        mEditTextNote = findViewById(R.id.editTextNote);
        mRecyclerViewAvailability = findViewById(R.id.recyclerViewAvailability);
        mFSNDoctor = findViewById(R.id.boh);
        mContainerOval = findViewById(R.id.containerOval);
        mRecyclerViewAddress = findViewById(R.id.recyclerViewAddress);
        mAddaddress = findViewById(R.id.add_address);
        mSave = findViewById(R.id.menu);
        mBack = findViewById(R.id.back);
    }

    private void setView() throws JSONException {
        ChipsLayoutManager chipsLayoutManager = ChipsLayoutManager.newBuilder(getApplicationContext())
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
        mRecyclerViewCategory.setLayoutManager(chipsLayoutManager);
        //mRecyclerViewPhoneNumbers.setLayoutManager(chipsLayoutManager);
        mRecyclerViewCategory.addItemDecoration(new SpacingItemDecoration(getResources().getDimensionPixelOffset(R.dimen.item_space),
                getResources().getDimensionPixelOffset(R.dimen.item_space)));
        adapterCategory = new CategoryAdapter(this, this, docCategory);
        mRecyclerViewCategory.setAdapter(adapterCategory);
        mNumbVisit.setText("0");
        mRelevance.setText("0");
        ChipsLayoutManager chipsLayoutManagerPhone = ChipsLayoutManager.newBuilder(getApplicationContext())
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
        mRecyclerViewPhoneNumbers.setLayoutManager(chipsLayoutManagerPhone);
        mRecyclerViewPhoneNumbers.addItemDecoration(new SpacingItemDecoration(getResources().getDimensionPixelOffset(R.dimen.item_space),
                getResources().getDimensionPixelOffset(R.dimen.item_space)));
        phoneNumber = new JSONArray();
        JSONObject jsonObject = new JSONObject();
        jsonObject.put(Const.PHONE, Const.EMPTY);
        phoneNumber.put(jsonObject);
        adapterPhoneNumber = new PhoneNumberAdapter(this, this, phoneNumber, Const.ADD_MODOFY_ACTIVITY);
        mRecyclerViewPhoneNumbers.setAdapter(adapterPhoneNumber);
        docCategory = new JSONArray();
        jsonObject = new JSONObject();
        jsonObject.put(Category.COLUMN_CATEGORY_NAME, Const.EMPTY);
        jsonObject.put(Category.COLUMN_ID, -1);
        docCategory.put(jsonObject);
        adapterCategory.setCategory(docCategory);
        day_hour = AppUtil.getDefaultJsonAvaibility();
        for (int i =  0; i<day_hour.length(); i++){
            Log.d(TAG, "OPEN: " + day_hour.getJSONObject(i).getString(Const.MORNING + Const.OPEN));
        }
        day_hourAdapter = new Day_HourAdapter(this, getApplicationContext(), day_hour, Const.NOT_AVAIBLE);
        day_hourAdapter.setData(day_hour);
        mRecyclerViewAvailability.setLayoutManager(new LinearLayoutManager(getApplicationContext(), LinearLayoutManager.HORIZONTAL, false));
        mRecyclerViewAvailability.setAdapter(day_hourAdapter);
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

        mRecyclerViewAddress.setLayoutManager(chipsLayoutManagerAddress);
        mRecyclerViewAddress.addItemDecoration(new SpacingItemDecoration(getResources().getDimensionPixelOffset(R.dimen.item_space),
                getResources().getDimensionPixelOffset(R.dimen.item_space)));
        arrayAddress = new JSONArray();
        jsonObject = new JSONObject();
        jsonObject.put(Const.CITY, Const.EMPTY);
        jsonObject.put(Const.STREET, -1);
        jsonObject.put(Const.CIVIC, -1);
        arrayAddress.put(jsonObject);
        adapterAddress = new AddressAdapter(this, this, arrayAddress);
        adapterAddress.setAddress(arrayAddress);
        mRecyclerViewAddress.setAdapter(adapterAddress);


    }

    private void setListener() {
        mButtonAddPhone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showDialogAddPhoneNumber(null, -1);
            }
        });
        mButtonAddCategory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showDialogCategory();
            }
        });
        mEnancheNumbVisit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int currentNumbVisit = Integer.valueOf(String.valueOf(mNumbVisit.getText()));
                currentNumbVisit = currentNumbVisit + 1;
                Toast.makeText(getApplicationContext(), String.valueOf(mNumbVisit.getText()), Toast.LENGTH_SHORT).show();
                mNumbVisit.setText(String.valueOf(currentNumbVisit));
            }
        });
        mDecreaseNumbVisit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int currentNumbVisit = Integer.valueOf(String.valueOf(mNumbVisit.getText()));
                if (currentNumbVisit > 0) {
                    currentNumbVisit = currentNumbVisit - 1;
                    mNumbVisit.setText(String.valueOf(currentNumbVisit));
                } else {
                    Toast.makeText(getApplicationContext(), "< 0 !!! Impossible", Toast.LENGTH_SHORT).show();
                }
            }
        });
        mEnancheRelevance.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int currentRelevance = Integer.valueOf(String.valueOf(mRelevance.getText()));
                currentRelevance = currentRelevance + 1;
                mRelevance.setText(String.valueOf(currentRelevance));
            }
        });
        mDecreaseRelevance.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int currentRelevance = Integer.valueOf(String.valueOf(mRelevance.getText()));

                if (currentRelevance > 0) {
                    currentRelevance = currentRelevance - 1;
                    mRelevance.setText(String.valueOf(currentRelevance));
                } else {
                    Toast.makeText(getApplicationContext(), "< 0 !!! Impossible", Toast.LENGTH_SHORT).show();
                }
            }
        });
        mBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });
        mSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String firstName = mEditTextName.getText().toString();
                String surname = mEditTextSurname.getText().toString();

                if (!TextUtils.isEmpty(firstName)) {
                    if (!TextUtils.isEmpty(surname)) {
                        if (adapterCategory.getCategories().length() > 1) {
                            int what = Const.ADD_DOCTOR;
                            if (mDoctor != null) {
                                what = Const.UPDATE_DOCTOR;
                            } else {
                                mDoctor = new Doctor();
                                mDoctor.setColor(AppUtil.getRandomColor());
                            }
                            mDoctor.setListAddresses(AppUtil.removeElementFromArray(arrayAddress, 0).toString());
                            mDoctor.setPhoneList(AppUtil.removeElementFromArray(phoneNumber, 0).toString());
                            mDoctor.setListCategories(AppUtil.removeElementFromArray(adapterCategory.getCategories(), 0).toString());
                            mDoctor.setName(firstName);
                            mDoctor.setSurname(surname);
                            mDoctor.setListDays_Hours(day_hour.toString());
                            mDoctor.setNote(mEditTextNote.getText().toString());
                            mDoctor.setRelevance(Integer.valueOf(mRelevance.getText().toString()));
                            mDoctor.setNumbVisit(Integer.valueOf(mNumbVisit.getText().toString()));
                            if (what == Const.ADD_DOCTOR) {
                                new DoctorTask(addModifyActivity, mDoctor.contentValuesFromDoctor(mDoctor), getContentResolver()).execute(what);
                                showDialogAlert("Sto aggiungendo il medico", true);

                            } else {
                                new DoctorTask(addModifyActivity, mDoctor.contentValuesFromDoctor(mDoctor), getContentResolver()).execute(what);
                                showDialogAlert("Sto effettuando le modifiche", true);

                            }
                        } else {
                            Toast.makeText(getApplicationContext(), "Devi selezionare almeno una specializzazione", Toast.LENGTH_LONG).show();
                        }
                    } else {
                        Toast.makeText(getApplicationContext(), "Il cognome del medico non può essere vuoto", Toast.LENGTH_LONG).show();
                    }
                } else {
                    Toast.makeText(getApplicationContext(), "Il nome del medico non può essere vuoto", Toast.LENGTH_LONG).show();

                }


            }
        });
        mAddaddress.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    AutocompleteFilter typeFilter = new AutocompleteFilter.Builder()
                            .setTypeFilter(AutocompleteFilter.TYPE_FILTER_ADDRESS)
                            .build();
                    Intent intent =
                            new PlaceAutocomplete.IntentBuilder(PlaceAutocomplete.MODE_FULLSCREEN).setFilter(typeFilter)
                                    .build(addModifyActivity);
                    startActivityForResult(intent, PLACE_AUTOCOMPLETE_REQUEST_CODE);
                } catch (GooglePlayServicesRepairableException e) {
                    // TODO: Handle the error.
                } catch (GooglePlayServicesNotAvailableException e) {
                    // TODO: Handle the error.
                }
            }
        });
    }

    int PLACE_AUTOCOMPLETE_REQUEST_CODE = 1;

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == PLACE_AUTOCOMPLETE_REQUEST_CODE) {
            if (resultCode == RESULT_OK) {
                try {
                    Place place = PlaceAutocomplete.getPlace(this, data);

                    JSONObject jsonObject = new JSONObject();
                    jsonObject.put(Const.CIVIC, "");
                    Log.i(TAG, "Place: place.getAddress().toString().split(\",\").length" + place.getAddress().toString().split(",").length);

                    if (place.getAddress().toString().split(",").length == 4) {
                        jsonObject.put(Const.STREET, place.getAddress().toString().split(",")[0]);
                        jsonObject.put(Const.CIVIC, place.getAddress().toString().split(",")[1]);
                        jsonObject.put(Const.CITY, place.getAddress().toString().split(",")[2]);
                        jsonObject.put(Const.LAT, place.getLatLng().latitude);
                        jsonObject.put(Const.LONG, place.getLatLng().longitude);
                        adapterAddress.addElement(jsonObject);
                    } else {
                        if (place.getAddress().toString().split(",").length == 3) {
                            jsonObject.put(Const.STREET, place.getAddress().toString().split(",")[0]);
                            jsonObject.put(Const.CITY, place.getAddress().toString().split(",")[1]);
                            jsonObject.put(Const.LAT, place.getLatLng().latitude);
                            jsonObject.put(Const.LONG, place.getLatLng().longitude);
                            adapterAddress.addElement(jsonObject);
                        } else
                            Toast.makeText(getApplicationContext(), "Ops", Toast.LENGTH_LONG).show();
                    }


                } catch (JSONException e) {
                    e.printStackTrace();
                }
            } else if (resultCode == PlaceAutocomplete.RESULT_ERROR) {
                Status status = PlaceAutocomplete.getStatus(this, data);
                // TODO: Handle the error.
                Log.i(TAG, status.getStatusMessage());

            } else if (resultCode == RESULT_CANCELED) {
                // The user canceled the operation.
            }
        }
    }

    private void showDialogCategory() {
        try {
            String[] categoriesMultiChoice = new String[allCategories.size()];
            final boolean[] checkedCategory = new boolean[allCategories.size()];
            for (int i = 0; i < allCategories.size(); i++) {
                categoriesMultiChoice[i] = allCategories.get(i).getName();
            }
            if (mDoctor != null || adapterCategory.getCategories().length() != 0) {
                for (int i = 0; i < allCategories.size(); i++) {
                    for (int j = 0; j < adapterCategory.getCategories().length(); j++) {

                        if (adapterCategory.getCategories().getJSONObject(j).getString(Category.COLUMN_CATEGORY_NAME).equalsIgnoreCase(allCategories.get(i).getName())) {
                            checkedCategory[i] = true;
                            break;
                        } else {
                            checkedCategory[i] = false;

                        }

                    }
                }
            }
            AlertDialog.Builder builder1 = new AlertDialog.Builder(addModifyActivity);
            builder1.setCancelable(true);
            builder1.setTitle("Seleziona categorie");

            builder1.setMultiChoiceItems(categoriesMultiChoice, checkedCategory, new DialogInterface.OnMultiChoiceClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i, boolean b) {
                    checkedCategory[i] = b;
                }
            });
            builder1.setPositiveButton(
                    getApplicationContext().getResources().getString(R.string.ok),
                    new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {

                            setChoosenCategory(checkedCategory);
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
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void setChoosenCategory(boolean[] checkedCategory) {
        boolean ok = false;
        for (int i = 0; i < checkedCategory.length; i++) {
            if (checkedCategory[i]) {
                ok = true;
                break;
            }
        }
        if (ok) {
            try {
                docCategory = new JSONArray();
                JSONObject jsonCat = new JSONObject();
                jsonCat.put(Category.COLUMN_CATEGORY_NAME, Const.EMPTY);
                jsonCat.put(Category.COLUMN_ID, -1);
                docCategory.put(jsonCat);
                for (int i = 0; i < allCategories.size(); i++) {
                    if (checkedCategory[i]) {
                        jsonCat = new JSONObject();
                        jsonCat.put(Category.COLUMN_CATEGORY_NAME, allCategories.get(i).getName());
                        jsonCat.put(Category.COLUMN_ID, allCategories.get(i).getId());
                        docCategory.put(jsonCat);
                    }
                }
                adapterCategory.setCategory(docCategory);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        } else {
            Toast.makeText(getApplicationContext(), "Devi selezionare almeno una categoria", Toast.LENGTH_LONG).show();
        }
    }

    private static final String TAG = "AddModifyActivity";

    private void showDialogAddPhoneNumber(final String phone, @Nullable final int index) {
        LayoutInflater inflater = LayoutInflater.from(getApplicationContext());
        View dialogView = inflater.inflate(R.layout.alert_edit_text, null);
        final EditText ed = dialogView.findViewById(R.id.editText);
        if (phone != null) {
            ed.setText(phone);
            ed.setSelection(phone.length());
        }
        AlertDialog.Builder builder1 = new AlertDialog.Builder(addModifyActivity);
        builder1.setCancelable(true);
        builder1.setTitle("Aggiungi numero di telefono");
        builder1.setView(dialogView);
        builder1.setPositiveButton(
                getApplicationContext().getResources().getString(R.string.ok),
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        if (!TextUtils.isEmpty(ed.getText())) {
                            if (phone != null) {
                                adapterPhoneNumber.removeElement(index);
                            }
                            adapterPhoneNumber.addElement(ed.getText().toString());
                        }
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


    private ArrayList<Category> allCategories;
    private LoaderManager.LoaderCallbacks<Cursor> mLoaderCallbacks =
            new LoaderManager.LoaderCallbacks<Cursor>() {

                @Override
                public Loader<Cursor> onCreateLoader(int id, Bundle args) {
                    switch (id) {
                        case LOADER_CATEGORY:
                            return new CursorLoader(getApplicationContext(),
                                    Provider.URI_CATEGORY,
                                    new String[]{Category.COLUMN_CATEGORY_NAME},
                                    null, null, Category.COLUMN_CATEGORY_NAME);

                        default:
                            throw new IllegalArgumentException();
                    }
                }

                @Override
                public void onLoadFinished(Loader<Cursor> loader, Cursor cursor) {
                    switch (loader.getId()) {
                        case LOADER_CATEGORY:
                            allCategories = new ArrayList<>();
                            Category category;
                            while (cursor.moveToNext()) {
                                category = new Category();
                                category.setId(cursor.getInt(cursor.getColumnIndexOrThrow(Category.COLUMN_ID)));
                                category.setName(cursor.getString(cursor.getColumnIndexOrThrow(Category.COLUMN_CATEGORY_NAME)));
                                allCategories.add(category);
                            }


                            break;
                    }
                }

                @Override
                public void onLoaderReset(Loader<Cursor> loader) {
                    switch (loader.getId()) {
                        case LOADER_CATEGORY:
                            break;
                    }
                }

            };

    @Override
    public void onClickCategory(int position) {
        adapterCategory.removeElement(position);
    }

    @Override
    public void onClickPhoneAdapter(int position, int what) {
        if (what == 0) {
            adapterPhoneNumber.removeElement(position);
         } else {
            try {
                Log.d(TAG, "onClickPhoneAdapter: ");
                showDialogAddPhoneNumber(phoneNumber.getJSONObject(position).getString(Const.PHONE), position);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void onClickDay_Hour(int position) {
        showDialogDayHour(position);
    }

    String morningOpenHour = "00";
    String morningOpenMinute = "00";
    String morningClosedHour = "00";
    String morningClosedMinute = "00";
    String afternoonOpenHour = "00";
    String afternoonOpenMinute = "00";
    String afternoonClosedHour = "00";
    String afternoonClosedMinute = "00";
    boolean morning_afternoon = false;
    Spinner spinnerAmPm;
    Spinner spinnerHourOpening;
    Spinner spinnerMinuteOpening;
    Spinner spinnerHourClosed;
    Spinner spinnerMinuteClosed;

    private String[] hoursAm = {"00", "01", "02", "03", "04", "05", "06", "07", "08", "09", "10", "11", "12"};
    private String[] hoursPm = {"13", "14", "15", "16", "17", "18", "19", "20", "21", "22", "23", "24"};
    private String[] minutes = new String[60];


    private void showDialogDayHour(final int position) {
        try {
            LayoutInflater inflater = LayoutInflater.from(getApplicationContext());
            View dialogView = inflater.inflate(R.layout.alert_day_hour, null);
            spinnerAmPm = dialogView.findViewById(R.id.spinner_mornig_afternoon);
            spinnerAmPm.setSelection(0);
            final ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this, R.array.select_state, android.R.layout.simple_spinner_item);
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            spinnerAmPm.setAdapter(adapter);
            spinnerHourOpening = dialogView.findViewById(R.id.spinnerHourMorningOpening);
            spinnerMinuteOpening = dialogView.findViewById(R.id.spinnerMinuteMorningOpening);
            spinnerHourClosed = dialogView.findViewById(R.id.spinnerHourMorningClosed);
            spinnerMinuteClosed = dialogView.findViewById(R.id.spinnerMinuteMorningClosed);

            for (int i = 0; i < 60; i++) {
                if (i >= 0 && i <= 9) {
                    minutes[i] = "0" + i;
                } else minutes[i] = String.valueOf(i);
            }
            morningOpenHour = day_hour.getJSONObject(position).getString(Const.MORNING + Const.OPEN).split(":")[0];
            morningOpenMinute = day_hour.getJSONObject(position).getString(Const.MORNING + Const.OPEN).split(":")[1];
            morningClosedHour = day_hour.getJSONObject(position).getString(Const.MORNING + Const.CLOSED).split(":")[0];
            morningClosedMinute = day_hour.getJSONObject(position).getString(Const.MORNING + Const.CLOSED).split(":")[1];
            afternoonOpenHour = day_hour.getJSONObject(position).getString(Const.AFTERNOON + Const.OPEN).split(":")[0];
            afternoonOpenMinute = day_hour.getJSONObject(position).getString(Const.AFTERNOON + Const.OPEN).split(":")[1];
            afternoonClosedHour = day_hour.getJSONObject(position).getString(Const.AFTERNOON + Const.CLOSED).split(":")[0];
            afternoonClosedMinute = day_hour.getJSONObject(position).getString(Const.AFTERNOON + Const.CLOSED).split(":")[1];
            setOnItemClickAdapter(position);
            AlertDialog.Builder builder1 = new AlertDialog.Builder(addModifyActivity);
            builder1.setCancelable(true);
            builder1.setTitle("Modifica disponibilità per " + AppUtil.getDay(position));
            builder1.setView(dialogView);
            builder1.setPositiveButton(
                    getApplicationContext().getResources().getString(R.string.ok),
                    new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            try {
                                JSONObject jsonObject = day_hour.getJSONObject(position);
                                jsonObject.put(Const.DAY, AppUtil.getDay(position));
                                String s = Const.MORNING + Const.OPEN + morningOpenHour + ":" + morningOpenMinute;
                                Log.d(TAG, "onClick:  " +s);
                                jsonObject.put(Const.MORNING + Const.OPEN, morningOpenHour + ":" + morningOpenMinute);
                                jsonObject.put(Const.MORNING + Const.CLOSED, morningClosedHour + ":" + morningClosedMinute);
                                jsonObject.put(Const.AFTERNOON + Const.OPEN, afternoonOpenHour + ":" + afternoonOpenMinute);
                                jsonObject.put(Const.AFTERNOON + Const.CLOSED, afternoonClosedHour + ":" + afternoonClosedMinute);
                                day_hour.put(position, jsonObject);
                                day_hourAdapter.notifyDataSetChanged();
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
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
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void initSpinnerMorning(int index) {

        ArrayAdapter<String> adapter1 = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, hoursAm);
        adapter1.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerHourOpening.setAdapter(adapter1);
        spinnerHourClosed.setAdapter(adapter1);
        spinnerHourOpening.setSelection(getElementOfSpinner(index, Const.MORNING + Const.OPEN, hoursAm, 0));
        spinnerHourClosed.setSelection(getElementOfSpinner(index, Const.MORNING + Const.CLOSED, hoursAm, 0));
        adapter1 = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, minutes);
        spinnerMinuteOpening.setAdapter(adapter1);
        spinnerMinuteClosed.setAdapter(adapter1);
        spinnerMinuteOpening.setSelection(getElementOfSpinner(index, Const.MORNING + Const.OPEN, minutes, 1));
        spinnerMinuteClosed.setSelection(getElementOfSpinner(index, Const.MORNING + Const.CLOSED, minutes, 1));

    }

    private int getElementOfSpinner(int index, String constA, String[] list, int minuts_hours) {
        String hour = null;
        try {
            hour = day_hour.getJSONObject(index).getString(constA).split(":")[minuts_hours];
            for (int i = 0; i < list.length; i++) {
                if (hour.equalsIgnoreCase(list[i]))
                    return i;
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return -1;
    }

    private void initSpinnerAfternoon(int index) {


        ArrayAdapter<String> adapter1 = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, hoursPm);
        adapter1.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerHourOpening.setAdapter(adapter1);
        spinnerHourClosed.setAdapter(adapter1);
        spinnerHourOpening.setSelection(getElementOfSpinner(index, Const.AFTERNOON + Const.OPEN, hoursPm, 0));
        spinnerHourClosed.setSelection(getElementOfSpinner(index, Const.AFTERNOON + Const.CLOSED, hoursPm, 0));
        adapter1 = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, minutes);
        spinnerMinuteOpening.setAdapter(adapter1);
        spinnerMinuteClosed.setAdapter(adapter1);
        spinnerMinuteOpening.setSelection(getElementOfSpinner(index, Const.AFTERNOON + Const.OPEN, minutes, 1));
        spinnerMinuteClosed.setSelection(getElementOfSpinner(index, Const.AFTERNOON + Const.CLOSED, minutes, 1));

    }

    private void setOnItemClickAdapter(final int index) {
        spinnerAmPm.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                switch (position) {
                    case 0:
                        morning_afternoon = false;
                        Toast.makeText(parent.getContext(), "Spinner item 1!", Toast.LENGTH_SHORT).show();
                        initSpinnerMorning(index);
                        break;
                    case 1:
                        morning_afternoon = true;
                        initSpinnerAfternoon(index);

                        Toast.makeText(parent.getContext(), "Spinner item 2!", Toast.LENGTH_SHORT).show();
                        break;

                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        spinnerHourOpening.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                if (!morning_afternoon)
                    morningOpenHour = hoursAm[i];
                else {
                    afternoonOpenHour = hoursPm[i];
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        spinnerMinuteOpening.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                if (!morning_afternoon) {
                    morningOpenMinute = minutes[i];

                } else
                    afternoonOpenMinute = minutes[i];

            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
        spinnerHourClosed.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                if (!morning_afternoon)
                    morningClosedHour = hoursAm[i];
                else {
                    afternoonClosedHour = hoursPm[i];
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
        spinnerMinuteClosed.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                if (!morning_afternoon)
                    morningClosedMinute = minutes[i];
                else {
                    afternoonClosedMinute = minutes[i];
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
    }


    @Override
    public void onClickAddress(int position) {
        adapterAddress.removeElement(position);

    }


    @Override
    public void onDone() {
        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                alertFinish.dismiss();
                finish();
            }
        },1000);

     }

    @Override
    public boolean dispatchTouchEvent(MotionEvent event) {

        if (event.getAction() == MotionEvent.ACTION_DOWN) {
            View v = getCurrentFocus();
            if (mEditTextName.isFocused()) {
                Rect outRect = new Rect();
                mEditTextName.getGlobalVisibleRect(outRect);
                if (!outRect.contains((int) event.getRawX(), (int) event.getRawY())) {
                    mEditTextName.clearFocus();
                    //
                    // Hide keyboard
                    //
                    InputMethodManager imm = (InputMethodManager) v.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
                }
            } else {
                if (mEditTextSurname.isFocused()) {
                    Rect outRect = new Rect();
                    mEditTextName.getGlobalVisibleRect(outRect);
                    if (!outRect.contains((int) event.getRawX(), (int) event.getRawY())) {
                        mEditTextName.clearFocus();
                        //
                        // Hide keyboard
                        //
                        InputMethodManager imm = (InputMethodManager) v.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
                        imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
                    }
                } else {
                    if (mEditTextNote.isFocused()) {
                        Rect outRect = new Rect();
                        mEditTextName.getGlobalVisibleRect(outRect);
                        if (!outRect.contains((int) event.getRawX(), (int) event.getRawY())) {
                            mEditTextName.clearFocus();
                            //
                            // Hide keyboard
                            //
                            InputMethodManager imm = (InputMethodManager) v.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
                            imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
                        }
                    }
                }
            }
        }
        return super.dispatchTouchEvent(event);
    }

    @Override
    public void onBackPressed() {
        showDialogAlert("Sei sicuro di voler uscire", false);

    }
    AlertDialog alertFinish;
    private void showDialogAlert(String title, boolean showProgess) {
        LayoutInflater inflater = LayoutInflater.from(getApplicationContext());
        View dialogView = inflater.inflate(R.layout.alert_dialog, null);
        TextView titleText = dialogView.findViewById(R.id.title);
        titleText.setText(title);
        RelativeLayout.LayoutParams p = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
        ProgressBar progressBar = dialogView.findViewById(R.id.progressBar);
        if (!showProgess) {
            progressBar.setVisibility(View.GONE);
            p.addRule(RelativeLayout.CENTER_IN_PARENT, titleText.getId());
        } else {
            p.addRule(RelativeLayout.ALIGN_PARENT_TOP, titleText.getId());
            p.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM, progressBar.getId());
        }
        AlertDialog.Builder builder1 = new AlertDialog.Builder(addModifyActivity);
        builder1.setCancelable(true);

        builder1.setView(dialogView);
        if (!showProgess)
        builder1.setPositiveButton(
                getApplicationContext().getResources().getString(R.string.ok),
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {

                        dialog.cancel();
                        finish();
                    }
                });


        alertFinish = builder1.create();
        alertFinish.show();
    }
}
