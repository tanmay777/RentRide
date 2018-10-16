package com.example.tanmay.rentbaazvehicleadministration.Entity.ToRent

import android.content.Context
import android.content.Intent
import android.support.v4.content.ContextCompat.startActivity
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.bumptech.glide.Glide
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
        holder.pick_up_detail.text = SimpleDateFormat("dd MMM", Locale.US).format(bookingList.get(position).pickup_details)+", "+SimpleDateFormat("HH:mm").format(bookingList.get(position).drop_details)
        holder.drop_detail.text = SimpleDateFormat("dd MMM", Locale.US).format(bookingList.get(position).drop_details)+", "+SimpleDateFormat("HH:mm").format(bookingList.get(position).pickup_details)
        holder.view_holder.setOnClickListener {
            /*
            val intent = Intent(context, OnRentActivity::class.java)
            intent.putExtra("item_id", bookingList.get(position).id)
            startActivity(context,intent,null)
            */
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