package com.sachinvarma.betterdoctor.ui.home;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.IntentSender.SendIntentException;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationManager;
import android.os.Build.VERSION;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.constraint.ConstraintLayout;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.NavigationView;
import android.support.design.widget.NavigationView.OnNavigationItemSelectedListener;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.Toast;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import com.github.florent37.fiftyshadesof.FiftyShadesOf;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.GoogleApiClient.Builder;
import com.google.android.gms.common.api.PendingResult;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationSettingsResult;
import com.sachinvarma.betterdoctor.R;
import com.sachinvarma.betterdoctor.helper.EndlessScrollListener;
import com.sachinvarma.betterdoctor.interfaces.ApiInterface;
import com.sachinvarma.betterdoctor.model.dataresponse.DoctorsDataModel;
import com.sachinvarma.betterdoctor.model.dataresponse.DoctorsListModel;
import com.sachinvarma.betterdoctor.services.ApiClient;
import com.sachinvarma.betterdoctor.ui.home.HomeContract.View;
import com.sachinvarma.betterdoctor.ui.searchdoctor.SearchDoctorActivity;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class HomeActivity extends AppCompatActivity
  implements HomeContract.View, OnNavigationItemSelectedListener, GoogleApiClient.ConnectionCallbacks,
  GoogleApiClient.OnConnectionFailedListener, LocationListener {

  private static final String TAG = "HomeActivity";
  @BindView(R.id.toolbar)
  Toolbar toolbar;
  @BindView(R.id.appbar)
  AppBarLayout appbar;
  @BindView(R.id.rvDoctors)
  RecyclerView rvDoctors;
  @BindView(R.id.nav_view)
  NavigationView navView;
  @BindView(R.id.drawer_layout)
  DrawerLayout drawerLayout;
  @BindView(R.id.llParent)
  LinearLayout llParent;
  @BindView(R.id.progressBar)
  ProgressBar progressBar;
  @BindView(R.id.cl_filter)
  ConstraintLayout cl_filter;
  private HomePresenter homePresenter;
  private String limit = "10";
  private String pageCount = "-10";

  private GoogleApiClient googleApiClient;
  private String name = "";
  private String first_name = "";
  private String last_name = "";
  private String query = "";
  private String specialty_uid = "";
  private String insurance_uid = "";
  private String practice_uid = "";
  private String location = "";
  private String user_location = "";
  private String gender = "";
  private String sort = "";
  private String fields = "";
  private String locationName = "";

  private boolean isLoading = false;
  final FiftyShadesOf fiftyShadesOf = FiftyShadesOf.with(this);
  private LocationRequest locationRequest;
  private List<DoctorsListModel> doctorsDataList = new ArrayList<>();

  @Override
  protected void onCreate(@Nullable Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_home);
    ButterKnife.bind(this);
    initLocation();
    init();
  }

  @OnClick({ R.id.btFilterEdit, R.id.btFilterRemove })
  public void onClick(final android.view.View view) {

    switch (view.getId()) {

      case R.id.btFilterEdit:
        Intent intent = new Intent(this, SearchDoctorActivity.class);
        if (!TextUtils.isEmpty(name)) {
          intent.putExtra("name", name);
        }
        if (!TextUtils.isEmpty(specialty_uid)) {
          intent.putExtra("specialty_uid", specialty_uid);
        }
        if (!TextUtils.isEmpty(insurance_uid)) {
          intent.putExtra("insurance_uid", insurance_uid);
        }
        if (!TextUtils.isEmpty(query)) {
          intent.putExtra("query", query);
        }
        if (!TextUtils.isEmpty(gender)) {
          intent.putExtra("gender", gender);
        }
        if (!TextUtils.isEmpty(locationName)) {
          intent.putExtra("locationName", locationName);
        }
        try {
          if (!TextUtils.isEmpty(location)) {
            String[] locations = location.split(",");
            if (locations[0] != null
              && !TextUtils.isEmpty(locations[0])
              && locations[1] != null
              && !TextUtils.isEmpty(locations[1])) {
              intent.putExtra("lat", locations[0]);
              intent.putExtra("lon", locations[1]);
            }
          }
        } catch (Exception e) {
          e.printStackTrace();
        }
        startActivityForResult(intent, 100);

        break;

      case R.id.btFilterRemove:

        clearAllData();
        rvDoctors.setVisibility(android.view.View.GONE);
        rvDoctors.setNestedScrollingEnabled(false);
        cl_filter.setVisibility(android.view.View.GONE);
        llParent.setVisibility(android.view.View.VISIBLE);
        fiftyShadesOf.on(R.id.layout, R.id.layout1, R.id.layout2, R.id.layout3, R.id.layout4)
          .start();
        initLocation();

        break;
    }
  }

  @Override
  public void setDoctorsData(DoctorsDataModel doctorsDataModel) {
    isLoading = false;
    progressBar.setVisibility(android.view.View.GONE);
    if (!isFinishing()
      && doctorsDataModel != null
      && doctorsDataModel.data != null
      && doctorsDataModel.data.size() > 0) {
      rvDoctors.setVisibility(android.view.View.VISIBLE);
      fiftyShadesOf.stop();
      llParent.setVisibility(android.view.View.GONE);
      if (doctorsDataList == null || doctorsDataList.size() <= 0) {
        if (doctorsDataList != null) {
          doctorsDataList.addAll(doctorsDataModel.data);
        }
        rvDoctors.setAdapter(new HomeDoctorsRecyclerAdapter(doctorsDataList));
      } else {
        doctorsDataList.addAll(doctorsDataModel.data);
        rvDoctors.getAdapter().notifyDataSetChanged();
        //((HomeDoctorsRecyclerAdapter) rvDoctors.getAdapter()).updateList(doctorsDataModel.data);
      }
    } else {
      fiftyShadesOf.stop();
      llParent.setVisibility(android.view.View.GONE);
      if (doctorsDataList.size() <= 0) {
        Snackbar.make(drawerLayout, "Oops! No Doctor's Found", 3000).show();
      }

      pageCount = String.valueOf(Integer.parseInt(pageCount) - 10);
    }
  }

  @Override
  public void noDoctorsFound() {
    isLoading = false;
    progressBar.setVisibility(android.view.View.GONE);

    pageCount = String.valueOf(Integer.parseInt(pageCount) - 10);
    fiftyShadesOf.stop();
    llParent.setVisibility(android.view.View.GONE);
    if (doctorsDataList.size() <= 0) {
      Snackbar.make(drawerLayout, "Oops! No Doctor's Found", 3000).show();
    }
  }

  @Override
  public boolean onOptionsItemSelected(MenuItem item) {

    switch (item.getItemId()) {

      case R.id.action_search:

        //Dialog dialog = new Dialog(this);
        //dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        //dialog.getWindow()
        //  .setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        //WindowManager.LayoutParams wmlp = dialog.getWindow().getAttributes();
        //wmlp.width = android.view.WindowManager.LayoutParams.MATCH_PARENT;
        //wmlp.height = WindowManager.LayoutParams.WRAP_CONTENT;
        //dialog.setContentView(R.layout.activity_doctor_search);
        //
        //Window window = dialog.getWindow();
        //window.setLayout(RelativeLayout.LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
        //dialog.show();
        startActivityForResult(new Intent(this, SearchDoctorActivity.class), 100);

        break;
    }

    return false;
  }

  @Override
  public boolean onCreateOptionsMenu(Menu menu) {
    // Inflate the menu; this adds items to the action bar if it is present.
    getMenuInflater().inflate(R.menu.main_menu, menu);
    return true;
  }

  @Override
  public void onBackPressed() {
    if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
      drawerLayout.closeDrawer(GravityCompat.START);
    } else {
      super.onBackPressed();
    }
  }

  public void init() {

    setSupportActionBar(toolbar);

    ActionBarDrawerToggle toggle =
      new ActionBarDrawerToggle(this, drawerLayout, toolbar, R.string.navigation_drawer_open,
        R.string.navigation_drawer_close);
    drawerLayout.addDrawerListener(toggle);
    toggle.syncState();

    navView.setNavigationItemSelectedListener(this);

    llParent.setVisibility(android.view.View.VISIBLE);
    fiftyShadesOf.on(R.id.layout, R.id.layout1, R.id.layout2, R.id.layout3, R.id.layout4).start();

    homePresenter = new HomePresenter(this, ApiClient.getClient().create(ApiInterface.class));
    rvDoctors.setLayoutManager(new LinearLayoutManager(this));
    rvDoctors.addOnScrollListener(new EndlessScrollListener() {
      @Override
      public void onLastItemVisible() {
        if (!isLoading) {
          isLoading = true;
          getDoctorsData();
        }
      }
    });
    rvDoctors.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.VERTICAL));

  }

  public void dismissPb() {

  }

  public void showPb() {

  }

  public void showToast(String message) {

    Snackbar.make(drawerLayout, message, 3000).show();
  }

  public void onFailure(
    Object call, Object throwable, Object callback, Object additionalData
  ) {

  }

  public void showRetryDialog(
    Object object1, Object object3, String message
  ) {

  }

  @SuppressWarnings("StatementWithEmptyBody")
  @Override
  public boolean onNavigationItemSelected(@NonNull MenuItem item) {
    // Handle navigation view item clicks here.
    int id = item.getItemId();

    DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
    drawer.closeDrawer(GravityCompat.START);
    return false;
  }

  private void getDoctorsData() {
    pageCount = String.valueOf(Integer.parseInt(pageCount) + 10);
    if (!pageCount.equalsIgnoreCase("0")) {
      progressBar.setVisibility(android.view.View.VISIBLE);
    }
    String url =
      "https://api.betterdoctor.com/2016-03-01/doctors?"/*location=37.773,-122.413,100&skip="+pageCount+"&limit="+limit+"&user_key=df5e0bcdc894eb54343ae4daaaf618eb"*/;

    //getAddress("37.773", "-122.413");

    if (!TextUtils.isEmpty(name)) {
      url = url + "name=" + name + "&";
    }
    if (!TextUtils.isEmpty(first_name)) {
      url = url + "first_name=" + first_name + "&";
    }
    if (!TextUtils.isEmpty(last_name)) {
      url = url + "last_name=" + last_name + "&";
    }
    if (!TextUtils.isEmpty(query)) {
      url = url + "query=" + query + "&";
    }
    if (!TextUtils.isEmpty(specialty_uid)) {
      url = url + "specialty_uid=" + specialty_uid + "&";
    }
    if (!TextUtils.isEmpty(insurance_uid)) {
      url = url + "insurance_uid=" + insurance_uid + "&";
    }
    if (!TextUtils.isEmpty(practice_uid)) {
      url = url + "practice_uid=" + practice_uid + "&";
    }
    if (!TextUtils.isEmpty(location)) {
      url = url + "location=" + location + "&";
    }
    if (!TextUtils.isEmpty(user_location)) {
      url = url + "user_location=" + user_location + "&";
    }
    if (!TextUtils.isEmpty(gender)) {
      if (!gender.equalsIgnoreCase("both")) {
        url = url + "gender=" + gender + "&";
      }
    }
    if (!TextUtils.isEmpty(sort)) {
      url = url + "sort=" + sort + "&";
    }
    if (!TextUtils.isEmpty(fields)) {
      url = url + "fields=" + fields + "&";
    }
    if (!TextUtils.isEmpty(pageCount)) {
      url = url + "skip=" + pageCount + "&";
    }
    if (!TextUtils.isEmpty(this.limit)) {
      url = url + "limit=" + this.limit + "&";
    }

    url = url + "user_key=" + getString(R.string.app_user_key);

    homePresenter.getDoctorsData(url);

    //homePresenter.getDoctorsData("", "", "", "", "", "", "", "37.773,-122.413,100",
    //  "37.773,-122.413", "male", "full-name-asc", "", pageCount, limit,
    //  getString(R.string.app_user_key));
  }

  @Override
  public void onStart() {
    super.onStart();
  }

  @Override
  public void onStop() {
    super.onStop();
    if (this.googleApiClient != null) {
      this.stopLocationUpdate();
      this.googleApiClient.unregisterConnectionCallbacks(this);
      this.googleApiClient.unregisterConnectionFailedListener(this);
      if (this.googleApiClient.isConnected()) {
        LocationServices.FusedLocationApi.removeLocationUpdates(this.googleApiClient, this);
      }

      this.googleApiClient.disconnect();
      this.googleApiClient = null;
    }

    super.onStop();
    Log.d("EASYLOCATION", "On Stop");
  }

  public void getAddress(String lat, String lng) {
    Geocoder geocoder = new Geocoder(this, Locale.getDefault());
    try {
      List<Address> addresses =
        geocoder.getFromLocation(Double.parseDouble(lat), Double.parseDouble(lng), 1);
      Address obj = addresses.get(0);

      if (obj.getCountryCode().equalsIgnoreCase("US")) {
        location = lat + "," + lng + ",100";
        getDoctorsData();
      } else {
        showToast("Services is provided only in USA");
        getAddress("37.773", "-122.413");
      }

      Log.v("IGA", "Address : " + obj.getCountryCode() + " " + obj.getCountryName());
    } catch (IOException e) {
      // TODO Auto-generated catch block
      e.printStackTrace();
      Toast.makeText(this, e.getMessage(), Toast.LENGTH_SHORT).show();
    }
  }

  private void getLocationUpdate() {
    if (googleApiClient == null) {
      initLocation();
    } else if (googleApiClient.isConnected()) {
      startLocationUpdate();
    } else if (!googleApiClient.isConnected()) {
      connectGoogleAPIClient();
    }
  }

  /**
   * Function to connect to Google API client. Also, we create a CountDownTimer which detects if
   * we getCartProductDetail location in 10 seconds
   */
  private void connectGoogleAPIClient() {
    if (isGPSActivated()) {
      //showProgress(getString(R.string.please_wait));
      googleApiClient.connect();
    }
  }

  private boolean isGPSActivated() {
    return ((LocationManager) getSystemService(Context.LOCATION_SERVICE)).isProviderEnabled(
      LocationManager.GPS_PROVIDER);
  }

  /**
   * Function to stop listening for location updates
   */
  private void stopLocationUpdate() {
    if (googleApiClient != null && googleApiClient.isConnected()) {
      Log.d(TAG, "Stopping location update service");
      LocationServices.FusedLocationApi.removeLocationUpdates(googleApiClient, this);
    }
  }

  public void onRequestPermissionsResult(
    int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults
  ) {
    super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    if (requestCode == 100) {
      if (ActivityCompat.checkSelfPermission(this, "android.permission.ACCESS_FINE_LOCATION")
        == 0) {
        this.initializeFusionLocation();
      } else {
        Toast.makeText(this, "Oops! , Permission denied can't access Location", Toast.LENGTH_SHORT)
          .show();
        this.finish();
      }
    }
  }

  protected void onActivityResult(int requestCode, int resultCode, Intent data) {
    super.onActivityResult(requestCode, resultCode, data);
    if (requestCode == 1) {
      this.initLocation();
    } else if (requestCode == 100) {

      if (resultCode == Activity.RESULT_OK) {
        clearAllData();
        if (data.hasExtra("gender")) {
          if (!TextUtils.isEmpty(data.getStringExtra("gender"))) {
            gender = data.getStringExtra("gender");
          } else {
            gender = "";
          }
        }

        if (data.hasExtra("location")) {
          if (!TextUtils.isEmpty(data.getStringExtra("location"))) {
            location = data.getStringExtra("location");
          } else {
            location = "";
          }
        } else {
          location = "";
        }

        if (data.hasExtra("name")) {
          if (!TextUtils.isEmpty(data.getStringExtra("name"))) {
            name = data.getStringExtra("name");
          } else {
            name = "";
          }
        } else {
          name = "";
        }

        if (data.hasExtra("specialty_uid")) {
          if (!TextUtils.isEmpty(data.getStringExtra("specialty_uid"))) {
            specialty_uid = data.getStringExtra("specialty_uid");
          } else {
            specialty_uid = "";
          }
        } else {
          specialty_uid = "";
        }

        if (data.hasExtra("insurance_uid")) {
          if (!TextUtils.isEmpty(data.getStringExtra("insurance_uid"))) {
            insurance_uid = data.getStringExtra("insurance_uid");
          } else {
            insurance_uid = "";
          }
        } else {
          insurance_uid = "";
        }

        if (data.hasExtra("query")) {
          if (!TextUtils.isEmpty(data.getStringExtra("query"))) {
            query = data.getStringExtra("query");
          } else {
            query = "";
          }
        } else {
          query = "";
        }

        if (data.hasExtra("locationName")) {
          if (!TextUtils.isEmpty(data.getStringExtra("locationName"))) {
            locationName = data.getStringExtra("locationName");
          } else {
            locationName = "";
          }
        } else {
          locationName = "";
        }

        rvDoctors.setVisibility(android.view.View.GONE);
        llParent.setVisibility(android.view.View.VISIBLE);
        fiftyShadesOf.on(R.id.layout, R.id.layout1, R.id.layout2, R.id.layout3, R.id.layout4)
          .start();
        cl_filter.setVisibility(android.view.View.VISIBLE);
        getDoctorsData();
      }
      if (resultCode == Activity.RESULT_CANCELED) {
        //Write your code if there's no result
      }
    }
  }

  private void initLocation() {
    if (this.isGooglePlayServicesAvailable()) {
      if (VERSION.SDK_INT >= 23) {
        int locationPermission =
          ContextCompat.checkSelfPermission(this, "android.permission.ACCESS_FINE_LOCATION");
        List<String> permissions = new ArrayList();
        if (locationPermission == -1) {
          permissions.add("android.permission.ACCESS_FINE_LOCATION");
          permissions.add("android.permission.ACCESS_COARSE_LOCATION");
        }

        if (locationPermission == -1) {
          ActivityCompat.requestPermissions(this,
            (String[]) permissions.toArray(new String[permissions.size()]), 100);
        } else {
          this.initializeFusionLocation();
        }
      } else {
        this.initializeFusionLocation();
      }
    } else {
      Toast.makeText(this, "Google Play Service is not available.", Toast.LENGTH_SHORT).show();
      this.finish();
    }
  }

  private boolean isGooglePlayServicesAvailable() {
    int status = GoogleApiAvailability.getInstance().isGooglePlayServicesAvailable(this);
    if (0 == status) {
      return true;
    } else {
      if (GoogleApiAvailability.getInstance().isUserResolvableError(status)) {
        GoogleApiAvailability.getInstance().getErrorDialog(this, status, 1000).show();
      }

      return false;
    }
  }

  private void initializeFusionLocation() {
    if (this.googleApiClient == null) {
      this.googleApiClient = (new Builder(this)).addApi(LocationServices.API)
        .addConnectionCallbacks(this)
        .addOnConnectionFailedListener(this)
        .build();
      this.googleApiClient.connect();
    }

    this.locationChecker(this.googleApiClient);
  }

  public void onConnected(@Nullable Bundle bundle) {
  }

  public void onConnectionSuspended(int i) {
  }

  public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
  }

  public void onLocationChanged(Location location) {
    Log.d("NewLocation", location.getLatitude() + " : " + location.getLongitude());

    getAddress(String.valueOf(location.getLatitude()), String.valueOf(location.getLongitude()));
    stopLocationUpdate();
  }

  public void locationChecker(GoogleApiClient mGoogleApiClient) {
    this.locationRequest = LocationRequest.create();
    this.locationRequest.setPriority(100);
    int timeInterval = 100000;
    this.locationRequest.setInterval((long) timeInterval);
    int fastestTimeInterval = 10000;
    this.locationRequest.setFastestInterval((long) fastestTimeInterval);
    com.google.android.gms.location.LocationSettingsRequest.Builder builder =
      (new com.google.android.gms.location.LocationSettingsRequest.Builder()).addLocationRequest(
        this.locationRequest);
    builder.setAlwaysShow(true);
    PendingResult<LocationSettingsResult> result =
      LocationServices.SettingsApi.checkLocationSettings(mGoogleApiClient, builder.build());
    result.setResultCallback(new ResultCallback<LocationSettingsResult>() {
      @SuppressLint({ "RestrictedApi" })
      public void onResult(@NonNull LocationSettingsResult result) {
        Status status = result.getStatus();
        switch (status.getStatusCode()) {
          case 0:
            getLocationUpdate();
            break;
          case 6:
            try {
              startIntentSenderForResult(status.getResolution().getIntentSender(), 1, (Intent) null,
                0, 0, 0, (Bundle) null);
            } catch (SendIntentException var5) {

            }
          case 16:
          case 8502:
        }
      }
    });
  }

  public void activityVisible() {
    if (this.googleApiClient == null) {
      this.initLocation();
    } else if (this.googleApiClient.isConnected()) {
      this.startLocationUpdate();
    } else if (!this.googleApiClient.isConnected()) {
      this.connectGoogleAPIClient();
    }
  }

  private void startLocationUpdate() {
    if (ActivityCompat.checkSelfPermission(this, "android.permission.ACCESS_FINE_LOCATION") == 0) {
      if (this.isGPSActivated()) {
        Log.d("EASYLOCATION", "Starting location update service");
        LocationServices.FusedLocationApi.requestLocationUpdates(this.googleApiClient,
          locationRequest, this);
        this.connectGoogleAPIClient();
      } else {
        this.initializeFusionLocation();
      }
    }
  }

  protected void onDestroy() {
    super.onDestroy();
  }

  private void clearAllData() {
    name = "";
    first_name = "";
    last_name = "";
    query = "";
    specialty_uid = "";
    insurance_uid = "";
    practice_uid = "";
    location = "";
    user_location = "";
    gender = "";
    sort = "";
    fields = "";
    doctorsDataList.clear();
    pageCount = "-10";
    locationName = "";
  }

  //@Override
  public void errorOccurred(@NonNull String error) {
    Toast.makeText(this, error, Toast.LENGTH_SHORT).show();
  }
}
