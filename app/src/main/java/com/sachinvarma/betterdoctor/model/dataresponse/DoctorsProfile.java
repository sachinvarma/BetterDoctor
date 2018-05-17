package com.sachinvarma.betterdoctor.model.dataresponse;

import java.util.List;

public class DoctorsProfile {

  public String first_name;
  public String middle_name;
  public String last_name;
  public String slug;
  public String title;
  public String image_url;
  public String bio;
  public List<LanguagesKnown> languages = null;
}
