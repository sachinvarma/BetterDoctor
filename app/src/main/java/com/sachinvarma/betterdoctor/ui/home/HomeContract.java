package com.sachinvarma.betterdoctor.ui.home;

import android.support.annotation.NonNull;
import com.sachinvarma.betterdoctor.interfaces.BasePresenter;
import com.sachinvarma.betterdoctor.interfaces.BaseView;
import com.sachinvarma.betterdoctor.model.dataresponse.DoctorsDataModel;

public class HomeContract {

  public interface View /*extends BaseView*/ {
    /**
     * Will give the response having the doctors data
     *
     * @param doctorsDataModel Response
     */
    void setDoctorsData(final DoctorsDataModel doctorsDataModel);

    void noDoctorsFound();
  }

  public interface Presenter /*extends BasePresenter*/ {

    void getDoctorsData(
     @NonNull String url
    );
  }
}
