package com.sachinvarma.betterdoctor.ui.doctordetails;

import android.graphics.Rect;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.constraint.ConstraintLayout;
import android.support.design.widget.AppBarLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatTextView;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import butterknife.BindView;
import butterknife.ButterKnife;
import com.sachinvarma.betterdoctor.R;
import com.sachinvarma.betterdoctor.custom.CircleImageView;
import com.sachinvarma.betterdoctor.model.dataresponse.DoctorsListModel;
import com.squareup.picasso.Picasso;
import java.util.ArrayList;
import java.util.List;

public class DoctorDetailsActivity extends AppCompatActivity {

  @BindView(R.id.toolbar)
  Toolbar toolbar;
  @BindView(R.id.appbar)
  AppBarLayout appbar;
  @BindView(R.id.clParent)
  ConstraintLayout clParent;
  @BindView(R.id.ivDoctor)
  CircleImageView ivDoctor;
  @BindView(R.id.tvDocName)
  AppCompatTextView tvDocName;
  @BindView(R.id.tvDocBio)
  AppCompatTextView tvDocBio;
  @BindView(R.id.tvEducation)
  AppCompatTextView tvEducation;
  @BindView(R.id.rvEducations)
  RecyclerView rvEducations;
  @BindView(R.id.tvSpecialities)
  AppCompatTextView tvSpecialities;
  @BindView(R.id.rvSpecialities)
  RecyclerView rvSpecialities;
  @BindView(R.id.tvPractises)
  AppCompatTextView tvPractises;
  @BindView(R.id.rvPractises)
  RecyclerView rvPractises;
  @BindView(R.id.tvLicenses)
  AppCompatTextView tvLicenses;
  @BindView(R.id.tvInsurances)
  AppCompatTextView tvInsurances;

  private DoctorsListModel doctorsListModel;

  @Override
  protected void onCreate(@Nullable Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_doctor_details);
    ButterKnife.bind(this);

    Bundle bundle = getIntent().getExtras();

