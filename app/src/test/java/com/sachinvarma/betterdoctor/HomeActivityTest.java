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
      Mockito.when(apiInterface.getDoctorsList(Mockito.anyString())).thenReturn(Observable.just(model));
      // When
      presenter.getDoctorsData(Mockito.anyString());
      // Then
      verify(view).setDoctorsData(model);
    }

    @Test
    public void noDataReceived() {
      // Given
      DoctorsDataModel model = new DoctorsDataModel();
      model.data = Collections.emptyList();
      Mockito.when(apiInterface.getDoctorsList(Mockito.anyString())).thenReturn(Observable.just(model));
      // When
      presenter.getDoctorsData(Mockito.anyString());
      // Then
      verify(view).noDoctorsFound();
    }
  }