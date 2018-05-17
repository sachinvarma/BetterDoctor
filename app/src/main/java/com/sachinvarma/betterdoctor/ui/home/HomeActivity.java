package com.sachinvarma.betterdoctor.ui.home;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.RecyclerView.ItemDecoration;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;
import butterknife.BindView;
import butterknife.ButterKnife;
import com.sachinvarma.betterdoctor.R;
import com.sachinvarma.betterdoctor.interfaces.ApiInterface;
import com.sachinvarma.betterdoctor.model.dataresponse.DoctorsDataModel;
import com.sachinvarma.betterdoctor.services.ApiClient;
import com.sachinvarma.betterdoctor.ui.home.HomeContract.View;

public class HomeActivity extends AppCompatActivity
  implements View, NavigationView.OnNavigationItemSelectedListener {

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

  private HomePresenter homePresenter;

  @Override
  protected void onCreate(@Nullable Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_home);
    ButterKnife.bind(this);
    init();
  }

  @Override
  public void setDoctorsData(DoctorsDataModel doctorsDataModel) {

    if (!isFinishing() && doctorsDataModel != null && doctorsDataModel.data != null) {
      rvDoctors.setLayoutManager(new LinearLayoutManager(this));
      rvDoctors.setAdapter(new HomeDoctorsRecyclerAdapter(doctorsDataModel.data));
      rvDoctors.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.VERTICAL));
    }
  }

  @Override
  public void noDoctorsFound() {

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

    homePresenter = new HomePresenter(this, ApiClient.getClient().create(ApiInterface.class));

    homePresenter.getDoctorsData("", "", "", "", "", "", "", "37.773,-122.413,100",
      "37.773,-122.413", "male", "full-name-asc", "", "", "10", getString(R.string.app_user_key));
  }

  public void dismissPb() {

  }

  public void showPb() {

  }

  public void showToast(String message) {

    Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
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
}
