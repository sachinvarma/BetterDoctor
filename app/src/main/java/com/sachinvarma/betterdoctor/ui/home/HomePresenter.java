package com.sachinvarma.betterdoctor.ui.home;

import android.support.annotation.NonNull;
import com.sachinvarma.betterdoctor.interfaces.ApiInterface;
import com.sachinvarma.betterdoctor.model.dataresponse.DoctorsDataModel;
import com.sachinvarma.betterdoctor.ui.home.HomeContract.View;
import io.reactivex.SingleObserver;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class HomePresenter implements HomeContract.Presenter {

  private HomeContract.View view;
  private ApiInterface apiInterface;
  private final CompositeDisposable mDisposable = new CompositeDisposable();

  public HomePresenter(
    @NonNull final HomeContract.View view, @NonNull final ApiInterface apiInterface
  ) {
    this.view = view;
    this.apiInterface = apiInterface;
  }

  //@Override
  //public void attachView(@NonNull final View view, @NonNull final ApiInterface apiInterface) {
  //  this.view = view;
  //  this.apiInterface = apiInterface;
  //}
  //
  //@Override
  //public void detach() {
  //  this.view = null;
  //}

  @Override
  public void getDoctorsData(
   /* @NonNull final String name,
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
    @NonNull final String user_key*/
    @NonNull String url
  ) {

    try {
      apiInterface.getDoctorsList(url)
        .subscribeOn(Schedulers.io())
        .observeOn(AndroidSchedulers.mainThread())
        .subscribe(new SingleObserver<DoctorsDataModel>() {
          @Override
          public void onSubscribe(Disposable d) {

          }

          @Override
          public void onSuccess(@NonNull DoctorsDataModel response) {
            if (view != null) {
              //view.dismissPb();
              if (response.data != null && !response.data.isEmpty()) {
                view.setDoctorsData(response);
              } else {
                view.noDoctorsFound();
              }
            }
          }

          @Override
          public void onError(Throwable e) {
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
