package com.sachinvarma.betterdoctor.ui.searchdoctor;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.res.AssetManager;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.constraint.ConstraintLayout;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatEditText;
import android.support.v7.widget.AppCompatMultiAutoCompleteTextView;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.MultiAutoCompleteTextView.CommaTokenizer;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.ui.PlaceAutocompleteFragment;
import com.google.android.gms.location.places.ui.PlaceSelectionListener;
import com.sachinvarma.betterdoctor.R;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class SearchDoctorActivity extends AppCompatActivity {

  private final String TAG = "HomeActivity";

  @BindView(R.id.toolbar)
  Toolbar toolbar;
  @BindView(R.id.appbar)
  AppBarLayout appbar;
  @BindView(R.id.etDoctorsName)
  AppCompatEditText etDoctorsName;
  @BindView(R.id.etSpecialised)
  AppCompatMultiAutoCompleteTextView etSpecialised;
  @BindView(R.id.etInsurances)
  AppCompatMultiAutoCompleteTextView etInsurances;
  @BindView(R.id.etDisease)
  AppCompatMultiAutoCompleteTextView etDisease;
  @BindView(R.id.rbMale)
  RadioButton rbMale;
  @BindView(R.id.rbFeMale)
  RadioButton rbFeMale;
  @BindView(R.id.rbBoth)
  RadioButton rbBoth;
  @BindView(R.id.rgGender)
  RadioGroup rgGender;
  @BindView(R.id.etLocation)
  AppCompatEditText etLocation;
  @BindView(R.id.clParent)
  ConstraintLayout clParent;
  PlaceAutocompleteFragment autocompleteFragment;
  private List<String> specialisationUidList = new ArrayList<>();
  private List<String> insuranceUidList = new ArrayList<>();
  private List<String> conditionsUidList = new ArrayList<>();
  private List<String> specialisationList = new ArrayList<>();
  private List<String> insuranceList = new ArrayList<>();
  private List<String> conditionsList = new ArrayList<>();
  private String lat = "";
  private String lon = "";

  @Override
  protected void onCreate(@Nullable Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_doctor_search);
    ButterKnife.bind(this);

    setSupportActionBar(toolbar);

    // add back arrow to toolbar
    if (getSupportActionBar() != null) {
      getSupportActionBar().setDisplayHomeAsUpEnabled(true);
      getSupportActionBar().setDisplayShowHomeEnabled(true);
      getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_arrow_back);
    }

    autocompleteFragment = (PlaceAutocompleteFragment) getFragmentManager().findFragmentById(
      R.id.place_autocomplete_fragment);
    try {

      if (getIntent() != null) {
        if (getIntent().hasExtra("lat")) {
          lat = getIntent().getStringExtra("lat");
        }
        if (getIntent().hasExtra("lon")) {
          lon = getIntent().getStringExtra("lon");
        }
        if (getIntent().hasExtra("name")) {
          etDoctorsName.setText(getIntent().getStringExtra("name"));
        }
        if (getIntent().hasExtra("specialty_uid")) {
          etSpecialised.setText(getIntent().getStringExtra("specialty_uid"));
        }
        if (getIntent().hasExtra("insurance_uid")) {
          etInsurances.setText(getIntent().getStringExtra("insurance_uid"));
        }
        if (getIntent().hasExtra("query")) {
          etDisease.setText(getIntent().getStringExtra("query"));
        }
        if (getIntent().hasExtra("locationName")) {
          autocompleteFragment.setText(getIntent().getStringExtra("locationName"));
        }
        if (getIntent().hasExtra("gender")) {
          if (getIntent().getStringExtra("gender").equalsIgnoreCase("male")) {
            rbMale.setChecked(true);
          } else if (getIntent().getStringExtra("gender").equalsIgnoreCase("female")) {
            rbFeMale.setChecked(true);
          } else if (getIntent().getStringExtra("gender").equalsIgnoreCase("both")) {
            rbBoth.setChecked(true);
          }
        }
      }
    } catch (Exception e) {
      e.printStackTrace();
    }

    autocompleteFragment.setHint("Locate In");
    autocompleteFragment.setOnPlaceSelectedListener(new PlaceSelectionListener() {
      @Override
      public void onPlaceSelected(Place place) {
        // TODO: Get info about the selected place.
        Log.i(TAG, "Place: " + place.getName());

        lat = String.valueOf(place.getLatLng().latitude);
        lon = String.valueOf(place.getLatLng().longitude);
      }

      @Override
      public void onError(Status status) {
        // TODO: Handle the error.
        Log.i(TAG, "An error occurred: " + status);
      }
    });

    JSONArray obj = null;
    try {
      obj = new JSONArray(loadContentFromFile(this, "specialisations.json"));
      for (int i = 0; i < obj.length(); i++)

      {
        JSONObject objs = obj.getJSONObject(i);
        specialisationList.add(objs.getString("actor"));
        specialisationUidList.add(objs.getString("uid"));
      }
    } catch (JSONException e) {
      e.printStackTrace();
    }
    ArrayAdapter<String> adapter =
      new ArrayAdapter<String>(this, android.R.layout.select_dialog_item, specialisationList);

    etSpecialised.setThreshold(1);//will start working from first character
    etSpecialised.setTokenizer(new CommaTokenizer());
    etSpecialised.setAdapter(adapter);//setting the adapter data into the AutoCompleteTextView
    etSpecialised.setTextColor(Color.BLACK);

    JSONArray objInsurance = null;
    try {
      objInsurance = new JSONArray(loadContentFromFile(this, "insurances.json"));
      for (int i = 0; i < objInsurance.length(); i++)

      {
        JSONObject objs = objInsurance.getJSONObject(i);
        insuranceList.add(objs.getString("name"));
        insuranceUidList.add(objs.getString("uid"));
      }
    } catch (JSONException e) {
      e.printStackTrace();
    }
    ArrayAdapter<String> adapterInsurance =
      new ArrayAdapter<String>(this, android.R.layout.select_dialog_item, insuranceList);

    etInsurances.setThreshold(1);//will start working from first character
    etInsurances.setTokenizer(new CommaTokenizer());
    etInsurances.setAdapter(
      adapterInsurance);//setting the adapter data into the AutoCompleteTextView
    etInsurances.setTextColor(Color.BLACK);

    JSONArray objConditions = null;
    try {
      objConditions = new JSONArray(loadContentFromFile(this, "conditions.json"));
      for (int i = 0; i < objConditions.length(); i++)

      {
        JSONObject objs = objConditions.getJSONObject(i);
        conditionsList.add(objs.getString("name"));
        conditionsUidList.add(objs.getString("uid"));
      }
    } catch (JSONException e) {
      e.printStackTrace();
    }
    ArrayAdapter<String> adapterConditions =
      new ArrayAdapter<String>(this, android.R.layout.select_dialog_item, conditionsList);

    etDisease.setThreshold(1);//will start working from first character
    etDisease.setTokenizer(new CommaTokenizer());
    etDisease.setAdapter(adapterConditions);//setting the adapter data into the AutoCompleteTextView
    etDisease.setTextColor(Color.BLACK);
  }

  public static InputStream loadInputStreamFromAssetFile(Context context, String fileName) {
    AssetManager am = context.getAssets();
    try {
      InputStream is = am.open(fileName);
      return is;
    } catch (IOException e) {
      e.printStackTrace();
    }
    return null;
  }

  public static String loadContentFromFile(Context context, String path) {
    String content = null;
    try {
      InputStream is = loadInputStreamFromAssetFile(context, path);
      int size = is.available();
      byte[] buffer = new byte[size];
      is.read(buffer);
      is.close();
      content = new String(buffer, "UTF-8");
    } catch (IOException ex) {
      ex.printStackTrace();
      return null;
    }
    return content;
  }

  @OnClick(R.id.btSearch)
  void onClickAction(final View view) {

    switch (view.getId()) {

      case R.id.btSearch:

        if (TextUtils.isEmpty(lat) && TextUtils.isEmpty(lon) && TextUtils.isEmpty(
          etDisease.getText().toString()) && TextUtils.isEmpty(
          etDoctorsName.getText().toString())) {
          Snackbar.make(clParent, "Please provide Location or Name or Symptom to proceed.", 3000)
            .show();
        } else {
          Intent returnIntent = new Intent();

          if (!TextUtils.isEmpty(lat) && !TextUtils.isEmpty(lon)) {
            returnIntent.putExtra("location", lat + "," + lon + ",100");
          }
          if (!TextUtils.isEmpty(etDoctorsName.getText().toString())) {

            returnIntent.putExtra("name", etDoctorsName.getText().toString());
          }
          if (!TextUtils.isEmpty(((EditText) autocompleteFragment.getView()
            .findViewById(R.id.place_autocomplete_search_input)).getText().toString())) {

            returnIntent.putExtra("locationName", ((EditText) autocompleteFragment.getView()
              .findViewById(R.id.place_autocomplete_search_input)).getText().toString());
          } else {
            returnIntent.removeExtra("location");
          }
          if (!TextUtils.isEmpty(etSpecialised.getText().toString())) {

            List<String> selectedSpecialisedList =
              new ArrayList<String>(Arrays.asList(etSpecialised.getText().toString().split(",")));
            List<String> finalSelectedSpecialisedList = new ArrayList<>();
            if (specialisationList != null
              && specialisationList.size() > 0
              && specialisationUidList != null
              && specialisationUidList.size() > 0
              && specialisationUidList.size() == specialisationList.size()
              && selectedSpecialisedList.size() > 0) {

              for (int i = 0; i < selectedSpecialisedList.size(); i++) {
                for (int j = 0; j < specialisationList.size(); j++) {
                  if (specialisationList.get(j).equalsIgnoreCase(selectedSpecialisedList.get(i))) {

                    finalSelectedSpecialisedList.add(specialisationUidList.get(j));
                  }
                }
              }

              if (finalSelectedSpecialisedList.size() > 0) {
                returnIntent.putExtra("specialty_uid",
                  android.text.TextUtils.join(",", finalSelectedSpecialisedList));
              }
            }
          }

          if (!TextUtils.isEmpty(etInsurances.getText().toString())) {

            List<String> selectedInsurancesList =
              new ArrayList<String>(Arrays.asList(etInsurances.getText().toString().split(",")));
            List<String> finalSelectedInsuranceList = new ArrayList<>();
            if (insuranceList != null
              && insuranceList.size() > 0
              && insuranceUidList != null
              && insuranceUidList.size() > 0
              && insuranceUidList.size() == insuranceList.size()
              && selectedInsurancesList.size() > 0) {

              for (int i = 0; i < selectedInsurancesList.size(); i++) {
                for (int j = 0; j < insuranceList.size(); j++) {
                  if (insuranceList.get(j).equalsIgnoreCase(selectedInsurancesList.get(i))) {

                    finalSelectedInsuranceList.add(insuranceUidList.get(j));
                  }
                }
              }

              if (finalSelectedInsuranceList.size() > 0) {
                returnIntent.putExtra("insurance_uid",
                  android.text.TextUtils.join(",", finalSelectedInsuranceList));
              }
            }
          }

          if (!TextUtils.isEmpty(etDisease.getText().toString())) {

            List<String> selectedDiseaseList =
              new ArrayList<String>(Arrays.asList(etDisease.getText().toString().split(",")));
            List<String> finalSelectedConditionsList = new ArrayList<>();
            if (conditionsList != null
              && conditionsList.size() > 0
              && conditionsUidList != null
              && conditionsUidList.size() > 0
              && conditionsUidList.size() == conditionsList.size()
              && selectedDiseaseList.size() > 0) {

              for (int i = 0; i < selectedDiseaseList.size(); i++) {
                for (int j = 0; j < conditionsList.size(); j++) {
                  if (conditionsList.get(j).equalsIgnoreCase(selectedDiseaseList.get(i))) {

                    finalSelectedConditionsList.add(conditionsUidList.get(j));
                  }
                }
              }

              if (finalSelectedConditionsList.size() > 0) {
                returnIntent.putExtra("query",
                  android.text.TextUtils.join(",", finalSelectedConditionsList));
              }
            }
          }

          String gender = "";
          if (rbMale.isChecked()) {
            gender = "male";
          } else if (rbFeMale.isChecked()) {
            gender = "female";
          } else if (rbBoth.isChecked()) {
            gender = "both";
          }
          returnIntent.putExtra("gender", gender);
          setResult(Activity.RESULT_OK, returnIntent);
          finish();
        }
        break;
    }
  }

  @Override
  public void onBackPressed() {
    Intent returnIntent = new Intent();
    setResult(Activity.RESULT_CANCELED, returnIntent);
    finish();
  }

  @Override
  public boolean onOptionsItemSelected(MenuItem item) {
    // handle arrow click here
    if (item.getItemId() == android.R.id.home) {
      onBackPressed(); // close this activity and return to preview activity (if there is any)
    }

    return super.onOptionsItemSelected(item);
  }

  @Override
  public boolean onCreateOptionsMenu(Menu menu) {
    // Inflate the menu; this adds items to the action bar if it is present.
    getMenuInflater().inflate(R.menu.dummy_menu, menu);
    return true;
  }
}
