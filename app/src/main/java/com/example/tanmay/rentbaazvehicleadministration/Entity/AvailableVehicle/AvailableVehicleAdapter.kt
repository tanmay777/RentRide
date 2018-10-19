package com.example.tanmay.rentbaazvehicleadministration.Entity.AvailableVehicle

import android.content.Context
import android.content.Intent
import android.support.v4.content.ContextCompat.startActivity
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.bumptech.glide.Glide
import com.example.tanmay.rentbaazvehicleadministration.Entity.Home.VehicleModel
import com.example.tanmay.rentbaazvehicleadministration.Entity.ToRent.ToRentActivity
import com.example.tanmay.rentbaazvehicleadministration.R
import kotlinx.android.synthetic.main.vehicle_detail_card.view.*


class AvailableVehicleAdapter (val availableVehicleList:ArrayList<VehicleModel>, val context: Context):RecyclerView.Adapter<ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(LayoutInflater.from(context).inflate(R.layout.vehicle_detail_card, parent, false))
    }

    override fun getItemCount(): Int {
        return availableVehicleList.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bike_name.text = availableVehicleList.get(position).vehicle_name
        holder.bike_number.text = availableVehicleList.get(position).vehicle_number
        Glide.with(context).load(availableVehicleList.get(position).vehicle_image_url).into(holder.bike_img)
        holder.bike_vendor_organization.text = availableVehicleList.get(position).vendor_organization
        holder.cost_weekday.text = "Rs "+availableVehicleList.get(position).weekday_cost+"\\hr"
        holder.cost_weekend.text = "Rs "+availableVehicleList.get(position).weekend_cost+"\\hr"
        holder.view_holder.setOnClickListener {
            val intent = Intent(context, ToRentActivity::class.java)
            intent.putExtra("item_id", availableVehicleList.get(position).id)
            startActivity(context,intent,null)
        }
    }

}

class ViewHolder(view: View):RecyclerView.ViewHolder(view){
    val bike_name=view.bike_name
    val bike_number=view.bike_number
    val bike_img=view.bike_img
    val bike_vendor_organization=view.bike_vendor_organization
    val cost_weekday=view.cost_weekday
    val cost_weekend=view.cost_weekend
    val view_holder=view.view_holder
}