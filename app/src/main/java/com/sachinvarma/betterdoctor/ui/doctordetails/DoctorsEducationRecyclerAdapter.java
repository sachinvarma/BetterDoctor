package com.sachinvarma.betterdoctor.ui.doctordetails;

import android.support.annotation.NonNull;
import android.support.v7.widget.AppCompatTextView;
import android.support.v7.widget.RecyclerView.Adapter;
import android.support.v7.widget.RecyclerView.ViewHolder;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import butterknife.BindView;
import butterknife.ButterKnife;
import com.sachinvarma.betterdoctor.R;
import com.sachinvarma.betterdoctor.model.dataresponse.DoctorEducations;
import com.sachinvarma.betterdoctor.ui.doctordetails.DoctorsEducationRecyclerAdapter.MyViewHolder;
import java.util.List;

public class DoctorsEducationRecyclerAdapter extends Adapter<MyViewHolder> {

  private List<DoctorEducations> doctorsDataList;

  class MyViewHolder extends ViewHolder {
    @BindView(R.id.tvEducations)
    AppCompatTextView tvEducations;

    MyViewHolder(View view) {
      super(view);
      ButterKnife.bind(this, view);
    }
  }

  DoctorsEducationRecyclerAdapter(List<DoctorEducations> doctorsDataList) {
    this.doctorsDataList = doctorsDataList;
  }

  @NonNull
  @Override
  public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
    View itemView =
      LayoutInflater.from(parent.getContext()).inflate(R.layout.item_education, parent, false);

    return new MyViewHolder(itemView);
  }

  @Override
  public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
    DoctorEducations doctorsListModel = doctorsDataList.get(position);
    if (!TextUtils.isEmpty(doctorsListModel.degree)) {
      if (!TextUtils.isEmpty(doctorsListModel.school)) {
        holder.tvEducations.setText(
          "Degree: " + doctorsListModel.degree + "\n" + "Institution: " + doctorsListModel.school);
      } else {
        holder.tvEducations.setText("Degree: " + doctorsListModel.degree);
      }
    }
    else
    {
      if (!TextUtils.isEmpty(doctorsListModel.school)) {
        holder.tvEducations.setText("Institution: " + doctorsListModel.school);
      }
    }
  }

  @Override
  public int getItemCount() {
    return doctorsDataList.size();
  }
}
