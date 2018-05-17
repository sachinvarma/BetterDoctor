package com.sachinvarma.betterdoctor.ui.home;

import android.support.annotation.NonNull;
import com.sachinvarma.betterdoctor.interfaces.BasePresenter;
import com.sachinvarma.betterdoctor.interfaces.BaseView;
import com.sachinvarma.betterdoctor.model.dataresponse.DoctorsDataModel;

public class HomeContract {

  public interface View extends BaseView {
    /**
     * Will give the response having the doctors data
     *
     * @param doctorsDataModel Response
     */
    void setDoctorsData(final DoctorsDataModel doctorsDataModel);

    void noDoctorsFound();
  }

  public interface Presenter extends BasePresenter<View> {

    void getDoctorsData(
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
    );
  }
}
