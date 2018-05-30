package com.sachinvarma.betterdoctor.interfaces;

import com.sachinvarma.betterdoctor.model.dataresponse.DoctorsDataModel;
import com.sachinvarma.betterdoctor.services.ApiClient;
import io.reactivex.Observable;
import io.reactivex.Single;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;
import retrofit2.http.Url;

/**
 * Created by User on 26-10-2017.
 */

public interface ApiInterface {

  /**
   * Api call for getting Doctors
   *
   * @return response
   */
  @GET()
  Observable<DoctorsDataModel> getDoctorsList(@Url String url
  );
}
