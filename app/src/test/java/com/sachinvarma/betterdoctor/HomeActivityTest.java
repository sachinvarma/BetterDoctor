package com.sachinvarma.betterdoctor;

import com.sachinvarma.betterdoctor.interfaces.ApiInterface;
import com.sachinvarma.betterdoctor.model.dataresponse.DoctorsDataModel;
import com.sachinvarma.betterdoctor.model.dataresponse.DoctorsListModel;
import com.sachinvarma.betterdoctor.ui.home.HomeContract;
import com.sachinvarma.betterdoctor.ui.home.HomePresenter;
import java.util.Arrays;
import java.util.Collections;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnit;
import org.mockito.junit.MockitoRule;
import retrofit2.mock.Calls;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class HomeActivityTest {

  @Rule
  public MockitoRule rule = MockitoJUnit.rule();

  @Mock
  private HomeContract.View view;

  @Mock
  private ApiInterface apiInterface;

  private HomePresenter presenter;

  @Before
  public void setUp() {
    presenter = new HomePresenter(view, apiInterface);
  }

  @Test
  public void dataReceived() {
    // Given
    DoctorsDataModel model = new DoctorsDataModel();
    model.data = Arrays.asList(new DoctorsListModel(), new DoctorsListModel());
    when(apiInterface.getDoctorsList("", "", "", "", "", "", "", "37.773,-122.413,100",
      "37.773,-122.413", "male", "full-name-asc", "", "", "10", "df5e0bcdc894eb54343ae4daaaf618eb"))
      .thenReturn(Calls.response(model));
    // When
    presenter.getDoctorsData("", "", "", "", "", "", "", "37.773,-122.413,100", "37.773,-122.413",
      "male", "full-name-asc", "", "", "10", "df5e0bcdc894eb54343ae4daaaf618eb");
    // Then
    verify(view).setDoctorsData(model);
  }

  @Test
  public void noDataReceived() {
    // Given
    DoctorsDataModel model = new DoctorsDataModel();
    model.data = Collections.<DoctorsListModel>emptyList();
    when(apiInterface.getDoctorsList("", "", "", "", "", "", "", "", "", "", "", "", "", "",
      "")).thenReturn(Calls.response(model));
    // When
    presenter.getDoctorsData("", "", "", "", "", "", "", "", "", "", "", "", "", "", "");
    // Then
    verify(view).noDoctorsFound();
  }
}