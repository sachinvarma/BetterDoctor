package com.sachinvarma.betterdoctor.model.dataresponse;

import java.io.Serializable;
import java.util.List;

public class DoctorsListModel implements Serializable{

  public DoctorsProfile profile;
  public List<DoctorsSpecialities> specialties = null;
  public List<DoctorPractises> practices = null;
  public List<DoctorEducations> educations = null;
  public List<DoctorsLicenses> licenses = null;
  public List<DoctorsInsurances> insurances = null;




}
