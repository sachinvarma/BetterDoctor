package com.sachinvarma.betterdoctor.ui.home;

import android.support.annotation.NonNull;
import android.support.constraint.ConstraintLayout;
import android.support.v7.widget.AppCompatTextView;
import android.support.v7.widget.RecyclerView.Adapter;
import android.support.v7.widget.RecyclerView.ViewHolder;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import butterknife.BindView;
import butterknife.ButterKnife;
import com.sachinvarma.betterdoctor.R;
import com.sachinvarma.betterdoctor.model.dataresponse.DoctorsListModel;
import com.sachinvarma.betterdoctor.ui.home.HomeDoctorsRecyclerAdapter.MyViewHolder;
import com.squareup.picasso.Picasso;
import java.util.List;

public class HomeDoctorsRecyclerAdapter extends Adapter<MyViewHolder> {

  private List<DoctorsListModel> doctorsDataList;

  class MyViewHolder extends ViewHolder {
    @BindView(R.id.ivDoctor)
    ImageView ivDoctor;
    @BindView(R.id.tvDoctorsName)
    AppCompatTextView tvDoctorsName;
    @BindView(R.id.tvDoctorsQualification)
    AppCompatTextView tvDoctorsQualification;
    @BindView(R.id.tvDoctorsSpecialities)
    AppCompatTextView tvDoctorsSpecialities;
    @BindView(R.id.tvDoctorsLocations)
    AppCompatTextView tvDoctorsLocations;
    @BindView(R.id.clParent)
    ConstraintLayout clParent;

    MyViewHolder(View view) {
      super(view);
      ButterKnife.bind(this, view);
    }
  }

  HomeDoctorsRecyclerAdapter(List<DoctorsListModel> doctorsDataList) {
    this.doctorsDataList = doctorsDataList;
  }

  @NonNull
  @Override
  public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
    View itemView =
      LayoutInflater.from(parent.getContext()).inflate(R.layout.item_doctors, parent, false);

    return new MyViewHolder(itemView);
  }

  @Override
  public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
    DoctorsListModel doctorsListModel = doctorsDataList.get(position);
    String doctorName = "";
    if (doctorsListModel.profile != null && !TextUtils.isEmpty(
      doctorsListModel.profile.first_name)) {
      doctorName = doctorsListModel.profile.first_name;
    }
    if (doctorsListModel.profile != null && !TextUtils.isEmpty(
      doctorsListModel.profile.middle_name)) {
      if (!TextUtils.isEmpty(doctorName)) {
        doctorName += " " + doctorsListModel.profile.middle_name;
      } else {
        doctorName = doctorsListModel.profile.middle_name;
      }
    }
    if (doctorsListModel.profile != null && !TextUtils.isEmpty(
      doctorsListModel.profile.last_name)) {
      if (!TextUtils.isEmpty(doctorName)) {
        doctorName += " " + doctorsListModel.profile.last_name;
      } else {
        doctorName = doctorsListModel.profile.last_name;
      }
    }

    if (!TextUtils.isEmpty(doctorName)) {
      holder.tvDoctorsName.setVisibility(View.VISIBLE);
      holder.tvDoctorsName.setText(doctorName);
    } else {
      holder.tvDoctorsName.setVisibility(View.GONE);
    }

    if (doctorsListModel.practices != null && doctorsListModel.practices.size() > 0) {
      String locations = "";
      for (int i = 0; i < doctorsListModel.practices.size(); i++) {
        if (!TextUtils.isEmpty(doctorsListModel.practices.get(i).location_slug)) {
          if (!TextUtils.isEmpty(locations)) {
            locations += ", " + doctorsListModel.practices.get(i).location_slug;
          } else {
            locations = "Locations: " + doctorsListModel.practices.get(i).location_slug;
          }
        }
      }

      if (!TextUtils.isEmpty(locations)) {
        holder.tvDoctorsLocations.setVisibility(View.VISIBLE);
        holder.tvDoctorsLocations.setText(locations);
      } else {
        holder.tvDoctorsLocations.setVisibility(View.GONE);
      }
    } else {
      holder.tvDoctorsLocations.setVisibility(View.GONE);
    }

    if (doctorsListModel.specialties != null && doctorsListModel.specialties.size() > 0) {
      String specialties = "";
      for (int i = 0; i < doctorsListModel.specialties.size(); i++) {
        if (!TextUtils.isEmpty(doctorsListModel.specialties.get(i).actor)) {
          if (!TextUtils.isEmpty(specialties)) {
            specialties += ", " + doctorsListModel.specialties.get(i).actor;
          } else {
            specialties = "Specialities: " + doctorsListModel.specialties.get(i).actor;
          }
        }
      }

      if (!TextUtils.isEmpty(specialties)) {
        holder.tvDoctorsSpecialities.setVisibility(View.VISIBLE);
        holder.tvDoctorsSpecialities.setText(specialties);
      } else {
        holder.tvDoctorsSpecialities.setVisibility(View.GONE);
      }
    } else {
      holder.tvDoctorsSpecialities.setVisibility(View.GONE);
    }

    if (!TextUtils.isEmpty(doctorsListModel.profile.title)) {
      holder.tvDoctorsQualification.setVisibility(View.VISIBLE);
      holder.tvDoctorsQualification.setText("Qualification: " + doctorsListModel.profile.title);
    } else {
      holder.tvDoctorsQualification.setVisibility(View.GONE);
    }

    if (doctorsListModel.profile != null) {
      Picasso.get().load(doctorsListModel.profile.image_url).into(holder.ivDoctor);
    }
  }

  @Override
  public int getItemCount() {
    return doctorsDataList.size();
  }
}