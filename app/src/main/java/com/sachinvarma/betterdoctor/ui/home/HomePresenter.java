package com.sachinvarma.betterdoctor.ui.home;

import android.support.annotation.NonNull;
import com.sachinvarma.betterdoctor.interfaces.ApiInterface;
import com.sachinvarma.betterdoctor.model.dataresponse.DoctorsDataModel;
import com.sachinvarma.betterdoctor.ui.home.HomeContract.View;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class HomePresenter implements HomeContract.Presenter {

  private HomeContract.View view;
  private ApiInterface apiInterface;

  //public HomePresenter(
  //  @NonNull final HomeContract.View view, @NonNull final ApiInterface apiInterface
  //) {
  //  this.view = view;
  //  this.apiInterface = apiInterface;
  //}
  //
  @Override
  public void attachView(@NonNull final View view, @NonNull final ApiInterface apiInterface) {
    this.view = view;
    this.apiInterface = apiInterface;
  }

  @Override
  public void detach() {
    this.view = null;
  }

  @Override
  public void getDoctorsData(
    @NonNull final String name,
    @NonNull final String first_name,
    @NonNull final String last_name,
    @NonNull final String query,
    @NonNull final String specialty_uid,
    @NonNull final String insurance_uid,
    @NonNull final String practice_uid,
    @NonNull final String location,
    @NonNull final String user_location,
    @NonNull final String gender,
    @NonNull final String sort,
    @NonNull final String fields,
    @NonNull final String skip,
    @NonNull final String limit,
    @NonNull final String user_key
  ) {

    try {
      apiInterface.getDoctorsList(name, first_name, last_name, query, specialty_uid, insurance_uid,
        practice_uid, location, user_location, gender, sort, fields, skip, limit, user_key)
        .enqueue(new Callback<DoctorsDataModel>() {
          @Override
          public void onResponse(
            @NonNull Call<DoctorsDataModel> call, @NonNull Response<DoctorsDataModel> response
          ) {
            if (view != null) {
              //view.dismissPb();
              if (response.isSuccessful() && response.body() != null) {
                if (response.body() != null && !response.body().data.isEmpty()) {
                  view.setDoctorsData(response.body());
                } else if (response.body() != null) {
                  view.noDoctorsFound();
                }
              } else {
                view.noDoctorsFound();
                //view.onFailure(call, null, this, null);
              }
            }
          }

          @Override
          public void onFailure(@NonNull Call<DoctorsDataModel> call, @NonNull Throwable t) {
            if (view != null) {
              //view.dismissPb();
              view.noDoctorsFound();
              //view.onFailure(call, t, this, null);
            }
          }
        });
    } catch (Exception e) {
      e.printStackTrace();
    }
  }
}
