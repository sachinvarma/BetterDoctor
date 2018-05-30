  package com.sachinvarma.betterdoctor;

  import android.text.TextUtils;
  import com.sachinvarma.betterdoctor.interfaces.ApiInterface;
  import com.sachinvarma.betterdoctor.model.dataresponse.DoctorsDataModel;
  import com.sachinvarma.betterdoctor.model.dataresponse.DoctorsListModel;
  import com.sachinvarma.betterdoctor.ui.home.HomeContract;
  import com.sachinvarma.betterdoctor.ui.home.HomePresenter;
  import com.sachinvarma.betterdoctor.utils.RxSchedulersOverrideRule;
  import io.reactivex.Observable;
  import io.reactivex.plugins.RxJavaPlugins;
  import io.reactivex.schedulers.Schedulers;
  import java.util.Arrays;
  import java.util.Collections;
  import org.junit.After;
  import org.junit.Before;
  import org.junit.Rule;
  import org.junit.Test;
  import org.mockito.Mock;
  import org.mockito.Mockito;
  import org.mockito.junit.MockitoJUnit;
  import org.mockito.junit.MockitoRule;

  import static org.mockito.Mockito.verify;

  public class HomeActivityTest {

    @Rule
    public MockitoRule rule = MockitoJUnit.rule();

    @Mock
    private HomeContract.View view;

    @Rule
    public RxSchedulersOverrideRule mRxSchedulersOverrideRule = new RxSchedulersOverrideRule();

    @Mock
    private ApiInterface apiInterface;

    private HomePresenter presenter;
    private String pageCount = "-10";

    @Before
    public void setUp() throws Exception {
      presenter = new HomePresenter(view, apiInterface);
      //RxJavaPlugins.setIoSchedulerHandler(scheduler -> Schedulers.trampoline());
    }

    @After
    public void cleanUp() {

      //RxJavaPlugins.reset();
    }

    @Test
    public void dataReceived() {
      // Given
      DoctorsDataModel model = new DoctorsDataModel();
      model.data = Arrays.asList(new DoctorsListModel(), new DoctorsListModel());
      Mockito.when(apiInterface.getDoctorsList(getDoctorsData())).thenReturn(Observable.just(model));
      // When
      presenter.getDoctorsData(getDoctorsData());
      // Then
      verify(view).setDoctorsData(model);
    }

    @Test
    public void noDataReceived() {
      // Given
      DoctorsDataModel model = new DoctorsDataModel();
      model.data = Collections.emptyList();
      Mockito.when(apiInterface.getDoctorsList(getDoctorsData())).thenReturn(Observable.just(model));
      // When
      presenter.getDoctorsData(getDoctorsData());
      // Then
      verify(view).noDoctorsFound();
    }

    private String getDoctorsData() {
      pageCount = String.valueOf(Integer.parseInt(pageCount) + 10);
      String url =
        "https://api.betterdoctor.com/2016-03-01/doctors?"/*location=37.773,-122.413,100&skip="+pageCount+"&limit="+limit+"&user_key=df5e0bcdc894eb54343ae4daaaf618eb"*/;

      String name = "";
      if (!TextUtils.isEmpty(name)) {
        url = url + "name=" + name + "&";
      }
      String first_name = "";
      if (!TextUtils.isEmpty(first_name)) {
        url = url + "first_name=" + first_name + "&";
      }
      String last_name = "";
      if (!TextUtils.isEmpty(last_name)) {
        url = url + "last_name=" + last_name + "&";
      }
      String query = "";
      if (!TextUtils.isEmpty(query)) {
        url = url + "query=" + query + "&";
      }
      String specialty_uid = "";
      if (!TextUtils.isEmpty(specialty_uid)) {
        url = url + "specialty_uid=" + specialty_uid + "&";
      }
      String insurance_uid = "";
      if (!TextUtils.isEmpty(insurance_uid)) {
        url = url + "insurance_uid=" + insurance_uid + "&";
      }
      String practice_uid = "";
      if (!TextUtils.isEmpty(practice_uid)) {
        url = url + "practice_uid=" + practice_uid + "&";
      }
      String location = "";
      if (!TextUtils.isEmpty(location)) {
        url = url + "location=" + location + "&";
      }
      String user_location = "";
      if (!TextUtils.isEmpty(user_location)) {
        url = url + "user_location=" + user_location + "&";
      }
      String gender = "";
      if (!TextUtils.isEmpty(gender)) {
        url = url + "gender=" + gender + "&";
      }
      String sort = "";
      if (!TextUtils.isEmpty(sort)) {
        url = url + "sort=" + sort + "&";
      }
      String fields = "";
      if (!TextUtils.isEmpty(fields)) {
        url = url + "fields=" + fields + "&";
      }
      if (!TextUtils.isEmpty(pageCount)) {
        url = url + "skip=" + pageCount + "&";
      }
      String limit = "10";
      if (!TextUtils.isEmpty(limit)) {
        url = url + "limit=" + limit + "&";
      }

      url = url + "user_key=df5e0bcdc894eb54343ae4daaaf618eb";

      return url;
    }
  }