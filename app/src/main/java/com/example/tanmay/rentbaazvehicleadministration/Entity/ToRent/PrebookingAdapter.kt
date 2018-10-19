package com.example.tanmay.rentbaazvehicleadministration.Entity.ToRent

import android.content.Context
import android.content.Intent
import android.support.v4.content.ContextCompat.startActivity
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.tanmay.rentbaazvehicleadministration.Entity.Home.bookingModel
import com.example.tanmay.rentbaazvehicleadministration.Entity.OnRent.OnRentActivity
import com.example.tanmay.rentbaazvehicleadministration.R
import kotlinx.android.synthetic.main.pre_booking_detail_card.view.*
import java.text.SimpleDateFormat
import java.util.*


class PrebookingAdapter(val bookingList: MutableList<bookingModel>, val context: Context) : RecyclerView.Adapter<ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(LayoutInflater.from(context).inflate(R.layout.pre_booking_detail_card, parent, false))
    }

    override fun getItemCount(): Int {
        return bookingList.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val pick_up_date=SimpleDateFormat("dd MMM", Locale.US).format(bookingList.get(position).pickup_details)
        val pick_up_time=SimpleDateFormat("HH:mm", Locale.US).format(bookingList.get(position).drop_details)
        val drop_date=SimpleDateFormat("dd MMM", Locale.US).format(bookingList.get(position).drop_details)
        val drop_time=SimpleDateFormat("HH:mm", Locale.US).format(bookingList.get(position).pickup_details)
        val pick_detail=pick_up_date+", "+pick_up_time
        val drop_detail=drop_date+", "+drop_time
        holder.pick_up_detail.text = pick_detail
        holder.drop_detail.text = drop_detail
        holder.view_holder.setOnClickListener {
            val intent = Intent(context, OnRentActivity::class.java)
            intent.putExtra("phone_number", bookingList.get(position).phone_num)
            intent.putExtra("pick_up_date",pick_up_date)
            intent.putExtra("pick_up_time",pick_up_time)
            intent.putExtra("drop_date",drop_date)
            intent.putExtra("drop_time",drop_time)
            startActivity(context,intent,null)
        }
        holder.right_arrow.setOnClickListener{

        }
    }

}

class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
    val pick_up_detail = view.pick_up_detail
    val drop_detail = view.drop_detail
    val view_holder = view.view_holder
    val right_arrow=view.right_arrow
}