package com.sachinvarma.betterdoctor.interfaces;

import com.sachinvarma.betterdoctor.model.dataresponse.DoctorsDataModel;
import com.sachinvarma.betterdoctor.services.ApiClient;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

/**
 * Created by User on 26-10-2017.
 */

public interface ApiInterface {

  /**
   * Api call for getting Doctors
   *
   * @return response
   */
  @GET(ApiClient.BASE_URL + "doctors")
  Call<DoctorsDataModel> getDoctorsList(
    @Query("name") String name,
    @Query("first_name") String first_name,
    @Query("last_name") String last_name,
    @Query("query") String query,
    @Query("specialty_uid") String specialty_uid,
    @Query("insurance_uid") String insurance_uid,
    @Query("practice_uid") String practice_uid,
    @Query("location") String location,
    @Query("user_location") String user_location,
    @Query("gender") String gender,
    @Query("sort") String sort,
    @Query("fields") String fields,
    @Query("skip") String skip,
    @Query("limit") String limit,
    @Query("user_key") String user_key
  );
}
