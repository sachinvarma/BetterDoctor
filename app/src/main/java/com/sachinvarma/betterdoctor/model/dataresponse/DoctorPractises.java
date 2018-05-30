package com.sachinvarma.betterdoctor.model.dataresponse;

import java.io.Serializable;
import java.util.List;

public class DoctorPractises implements Serializable {

  public String location_slug;
  public boolean within_search_area;
  public String distance;
  public String lat;
  public String lon;
  public String uid;
  public String name;
  public String website;
  public boolean accepts_new_patients;
  public VisitAddressModel visit_address;
  public List<PhonesModel> phones = null;

}
