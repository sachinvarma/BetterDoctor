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
import com.sachinvarma.betterdoctor.model.dataresponse.DoctorsSpecialities;
import com.sachinvarma.betterdoctor.ui.doctordetails.DoctorsSpecialisationRecyclerAdapter.MyViewHolder;
import java.util.List;

public class DoctorsSpecialisationRecyclerAdapter extends Adapter<MyViewHolder> {

  private List<DoctorsSpecialities> doctorsDataList;

  class MyViewHolder extends ViewHolder {
    @BindView(R.id.tvName)
    AppCompatTextView tvName;
    @BindView(R.id.tvDes)
    AppCompatTextView tvDes;

    MyViewHolder(View view) {
      super(view);
      ButterKnife.bind(this, view);
    }
  }

  DoctorsSpecialisationRecyclerAdapter(List<DoctorsSpecialities> doctorsDataList) {
    this.doctorsDataList = doctorsDataList;
  }

  @NonNull
  @Override
  public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
    View itemView =
      LayoutInflater.from(parent.getContext()).inflate(R.layout.item_specialities, parent, false);

    return new MyViewHolder(itemView);
  }

  @Override
  public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
    DoctorsSpecialities doctorsListModel = doctorsDataList.get(position);
    if (!TextUtils.isEmpty(doctorsListModel.actor)) {
      holder.tvName.setVisibility(View.VISIBLE);
      holder.tvName.setText(doctorsListModel.actor);
    } else {
      holder.tvName.setVisibility(View.GONE);
    }
    if (!TextUtils.isEmpty(doctorsListModel.description)) {
      holder.tvDes.setVisibility(View.VISIBLE);
      holder.tvDes.setText(doctorsListModel.description);
    } else {
      holder.tvDes.setVisibility(View.GONE);
    }
  }

  @Override
  public int getItemCount() {
    return doctorsDataList.size();
  }
}

