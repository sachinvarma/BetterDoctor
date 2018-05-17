package com.sachinvarma.betterdoctor.interfaces;

import android.support.annotation.NonNull;

public interface BasePresenter<T extends BaseView> {

  void attachView(@NonNull final T view,@NonNull final ApiInterface apiInterface );

  void detach();
}