    if (bundle != null && bundle.containsKey("DoctorsData")) {
      doctorsListModel = (DoctorsListModel) bundle.getSerializable("DoctorsData");
    }
    init();
  }

  private void init() {
    setSupportActionBar(toolbar);

    // add back arrow to toolbar
    if (getSupportActionBar() != null) {
      getSupportActionBar().setDisplayHomeAsUpEnabled(true);
      getSupportActionBar().setDisplayShowHomeEnabled(true);
      getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_arrow_back);
    }

    if (doctorsListModel != null) {
      if (doctorsListModel.profile != null) {
        if (!TextUtils.isEmpty(doctorsListModel.profile.image_url)) {
          Picasso.get().load(doctorsListModel.profile.image_url).into(ivDoctor);
        } else {
          ivDoctor.setVisibility(View.GONE);
        }

        String name;

        if (!TextUtils.isEmpty(doctorsListModel.profile.first_name)) {
          name = doctorsListModel.profile.first_name;
          if (!TextUtils.isEmpty(doctorsListModel.profile.middle_name)) {
            name += " " + doctorsListModel.profile.middle_name;
          }
          if (!TextUtils.isEmpty(doctorsListModel.profile.last_name)) {
            name += " " + doctorsListModel.profile.last_name;
          }
          if (!TextUtils.isEmpty(doctorsListModel.profile.title)) {
            name += " (" + doctorsListModel.profile.title + ")";
          }

          if (!TextUtils.isEmpty(name)) {
            tvDocName.setVisibility(View.VISIBLE);
            tvDocName.setText(name);
          } else {
            tvDocName.setVisibility(View.GONE);
          }
        }
        if (!TextUtils.isEmpty(doctorsListModel.profile.bio)) {
          tvDocBio.setVisibility(View.VISIBLE);
          tvDocBio.setText(doctorsListModel.profile.bio);
        } else {
          tvDocBio.setVisibility(View.GONE);
        }
      }
      String licenses = "";
      List<String> list = new ArrayList<>();
      if (doctorsListModel.licenses != null && doctorsListModel.licenses.size() > 0) {

        for (int i = 0; i < doctorsListModel.licenses.size(); i++) {

          if (!TextUtils.isEmpty(doctorsListModel.licenses.get(i).state)) {
            if (!list.contains(doctorsListModel.licenses.get(i).state)) {
              list.add(doctorsListModel.licenses.get(i).state);
              licenses += list.size()+") "+doctorsListModel.licenses.get(i).state + "\n";
            }
          }
        }

        if (!TextUtils.isEmpty(licenses)) {

          tvLicenses.setVisibility(View.VISIBLE);
          tvLicenses.setText(licenses);
        } else {
          tvLicenses.setVisibility(View.GONE);
        }
      } else {
        tvLicenses.setVisibility(View.GONE);
      }
      list.clear();
      if (doctorsListModel.educations != null && doctorsListModel.educations.size() > 0) {
        rvEducations.setVisibility(View.VISIBLE);
        tvEducation.setVisibility(View.VISIBLE);
        rvEducations.setLayoutManager(new LinearLayoutManager(this));
        rvEducations.setAdapter(new DoctorsEducationRecyclerAdapter(doctorsListModel.educations));
        rvEducations.addItemDecoration(
          new DividerItemDecoration(this, DividerItemDecoration.VERTICAL));
        rvEducations.addItemDecoration(new DividerItemDecoration(this,
          ((LinearLayoutManager) rvEducations.getLayoutManager()).getOrientation()) {
          @Override
          public void getItemOffsets(
            Rect outRect, View view, RecyclerView parent, RecyclerView.State state
          ) {
            int position = parent.getChildAdapterPosition(view);
            // hide the divider for the last child
            if (position == parent.getAdapter().getItemCount() - 1) {
              outRect.setEmpty();
            } else {
              super.getItemOffsets(outRect, view, parent, state);
            }
          }
        });
        rvEducations.setNestedScrollingEnabled(false);
      } else {
        rvEducations.setVisibility(View.GONE);
        tvEducation.setVisibility(View.GONE);
      }

      if (doctorsListModel.specialties != null && doctorsListModel.specialties.size() > 0) {
        rvSpecialities.setVisibility(View.VISIBLE);
        tvSpecialities.setVisibility(View.VISIBLE);
        rvSpecialities.setLayoutManager(new LinearLayoutManager(this));
        rvSpecialities.setAdapter(
          new DoctorsSpecialisationRecyclerAdapter(doctorsListModel.specialties));
        rvSpecialities.addItemDecoration(
          new DividerItemDecoration(this, DividerItemDecoration.VERTICAL));
        rvSpecialities.setNestedScrollingEnabled(false);
      } else {
        rvSpecialities.setVisibility(View.GONE);
        tvSpecialities.setVisibility(View.GONE);
      }

      if (doctorsListModel.practices != null && doctorsListModel.practices.size() > 0) {
        rvPractises.setVisibility(View.VISIBLE);
        tvPractises.setVisibility(View.VISIBLE);
        rvPractises.setLayoutManager(new LinearLayoutManager(this));
        rvPractises.setAdapter(new DoctorsPractisesRecyclerAdapter(doctorsListModel.practices));
        rvPractises.addItemDecoration(
          new DividerItemDecoration(this, DividerItemDecoration.VERTICAL));
        rvPractises.setNestedScrollingEnabled(false);
      } else {
        rvPractises.setVisibility(View.GONE);
        tvPractises.setVisibility(View.GONE);
      }
    }
  }

  @Override
  public boolean onOptionsItemSelected(MenuItem item) {
    // handle arrow click here
    if (item.getItemId() == android.R.id.home) {
      onBackPressed(); // close this activity and return to preview activity (if there is any)
    }

    return super.onOptionsItemSelected(item);
  }

  @Override
  public boolean onCreateOptionsMenu(Menu menu) {
    // Inflate the menu; this adds items to the action bar if it is present.
    getMenuInflater().inflate(R.menu.dummy_menu, menu);
    return true;
  }
}
