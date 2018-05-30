package com.sachinvarma.betterdoctor.ui.home;

import android.support.annotation.NonNull;
import com.sachinvarma.betterdoctor.interfaces.ApiInterface;
import com.sachinvarma.betterdoctor.model.dataresponse.DoctorsDataModel;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

public class HomePresenter implements HomeContract.Presenter {

  private HomeContract.View view;
  private ApiInterface apiInterface;
  private final CompositeDisposable mDisposable = new CompositeDisposable();


  public HomePresenter(
    @NonNull final HomeContract.View view, @NonNull final ApiInterface apiInterface) {
    this.view = view;
    this.apiInterface = apiInterface;

  }


  @Override
  public void getDoctorsData(
    @NonNull String url
  ) {
    try {
      apiInterface.getDoctorsList(url)
        .subscribeOn(Schedulers.io())
        .observeOn(AndroidSchedulers.mainThread())
        .subscribe(new Observer<DoctorsDataModel>() {
          @Override
          public void onSubscribe(Disposable d) {
            mDisposable.add(d);
          }

          @Override
          public void onNext(@NonNull DoctorsDataModel response) {
            if (view != null) {
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
              view.noDoctorsFound();
            }
          }

          @Override
          public void onComplete() {

          }
        });
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  //@Override
  public void destroy() {
    // Clearing the observables
    this.mDisposable.dispose();
    // Removing View reference to prevent memory leaks
    this.view = null;
  }
}
