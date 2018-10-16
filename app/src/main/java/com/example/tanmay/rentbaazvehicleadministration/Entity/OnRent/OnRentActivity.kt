package com.example.tanmay.rentbaazvehicleadministration.Entity.OnRent

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import com.bumptech.glide.Glide
import com.example.tanmay.rentbaazvehicleadministration.Entity.Home.HomeActivity
import com.example.tanmay.rentbaazvehicleadministration.Entity.Home.VehicleModel
import com.example.tanmay.rentbaazvehicleadministration.Entity.Home.bookingModel
import com.example.tanmay.rentbaazvehicleadministration.Entity.ToRent.RenteeModel
import com.example.tanmay.rentbaazvehicleadministration.R
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.android.synthetic.main.activity_on_rent.*
import java.text.SimpleDateFormat
import java.util.*

class OnRentActivity : AppCompatActivity() {

    var hoursFlag: Boolean = true
    var extendTimeViewVisibile: Boolean = false
    lateinit var itemId: String
    lateinit var onRentVehicleDocumentReference: DocumentReference
    var bookingList = mutableListOf<bookingModel>()
    var vehicle: VehicleModel? = null
    lateinit var renteeDetail: RenteeModel


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_on_rent)

        itemId = intent.getStringExtra("item_id")
        progressBar.visibility=View.VISIBLE
        onRentVehicleDocumentReference = FirebaseFirestore.getInstance().collection("vehicle").document(itemId)
        onRentVehicleDocumentReference.get().addOnSuccessListener { it: DocumentSnapshot ->
            vehicle = it.toObject(VehicleModel::class.java)
            bike_name.text = vehicle?.vehicle_name
            Glide.with(this).load(vehicle?.vehicle_image_url).into(bike_img)
            bike_number.text = vehicle?.vehicle_number
            bike_organization.text = vehicle?.vendor_organization
            cost_weekday.text = vehicle?.weekday_cost
            cost_weekend.text = vehicle?.weekend_cost
            Log.v("Vehicle_model", vehicle.toString())
            for (bookingModel in vehicle!!.booking) {
                if ((bookingModel.pickup_details.compareTo(Date()) < 0) &&
                        (bookingModel.drop_details.compareTo(Date()) > 0)) {
                    val myFormat = "dd/MM/yyyy" // mention the format you need
                    val simpleDateFormat = SimpleDateFormat(myFormat, Locale.US)
                    layout_pickup_date.text = simpleDateFormat.format(bookingModel.pickup_details)
                    layout_pickup_time.text = SimpleDateFormat("HH:mm").format(bookingModel.pickup_details)
                    layout_return_date.text = simpleDateFormat.format(bookingModel.drop_details)
                    layout_return_time.text = SimpleDateFormat("HH:mm").format(bookingModel.drop_details)
                    FirebaseFirestore.getInstance().collection("rentee_details").document(bookingModel.phone_num).get().addOnSuccessListener {
                        val fullName = it.get("first_name").toString() + " " + it.get("last_name").toString()
                        layout_first_name.text = fullName
                        layout_registration_number.text = it.get("registration_number").toString()
                        layout_contact_number.text = it.get("phone_number").toString()
                    }
                }
            }
            progressBar.visibility=View.GONE
        }
    }

    override fun onStart() {
        super.onStart()
        end_session_button.setOnClickListener {
            onRentVehicleDocumentReference.get().addOnSuccessListener { it: DocumentSnapshot ->
                vehicle = it.toObject(VehicleModel::class.java)
                for (bookingModel in vehicle!!.booking) {
                    if ((bookingModel.pickup_details.compareTo(Date()) < 0) &&
                            (bookingModel.drop_details.compareTo(Date()) > 0))
                    {
                        vehicle!!.booking.remove(bookingModel)
                        onRentVehicleDocumentReference.set(vehicle!!)
                        startActivity(Intent(this@OnRentActivity,HomeActivity::class.java))
                    }
                }
            }
        }

        extend_time.setOnClickListener {
            if (extendTimeViewVisibile) {
                extend_time_minus_sign.visibility = View.GONE
                extend_time_hours.visibility = View.GONE
                extend_time_colon.visibility = View.GONE
                extend_time_minutes.visibility = View.GONE
                extend_time_plus_sign.visibility = View.GONE
                extend_time_hours_text.visibility = View.GONE
                extendTimeViewVisibile = false
            } else {
                extend_time_minus_sign.visibility = View.VISIBLE
                extend_time_hours.visibility = View.VISIBLE
                extend_time_colon.visibility = View.VISIBLE
                extend_time_minutes.visibility = View.VISIBLE
                extend_time_plus_sign.visibility = View.VISIBLE
                extend_time_hours_text.visibility = View.VISIBLE
                extendTimeViewVisibile = true
            }
        }


        extend_time_plus_sign.setOnClickListener {
            if (hoursFlag) {
                if ((extend_time_hours.text.toString().toInt() + 1) >= 24) {
                    extend_time_hours.text = "0"
                }
                extend_time_hours.text = (extend_time_hours.text.toString().toInt() + 1).toString()
            } else {
                if ((extend_time_minutes.text.toString().toInt() + 15) >= 60) {
                    if ((extend_time_hours.text.toString().toInt() + 1) >= 24) {
                        extend_time_hours.text = "0"
                    } else
                        extend_time_hours.text = (extend_time_hours.text.toString().toInt() + 1).toString()
                    extend_time_minutes.text = "0"
                } else
                    extend_time_minutes.text = (extend_time_minutes.text.toString().toInt() + 15).toString()
            }
        }

        extend_time_minus_sign.setOnClickListener {
            if (hoursFlag) {
                if ((extend_time_hours.text.toString().toInt() - 1) >= 0)
                    extend_time_hours.text = (extend_time_hours.text.toString().toInt() - 1).toString()
            } else {
                if ((extend_time_minutes.text.toString().toInt() - 15) < 0 && ((extend_time_hours.text.toString().toInt() - 1) >= 0)) {
                    extend_time_hours.text = (extend_time_hours.text.toString().toInt() - 1).toString()
                    extend_time_minutes.text = "45"
                } else if ((extend_time_minutes.text.toString().toInt() - 15) < 0 && ((extend_time_hours.text.toString().toInt() - 1) < 0)) {
                    extend_time_minutes.text = "0"
                } else {
                    extend_time_minutes.text = (extend_time_minutes.text.toString().toInt() - 15).toString()
                }
            }
        }

        extend_time_hours.setOnClickListener {
            hoursFlag = true
        }
        extend_time_minutes.setOnClickListener {
            hoursFlag = false
        }
    }
}

//TODO: Add prebookings in the xml file