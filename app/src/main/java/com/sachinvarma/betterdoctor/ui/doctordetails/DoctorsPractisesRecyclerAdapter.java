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
import com.sachinvarma.betterdoctor.model.dataresponse.DoctorPractises;
import com.sachinvarma.betterdoctor.ui.doctordetails.DoctorsPractisesRecyclerAdapter.MyViewHolder;
import java.util.ArrayList;
import java.util.List;

public class DoctorsPractisesRecyclerAdapter extends Adapter<MyViewHolder> {

  private List<DoctorPractises> doctorsDataList;

  class MyViewHolder extends ViewHolder {
    @BindView(R.id.tvHospName)
    AppCompatTextView tvHospName;
    @BindView(R.id.tvHospPhone)
    AppCompatTextView tvHospPhone;
    @BindView(R.id.tvHospAddress)
    AppCompatTextView tvHospAddress;
    @BindView(R.id.tvHosWebsite)
    AppCompatTextView tvHosWebsite;

    MyViewHolder(View view) {
      super(view);
      ButterKnife.bind(this, view);
    }
  }

  DoctorsPractisesRecyclerAdapter(List<DoctorPractises> doctorsDataList) {
    this.doctorsDataList = doctorsDataList;
  }

  @NonNull
  @Override
  public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
    View itemView =
      LayoutInflater.from(parent.getContext()).inflate(R.layout.item_practises, parent, false);

    return new MyViewHolder(itemView);
  }

  @Override
  public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
    DoctorPractises doctorsListModel = doctorsDataList.get(position);
    if (!TextUtils.isEmpty(doctorsListModel.name)) {
      holder.tvHospName.setVisibility(View.VISIBLE);
      holder.tvHospName.setText(doctorsListModel.name);
    } else {
      holder.tvHospName.setVisibility(View.GONE);
    }

    List<String> phoneNumber = new ArrayList<>();
    List<String> faxNumber = new ArrayList<>();

    if (doctorsListModel.phones != null && doctorsListModel.phones.size() > 0) {
      for (int i = 0; i < doctorsListModel.phones.size(); i++) {
        if (!TextUtils.isEmpty(doctorsListModel.phones.get(i).type)) {
          if (doctorsListModel.phones.get(i).type.equalsIgnoreCase("fax") && !TextUtils.isEmpty(
            doctorsListModel.phones.get(i).number)) {
            faxNumber.add(doctorsListModel.phones.get(i).number);
          } else if (!TextUtils.isEmpty(doctorsListModel.phones.get(i).number)) {
            phoneNumber.add(doctorsListModel.phones.get(i).number);
          }
        }
      }
      String phoneNumbers = "";
      String faxNumbers = "";

      if (phoneNumber.size() > 0) {
        phoneNumbers = TextUtils.join(",", phoneNumber);
      }
      if (phoneNumber.size() > 0) {
        faxNumbers = TextUtils.join(",", faxNumber);
      }

      if (!TextUtils.isEmpty(phoneNumbers) && !TextUtils.isEmpty(faxNumbers)) {
        holder.tvHospPhone.setVisibility(View.VISIBLE);
        holder.tvHospPhone.setText("Phone: " + phoneNumbers + "\n" + "Fax: " + faxNumbers);
      } else if (!TextUtils.isEmpty(phoneNumbers)) {
        holder.tvHospPhone.setVisibility(View.VISIBLE);
        holder.tvHospPhone.setText("Phone: " + phoneNumbers);
      } else if (!TextUtils.isEmpty(faxNumbers)) {
        holder.tvHospPhone.setVisibility(View.VISIBLE);
        holder.tvHospPhone.setText("Fax: " + faxNumbers);
      } else {
        holder.tvHospPhone.setVisibility(View.GONE);
      }
    } else {
      holder.tvHospPhone.setVisibility(View.GONE);
    }

    if (doctorsDataList.get(position).visit_address != null) {
      String address = "";
      if (!TextUtils.isEmpty(doctorsDataList.get(position).visit_address.street)) {
        address = doctorsDataList.get(position).visit_address.street;
      }
      if (!TextUtils.isEmpty(doctorsDataList.get(position).visit_address.city)) {
        address += ", " + doctorsDataList.get(position).visit_address.city;
      }
      if (!TextUtils.isEmpty(doctorsDataList.get(position).visit_address.state_long)) {
        address += ", " + doctorsDataList.get(position).visit_address.state_long;
      } else {
        if (!TextUtils.isEmpty(doctorsDataList.get(position).visit_address.state)) {
          address += ", " + doctorsDataList.get(position).visit_address.state;
        }
      }
      if (!TextUtils.isEmpty(doctorsDataList.get(position).visit_address.zip)) {
        address += ", " + doctorsDataList.get(position).visit_address.zip;
      }

      if (!TextUtils.isEmpty(address)) {
        holder.tvHospAddress.setVisibility(View.VISIBLE);
        holder.tvHospAddress.setText("Address: "+address);
      } else {
        holder.tvHospAddress.setVisibility(View.GONE);
      }
    } else {
      holder.tvHospAddress.setVisibility(View.GONE);
    }

    if (!TextUtils.isEmpty(doctorsListModel.website)) {
      holder.tvHosWebsite.setVisibility(View.VISIBLE);
      holder.tvHosWebsite.setText("Website: " + doctorsListModel.website);
    } else {
      holder.tvHosWebsite.setVisibility(View.GONE);
    }
  }

  @Override
  public int getItemCount() {
    return doctorsDataList.size();
  }
}


