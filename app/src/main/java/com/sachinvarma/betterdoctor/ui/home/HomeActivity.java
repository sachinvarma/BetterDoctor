package com.sachinvarma.betterdoctor.ui.home;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.AppBarLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.widget.Toast;
import butterknife.BindView;
import butterknife.ButterKnife;
import com.sachinvarma.betterdoctor.R;
import com.sachinvarma.betterdoctor.model.datarequest.GetDoctorRequestModel;
import com.sachinvarma.betterdoctor.model.dataresponse.DoctorsDataModel;
import com.sachinvarma.betterdoctor.model.dataresponse.DoctorsListModel;
import com.sachinvarma.betterdoctor.ui.home.HomeContract.View;
import java.util.List;

public class HomeActivity extends AppCompatActivity implements HomeContract.View {

  @BindView(R.id.toolbar)
  Toolbar toolbar;
  @BindView(R.id.appbar)
  AppBarLayout appbar;
  @BindView(R.id.rvDoctors)
  RecyclerView rvDoctors;

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

  }

  @Override
  public void noDoctorsFound() {

  }

  public void init() {

    //homePresenter = new HomePresenter(this);
    //homePresenter.attachView(this);

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
}
